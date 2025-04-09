package export.tex_building_blocks;

import org.apache.uima.jcas.JCas;
import org.bson.Document;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.SortedMap;

/**
 * factory class for creating and interacting with .tex and .pdf files
 * @author Sophie Kaiser
 */
public class TexFactory {

    /**
     * path of the directory where pdf output will be generated
     */
    private final String pdfPath;

    /**
     * path of the directory where tex output will be generated
     */
    private final String texPath;

    /**
     * constructor for the factory
     * @param pdfPath path of the directory where pdf output will be generated
     * @param texPath path of the directory where tex output will be generated
     * @author Sophie Kaiser
     */
    public TexFactory(String pdfPath, String texPath) {
        this.pdfPath = pdfPath;
        this.texPath = texPath;
    }

    /**
     * get the path to the pdf directory
     * @return path of the directory where pdf output will be generated
     * @author Sophie Kaiser
     */
    public String getPdfPath() {
        return this.pdfPath;
    }

    /**
     * get the path to the tex directory
     * @return path of the directory where tex output will be generated
     * @author Sophie Kaiser
     */
    public String getTexPath() {
        return this.texPath;
    }

    /**
     * create a new instance of TexDocument
     * @param fileName file name
     * @param sessionProtocols linked list of session protocols
     * @return new instance of TexDocument
     * @throws IOException
     * @author Sophie Kaiser
     */
    public TexDocument createTexDocument(String fileName, LinkedList<TexSessionProtocol> sessionProtocols) throws IOException {
        return new TexDocument(fileName, this.texPath, sessionProtocols);
    }

    /**
     * create new instance of TexNLPVisualization
     * @param fileName
     * @return new instance of TexNLPVisualization
     * @author Sophie Kaiser
     */
    public TexNLPVisualization createTexNLPVisualization(String fileName) {
        return new TexNLPVisualization(fileName, this.texPath);
    }

    /**
     * create new instance of TexSessionProtocol
     * @param fileName
     * @param agendaItemMap map mapping all agenda items to the speeches they contain
     * @return new instance of TexSessionProtocol
     * @author Sophie Kaiser
     */
    public TexSessionProtocol createTexSessionProtocol(String fileName, SortedMap<String, LinkedList<TexSpeech>> agendaItemMap) {
        return new TexSessionProtocol(fileName, this.texPath, agendaItemMap);
    }

    /**
     * create new instance of TexSpeakerInfo
     * @param fileName
     * @return new instance of TexSpeakerInfo
     * @author Sophie Kaiser
     */
    public TexSpeakerInfo createTexSpeakerInfo(String fileName) {
        return new TexSpeakerInfo(fileName, this.texPath);
    }

    /**
     * create new instance of TexSpeech from JCas
     * @param fileName
     * @param cas
     * @return new instance of TexSpeech
     * @author Sophie Kaiser
     */
    public TexSpeech createTexSpeech(String fileName, JCas cas) {
        return new TexSpeech(fileName, this.texPath, cas);
    }

    /**
     * create new instance of TexSpeech from Document
     * @param fileName
     * @param speech document
     * @return new instance of TexSpeech
     * @author Sophie Kaiser
     */
    public TexSpeech createTexSpeech(String fileName, Document speech) {
        return new TexSpeech(fileName, this.texPath, speech);
    }

    /**
     * compile pdf file using process builder.
     * delete aux and log files generated during compilation.
     *
     * @param fileName the name of the file to be compiled into pdf
     *
     * @author Oliwia Daszczynska
     * @author Sophie Kaiser
     */
    public void compilePDF(String fileName) {
        try {
            new ProcessBuilder(
                    "pdflatex",
                    "-output-directory=" + this.pdfPath,
                    "-interaction=nonstopmode",
                    this.texPath + "/" + fileName + ".tex"
            )
                    .redirectError(ProcessBuilder.Redirect.INHERIT) // enables printing of latex errors
                    .start()
                    .waitFor(); // pauses java until pdflatex finishes
            /* second compilation to make the table of contents display properly and to make
            the final pdf output stable;
            the toc file cannot be deleted before the second compilation
             */
            new ProcessBuilder(
                    "pdflatex",
                    "-output-directory=" + this.pdfPath,
                    "-interaction=nonstopmode",
                    this.texPath + "/" + fileName + ".tex"
            )
                    .redirectError(ProcessBuilder.Redirect.INHERIT) // enables printing of latex errors
                    .start()
                    .waitFor(); // pauses java until pdflatex finishes
            Files.deleteIfExists(Paths.get(this.pdfPath, fileName + ".aux")); // delete aux file
            Files.deleteIfExists(Paths.get(this.pdfPath, fileName + ".log")); // delete log file
            /* deleting the toc file prematurely causes the table of contents to not display,
            two compilations are needed to properly compile and display the table of contents,
            the toc file is necessary for this
             */
            Files.deleteIfExists(Paths.get(this.pdfPath, fileName + ".toc")); // delete toc file
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
