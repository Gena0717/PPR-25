package database;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.List;

/**
 * mongo database handler interface.
 *
 * @author Oliwia Daszczynska
 */
public interface MongoDatabaseHandler {
    /**
     * insert document into collection
     * @author Oliwia Daszczynska
     */
    void insertMDB(MongoCollection<Document> collection, List<Document> documents);

    /**
     * fetches data from database.
     *
     * @author Oliwia Daszczynska
     */
    FindIterable<Document> findMDB(MongoCollection<Document> collection, Bson filter);

    /**
     * modifies data from database
     *
     * @author Oliwia Daszczynska
     */
    void updateMDB(MongoCollection<Document> collection, Bson filter, Bson update);

    /**
     * deletes data from database.
     *
     * @author Oliwia Daszczynska
     */
    void deleteMDB(MongoCollection<Document> documents, Bson filter);

    /**
     * counts data from database.
     *
     * @author Oliwia Daszczynska
     */
    long countMDB(MongoCollection<Document> documents);
}
