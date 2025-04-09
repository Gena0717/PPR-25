package nlp;

import com.mongodb.client.MongoCollection;
import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import org.apache.uima.cas.CASException;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.CasCopier;
import org.bson.Document;
import org.texttechnologylab.DockerUnifiedUIMAInterface.io.DUUICollectionReader;
import org.texttechnologylab.DockerUnifiedUIMAInterface.monitoring.AdvancedProgressMeter;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


/**
 * DUUI_Reader is a UIMA CollectionReader implementation for loading and providing
 * unprocessed speech documents from a MongoDB collection for NLP processing.
 * It maintains a queue of JCas objects and loads documents asynchronously in the background.
 * @author Genadij Vorontsov
 */
public class DUUI_Reader implements DUUICollectionReader {

    private AdvancedProgressMeter progress;
    private ConcurrentLinkedQueue<JCas> queue;
    private AtomicInteger done;
    private ConcurrentLinkedQueue<String> unprocessedSpeeches;
    private long size;

    /**
     * Initializes the reader with unprocessed speech IDs and the associated MongoDB collection.
     * Starts a background thread to continuously preload speech documents into a queue.
     *
     * @param unprocessedSpeeches List of unprocessed speech IDs.
     * @param speechesCollection MongoDB collection containing speech documents.
     * @throws ResourceInitializationException If UIMA CAS cannot be created.
     * @throws CASException If CAS setup fails.
     * @author Genadij Vorontsov
     */
    public DUUI_Reader(LinkedList<String> unprocessedSpeeches, MongoCollection<Document> speechesCollection) throws ResourceInitializationException, CASException {
        this.progress = new AdvancedProgressMeter(unprocessedSpeeches.size());
        this.unprocessedSpeeches = new ConcurrentLinkedQueue<>(unprocessedSpeeches);
        this.queue = new ConcurrentLinkedQueue<>();
        this.done = new AtomicInteger(0);
        this.size = unprocessedSpeeches.size();
        for (int i = 0; i < 3; i++) {
            queue.add(getCas(speechesCollection.find(new Document("_id", unprocessedSpeeches.poll())).first()));
        }
        Runnable r = () -> {
            while (!unprocessedSpeeches.isEmpty()) {
                if (queue.size() < 20) {
                    String id = unprocessedSpeeches.poll();
                    if (id != null) {
                        try {
                            queue.add(getCas(speechesCollection.find(new Document("_id", id)).first()));
                        } catch (ResourceInitializationException e) {
                            throw new RuntimeException(e);
                        } catch (CASException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

    /**
     * Converts a MongoDB speech document into a JCas object.
     *
     * @param speechDocument The MongoDB document to convert.
     * @return A fully initialized JCas with metadata.
     * @throws ResourceInitializationException If CAS creation fails.
     * @throws CASException If metadata setup fails.
     * @author Genadij Vorontsov
     */
    private JCas getCas(Document speechDocument) throws ResourceInitializationException, CASException {

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
     * Returns the current progress of processed CAS units.
     *
     * @return Progress meter tracking progress.
     * @author Genadij Vorontsov
     */
    @Override
    public AdvancedProgressMeter getProgress() {
        return progress;
    }

    /**
     * Provides the next available CAS from the queue and updates progress.
     * Waits briefly if no CAS is immediately available.
     *
     * @param jCas The JCas object to be filled with the next unit of data.
     * @author Genadij Vorontsov
     */
    @Override
    public void getNextCas(JCas jCas) {
        jCas.reset();
        JCas next = queue.poll();
        if (next == null && !unprocessedSpeeches.isEmpty() ) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            next = queue.poll();
        }
        int done = this.done.incrementAndGet();
        CasCopier.copyCas(next.getCas(), jCas.getCas(), true);
        progress.setDone(done);
        progress.setLeft(size - done);
    }

    /**
     * Indicates whether more data is available to read.
     *
     * @return True if the queue is not empty.
     * @author Genadij Vorontsov
     */
    @Override
    public boolean hasNext() {
        return !queue.isEmpty();
    }

    /**
     * Returns the total number of CAS units expected to be processed.
     *
     * @return Total number of speeches to process.
     * @author Genadij Vorontsov
     */
    @Override
    public long getSize() {
        return size;
    }

    /**
     * Returns the number of CAS units that have already been processed.
     *
     * @return Count of completed CAS units.
     * @author Genadij Vorontsov
     */
    @Override
    public long getDone() {
        return progress.getCount();
    }
}
