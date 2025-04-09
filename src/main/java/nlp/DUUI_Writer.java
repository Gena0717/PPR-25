package nlp;

import com.mongodb.client.MongoCollection;
import database.MongoDatabaseHandler_Impl;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.dependency.Dependency;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.bson.Document;
import org.dkpro.core.api.io.JCasFileWriter_ImplBase;
import org.hucompute.textimager.uima.type.Sentiment;
import org.hucompute.textimager.uima.type.category.CategoryCoveredTagged;
import org.texttechnologylab.annotation.type.AudioToken;

import java.util.ArrayList;
import java.util.List;

/**
 * DUUI_Writer is a custom UIMA AnalysisEngine component that stores processed
 * NLP data into a MongoDB collection. It extracts annotations like tokens,
 * dependencies, sentiments, etc., from the JCas and structures them into BSON documents.
 * @author Genadij Vorontsov
 */
public class DUUI_Writer extends JCasFileWriter_ImplBase {

    private MongoCollection<Document> collection;

    /**
     * Initializes the AnalysisEngine
     *
     * @param context The UIMA context containing runtime parameters.
     * @throws ResourceInitializationException If initialization fails.
     * @author Genadij Vorontsov
     */
    @Override
    public void initialize(UimaContext context) throws ResourceInitializationException {
        super.initialize(context);
        collection = MongoDatabaseHandler_Impl.getCollectionProcessedSpeeches();
    }

    /**
     * Processes the given JCas by extracting NLP annotations and inserting them into MongoDB.
     *
     * @param cas The JCas to be processed.
     * @throws AnalysisEngineProcessException If an error occurs during processing.
     * @author Genadij Vorontsov
     */
    @Override
    public void process(JCas cas) throws AnalysisEngineProcessException {
        DocumentMetaData id = DocumentMetaData.get(cas);
        Document processedSpeech = new Document("_id", id.getDocumentId())
                .append("sentences", extractSentences(cas))
                .append("tokens", extractTokens(cas))
                .append("posTags", extractPosTags(cas))
                .append("lemmas", extractLemmas(cas))
                .append("dependencies", extractDependencies(cas))
                .append("namedEntities", extractNamedEntities(cas))
                .append("topics", extractTopics(cas))
                .append("sentiments", extractSentiments(cas));

        collection.insertOne(processedSpeech);
        System.out.println("Inserted processed speech with ID: " + id.getDocumentId());
    }

    /**
     * Extracts sentences from a JCas and returns them as a list of documents.
     *
     * @param cas The JCas containing the sentences.
     * @return A list of documents with sentence information (id, begin, end).
     * @author Genadij Vorontsov
     */
    private List<Document> extractSentences(JCas cas) {
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
    private List<Document> extractTokens(JCas cas) {
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
    private List<Document> extractPosTags(JCas cas){
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
    private List<Document> extractLemmas(JCas cas) {
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
    private List<Document> extractDependencies(JCas cas) {
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
    private List<Document> extractNamedEntities(JCas cas) {
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
    private List<Document> extractTopics(JCas cas) {
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
    private List<Document> extractSentiments(JCas cas) {
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
    private List<Document> extractAudioTokens(JCas transcriptCas) {
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
}
