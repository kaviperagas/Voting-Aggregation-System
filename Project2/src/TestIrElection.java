import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;

public class TestIrElection {

    @Test
    public void testConstructor(){
        File myFile = new File("IR.txt");
        ArrayList<File> files = new ArrayList<>();
        files.add(myFile);
        IrElection myElection = new IrElection(files);
        assertSame(files, myElection.getFile());
        assertEquals(myElection.getCandidates(), new ArrayList<Candidate>());
        assertEquals(myElection.getRankedCandidates(), new ArrayList<Candidate>());
    }

    @Test
    public void testProcessOneFile() throws URISyntaxException{
      URL path1 = ClassLoader.getSystemResource("IR.txt");
      File myFile1 = new File(path1.toURI());
      ArrayList<File> files = new ArrayList<>();
      files.add(myFile1);
      IrElection myElection = new IrElection(files);
      myElection.processFile();
      assertEquals(4, myElection.getNumCandidates());
      assertEquals(6, myElection.getNumBallots());
      ArrayList<Candidate> myCandidates = new ArrayList<Candidate>();
      Candidate myCandidate1 = new Candidate("Rosen", 'D');
      Candidate myCandidate2 = new Candidate("Kleinberg", 'R');
      Candidate myCandidate3 = new Candidate("Chou", 'I');
      Candidate myCandidate4 = new Candidate("Royce", 'L');
      Ballot myBallot = new Ballot();
      myCandidate1.addBallot(myBallot);
      myCandidate1.addBallot(myBallot);
      myCandidate1.addBallot(myBallot);
      myCandidates.add(myCandidate1);
      myCandidates.add(myCandidate2);
      myCandidate3.addBallot(myBallot);
      myCandidate3.addBallot(myBallot);
      myCandidates.add(myCandidate3);
      myCandidate4.addBallot(myBallot);
      myCandidates.add(myCandidate4);
      assertEquals(myCandidates.size(), myElection.getCandidates().size());
      for (int i = 0; i < myCandidates.size(); i++){
        assertEquals(myCandidates.get(i).getName(), myElection.getCandidates().get(i).getName());
        assertEquals(myCandidates.get(i).getParty(), myElection.getCandidates().get(i).getParty());
        assertEquals(myCandidates.get(i).getNumBallots(), myElection.getCandidates().get(i).getNumBallots());
        assertEquals(myCandidates.get(i).getName(), myElection.getRankedCandidates().get(i).getName());
        assertEquals(myCandidates.get(i).getParty(), myElection.getRankedCandidates().get(i).getParty());
        assertEquals(myCandidates.get(i).getNumBallots(), myElection.getRankedCandidates().get(i).getNumBallots());
      }
    }

    @Test
    public void testProcessMultipleFiles() throws URISyntaxException{
      URL path1 = ClassLoader.getSystemResource("IR.txt");
      File myFile1 = new File(path1.toURI());
      URL path2 = ClassLoader.getSystemResource("IR2.txt");
      File myFile2 = new File(path2.toURI());
      ArrayList<File> files = new ArrayList<>();
      files.add(myFile1);
      files.add(myFile2);
      IrElection myElection = new IrElection(files);
      myElection.processFile();
      assertEquals(4, myElection.getNumCandidates());
      assertEquals(12, myElection.getNumBallots());
      ArrayList<Candidate> myCandidates = new ArrayList<Candidate>();
      Candidate myCandidate1 = new Candidate("Rosen", 'D');
      Candidate myCandidate2 = new Candidate("Kleinberg", 'R');
      Candidate myCandidate3 = new Candidate("Chou", 'I');
      Candidate myCandidate4 = new Candidate("Royce", 'L');
      Ballot myBallot = new Ballot();
      myCandidate1.addBallot(myBallot);
      myCandidate1.addBallot(myBallot);
      myCandidate1.addBallot(myBallot);
      myCandidate1.addBallot(myBallot);
      myCandidate1.addBallot(myBallot);
      myCandidate1.addBallot(myBallot);
      myCandidates.add(myCandidate1);
      myCandidates.add(myCandidate2);
      myCandidate3.addBallot(myBallot);
      myCandidate3.addBallot(myBallot);
      myCandidate3.addBallot(myBallot);
      myCandidate3.addBallot(myBallot);
      myCandidates.add(myCandidate3);
      myCandidate4.addBallot(myBallot);
      myCandidate4.addBallot(myBallot);
      myCandidates.add(myCandidate4);
      assertEquals(myCandidates.size(), myElection.getCandidates().size());
      for (int i = 0; i < myCandidates.size(); i++){
        assertEquals(myCandidates.get(i).getName(), myElection.getCandidates().get(i).getName());
        assertEquals(myCandidates.get(i).getParty(), myElection.getCandidates().get(i).getParty());
        assertEquals(myCandidates.get(i).getNumBallots(), myElection.getCandidates().get(i).getNumBallots());
        assertEquals(myCandidates.get(i).getName(), myElection.getRankedCandidates().get(i).getName());
        assertEquals(myCandidates.get(i).getParty(), myElection.getRankedCandidates().get(i).getParty());
        assertEquals(myCandidates.get(i).getNumBallots(), myElection.getRankedCandidates().get(i).getNumBallots());
      }
    }

    @Test
    public void testAssignBallots() throws URISyntaxException{
        URL path1 = ClassLoader.getSystemResource("IR.txt");
        File myFile1 = new File(path1.toURI());
        ArrayList<File> files = new ArrayList<>();
        files.add(myFile1);
        IrElection myElection = new IrElection(files);
        myElection.processFile();
        assertSame(3, myElection.getCandidates().get(0).getNumBallots());
        assertSame(0, myElection.getCandidates().get(1).getNumBallots());
        assertSame(2, myElection.getCandidates().get(2).getNumBallots());
        assertSame(1, myElection.getCandidates().get(3).getNumBallots());  
    }

    @Test
    public void testRunElection()throws URISyntaxException{
      URL path1 = ClassLoader.getSystemResource("IR.txt");
      File myFile1 = new File(path1.toURI());
      ArrayList<File> files = new ArrayList<>();
      files.add(myFile1);
      IrElection myElection = new IrElection(files);
      myElection.runElection();
      assertSame(1, myElection.getWinners().size());
      assertEquals("Rosen", myElection.getWinners().get(0).getName());
      assertEquals('D', myElection.getWinners().get(0).getParty());
    }  

    @Test
    public void testInvalidateBallots()throws URISyntaxException{
    	URL path1 = ClassLoader.getSystemResource("IRinvalidballots.txt");
      File myFile1 = new File(path1.toURI());
      ArrayList<File> files = new ArrayList<>();
      files.add(myFile1);
      IrElection myElection = new IrElection(files);
	    myElection.runElection();
	    assertSame(4, myElection.getNumBallots());
      assertSame(1, myElection.getNumValidBallots());
    }  

    @Test
    public void testRunElectionIR2waytie()throws URISyntaxException{
    	URL path1 = ClassLoader.getSystemResource("IR2waytie.txt");
      File myFile1 = new File(path1.toURI());
      ArrayList<File> files = new ArrayList<>();
      files.add(myFile1);
      IrElection myElection = new IrElection(files);
	    myElection.runElection();
	    assertSame(1, myElection.getWinners().size());
	    assertNotEquals("Kleinberg", myElection.getWinners().get(0).getName());
	    assertNotEquals('R', myElection.getWinners().get(0).getParty());
    }  
    
    @Test
    public void testEliminateBallot()throws URISyntaxException{
      URL path1 = ClassLoader.getSystemResource("IReliminateballot.txt");
      File myFile1 = new File(path1.toURI());
      ArrayList<File> files = new ArrayList<>();
      files.add(myFile1);
      IrElection myElection = new IrElection(files);
      myElection.runElection();
	    assertSame(1, myElection.getWinners().size());
	    assertNotEquals("Rosen", myElection.getWinners().get(0).getName());
	    assertNotEquals('D', myElection.getWinners().get(0).getParty());
	    assertNotEquals("Royce", myElection.getWinners().get(0).getName());
	    assertNotEquals('L', myElection.getWinners().get(0).getParty());
    }
}
