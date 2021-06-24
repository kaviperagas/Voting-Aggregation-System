import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class TestBallot {

    @Test
    public void testConstructor() {
        Ballot myBallot = new Ballot();
        assertNotNull(myBallot);
        assertNull(myBallot.getChoices());
    }

    @Test
    public void testGetChoices() {
        Candidate myCandidate = new Candidate("Bob", 'R');
        ArrayList<Candidate> myCandidateArray = new ArrayList<Candidate>();
        myCandidateArray.add(myCandidate);
        Ballot myBallot = new Ballot(myCandidateArray);
        assertNotNull(myBallot);
        assertNotNull(myBallot.getChoices());
        assertSame(myCandidateArray, myBallot.getChoices());
        assertSame(myBallot.getChoices().get(0), myCandidate);
    }
}
