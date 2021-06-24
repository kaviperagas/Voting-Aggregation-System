import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import org.junit.jupiter.api.Test;


public class TestAuditService {
	@Test
	void testConstructorAndAppend() {
		AuditService myAS = new AuditService(); 
		try (FileWriter fr = myAS.getFr()) {
			assertNull(fr);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String name = "";
		AuditService.createAndOpenFile();
		try (FileWriter fr = myAS.getFr()) {
			assertNotNull(fr);
			name = "Audit" + myAS.getDate() + ".txt";
			AuditService.appendToAuditFile("OK");
			AuditService.closeAuditFile();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		Scanner scanner;
		try {
			scanner = new Scanner(new File(name));
			String aString = scanner.nextLine();
			assertEquals ("Audit file created.", aString);
			aString = scanner.nextLine();
			assertEquals ("OK", aString);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}

