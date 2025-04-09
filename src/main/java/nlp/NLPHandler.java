package nlp;

import com.mongodb.client.MongoCollection;
import database.MongoDatabaseHandler_Impl;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.dependency.Dependency;
import org.apache.commons.io.IOUtils;
import org.apache.uima.UIMAException;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.impl.XmiCasDeserializer;
import org.apache.uima.cas.impl.XmiCasSerializer;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.bson.Document;
import org.hucompute.textimager.uima.type.Sentiment;
import org.hucompute.textimager.uima.type.category.CategoryCoveredTagged;
import org.texttechnologylab.DockerUnifiedUIMAInterface.DUUIComposer;
import org.texttechnologylab.DockerUnifiedUIMAInterface.driver.DUUIRemoteDriver;
import org.texttechnologylab.DockerUnifiedUIMAInterface.driver.DUUIUIMADriver;
import org.texttechnologylab.DockerUnifiedUIMAInterface.io.DUUIAsynchronousProcessor;
import org.texttechnologylab.DockerUnifiedUIMAInterface.lua.DUUILuaContext;
import org.texttechnologylab.annotation.type.AudioToken;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static helpers.DataChecker.*;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;


/**
 * NLPHandler class for processing natural language data using UIMA and Docker-based NLP components.
 *
 * @author Genadij Vorontsov
 */
public class NLPHandler {
    private static DUUIComposer pComposer = null;
    private static int iWorkers = 1;

    /**
     * Initializes the Docker NLP processing pipeline.
     * Initializes the DUUIComposer with the required drivers.
     *
     * @throws IOException If an I/O error occurs during initialization.
     * @throws URISyntaxException If there is an issue with URI syntax.
     * @throws UIMAException If an error occurs during UIMA initialization.
     * @throws SAXException If an XML parsing error occurs.
     * @author Genadij Vorontsov
     */
    public void init() throws IOException, URISyntaxException, UIMAException, SAXException {

        DUUILuaContext ctx = new DUUILuaContext().withJsonLibrary();

        pComposer = new DUUIComposer()
                .withSkipVerification(true)
                .withLuaContext(ctx)
                .withWorkers(iWorkers);

        DUUIUIMADriver uima_driver = new DUUIUIMADriver();
        DUUIRemoteDriver remoteDriver = new DUUIRemoteDriver();

        pComposer.addDriver(uima_driver, remoteDriver);
    }

    /**
     * Creates a JCas from a MongoDB speech document.
     *
     * @param speechDocument The MongoDB document containing speech text.
     * @return The JCas object representing the processed text.
     * @throws ResourceInitializationException If an error occurs during JCas initialization.
     * @throws CASException If an error occurs when setting CAS metadata.
     * @author Genadij Vorontsov
     */
    public JCas getCas(Document speechDocument) throws ResourceInitializationException, CASException {

        List<Document> contents = speechDocument.getList("contents", Document.class);

        String text = contents.stream()
                .map(doc -> doc.getString("text"))
                .collect(Collectors.joining(" "));

        JCas cas = JCasFactory.createText(text, "de");

        DocumentMetaData metaData = DocumentMetaData.create(cas);
        metaData.setDocumentId(speechDocument.getString("_id"));
        metaData.addToIndexes();

        return cas;
    }

    /**
     * Creates a JCas for Video NLP.
     *
     * @return The JCas object representing the processed video.
     * @throws ResourceInitializationException If an error occurs during JCas initialization.
     * @throws CASException If an error occurs when setting CAS metadata.
     * @author Genadij Vorontsov
     */
    public JCas getVideoCas() throws ResourceInitializationException, CASException {
        JCas cas = JCasFactory.createText("", "de");

        DocumentMetaData dmd = new DocumentMetaData(cas);
        dmd.setDocumentId("video");
        dmd.setDocumentTitle("DUUI on Video");
        dmd.addToIndexes();

        return cas;
    }

    /**
     * Creates a JCas for Transcript NLP.
     *
     * @return The JCas object representing the processed Transcript.
     * @throws ResourceInitializationException If an error occurs during JCas initialization.
     * @throws CASException If an error occurs when setting CAS metadata.
     * @author Genadij Vorontsov
     */
    public JCas getTranscriptCas(Document transcriptDocument) throws ResourceInitializationException, CASException {
        JCas cas = JCasFactory.createText(transcriptDocument.getString("transcription"), "de");

        DocumentMetaData metaData = DocumentMetaData.create(cas);
        metaData.setDocumentId(transcriptDocument.getString("_id"));
        metaData.addToIndexes();

        return cas;
    }

    /**
     * Configures and runs the Docker-based NLP processing pipeline for text analysis.
     *
     * @throws Exception If an error occurs during pipeline execution.
     * @author Genadij Vorontsov
     */
    public void runDockerDriver() throws Exception {
        pComposer.resetPipeline();

        pComposer.add(new DUUIRemoteDriver.Component("http://spacy.lehre.texttechnologylab.org")
                .withScale(iWorkers)
                .build());

        pComposer.add(new DUUIRemoteDriver.Component("http://gervader.lehre.texttechnologylab.org")
                .withScale(iWorkers)
                .withParameter("selection", "text")
                .build());

        pComposer.add(new DUUIRemoteDriver.Component("http://parlbert.lehre.texttechnologylab.org")
                .withScale(iWorkers)
                .build());

        // !!! Only one process at a time. If you want to process multiple speeches in parallel,
        // use processUnprocessedSpeechesParallel() and comment out processUnprocessedSpeeches()

        processUnprocessedSpeeches(); // sequential processing

        //processUnprocessedSpeechesParallel(collectionSpeeches);  // parallel processing with DUUI Reader
    }

    /**
     * Configures and runs the Docker-based NLP processing pipeline for video analysis.
     *
     * @throws Exception If an error occurs during pipeline execution.
     * @author Genadij Vorontsov
     */
    public void runVideos() throws Exception {
        pComposer.resetPipeline();

        pComposer.add(new DUUIRemoteDriver.Component("http://whisperx.lehre.texttechnologylab.org")
                .withScale(iWorkers)
                .withSourceView("video")
                .withTargetView("transcript")
                .build());

        // !!! Only one process at a time. If you want to process multiple videos in parallel,
        // use processUnprocessedVideosParallel() and comment out processUnprocessedVideos()

        processUnprocessedVideos();  // sequential processing

        //processUnprocessedVideosParallel(collectionVideos);  // parallel processing with DUUI Reader
    }

    /**
     * Configures and runs the Docker-based NLP processing pipeline for transcript analysis.
     *
     * @throws Exception If an error occurs during pipeline execution.
     * @author Genadij Vorontsov
     */
    public void runTranscripts() throws Exception {
        pComposer.resetPipeline();

        pComposer.add(new DUUIRemoteDriver.Component("http://spacy.lehre.texttechnologylab.org")
                .withScale(iWorkers)
                .build());

        pComposer.add(new DUUIRemoteDriver.Component("http://gervader.lehre.texttechnologylab.org")
                .withScale(iWorkers)
                .withParameter("selection", "text")
                .build());

        pComposer.add(new DUUIRemoteDriver.Component("http://parlbert.lehre.texttechnologylab.org")
                .withScale(iWorkers)
                .build());

        processUnprocessedTranscripts();
    }

    /**
     * Processes speeches in parallel that have not been processed yet.
     * This method leverages asynchronous processing for efficiency.
     *
     * @throws Exception If an error occurs during processing.
     * @author Genadij Vorontsov
     */
    public void processUnprocessedSpeechesParallel() throws Exception {
        MongoCollection<Document> collectionSpeeches = MongoDatabaseHandler_Impl.getCollectionSpeeches();
        LinkedList<String> unprocessedSpeechIds = getUnprocessedSpeechIds(collectionSpeeches);
        DUUIAsynchronousProcessor processor = new DUUIAsynchronousProcessor(new DUUI_Reader(unprocessedSpeechIds, collectionSpeeches));

        pComposer.add(new DUUIUIMADriver.Component(createEngineDescription(DUUI_Writer.class))
                .build());
        pComposer.run(processor, "reader");
    }

    /**
     * Processes videos in parallel that have not been processed yet.
     * This method uses asynchronous processing via DUUI.
     *
     * @throws Exception If an error occurs during video processing.
     * @author Genadij Vorontsov
     */
    public void processUnprocessedVideosParallel() throws Exception {
        MongoCollection<Document> collectionVideos = MongoDatabaseHandler_Impl.getCollectionVideos();
        LinkedList<String> unprocessedVideoIds = getUnprocessedVideos(collectionVideos);
        DUUIAsynchronousProcessor processor = new DUUIAsynchronousProcessor(new DUUI_Reader_Videos(unprocessedVideoIds, collectionVideos));

        pComposer.add(new DUUIUIMADriver.Component(createEngineDescription(DUUI_Writer_Videos.class))
                .build());
        pComposer.run(processor, "reader");
    }

    /**
     * Performs NLP analysis for all speeches that are not included in the "processed_speeches" collection.
     * The results are stored in the "processed_speeches" collection.
     *
     * @author Genadij Vorontsov
     */
    public void processUnprocessedSpeeches() {
        MongoCollection <Document> collectionSpeeches = MongoDatabaseHandler_Impl.getCollectionSpeeches();
        MongoCollection <Document> collectionProcessedSpeeches = MongoDatabaseHandler_Impl.getCollectionProcessedSpeeches();
        LinkedList<String> unprocessedSpeechIds = getUnprocessedSpeechIds(collectionSpeeches);
        int count = 0;

        for (String speechId : unprocessedSpeechIds) {
            Document speechDocument = collectionSpeeches.find(new Document("_id", speechId)).first();

            if (speechDocument != null) {
                try {
                    JCas cas = getCas(speechDocument);
                    analyzeCas(cas);

                    serializeCasToDatabase(cas, speechId, MongoDatabaseHandler_Impl.getCollectionSerializedSpeeches());

                    Document processedSpeech = new Document("_id", speechId)
                            .append("sentences", extractSentences(cas))
                            .append("tokens", extractTokens(cas))
                            .append("posTags", extractPosTags(cas))
                            .append("lemmas", extractLemmas(cas))
                            .append("dependencies", extractDependencies(cas))
                            .append("namedEntities", extractNamedEntities(cas))
                            .append("topics", extractTopics(cas))
                            .append("sentiments", extractSentiments(cas));

                    collectionProcessedSpeeches.insertOne(processedSpeech);
                    System.out.println("Processed speech stored: " + speechId);
                    count += 1;
                } catch (Exception e) {
                    System.err.println("Error processing speech with ID " + speechId + ": " + e.getMessage());
                }
            }
        }
        System.out.println("Processed " + count + " speeches.");
    }

    /**
     * Performs NLP analysis for all videos that are not included in the "processed_videos" collection.
     * The results are stored in the "processed_videos" collection.
     *
     * @author Genadij Vorontsov
     */
    public void processUnprocessedVideos()  {
        MongoCollection<Document> collectionVideos = MongoDatabaseHandler_Impl.getCollectionVideos();
        MongoCollection<Document> collectionProcessedVideos = MongoDatabaseHandler_Impl.getCollectionProcessedVideos();
        LinkedList<String> unprocessedVideos = getUnprocessedVideos(collectionVideos);
        int i = 0;

        for (String id : unprocessedVideos) {
            Document videoDocument = collectionVideos.find(new Document("_id", id)).first();

            if (videoDocument != null) {
                try {
                    String video_id = videoDocument.getString("video_id");
                    String urlString = "https://cldf-od.r53.cdn.tv1.eu/1000153copo/ondemand/app144277506/145293313/" + video_id + "/" + video_id + "_h264_512_288_514kb_baseline_de_514.mp4?fdl=1";
                    URL url = new URL(urlString);

                    InputStream inputStream = url.openStream();
                    byte[] videoBytes = IOUtils.toByteArray(inputStream);
                    String encodedString = Base64.getEncoder().encodeToString(videoBytes);

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    String pMimeType = connection.getContentType();


                    JCas pCas = getVideoCas();

                    JCas videoCas = pCas.createView("video");
                    videoCas.setSofaDataString(encodedString, pMimeType);
                    videoCas.setDocumentLanguage("de");

                    JCas transcriptCas = pCas.createView("transcript");

                    analyzeCas(pCas);

                    serializeCasToDatabase(pCas, id, MongoDatabaseHandler_Impl.getCollectionSerializedVideos());

                    Document processedVideo = new Document("_id", id)
                            .append("audioTokens", extractAudioTokens(transcriptCas))
                            .append("transcription", transcriptCas.getSofaDataString());

                    collectionProcessedVideos.insertOne(processedVideo);

                    System.out.println("Processed video stored: " + id);
                    i += 1;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Processed " + i + " videos.");
    }

    /**
     * Performs NLP analysis for all transcripts that are not included in the "processed_transcripts" collection.
     * The results are stored in the "processed_transcripts" collection.
     *
     * @author Genadij Vorontsov
     */
    public void processUnprocessedTranscripts(){
        MongoCollection<Document> collectionProcessedVideos = MongoDatabaseHandler_Impl.getCollectionProcessedVideos();
        MongoCollection<Document> collectionProcessedTranscripts = MongoDatabaseHandler_Impl.getCollectionProcessedTranscripts();
        LinkedList<String> unprocessedTranscripts = getUnprocessedTranscripts(collectionProcessedVideos);
        int count = 0;

        for (String id : unprocessedTranscripts) {
            Document scriptDoc = collectionProcessedVideos.find(new Document("_id", id)).first();

            if (scriptDoc != null) {
                try {
                    JCas cas = getTranscriptCas(scriptDoc);
                    analyzeCas(cas);

                    Document processedTranscript = new Document("_id", id)
                            .append("sentences", extractSentences(cas))
                            .append("tokens", extractTokens(cas))
                            .append("posTags", extractPosTags(cas))
                            .append("lemmas", extractLemmas(cas))
                            .append("dependencies", extractDependencies(cas))
                            .append("namedEntities", extractNamedEntities(cas))
                            .append("topics", extractTopics(cas))
                            .append("sentiments", extractSentiments(cas));

                    collectionProcessedTranscripts.insertOne(processedTranscript);
                    System.out.println("Processed transcript stored: " + id);
                    count += 1;
                } catch (Exception e) {
                    System.err.println("Error processing transcript with ID " + id + ": " + e.getMessage());
                }
            }
        }
        System.out.println("Processed " + count + " transcripts.");

    }

    /**
     * Serialize a JCas in MongoDB Collection.
     *
     * @param cas        JCas Object
     * @param documentId  documentId
     * @param collection serialized_speeches
     * @throws IOException, SAXException
     * @author Genadij Vorontsov
     */
    private void serializeCasToDatabase(JCas cas, String documentId, MongoCollection<Document> collection) throws IOException, SAXException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream)) {
            XmiCasSerializer.serialize(cas.getCas(), gzipOutputStream);
        }
        byte[] compressedData = byteArrayOutputStream.toByteArray();
        MongoDatabaseHandler_Impl.saveAnnotation(documentId, compressedData, collection);
    }

    /**
     * Deserialize a JCas from MongoDB.
     *
     * @param documentId  documentId
     * @param collection  serialized_speeches
     * @return cas
     * @throws IOException, ResourceInitializationException, CASException, SAXException
     * @author Genadij Vorontsov
     */
    private JCas deserializeCasFromDatabase(String documentId, MongoCollection<Document> collection) throws IOException, ResourceInitializationException, CASException, SAXException {
        byte[] compressedData = MongoDatabaseHandler_Impl.loadAnnotation(documentId, collection);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(compressedData);
        try (GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream)) {
            JCas cas = JCasFactory.createJCas();
            XmiCasDeserializer.deserialize(gzipInputStream, cas.getCas(), true);
            return cas;
        }
    }

    /**
     * Extracts sentences from a JCas and returns them as a list of documents.
     *
     * @param cas The JCas containing the sentences.
     * @return A list of documents with sentence information (id, begin, end).
     * @author Genadij Vorontsov
     */
    public List<Document> extractSentences(JCas cas) {
        List<Document> sentences = new ArrayList<>();
        JCasUtil.select(cas, Sentence.class)
                .forEach(sentence -> {
                    Document sentenceDoc = new Document()
                            .append("id", sentence.getAddress())
                            .append("begin", sentence.getBegin())
                            .append("end", sentence.getEnd());
                    sentences.add(sentenceDoc);
                });
        return sentences;
    }

    /**
     * Extracts tokens from a JCas and returns them as a list of documents.
     *
     * @param cas The JCas containing the tokens.
     * @return A list of documents with token information (id, begin, end, parent, lemma, posTag).
     * @author Genadij Vorontsov
     */
    public List<Document> extractTokens(JCas cas) {
        List<Document> tokens = new ArrayList<>();
        JCasUtil.select(cas, Token.class)
                .forEach(token -> {
                    tokens.add(new Document()
                            .append("id", token.getAddress())
                            .append("begin", token.getBegin())
                            .append("end", token.getEnd())
                            .append("parent", token.getParent() != null ? token.getParent().getAddress() : token.getAddress())
                            .append("lemma", token.getLemma().getAddress())
                            .append("pos", token.getPos().getAddress()));
                });
        return tokens;
    }

    /**
     * Extracts posTags from a JCas and returns them as a list of documents.
     *
     * @param cas The JCas containing the posTags.
     * @return A list of documents with posTag information (id, begin, end, PosValue, coarseValue).
     * @author Genadij Vorontsov
     */
    public List<Document> extractPosTags(JCas cas){
        List<Document> posTags = new ArrayList<>();
        JCasUtil.select(cas, POS.class)
                .forEach(pos ->{
                    posTags.add(new Document()
                            .append("id", pos.getAddress())
                            .append("begin", pos.getBegin())
                            .append("end", pos.getEnd())
                            .append("PosValue", pos.getPosValue())
                            .append("coarseValue", pos.getCoarseValue()));
                });
        return posTags;
    }

    /**
     * Extracts lemmas from a JCas and returns them as a list of documents.
     *
     * @param cas JCas containing NLP annotations
     * @return List of documents with lemma information (id, begin, end, value)
     * @author Genadij Vorontsov
     */
    public List<Document> extractLemmas(JCas cas) {
        List<Document> lemmas = new ArrayList<>();
        JCasUtil.select(cas, Token.class)
                .forEach(token -> {
                    if (token.getLemma() != null) {
                        lemmas.add(new Document()
                                .append("id", token.getAddress())
                                .append("begin", token.getBegin())
                                .append("end", token.getEnd())
                                .append("value", token.getLemmaValue()));
                    }
                });
        return lemmas;
    }

    /**
     * Extracts syntactic dependencies from a JCas.
     *
     * @param cas The JCas containing the dependencies.
     * @return A list of documents with dependency information (id, begin, end, Governor, Dependent, DependencyType).
     * @author Genadij Vorontsov
     */
    public List<Document> extractDependencies(JCas cas) {
        List<Document> dependencies = new ArrayList<>();
        JCasUtil.select(cas, Dependency.class)
                .forEach(dependency -> {
                    dependencies.add(new Document()
                            .append("id", dependency.getAddress())
                            .append("begin", dependency.getBegin())
                            .append("end", dependency.getEnd())
                            .append("Governor", dependency.getGovernor().getAddress())
                            .append("Dependent", dependency.getDependent().getAddress())
                            .append("DependencyType", dependency.getDependencyType()));
                });
        return dependencies;
    }

    /**
     * Extracts named entities from a JCas.
     *
     * @param cas The JCas containing the named entities.
     * @return A list of documents with entity information (id, begin, end, value).
     * @author Genadij Vorontsov
     */
    public List<Document> extractNamedEntities(JCas cas) {
        List<Document> namedEntities = new ArrayList<>();
        JCasUtil.select(cas, NamedEntity.class)
                .forEach(entity -> {
                    namedEntities.add(new Document()
                            .append("id", entity.getAddress())
                            .append("begin", entity.getBegin())
                            .append("end", entity.getEnd())
                            .append("value", entity.getValue()));
                });
        return namedEntities;
    }

    /**
     * Extracts topics from a JCas and returns them as a list of documents.
     *
     * @param cas JCas containing NLP annotations
     * @return List of documents with topics (id, begin, end, value, score)
     * @author Genadij Vorontsov
     */
    public List<Document> extractTopics(JCas cas) {
        List<Document> topics = new ArrayList<>();
        JCasUtil.select(cas, CategoryCoveredTagged.class)
                .forEach(topic -> {
                    topics.add(new Document()
                            .append("id", topic.getAddress())
                            .append("begin", topic.getBegin())
                            .append("end", topic.getEnd())
                            .append("value", topic.getValue())
                            .append("score", topic.getScore()));
                });
        return topics;
    }

    /**
     * Extracts sentiment information from a JCas.
     *
     * @param cas The JCas containing the sentiment information.
     * @return A list of documents with sentiment information (id, begin, end, sentiment).
     * @author Genadij Vorontsov
     */
    public List<Document> extractSentiments(JCas cas) {
        List<Document> sentiments = new ArrayList<>();
        JCasUtil.select(cas, Sentiment.class)
                .forEach(sentiment -> {
                    sentiments.add(new Document()
                            .append("id", sentiment.getAddress())
                            .append("begin", sentiment.getBegin())
                            .append("end", sentiment.getEnd())
                            .append("sentiment", sentiment.getSentiment()));
                });
        return sentiments;
    }

    /**
     * Extracts video information from a JCas.
     *
     * @param transcriptCas The JCas containing the video information.
     * @return A list of documents with video information (id, begin, end, timeStart, timeEnd, value).
     * @author Genadij Vorontsov
     */
    public List<Document> extractAudioTokens(JCas transcriptCas) {
        List<Document> audioTokens = new ArrayList<>();

        for (AudioToken token : JCasUtil.select(transcriptCas, AudioToken.class)) {
            Document tokenDoc = new Document()
                    .append("id", token.getAddress())
                    .append("begin", token.getBegin())
                    .append("end", token.getEnd())
                    .append("timeStart", token.getTimeStart())
                    .append("timeEnd", token.getTimeEnd())
                    .append("value", token.getValue());

            audioTokens.add(tokenDoc);
        }

        return audioTokens;
    }

    /**
     * Analyzes a given JCas by running it through the configured processing pipeline.
     *
     * @param cas The JCas containing the text and annotations to be processed.
     * @throws Exception If an error occurs during the processing pipeline execution.
     * @author Genadij Vorontsov
     */
    public void analyzeCas(JCas cas) throws Exception {
        pComposer.run(cas);
    }
}
