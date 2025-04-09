package nlp;

import com.mongodb.client.MongoCollection;
import database.MongoDatabaseHandler_Impl;
import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CASException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.bson.Document;
import org.dkpro.core.api.io.JCasFileWriter_ImplBase;
import org.texttechnologylab.annotation.type.AudioToken;
import org.apache.uima.fit.util.JCasUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * UIMA Writer that processes video transcripts and stores tokenized audio and transcription
 * data into a MongoDB collection.
 * The class reads the "transcript" view of the CAS, extracts audio tokens,
 * and saves both token and transcription data into the "processed videos" collection.
 *
 * @author Genadij Vorontsov
 */
public class DUUI_Writer_Videos extends JCasFileWriter_ImplBase {

    private MongoCollection<Document> videosCollection = MongoDatabaseHandler_Impl.getCollectionVideos();
    private MongoCollection<Document> processedVideosCollection;


    /**
     * Initializes the writer and retrieves the collection for processed videos.
     *
     * @param context The UIMA context.
     * @throws ResourceInitializationException if an error occurs during initialization.
     * @author Genadij Vorontsov
     */
    @Override
    public void initialize(UimaContext context) throws ResourceInitializationException {
        super.initialize(context);
        processedVideosCollection = MongoDatabaseHandler_Impl.getCollectionProcessedVideos();
    }


    /**
     * Processes a CAS object, extracts audio tokens and transcription, and stores them in MongoDB.
     *
     * @param cas The CAS object to process.
     * @throws AnalysisEngineProcessException if an error occurs during processing.
     * @author Genadij Vorontsov
     */
    @Override
    public void process(JCas cas) throws AnalysisEngineProcessException {
        try {
            JCas transcript = cas.getView("transcript");
            List<Document> tokens = extractAudioTokens(transcript);
            String transcription = transcript.getSofaDataString();
            String id = DocumentMetaData.get(cas).getDocumentId();
            String video_id = getVideoId(id);

            if (transcription == null || transcription.trim().isEmpty()) {
                System.err.println("WARN: No Transcription : " + id);
                return;
            }

            if (tokens.isEmpty()) {
                System.err.println("WARN: No Tokens: " + id);
                return;
            }

            Document processedVideo = new Document("_id", id)
                    .append("video_id", video_id)
                    .append("audioTokens", tokens)
                    .append("transcription", transcription);
            processedVideosCollection.insertOne(processedVideo);
        } catch (CASException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Inserted processed video: " + DocumentMetaData.get(cas).getDocumentId() + " by Thread: " + Thread.currentThread().getName());
    }


    /**
     * Retrieves the video ID from the original videos collection using the given document ID.
     *
     * @param id The document ID.
     * @return The corresponding video ID, or null.
     * @author Genadij Vorontsov
     */
    private String getVideoId(String id) {
        Document videoDoc = videosCollection.find(new Document("_id", id)).first();
        if (videoDoc != null) {
            return videoDoc.getString("video_id");
        }
        return null;
    }

    /**
     * Extracts video information from a JCas.
     *
     * @param transcriptCas The JCas containing the video information.
     * @return A list of documents with video information (id, begin, end, timeStart, timeEnd, value).
     * @author Genadij Vorontsov
     */
    private List<Document> extractAudioTokens(JCas transcriptCas) {
        List<Document> audioTokens = new ArrayList<>();
        for (AudioToken token : JCasUtil.select(transcriptCas, AudioToken.class)) {
            audioTokens.add(new Document()
                    .append("id", token.getAddress())
                    .append("begin", token.getBegin())
                    .append("end", token.getEnd())
                    .append("timeStart", token.getTimeStart())
                    .append("timeEnd", token.getTimeEnd())
                    .append("value", token.getValue()));
        }
        return audioTokens;
    }
}
