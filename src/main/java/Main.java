import database.MongoDatabaseHandler_Impl;
import nlp.NLPHandler;
import org.apache.uima.UIMAException;
import org.xml.sax.SAXException;
import parliament.ParliamentFactory;
import parliament.impl.ParliamentFactory_Impl;
import rest.PageController;
import rest.RESTHandler;

import java.io.IOException;
import java.net.URISyntaxException;

import static database.FactoryToMongoDB.factoryToMongoDB;
import static helpers.Downloader.downloadFiles;
import static helpers.LookupTimer.startLookupTimer;
import static helpers.Parsing.Members.parseMDB;
import static helpers.Parsing.Protocol.parseProtocols;

/**
 * Multimodal Parliament Explorer
 *
 * @author Oliwia Daszczynska
 * @author Genadij Vorontsov
 * @author Ishak Bouaziz
 */
public class Main {
    /**
     * Multimodal Parliament Explorer.
     *
     * @author Oliwia Daszczynska
     * @author Genadij Vorontsov
     * @author Ishak Bouaziz
     */
    public static void main(String[] args) throws Exception {
        if (false) { // set true to enable download of new protocols during runtime.
            startLookupTimer();
        }
        if (false) { // set true to download files into resources.
            downloadFiles();
        }

        // This factory handles the creation of objects in our application.
        ParliamentFactory factory = new ParliamentFactory_Impl();

        MongoDatabaseHandler_Impl.connectDatabase();
        if (false) { // set true to parse downloaded data.
            parseMDB(factory, false);
            parseProtocols(factory);
        }
        if (false) { // set true to insert factory into MongoDB.
            factoryToMongoDB(factory);
        }

       //NLP Analyse Optional
        if (false){
        NLPHandler nlpHandler = new NLPHandler();
        nlpHandler.init();

        if (false) {
            nlpHandler.runDockerDriver();
        }
        if (false) {
            nlpHandler.runVideos();
        }
        if (false){
        nlpHandler.runTranscripts();
        }}

        PageController.get();
        RESTHandler.get().CreateRoutes();
    }
}
