package helpers;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/***
 * utility methods
 * @author Oliwia Daszczynska
 */
public class Utils {
    /***
     * Converts stream to string.
     * @return string
     * @throws IOException Signals that an I/O exception has occurred
     * @author Oliwia Daszczynska
     */
    public static String StreamToString(InputStream in) throws IOException {
        InputStreamReader isr = new InputStreamReader(in);
        BufferedReader br = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();

        String str;
        while ((str = br.readLine()) != null) {
            sb.append(str + "\n");
        }
        isr.close();
        return sb.toString();
    }

    /***
     * Method to unzip input files, source:
     * @throws IOException Signals that an I/O exception has occurred.
     * @author <a href="https://www.geeksforgeeks.org/how-to-zip-and-unzip-files-in-java/">source</a>
     */
    public static void unzip(String zipFile, String outpath) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry;
            byte[] buffer = new byte[1024];
            while ((entry = zis.getNextEntry()) != null) {
                File newFile = new File(outpath + File.separator + entry.getName());
                if (entry.isDirectory()) {
                    newFile.mkdirs();
                } else {
                    new File(newFile.getParent()).mkdirs();
                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        int length;
                        while ((length = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, length);
                        }
                    }
                }
            }
        }
    }

    /***
     * Method to decompress gz
     * @author <a href="https://www.geeksforgeeks.org/compressing-decompressing-files-using-gzip-format-java/">source</a>
     */
    static void decompress(String input, String output) {
        byte[] buffer = new byte[1024];
        try {
            GZIPInputStream is = new GZIPInputStream(new FileInputStream(input));

            FileOutputStream out = new FileOutputStream(output);

            int totalSize;
            while ((totalSize = is.read(buffer)) > 0) {
                out.write(buffer, 0, totalSize);
            }

            out.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * Method to write to a JSON file
     */
    public static void writeJsonToFile(String jsonData, String outPath) {
        File file = new File(outPath);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(jsonData);
            System.out.println(outPath + " created.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
