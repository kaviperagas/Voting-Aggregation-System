import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Holds methods used to help write to the audit file.
 *
 * @author Thomas Rooney
 */
public class AuditService {
    /**
     * Used when writing to the audit file.
     */
    private static FileWriter fr;

    /**
     * Date and time when audit file is created.
     */
    private static String date;

    /**
     * Creates and then opens an audit file to be written to.
     */
    public static void createAndOpenFile() {
      try {
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy-hhmmss.SSS");
        String date = sdf.format(new Date());
        File file = new File("Audit" + date + ".txt");
        fr = new FileWriter(file, false);
        appendToAuditFile("Audit file created.");
      } catch(Exception e) {
        e.printStackTrace();
      }
    }

    /**
     * Tries to append a string to the end of the audit file.
     *
     * @param value String to be appended to the end of the audit file.
     */
    public static void appendToAuditFile(String value) {
      try {
        fr.write(value + "\n");
      } catch(Exception e) {
        e.printStackTrace();
      }
    }

    /**
     * Closes the audit file after writing to it.
     */
    public static void closeAuditFile() {
      try {
        fr.close();
      } catch(Exception e) {
        e.printStackTrace();
      }
    }


    /**
     * @return The file writer.
     */
    public FileWriter getFr() {
    	return AuditService.fr;
    }

    /**
     * @return Today's date
     */
    public String getDate() {
    	return AuditService.date;
    }
}
