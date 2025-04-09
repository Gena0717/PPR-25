package export.xmi_building_blocks;

/**
 * abstract class containing methods to generate xmi files that can be exported
 * @author Sophie Kaiser
 */
public abstract class Xmiify {

    String fileName;

    String pathFileName;

    /**
     * basic constructor for a class that has the toXmi() method
     *
     * @param fileName the name the xmi file will have
     * @param xmiPath the directory the xmi file will be generated in
     * @author Sophie Kaiser
     */
    Xmiify(String fileName, String xmiPath) {
        this.fileName = fileName;
        this.pathFileName = xmiPath + "/" + fileName + ".xmi";
    }

    /**
     * generate an xmi file
     *
     * @return path for the file, from root, including the file name and ending
     * @author Sophie Kaiser
     */
    public String toXmi() {
        return "";
    }
}
