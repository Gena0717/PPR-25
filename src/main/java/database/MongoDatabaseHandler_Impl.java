package database;

import com.mongodb.client.*;
import helpers.Config;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.Binary;

import java.util.List;

/**
 * handles operations related to mongo database.
 */
public class MongoDatabaseHandler_Impl implements MongoDatabaseHandler {

    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private static MongoCollection<Document> collectionSpeaker;
    private static MongoCollection<Document> collectionSpeeches;
    private static MongoCollection<Document> collectionPictures;
    private static MongoCollection<Document> collectionVideos;
    private static MongoCollection<Document> collectionProcessedSpeeches;
    private static MongoCollection<Document> collectionSerializedSpeeches;
    private static MongoCollection<Document> collectionProcessedVideos;
    private static MongoCollection<Document> collectionSerializedVideos;
    private static MongoCollection<Document> collectionProcessedTranscripts;

    /**
     * insert into database
     *
     * @author Oliwia Daszczynska
     */
    @Override
    public void insertMDB(MongoCollection<Document> collection, List<Document> documents) {
        collection.insertMany(documents);
    }

    /**
     * fetches data from database.
     *
     * @author Oliwia Daszczynska
     */
    @Override
    public FindIterable<Document> findMDB(MongoCollection<Document> collection, Bson filter) {
        return collection.find(filter);
    }

    /**
     * modifies data from database
     *
     * @author Oliwia Daszczynska
     */
    @Override
    public void updateMDB(MongoCollection<Document> collection, Bson filter, Bson update) {
        collection.updateMany(filter, update);
    }

    /**
     * deletes data from database.
     *
     * @author Oliwia Daszczynska
     */
    @Override
    public void deleteMDB(MongoCollection<Document> collection, Bson filter) {
        collection.deleteMany(filter);
    }

    /**
     * aggregates data from database
     *
     * @author Oliwia Daszczynska
     */
    public static AggregateIterable<Document> aggregateMDB(MongoCollection<Document> collection, List<Bson> pipeline) {
        return collection.aggregate(pipeline);
    }

    /**
     * counts data from database.
     *
     * @author Oliwia Daszczynska
     */
    @Override
    public long countMDB(MongoCollection<Document> collection) {
        return collection.countDocuments();
    }


    /**
     * Connects to the MongoDB database.
     * Initializes and assigns the static references to the following
     * collections: "speaker", "speeches", "pictures", "videos", "processed_speeches", "processed_videos", "processed_transcripts" .
     *
     * @author Genadij Vorontsov
     */
    public static void connectDatabase() {
        if (mongoClient == null) {
            try {
                System.out.println("Connecting...");
                Config config = Config.get();
                mongoClient = MongoClients.create(config.MongoConnectionString());
                database = mongoClient.getDatabase(config.getDatabaseName());
                collectionSpeaker = database.getCollection("speaker");
                collectionSpeeches = database.getCollection("speeches");
                collectionPictures = database.getCollection("pictures");
                collectionVideos = database.getCollection("videos");
                collectionProcessedSpeeches = database.getCollection("processed_speeches");
                collectionSerializedSpeeches = database.getCollection("serialized_speeches");
                collectionProcessedVideos = database.getCollection("processed_videos");
                collectionSerializedVideos = database.getCollection("serialized_videos");
                collectionProcessedTranscripts = database.getCollection("processed_transcripts");
                System.out.println("Successfully connected to MongoDB.");
            } catch (Exception e) {
                System.out.println("Could not establish a connection to MongoDB: " + e.getMessage());
            }
        } else {
            System.out.println("Already connected to MongoDB.");
        }
    }

    /**
     * Retrieves the MongoDatabase instance that was set during the database connection.
     *
     * @return MongoDatabase
     * @author Genadij Vorontsov
     */
    public static MongoDatabase getDatabase() {
        return database;
    }

    /**
     * Returns the "speaker" collection from the connected MongoDB database.
     *
     * @return MongoCollection for the "speaker" collection
     * @author Genadij Vorontsov
     */
    public static MongoCollection<Document> getCollectionSpeaker() {
        return collectionSpeaker;
    }

    /***
     * Returns the "speeches" collection from the connected MongoDB database.
     *
     * @return MongoCollection for the "speaker" collection
     * @author Genadij Vorontsov
     */
    public static MongoCollection<Document> getCollectionSpeeches() {
        return collectionSpeeches;
    }

    /***
     * Returns the "processed_speeches" collection from the connected MongoDB database.
     *
     * @return MongoCollection for the "processed_speeches" collection
     * @author Genadij Vorontsov
     */
    public static MongoCollection<Document> getCollectionProcessedSpeeches() {
        return collectionProcessedSpeeches;
    }

    /***
     * Returns the "serialized_speeches" collection from the connected MongoDB database.
     *
     * @return MongoCollection for the "serialized_speeches" collection
     * @author Genadij Vorontsov
     */
    public static MongoCollection<Document> getCollectionSerializedSpeeches() {
        return collectionSerializedSpeeches;
    }

    /***
     * Returns the "pictures" collection from the connected MongoDB database.
     *
     * @return MongoCollection for the "pictures" collection
     * @author Ishak Bouaziz
     */
    public static MongoCollection<Document> getCollectionPictures() {
        return collectionPictures;
    }

    /***
     * Returns the "videos" collection from the connected MongoDB database.
     *
     * @return MongoCollection for the "videos" collection
     * @author Genadij Vorontsov
     */
    public static MongoCollection<Document> getCollectionVideos() {
        return collectionVideos;
    }

    /***
     * Returns the "processed_videos" collection from the connected MongoDB database.
     *
     * @return MongoCollection for the "processed_videos" collection
     * @author Genadij Vorontsov
     */
    public static MongoCollection<Document> getCollectionProcessedVideos() {
        return collectionProcessedVideos;
    }

    /***
     * Returns the "serialized_videos" collection from the connected MongoDB database.
     *
     * @return MongoCollection for the "serialized_videos" collection
     * @author Genadij Vorontsov
     */
    public static MongoCollection<Document> getCollectionSerializedVideos() {
        return collectionSerializedVideos;
    }

    /***
     * Returns the "processed_transcripts" collection from the connected MongoDB database.
     *
     * @return MongoCollection for the "processed_transcripts" collection
     * @author Genadij Vorontsov
     */
    public static MongoCollection<Document> getCollectionProcessedTranscripts() {
        return collectionProcessedTranscripts;
    }

    /**
     * Inserts a new speech document into the "speeches" collection.
     *
     * @param doc Document representing the speech data to be inserted
     * @author Genadij Vorontsov
     */
    public static void addSpeech(Document doc) {
        collectionSpeeches.insertOne(doc);
    }

    /**
     * Inserts a new processed speech document into the "processed_speeches" collection.
     *
     * @param doc Document representing the processed speech data to be inserted
     * @author Genadij Vorontsov
     */
    public static void addProccessedSpeech(Document doc) {
        collectionProcessedSpeeches.insertOne(doc);
    }

    /**
     * Inserts a serialized NLP annotation into the given collection.
     *
     * @param documentId The unique ID used as key for the annotation document
     * @param data       The binary serialized annotation data (e.g. compressed XMI)
     * @param collection The target MongoDB collection to store the annotation
     * @author Genadij Vorontsov
     */
    public static void saveAnnotation(String documentId, byte[] data, MongoCollection<Document> collection) {
        Document document = new Document("_id", documentId)
                .append("annotations", data);
        collection.insertOne(document);
    }

    /**
     * Loads a serialized NLP annotation from the given collection.
     *
     * @param documentId The unique ID used to find the annotation document
     * @param collection The MongoDB collection containing the serialized annotations
     * @return A byte array with the serialized annotation data or null if not found
     * @author Genadij Vorontsov
     */
    public static byte[] loadAnnotation(String documentId, MongoCollection<Document> collection) {
        Document document = collection.find(new Document("_id", documentId)).first();
        Binary binary = document.get("annotations", Binary.class);
        return binary.getData();
    }
}
