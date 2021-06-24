import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class TestCandidate {
  @Test
  public void testConstructor() {
    Candidate myCandidate = new Candidate ("Bob", 'R');
    assertEquals("Bob", myCandidate.getName());
    assertEquals('R', myCandidate.getParty());
    assertEquals(new ArrayList<Ballot>(), myCandidate.getBallots());
    assertSame(0, myCandidate.getNumBallots());
    assertEquals(new ArrayList<Ballot>(), myCandidate.getBallots());
    assertSame(0, myCandidate.getNumBallots());
  }

  @Test
  public void addBallot(){
    Candidate myCandidate = new Candidate ("Bob", 'R');
    assertSame(0, myCandidate.getNumBallots());
    Ballot myBallot = new Ballot();
    myCandidate.addBallot(myBallot);
    assertSame(1, myCandidate.getNumBallots());
    myCandidate.addBallot(myBallot);
    assertSame(2, myCandidate.getNumBallots());
  }

  @Test
  public void deleteBallot(){
    Candidate myCandidate = new Candidate ("Bob", 'R');
    assertSame(0, myCandidate.getNumBallots());
    Ballot myBallot1 = new Ballot();
    Ballot myBallot2 = new Ballot();
    myCandidate.addBallot(myBallot1);
    myCandidate.addBallot(myBallot2);
    assertSame(2, myCandidate.getNumBallots());
    myCandidate.deleteBallot(myBallot2);
    assertSame(1, myCandidate.getNumBallots());
    ArrayList<Ballot> myBallots = new ArrayList<Ballot>();
    myBallots.add(myBallot1);
    assertEquals(myBallots, myCandidate.getBallots());
  }
}


