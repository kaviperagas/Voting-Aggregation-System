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

    /**
     * Checks if a IRV ballot is valid or not (if it has at least 0.5 rounded up candidates ranked)
     *
     * @param numCandidates number of candidates in the election
     * @return true if the ballot is valid and false otherwise
     */
    public Boolean isValid(int numCandidates){
      return this.choices.size() >= (numCandidates+1)/2;
    }

}
