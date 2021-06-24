import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;

public class TestPoElection {
  @Test
    public void testConstructor(){
        File myFile = new File("PO.txt");
        ArrayList<File> files = new ArrayList<>();
        files.add(myFile);
        PoElection myElection = new PoElection(files);
        assertSame(files, myElection.getFile());
    }

    @Test
    public void testProcessOneFile() throws URISyntaxException{
      URL path1 = ClassLoader.getSystemResource("PO.txt");
      File myFile1 = new File(path1.toURI());
      ArrayList<File> files = new ArrayList<>();
      files.add(myFile1);
      PoElection myElection = new PoElection(files);
      myElection.processFile();
      assertEquals(6, myElection.getNumCandidates());
      assertEquals(9, myElection.getNumBallots());
      ArrayList<Candidate> myCandidates = new ArrayList<Candidate>();
      Candidate myCandidate1 = new Candidate("Pike", 'D');
      Candidate myCandidate2 = new Candidate("Foster", 'D');
      Candidate myCandidate3 = new Candidate("Deutsch", 'R');
      Candidate myCandidate4 = new Candidate("Borg", 'R');
      Candidate myCandidate5 = new Candidate("Jones", 'R');
      Candidate myCandidate6 = new Candidate("Smith", 'I');
      Ballot myBallot = new Ballot();
      myCandidate1.addBallot(myBallot);
      myCandidate1.addBallot(myBallot);
      myCandidate1.addBallot(myBallot);
      myCandidates.add(myCandidate1);
      myCandidate2.addBallot(myBallot);
      myCandidate2.addBallot(myBallot);
      myCandidates.add(myCandidate2);
      myCandidates.add(myCandidate3);
      myCandidate4.addBallot(myBallot);
      myCandidate4.addBallot(myBallot);
      myCandidates.add(myCandidate4);
      myCandidates.add(myCandidate5);
      myCandidate5.addBallot(myBallot);
      myCandidates.add(myCandidate6);
      myCandidate6.addBallot(myBallot);
      assertEquals(myCandidates.size(), myElection.getCandidates().size());
      for (int i = 0; i < myCandidates.size(); i++){
        assertEquals(myCandidates.get(i).getName(), myElection.getCandidates().get(i).getName());
        assertEquals(myCandidates.get(i).getParty(), myElection.getCandidates().get(i).getParty());
        assertEquals(myCandidates.get(i).getNumBallots(), myElection.getCandidates().get(i).getNumBallots());
        
      }
    }

    @Test
    public void testProcessMultipleFiles() throws URISyntaxException{
      URL path1 = ClassLoader.getSystemResource("PO.txt");
      File myFile1 = new File(path1.toURI());
      File myFile2 = new File(path1.toURI());
      ArrayList<File> files = new ArrayList<>();
      files.add(myFile1);
      files.add(myFile2);
      PoElection myElection = new PoElection(files);
      myElection.processFile();
      assertEquals(6, myElection.getNumCandidates());
      assertEquals(18, myElection.getNumBallots());
      ArrayList<Candidate> myCandidates = new ArrayList<Candidate>();
      Candidate myCandidate1 = new Candidate("Pike", 'D');
      Candidate myCandidate2 = new Candidate("Foster", 'D');
      Candidate myCandidate3 = new Candidate("Deutsch", 'R');
      Candidate myCandidate4 = new Candidate("Borg", 'R');
      Candidate myCandidate5 = new Candidate("Jones", 'R');
      Candidate myCandidate6 = new Candidate("Smith", 'I');
      Ballot myBallot = new Ballot();
      myCandidate1.addBallot(myBallot);
      myCandidate1.addBallot(myBallot);
      myCandidate1.addBallot(myBallot);
      myCandidate1.addBallot(myBallot);
      myCandidate1.addBallot(myBallot);
      myCandidate1.addBallot(myBallot);
      myCandidates.add(myCandidate1);
      myCandidate2.addBallot(myBallot);
      myCandidate2.addBallot(myBallot);
      myCandidate2.addBallot(myBallot);
      myCandidate2.addBallot(myBallot);
      myCandidates.add(myCandidate2);
      myCandidates.add(myCandidate3);
      myCandidate4.addBallot(myBallot);
      myCandidate4.addBallot(myBallot);
      myCandidate4.addBallot(myBallot);
      myCandidate4.addBallot(myBallot);
      myCandidates.add(myCandidate4);
      myCandidates.add(myCandidate5);
      myCandidate5.addBallot(myBallot);
      myCandidate5.addBallot(myBallot);
      myCandidates.add(myCandidate6);
      myCandidate6.addBallot(myBallot);
      myCandidate6.addBallot(myBallot);
      assertEquals(myCandidates.size(), myElection.getCandidates().size());
      for (int i = 0; i < myCandidates.size(); i++){
        assertEquals(myCandidates.get(i).getName(), myElection.getCandidates().get(i).getName());
        assertEquals(myCandidates.get(i).getParty(), myElection.getCandidates().get(i).getParty());
        assertEquals(myCandidates.get(i).getNumBallots(), myElection.getCandidates().get(i).getNumBallots());
        
      }
    }
}
