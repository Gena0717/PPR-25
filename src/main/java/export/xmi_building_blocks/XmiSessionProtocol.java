package export.xmi_building_blocks;

/**
 * represents a xmi file that contains a session protocol
 * @author Sophie Kaiser
 */
public class XmiSessionProtocol extends Xmiify {
    /**
     * basic constructor for a class that has the toXmi() method
     *
     * @param fileName the name the xmi file will have
     * @param xmiPath  the directory the xmi file will be generated in
     * @author Sophie Kaiser
     */
    XmiSessionProtocol(String fileName, String xmiPath) {
        super(fileName, xmiPath);
    }
}
