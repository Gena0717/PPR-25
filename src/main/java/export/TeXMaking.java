package export;

import com.mongodb.client.MongoCollection;
import database.MongoDatabaseHandler_Impl;
import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import export.tex_building_blocks.TexDocument;
import export.tex_building_blocks.TexFactory;
import export.tex_building_blocks.TexSessionProtocol;
import export.tex_building_blocks.TexSpeech;
import net.bytebuddy.dynamic.scaffold.MethodGraph;
import nlp.NLPHandler;
import org.apache.uima.cas.CASException;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.bson.Document;
import org.bson.Document;

import javax.ws.rs.core.Link;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.LinkedList;

/**
 * tex and pdf generation.
 *
 * @author Oliwia Daszczynska
 */
public class TeXMaking {

    static int var = 0; // should be an id to not overwrite different output files
    static String fileName = "doc" + var; // generic file title.

    /**
     * @author Oliwia Daszczynska
     * @author Sophie Kaiser
     */
    public static void makeTex(List<String> ids, String fileType)
                                throws IOException {
        Files.createDirectories(Paths.get("src/main/resources/export/pdf_output")); // path has to exist first
        Files.createDirectories(Paths.get("src/main/resources/export/tex_output")); // path has to exist first
        //for counting how many files have been created
        long fileCount = Files.list(Paths.get("src/main/resources/export/tex_output")).count();

        MongoCollection<Document> speeches = MongoDatabaseHandler_Impl.getCollectionSpeeches();
//        MongoCollection<Document> nlpSpeeches = MongoDatabaseHandler_Impl.getCollectionProcessedSpeeches();
//        MongoCollection<Document> speakers = MongoDatabaseHandler_Impl.getCollectionSpeaker();
//        MongoCollection<Document> pics = MongoDatabaseHandler_Impl.getCollectionPictures();

        // create export name
        String exportFileName = "Parliament_Explorer_Export_" + fileCount;

        // get all selected speeches
        LinkedList<Document> selectedSpeeches = new LinkedList<>();
        // get all selected session nrs in a list
        SortedSet<Integer> sessionNrs = new TreeSet<>();
        for (String id : ids) {
            Document speech = speeches.find(new Document("_id", id)).first();
//            Document nlpSpeech = nlpSpeeches.find(new Document("_id", id)).first();
//            assert speech != null;
//            String speakerId = speech.getString("speaker_id");
//            Document speaker = speakers.find(new Document("_id", speakerId)).first();
//            Document img = pics.find(new Document("_id", speakerId)).first();
//            makeTexForSpeech(speech, nlpSpeech, speaker, img, fileType);

            selectedSpeeches.add(speech);
            // get session nrs of the speech currently being looked at
            sessionNrs.add(speech.getInteger("session_nr"));
        }

        // making a pdf
        // define output paths in factory
        TexFactory texFactory = new TexFactory(
                "src/main/resources/export/pdf_output",
                "src/main/resources/export/tex_output"
        );

        // all created protocols
        LinkedList<TexSessionProtocol> sessionProtocols = new LinkedList<>();
        for (Integer sessionNr : sessionNrs) {
            // all agenda items
            List<String> agendaItems = new ArrayList<>();
            // all speeches of one session
            for (Document speech : selectedSpeeches) {
                if (Objects.equals(speech.getInteger("session_nr"), sessionNr)) {
                    agendaItems.add(speech.getString("agenda_item"));
                }
            }
            // all agenda items and their speeches
            SortedMap<String, LinkedList<TexSpeech>> texAgendaItems = new TreeMap<String, LinkedList<TexSpeech>>();
            // all speeches from a particular session and agenda item
            for (String agendaItem : agendaItems) {
                LinkedList<TexSpeech> agendaItemTexSpeeches = new LinkedList<>();
                for (Document speech : selectedSpeeches) {

                    if (Objects.equals(speech.getInteger("session_nr"), sessionNr) &&
                            Objects.equals(speech.getString("agenda_item"), agendaItem)
                    ) {
                        // create TexSpeech from it, add to list
                        agendaItemTexSpeeches.add(texFactory.createTexSpeech(
                                speech.getString("_id"),
                                speech));
                    }
                }
                if (agendaItem == null) {
                    agendaItem = "no agenda item";
                }
                texAgendaItems.put(agendaItem, agendaItemTexSpeeches);
            }
            // create session protocols
            sessionProtocols.add(texFactory.createTexSessionProtocol(
                    String.valueOf(sessionNr),
                    texAgendaItems
            ));
        }
        TexDocument texDocument = texFactory.createTexDocument(
                exportFileName, sessionProtocols
        );
        texDocument.toTex();

        // write into preview.ftl
        LinkedList<String> preview = texDocument.getTexText();
        preview.addLast("<br>");
        preview.addLast("<br>");
        preview.addLast("your pdf was saved to src/main/resources/export/pdf_output");
        Files.write(Path.of("src/main/resources/web/pages/preview.ftl"),preview);

        // compile pdf
        if (isPdflatexInstalled()) {
            texFactory.compilePDF(texDocument.getFileName());
        } else {
            System.out.println("pdflatex not found on system. Install TeX:");
            System.out.println("https://tug.org/texlive/");
        }
    }

    /**
     * returns true if pdflatex --version executes with exit code 0.
     *
     * @author Oliwia Daszczynska
     */
    public static boolean isPdflatexInstalled() {
        try {
            return new ProcessBuilder("pdflatex", "--version").start().waitFor() == 0; // 0 means exit code 0
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * compile pdf file using process builder.
     * delete aux and log files generated during compilation.
     *
     * @author Oliwia Daszczynska
     */
    @Deprecated
    public static void compilePDF(String input, String output) {
        try {
            new ProcessBuilder("pdflatex", "-output-directory=" + output, "-interaction=nonstopmode", input)
                    .redirectError(ProcessBuilder.Redirect.INHERIT) // enables printing of latex errors
                    .start()
                    .waitFor(); // pauses java until pdflatex finishes
            Files.deleteIfExists(Paths.get(output, fileName + ".aux")); // delete aux file
            Files.deleteIfExists(Paths.get(output, fileName + ".log")); // delete log file
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * process user-selected speech
     *
     * @author Oliwia Daszczynska
     */
    @Deprecated
    public static void makeTexForSpeech(Document speech, Document nlpSpeech, Document speaker, Document img, String fileType) {
        try {
            switch (fileType) {
                case "pdf":
                    // to tex
                    StringBuilder result = new StringBuilder();
                    result.append("\\subsection{").append(speech.getString("agenda_item")).append(", ").append(speech.getInteger("session_nr")).append(" nr}");

                    if (img != null) {
                        String imgUrl = img.getString("url");
                    }
                    result.append("subsection*{Speaker Profile:}\n");
                    if (img != null) {
                        String imgUrl = img.getString("url");
                        /*download image*/
                        result.append("\\includegraphics[width=0.5\\linewidth]{" + "TODO TODO TODO" + "}\n");
                    }
                    result.append("\\begin{itemize}\n");
                    result.append("\\item[] \\textbf{Lastname:} ").append(speaker.getString("lastname")).append("\n");
                    result.append("\\item[] \\textbf{Firstname:} ").append(speaker.getString("firstname")).append("\n");
                    result.append(checkIfBlank(speaker.getString("party_name"), "Party"));
                    result.append(checkIfBlank(speaker.getString("location"), "Party"));
                    result.append(checkIfBlank(speaker.getString("nobility"), "Nobility"));
                    result.append(checkIfBlank(speaker.getString("prefix"), "Prefix"));
                    result.append(checkIfBlank(speaker.getString("address_title"), "Address Title"));
                    result.append(checkIfBlank(speaker.getString("academic_title"), "Academic Title"));
                    result.append(checkIfBlank(speaker.getString("date_of_birth"), "Date of Birth"));
                    result.append(checkIfBlank(speaker.getString("place_of_birth"), "Party"));
                    result.append(checkIfBlank(speaker.getString("date_of_death"), "Date of Death"));
                    result.append(checkIfBlank(speaker.getString("gender"), "Gender"));
                    result.append(checkIfBlank(speaker.getString("marital_status"), "Marital Status"));
                    result.append(checkIfBlank(speaker.getString("religion"), "religion"));
                    result.append(checkIfBlank(speaker.getString("profession"), "Profession"));
                    result.append(checkIfBlank(speaker.getString("curriculum_vitae"), "curriculum_vitae"));
                    result.append("\\end{itemize}");
                    @SuppressWarnings("unchecked") ArrayList<Document> contents = (ArrayList<Document>) speech.get("contents");
                    result.append("\\subsection*{Speech:}");
                    for (Document content : contents) {
                        String contentTag = content.getString("content_tag");
                        String contentText = content.getString("text");
                        switch (content.getString("content_tag")) {
                            case "SPEAKER_INTRODUCTION":
                                result.append("\\textbf{").append(content.getString("text")).append("}\\par\n");
                                break;
                            case "SPEECH_TEXT":
                                result.append(content.getString("text")).append("\\par\n");
                                break;
                            case "COMMENT",
                                    "QUOTE":
                                result.append("\\begin{quote}").append(content.getString("text")).append("\\par").append("\\end{quote}\n");
                                break;
                            default:
                                break;
                        }
                    }
                    result.append("\\subsection*{NLP Analysis:} \\par\n");
                    result.append("\\textbf{Sentences:} ").append((ArrayList<String>) nlpSpeech.get("sentences")).append("\\par");
                    result.append("\\textbf{Tokens:} ").append((ArrayList<String>) nlpSpeech.get("tokens")).append("\\par\n");
                    result.append("\\textbf{POS:} ").append((ArrayList<String>) nlpSpeech.get("posTags")).append("\\par");
                    result.append("\\textbf{Lemmas:} ").append((ArrayList<String>) nlpSpeech.get("lemmas")).append("\\par\n");
                    result.append("\\textbf{Dependencies:} ").append((ArrayList<String>) nlpSpeech.get("dependencies")).append("\\par");
                    result.append("\\textbf{Named Entities:} ").append((ArrayList<String>) nlpSpeech.get("namedEntities")).append("\\par\n");
                    result.append("\\textbf{Topics:} ").append((ArrayList<String>) nlpSpeech.get("Topics")).append("\\par");
                    result.append("\\textbf{Sentiments:} ").append((ArrayList<String>) nlpSpeech.get("sentiments")).append("\\par\n");
                    /*send result back to parent method, then compile all speeches*/
                    System.out.println(result);
                    break;
                case "xmi":
                    // to xmi
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * helper to check if field is blank
     *
     * @author Oliwia Daszczynska
     */
    private static String checkIfBlank(String field, String fieldName) {
        if (field == null || field.isBlank()) {
            return "";
        } else {
            return "\\item[] \\textbf{" + fieldName + ":} " + field + "\n";
        }
    }

    /**
     * @author Oliwia Daszczynska
     * @author Sophie Kaiser
     */
    public static void texTest() throws Exception {
//        String exampleContent = """
//                \\documentclass{article}
//                \\usepackage{tikz}
//                \\begin{document}
//                \\textbf{new latex file}
//                \\end{document}
//                """;
//
//        String texPath = "src/main/resources/export/tex_output";
//        String texFile = texPath + "/" + fileName + ".tex";
//        String pdfPath = "src/main/resources/export/pdf_output";
//
//        try {
//            if (isPdflatexInstalled()) {
//                Files.createDirectories(Paths.get(texPath)); // path has to exist first
//                Files.createDirectories(Paths.get(pdfPath)); // path has to exist first
//                Files.write(Paths.get(texFile), exampleContent.getBytes());    //this should go into a ToTeX method instead
//                compilePDF(texFile, pdfPath);
//
//            } else {
//                System.out.println("pdflatex not found on system. Install TeX:");
//                System.out.println("https://tug.org/texlive/");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        String dummy = "This is dummy text. It is here for testing purposes. Lars the polar bear sends his regards.";
//        JCas cas = JCasFactory.createText(dummy, "de");
//
//        // manually annotating for testing purposes
//        new Sentence(cas, 0, 20).addToIndexes();
//        new Sentence(cas, 20, 53).addToIndexes();
//        new Sentence(cas, 53, 91).addToIndexes();
//
//        TexFactory texFactory = new TexFactory(
//                "src/main/resources/export/pdf_output",
//                "src/main/resources/export/tex_output"
//        );
//        LinkedList<TexSpeech> speechList = new LinkedList<>();
//        TexSpeech speech_0 = texFactory.createTexSpeech("test_speech", cas);
//        TexSpeech speech_1 = texFactory.createTexSpeech("test_speech", cas);
//        TexSpeech speech_2 = texFactory.createTexSpeech("test_speech", cas);
//        speechList.add(speech_0);
//        speechList.add(speech_1);
//        speechList.add(speech_2);
//
//        TexSessionProtocol sessionProtocol = texFactory.createTexSessionProtocol("test_session", speechList);
//        LinkedList<TexSessionProtocol> sessionProtocols = new LinkedList<>();
//        sessionProtocols.add(sessionProtocol);
//        // testing making a full document
//        TexDocument texDocument = texFactory.createTexDocument("test_document", sessionProtocols);
//        texDocument.toTex();
//        // testing compiling it into a pdf
//        if (isPdflatexInstalled()) {
//            texFactory.compilePDF(texDocument.getFileName());
//        } else {
//            System.out.println("pdflatex not found on system. Install TeX:");
//            System.out.println("https://tug.org/texlive/");
//        }
    }
}
