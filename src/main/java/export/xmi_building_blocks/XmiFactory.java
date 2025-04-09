package export.xmi_building_blocks;

/**
 * factory class for creating and interacting with .xmi files
 * @author Sophie Kaiser
 */
public class XmiFactory {

    private final String xmiPath;

    /**
     * constructor for the xmi factory
     * @param xmiPath path to the output directory
     * @author Sophie Kaiser
     */
    public XmiFactory(String xmiPath) {
        this.xmiPath = xmiPath;
    }

    /**
     * create new instance of XmiDocument
     * @param fileName file name
     * @return new instance of XmiDocument
     * @author Sophie Kaiser
     */
    public XmiDocument createXmiDocument(String fileName) {
        return new XmiDocument(fileName, this.xmiPath);
    }

    /**
     * create new instance of XmiNLPVisualization
     * @param fileName file name
     * @return new instance of XmiNLPVisualization
     * @author Sophie Kaiser
     */
    public XmiNLPVisualization createXmiNLPVisualization(String fileName) {
        return new XmiNLPVisualization(fileName, this.xmiPath);
    }

    /**
     * create new instance of XmiSessionProtocol
     * @param fileName file name
     * @return new instance of XmiSessionProtocol
     * @author Sophie Kaiser
     */
    public XmiSessionProtocol createXmiSessionProtocol(String fileName) {
        return new XmiSessionProtocol(fileName, this.xmiPath);
    }

    /**
     * create new instance of XmiSpeakerInfo
     * @param fileName file name
     * @return new instance of XmiSpeakerInfo
     * @author Sophie Kaiser
     */
    public XmiSpeakerInfo createXmiSpeakerInfo(String fileName) {
        return new XmiSpeakerInfo(fileName, this.xmiPath);
    }

    /**
     * create new instance of XmiSpeech
     * @param fileName file name
     * @return new instance of XmiSpeech
     * @author Sophie Kaiser
     */
    public XmiSpeech createXmiSpeech(String fileName) {
        return new XmiSpeech(fileName, this.xmiPath);
    }
}
