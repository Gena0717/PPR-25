package export.xmi_building_blocks;

/**
 * represents a xmi file that includes a speaker's full name,
 * party affiliation and, if available, picture
 * @author Sophie Kaiser
 */
public class XmiSpeakerInfo extends Xmiify {
    /**
     * basic constructor for a class that has the toXmi() method
     *
     * @param fileName the name the xmi file will have
     * @param xmiPath  the directory the xmi file will be generated in
     * @author Sophie Kaiser
     */
    XmiSpeakerInfo(String fileName, String xmiPath) {
        super(fileName, xmiPath);
    }
}
