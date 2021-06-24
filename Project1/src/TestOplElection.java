import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;


public class TestOplElection {

    @Test
    public void testConstructor(){
        File myFile = new File("OPL.txt");
        OplElection myElection = new OplElection(myFile);
        assertNotNull(myElection);
        assertSame(myFile, myElection.getFile());
        assertEquals(myElection.getCandidates(), new ArrayList<Candidate>());
        assertEquals(myElection.getParties(), new ArrayList<Party>());
        assertEquals(myElection.getWinners(), new ArrayList<Candidate>());
    }

    @Test
    public void testProcessFile() throws URISyntaxException{
      URL path = ClassLoader.getSystemResource("OPL.txt");
      File myFile = new File(path.toURI());
      OplElection myElection = new OplElection(myFile);
      myElection.processFile();
      assertEquals(3, myElection.getParties().size());
      assertEquals(6, myElection.getCandidates().size());
      assertEquals(9, myElection.getNumBallots());
      assertEquals(3, myElection.getNumSeats());
      ArrayList<Candidate> myCandidates = new ArrayList<Candidate>();
      Candidate myCandidate1 = new Candidate("Pike",'D');
      Candidate myCandidate2 = new Candidate("Foster",'D');
      Candidate myCandidate3 = new Candidate("Deutsch",'R');
      Candidate myCandidate4 = new Candidate("Borg",'R');
      Candidate myCandidate5 = new Candidate("Jones",'R');
      Candidate myCandidate6 = new Candidate("Smith",'I');
      myCandidates.add(myCandidate1);
      myCandidates.add(myCandidate2);
      myCandidates.add(myCandidate3);
      myCandidates.add(myCandidate4);
      myCandidates.add(myCandidate5);
      myCandidates.add(myCandidate6);
      assertEquals(myCandidates.size(), myElection.getCandidates().size());
      for (int i = 0; i < myCandidates.size(); i++){
        assertEquals(myCandidates.get(i).getName(), myElection.getCandidates().get(i).getName());
        assertEquals(myCandidates.get(i).getParty(), myElection.getCandidates().get(i).getParty());
      }
      assertEquals(3, myElection.getQuota());
    }

    
    @Test
    public void testAssignBallots() throws URISyntaxException{
      URL path = ClassLoader.getSystemResource("OPL.txt");
      File myFile = new File(path.toURI());
      OplElection myElection = new OplElection(myFile);
      myElection.processFile();
      Candidate myCandidate1 = new Candidate("Pike",'D');
      Candidate myCandidate2 = new Candidate("Foster",'D');
      Candidate myCandidate3 = new Candidate("Deutsch",'R');
      Candidate myCandidate4 = new Candidate("Borg",'R');
      Candidate myCandidate5 = new Candidate("Jones",'R');
      Candidate myCandidate6 = new Candidate("Smith",'I');
      Party democrat = new Party('D');
      democrat.addCandidate(myCandidate1);
      democrat.addCandidate(myCandidate2);
      Party republican = new Party('R');
      republican.addCandidate(myCandidate3);
      republican.addCandidate(myCandidate4);
      republican.addCandidate(myCandidate5);
      Party independent = new Party('I');
      independent.addCandidate(myCandidate6);
      ArrayList<Party> myParties = new ArrayList<Party>();
      myParties.add(democrat);
      myParties.add(republican);
      myParties.add(independent);
      assertEquals(myParties.size(), myElection.getParties().size());
      for (int i = 0; i < myParties.size(); i++){
        assertEquals(myParties.get(i).getName(), myElection.getParties().get(i).getName());
        assertEquals(myParties.get(i).getCandidates().size(), myElection.getParties().get(i).getCandidates().size());
        for (int j = 0; j < myParties.get(i).getCandidates().size(); j++){
          assertEquals(myParties.get(i).getCandidates().get(j).getName(), myElection.getParties().get(i).getCandidates().get(j).getName());
          assertEquals(myParties.get(i).getCandidates().get(j).getParty(), myElection.getParties().get(i).getCandidates().get(j).getParty());
        }
      }
      assertEquals(3, myElection.getQuota());
    }

    @Test
    public void testRunElection() throws URISyntaxException{
      URL path = ClassLoader.getSystemResource("OPL.txt");
      File myFile = new File(path.toURI());
      OplElection myElection = new OplElection(myFile);
      myElection.runElection();
      assertSame(3, myElection.getWinners().size());
      assertEquals("Pike", myElection.getWinners().get(0).getName());
      assertEquals('D', myElection.getWinners().get(0).getParty());
      assertSame(3, myElection.getWinners().get(0).getNumBallots());
      assertEquals("Foster", myElection.getWinners().get(1).getName());
      assertEquals('D', myElection.getWinners().get(1).getParty());
      assertSame(2, myElection.getWinners().get(1).getNumBallots());
      assertEquals("Borg", myElection.getWinners().get(2).getName());
      assertEquals('R', myElection.getWinners().get(2).getParty());
      assertSame(2, myElection.getWinners().get(2).getNumBallots());
    }

    @Test
    public void testRunElectionTie() throws URISyntaxException{
      URL path = ClassLoader.getSystemResource("OPLTIE.txt");
      File myFile = new File(path.toURI());
      OplElection myElection = new OplElection(myFile);
      myElection.runElection();
      assertSame(4, myElection.getWinners().size());
    }
    
    @Test
    public void testRunElectionTooManySeats() throws URISyntaxException{
      URL path = ClassLoader.getSystemResource("OPLTOOMANYSEATS.txt");
      File myFile = new File(path.toURI());
      OplElection myElection = new OplElection(myFile);
      myElection.runElection();
      assertSame(3, myElection.getWinners().size());
    }
    
    @Test
    public void testRunElectionTooManySeats2() throws URISyntaxException{
      URL path = ClassLoader.getSystemResource("OPLTOOMANYSEATS2.txt");
      File myFile = new File(path.toURI());
      OplElection myElection = new OplElection(myFile);
      myElection.runElection();
      assertEquals(6, myElection.getWinners().size());
    }
}


