package helpers;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import database.MongoDatabaseHandler_Impl;
import org.bson.Document;

import java.util.*;

public class DataChecker {

    /**
     * Finds all speech IDs in the "speeches" collection that do not exist in the "processed_speeches" collection.
     *
     * @param collectionSpeeches The MongoDB collection containing all speeches.
     * @return A set of speech IDs that have not been processed.
     * @author Genadij Vorontsov
     */
    public static LinkedList<String> getUnprocessedSpeechIds(MongoCollection<Document> collectionSpeeches) {
        LinkedList<String> unprocessedSpeechIds = new LinkedList<>();

        AggregateIterable<Document> result = collectionSpeeches.aggregate(Arrays.asList(
                new Document("$lookup", new Document()
                        .append("from", "processed_speeches")
                        .append("localField", "_id")
                        .append("foreignField", "_id")
                        .append("as", "matched")),
                new Document("$match", new Document("matched", new Document("$size", 0)))
        ));

        for (Document doc : result) {
            unprocessedSpeechIds.add(doc.getString("_id"));
        }
        System.out.println("Unprocessed Speech-IDs: " + unprocessedSpeechIds.size());
        return unprocessedSpeechIds;
    }

    /**
     * Finds all speech IDs in the "videos" collection that do not exist in the "processed_videos" collection.
     *
     * @param collectionVideos The MongoDB collection containing all videos.
     * @return A set of speech IDs that have not been processed.
     * @author Genadij Vorontsov
     */
    public static LinkedList<String> getUnprocessedVideos(MongoCollection<Document> collectionVideos) {
        LinkedList<String> unprocessedVideos = new LinkedList<>();

        AggregateIterable<Document> result = collectionVideos.aggregate(Arrays.asList(
                new Document("$lookup", new Document()
                        .append("from", "processed_videos")
                        .append("localField", "_id")
                        .append("foreignField", "_id")
                        .append("as", "matched")),
                new Document("$match", new Document()
                        .append("matched", new Document("$size", 0))
                        .append("video_id", new Document("$ne", "7555255")))
        ));

        for (Document doc : result) {
            unprocessedVideos.add(doc.getString("_id"));
        }
        System.out.println("Unprocessed Videos: " + unprocessedVideos.size());
        return unprocessedVideos;
    }

    /**
     * Finds all transcript IDs in the "processed_videos" collection that do not exist in the "processed_transcripts" collection.
     *
     * @param processedVideosCollection The MongoDB collection containing all processed Videos.
     * @return A list of transcript IDs that have not been processed.
     */
    public static LinkedList<String> getUnprocessedTranscripts(MongoCollection<Document> processedVideosCollection) {
        LinkedList<String> unprocessedTranscriptIds = new LinkedList<>();

        AggregateIterable<Document> result = processedVideosCollection.aggregate(Arrays.asList(
                new Document("$lookup", new Document()
                        .append("from", "processed_transcripts")
                        .append("localField", "_id")
                        .append("foreignField", "_id")
                        .append("as", "matched")),
                new Document("$match", new Document("matched", new Document("$size", 0)))
        ));

        for (Document doc : result) {
            unprocessedTranscriptIds.add(doc.getString("_id"));
        }
        System.out.println("Unprocessed Transcript-IDs: " + unprocessedTranscriptIds.size());
        return unprocessedTranscriptIds;
    }


    /**
     * Retrieves a hash set of video IDs for which no processed version exists yet.
     *
     * @return A set of unprocessed video IDs from the "videos" collection.
     * @author Genadij Vorontsov
     */
    public static HashSet<String> getUnprocessedVideosHashed() {
        HashSet<String> unprocessedSpeechIds = new HashSet<>();
        MongoCollection<Document> collectionVideos = MongoDatabaseHandler_Impl.getCollectionVideos();
        AggregateIterable<Document> result = collectionVideos.aggregate(Arrays.asList(
                new Document("$lookup", new Document()
                        .append("from", "processed_videos")
                        .append("localField", "_id")
                        .append("foreignField", "_id")
                        .append("as", "matched")),
                new Document("$match", new Document("matched", new Document("$size", 0)))
        ));

        for (Document doc : result) {
            unprocessedSpeechIds.add(doc.getString("video_id"));
        }
        return unprocessedSpeechIds;
    }

    /**
     * Returns a mapping from video_id to lists of _id values for all videos
     * that have not yet been processed.
     *
     * @return A map of video_id to unprocessed _id values.
     * @author Genadij Vorontsov
     */
    public static HashMap<String, List<String>> getUnprocessedVideos() {
        HashMap<String, List<String>> unprocessedVideos = new HashMap<>();
        AggregateIterable<Document> result = MongoDatabaseHandler_Impl.getCollectionVideos().aggregate(Arrays.asList(
                new Document("$lookup", new Document()
                        .append("from", "processed_videos")
                        .append("localField", "_id")
                        .append("foreignField", "_id")
                        .append("as", "matched")),
                new Document("$match", new Document("matched", new Document("$size", 0)))
        ));

        for (Document doc : result) {
            String videoId = doc.getString("video_id");
            String id = doc.getString("_id");
            if (unprocessedVideos.containsKey(videoId)) {
                unprocessedVideos.get(videoId).add(id);
            } else {
                List<String> ids = new ArrayList<>();
                ids.add(id);
                unprocessedVideos.put(videoId, ids);
            }
        }
        return unprocessedVideos;
    }

    /**
     * Finds all speech IDs in the "processed_speeches" collection that do not exist in the "speeches" collection.
     *
     * @param collectionProcessedSpeeches The MongoDB collection containing processed speeches.
     * @return A set of processed speech IDs that do not have a corresponding entry in the speeches collection.
     * @author Genadij Vorontsov
     */
    public static Set<String> getProcessedSpeechIdsNotInSpeeches(MongoCollection<Document> collectionProcessedSpeeches) {
        Set<String> processedSpeechIdsNotInSpeeches = new HashSet<>();

        AggregateIterable<Document> result = collectionProcessedSpeeches.aggregate(Arrays.asList(
                new Document("$lookup", new Document()
                        .append("from", "speeches")
                        .append("localField", "_id")
                        .append("foreignField", "_id")
                        .append("as", "matched")),
                new Document("$match", new Document("matched", new Document("$size", 0)))
        ));

        for (Document doc : result) {
            processedSpeechIdsNotInSpeeches.add(doc.getString("_id"));
        }
        System.out.println("Processed Speech-IDs not in speeches: " + processedSpeechIdsNotInSpeeches.size());
        return processedSpeechIdsNotInSpeeches;
    }
}

