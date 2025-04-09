package nlp;

import com.mongodb.client.MongoCollection;
import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import org.apache.commons.io.IOUtils;
import org.apache.uima.cas.CASException;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.CasCopier;
import org.bson.Document;
import org.texttechnologylab.DockerUnifiedUIMAInterface.io.DUUICollectionReader;
import org.texttechnologylab.DockerUnifiedUIMAInterface.monitoring.AdvancedProgressMeter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * DUUI_Reader_Videos is a UIMA CollectionReader for processing and reading video documents
 * from a MongoDB collection in a parallel and asynchronous way.
 *
 * It fetches videos, encodes them in base64, and prepares CAS views for video and transcript.
 * @author Genadij Vorontsov
 */
public class DUUI_Reader_Videos implements DUUICollectionReader {

    private AdvancedProgressMeter progress;
    private ConcurrentLinkedQueue<JCas> queue;
    private AtomicInteger done;
    private ConcurrentLinkedQueue<String> unprocessedVideos;
    private long size;
    private MongoCollection<Document> videosCollection;

    /**
     * Constructor that initializes the reader with unprocessed video IDs and collections.
     * Preloads a number of videos into a queue for processing.
     *
     * @param unprocessedVideos List of video IDs to be processed.
     * @param videosCollection  MongoDB collection containing the video documents.
     * @throws ResourceInitializationException If a UIMA resource could not be initialized.
     * @throws CASException                    If there is an error creating CAS views.
     * @throws IOException                     If an error occurs while accessing the video stream.
     * @author Genadij Vorontsov
     */
    public DUUI_Reader_Videos(LinkedList<String> unprocessedVideos, MongoCollection<Document> videosCollection) throws ResourceInitializationException, CASException, IOException {
        this.progress = new AdvancedProgressMeter(unprocessedVideos.size());
        this.unprocessedVideos = new ConcurrentLinkedQueue<>(unprocessedVideos);
        this.queue = new ConcurrentLinkedQueue<>();
        this.done = new AtomicInteger(0);
        this.size = unprocessedVideos.size();
        this.videosCollection = videosCollection;

        for (int i = 0; i < 5; i++) {
            try {
                JCas cas = getVideoCas(videosCollection.find(new Document("_id", unprocessedVideos.poll())).first());
                queue.add(cas);
            } catch (IOException | ResourceInitializationException | CASException e) {
                System.err.println("Error getting cas" + e.getMessage());
                e.printStackTrace();

            }
        }


        Runnable r = () -> {
            while (!unprocessedVideos.isEmpty()) {
                if (queue.size() < 5) {
                    String id = unprocessedVideos.poll();
                    if (id != null) {
                        try {
                            queue.add(getVideoCas(videosCollection.find(new Document("_id", id)).first()));
                        } catch (ResourceInitializationException | CASException | IOException e) {
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
     * Downloads, encodes and prepares a video as a CAS object with appropriate metadata and views.
     *
     * @param videoDocument MongoDB document containing the video_id and metadata.
     * @return JCas object with "video" and "transcript" views.
     * @throws ResourceInitializationException If UIMA CAS initialization fails.
     * @throws CASException                    If CAS view creation fails.
     * @throws IOException                     If downloading or encoding video fails.
     * @author Genadij Vorontsov
     */
    private JCas getVideoCas(Document videoDocument) throws ResourceInitializationException, CASException, IOException {

        String video_id = videoDocument.getString("video_id");
        String id = videoDocument.getString("_id");

        if (video_id == null || video_id.trim().isEmpty()) {
            throw new IOException("Video ID is null or empty. Skipping video with _id: " + id);
        }

        System.out.println("[Thread-" + Thread.currentThread().getName() + "] getCas with ID, Video-ID: " + video_id + ", " + id);

        String urlString = "https://cldf-od.r53.cdn.tv1.eu/1000153copo/ondemand/app144277506/145293313/" + video_id + "/" + video_id + "_h264_512_288_514kb_baseline_de_514.mp4?fdl=1";
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        String pMimeType = connection.getContentType();
        try (InputStream inputStream = connection.getInputStream()) {
            byte[] videoBytes = IOUtils.toByteArray(inputStream);
            String encodedString = Base64.getEncoder().encodeToString(videoBytes);

            JCas pCas = JCasFactory.createText("", "de");

            DocumentMetaData dmd = new DocumentMetaData(pCas);
            dmd.setDocumentId(id);
            dmd.setDocumentTitle("DUUI on Video");
            dmd.addToIndexes();

            JCas videoCas = pCas.createView("video");
            videoCas.setSofaDataString(encodedString, pMimeType);
            videoCas.setDocumentLanguage("de");

            pCas.createView("transcript");

            return pCas;
        }
    }

    /**
     * Returns the current progress meter of the reader.
     *
     * @return AdvancedProgressMeter instance tracking reading progress.
     * @author Genadij Vorontsov
     */
    @Override
    public AdvancedProgressMeter getProgress() {
        return progress;
    }

    /**
     * Fills the provided CAS object with the next available video CAS from the queue.
     * If the queue is empty, attempts to fetch and prepare a new one.
     *
     * @param jCas The JCas to be filled with the next available video.
     * @author Genadij Vorontsov
     */
    @Override
    public void getNextCas(JCas jCas) {
        System.out.printf("[Thread-%s] Get next CAS...%n", Thread.currentThread().getName());
        jCas.reset();
        JCas next = queue.poll();
        while (next == null && !unprocessedVideos.isEmpty()) {
            String nextId = unprocessedVideos.poll();
            if (nextId != null) {
                try {
                    Document doc = videosCollection.find(new Document("_id", nextId)).first();
                    if (doc != null) {
                        next = getVideoCas(doc);
                    } else {
                        System.err.println("No Document for ID: " + nextId);
                    }
                } catch (FileNotFoundException e) {
                    System.err.println("No File for Video-ID: " + nextId + " – skip.");
                } catch (Exception e) {
                    System.err.println("Error processing Video " + nextId + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        if (next == null) {
            System.err.println("No more Videos");
            return;
        }
        int done = this.done.incrementAndGet();
        CasCopier.copyCas(next.getCas(), jCas.getCas(), true);
        progress.setDone(done);
        progress.setLeft(size - done);
    }

    /**
     * Checks whether more CAS objects are available for processing.
     *
     * @return True if queue or pending IDs are not empty.
     * @author Genadij Vorontsov
     */
    @Override
    public boolean hasNext() {
        return !queue.isEmpty() || !unprocessedVideos.isEmpty();
    }

    /**
     * Returns the total number of videos expected to be processed.
     *
     * @return Number of total items.
     * @author Genadij Vorontsov
     */
    @Override
    public long getSize() {
        return size;
    }

    /**
     * Returns the number of already processed videos.
     *
     * @return Number of completed items.
     * @author Genadij Vorontsov
     */
    @Override
    public long getDone() {
        return progress.getCount();
    }
}
