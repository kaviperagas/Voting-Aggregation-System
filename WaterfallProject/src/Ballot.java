import java.util.*;

/**
 * Represents a ballot in the election.
 *
 * @author Trevor Guy
 */
public class Ballot {
    /**
     * ArrayList containing the preferred candidates on the ballot.
     */
    private ArrayList<Candidate> choices;

    /**
     * Constructor for ballot for OPL elections.
     */
    public Ballot() {}

    /**
     * Constructor for ballot for IR elections.
     *
     * @param choices An ArrayList of the preferred candidates on the ballot.
     */
    public Ballot(ArrayList<Candidate> choices){
        this.choices = choices;
    }

    /**
     * @return The ArrayList of preferred candidates on the ballot.
     */
    public ArrayList<Candidate> getChoices(){
      return choices;
    }

}
