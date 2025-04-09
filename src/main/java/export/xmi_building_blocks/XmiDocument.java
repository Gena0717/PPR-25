package export.xmi_building_blocks;

/**
 * represents a full xmi document
 * @author Sophie Kaiser
 */
public class XmiDocument extends Xmiify {
    /**
     * basic constructor for a class that has the toXmi() method
     *
     * @param fileName the name the xmi file will have
     * @param xmiPath  the directory the xmi file will be generated in
     * @author Sophie Kaiser
     */
    XmiDocument(String fileName, String xmiPath) {
        super(fileName, xmiPath);
    }
}
