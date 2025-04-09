package export.tex_building_blocks;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.bson.Document;

import java.util.ArrayList;

/**
 * represents a tex file that contains a speech
 * @author Sophie Kaiser
 */
public class TexSpeech extends Texify {

    /**
     * constructor for a speech using a JCas
     *
     * @param fileName
     * @param texPath
     * @param cas contains the speech
     * @author Sophie Kaiser
     */
    TexSpeech(String fileName, String texPath, JCas cas) {
        super(fileName, texPath);
        // add all the speech content
        // TODO speech needs title
        // speech heading is not a section/subsection
        this.addToTexText("{\\bf speech heading}\\vspace{.3cm}\\\\");
        /* TODO needs speaker info */
        JCasUtil.select(cas, Sentence.class)
                .forEach(sentence -> {
                    this.addToTexText(sentence.getCoveredText());
                });
        // TODO deal with comments
        // quote environment around comments; they may not be quotes,
        // but the environment achieves good visual contrast
        this.addToTexText("\\begin{quote}comments should be visually distinguishable from the speech itself, maybe by colour, maybe the quote environment is enough.\\end{quote}");
        // add space to the end
        this.addToTexText("\\vspace{1cm}\\\\");
    }

    /**
     * constructor for a speech using a Document
     *
     * @param fileName name of the tex file
     * @param texPath name of the path the tex file will be created in
     * @param speech document containing the speech text
     * @author Oliwia Daszczynska
     * @author Sophie Kaiser
     */
    TexSpeech(String fileName, String texPath, Document speech){
        super(fileName, texPath);
        // speech heading is not a section/subsection
        this.addToTexText("{\\bf Speech}\\vspace{.3cm}\\\\");
        ArrayList<Document> contents =
                (ArrayList<Document>) speech.get("contents");
        for (Document content : contents) {
            switch (content.getString("content_tag")) {
                case "SPEAKER_INTRODUCTION":
                    this.addToTexText("\\textbf{" +
                            content.getString("text") +
                            "}\\par\n"
                    );
                    break;
                case "SPEECH_TEXT":
                    this.addToTexText(content.getString("text"));
                    break;
                case "COMMENT", "QUOTE":
                    this.addToTexText("\\begin{quote}" +
                            content.getString("text") +
                            "\\end{quote}"
                    );
                    break;
                default:
                    break;
            }
        }
        // add space to the end of the speech
        this.addToTexText("\\vspace{1cm}\\\\");
    }
}
