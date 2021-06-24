import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class TestParty {
  @Test
  public void testConstructor() {
    Party myParty = new Party('X');
    assertEquals('X', myParty.getName());
    assertEquals(new ArrayList<Candidate>(), myParty.getCandidates());
  }

  @Test
  public void testAddCandidate() {
    Party myParty = new Party('X');
    Candidate myCandidate = new Candidate("John", 'X');
    assertSame(0, myParty.getCandidates().size());
    myParty.addCandidate(myCandidate);
    assertSame(1, myParty.getCandidates().size());
    assertSame("John", myParty.getCandidates().get(0).getName());
    assertSame('X', myParty.getCandidates().get(0).getParty());
  }

  @Test
  public void testRankCandidates() {
    Party myParty = new Party('X');
    Ballot myBallot = new Ballot();
    Candidate myCandidate1 = new Candidate("John", 'X');
    Candidate myCandidate2 = new Candidate("Jolly", 'X');
    myParty.setVotes();
    assertSame(0, myParty.getVotes());
    myCandidate1.addBallot(myBallot);
    myCandidate2.addBallot(myBallot);
    myCandidate2.addBallot(myBallot);
    myParty.addCandidate(myCandidate1);
    myParty.addCandidate(myCandidate2);
    myParty.setVotes();
    assertSame(3, myParty.getVotes());
    assertSame("John", myParty.getCandidates().get(0).getName());
    assertSame("Jolly", myParty.getCandidates().get(1).getName());
    myParty.rankCandidates();
    assertSame("Jolly", myParty.getCandidates().get(0).getName());
    assertSame("John", myParty.getCandidates().get(1).getName());
  }

  @Test
  public void testGetterSetterRemainder() {
    Party myParty = new Party('X');
    myParty.setRemainder(6);
    assertSame(6, myParty.getRemainder());
  }

  @Test
  public void testAddSeats() {
    Party myParty = new Party('X');
    myParty.setSeats(6);
    assertSame(6, myParty.getSeats());
    myParty.addSeat();
    assertSame(7, myParty.getSeats());
  }

  @Test
  public void testAssignSeats() {
    Party myParty = new Party('X');
    Ballot myBallot = new Ballot();
    Candidate myCandidate1 = new Candidate("John", 'X');
    Candidate myCandidate2 = new Candidate("Jolly", 'X');
    myParty.setVotes();
    assertSame(0, myParty.getVotes());
    myCandidate1.addBallot(myBallot);
    myCandidate2.addBallot(myBallot);
    myCandidate2.addBallot(myBallot);
    myParty.addCandidate(myCandidate1);
    myParty.addCandidate(myCandidate2);
    myParty.rankCandidates();
    myParty.setSeats(1);
    ArrayList<Candidate> winners = myParty.assignSeats();
    assertSame(1, winners.size());
    assertSame("Jolly", winners.get(0).getName());
  }
}


