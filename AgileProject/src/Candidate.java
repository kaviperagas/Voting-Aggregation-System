import java.util.*;

/**
 * Represents a candidate in the election.
 *
 * @author Thomas Rooney
 */
public class Candidate {
  /**
   * Name of the candidate.
   */
  private String name;

  /**
   * Party the candidate belongs to.
   */
  private char party;
  
  /**
   * An ArrayList holding the ballots that voted for the candidate.
   */
  private ArrayList<Ballot> ballots;

  /**
   * Constructor for Candidate.
   *
   * @param name  The name of the candidate.
   * @param party The name of the party the candidate belongs to.
   */
  public Candidate(String name, char party){
    this.name = name;
    this.party = party;
    this.ballots = new ArrayList<Ballot>();
  }

  /**
   * @return The name of the candidate.
   */
  public String getName() {
    return name;
  }

  /**
   * @return The name of the party the candidate belongs to.
   */
  public char getParty() {
    return party;
  }

  /**
   * Adds a Ballot object to an ArrayList.
   *
   * @param ballot A ballot that voted for the candidate.
   */
  public void addBallot(Ballot ballot) {
    this.ballots.add(ballot);
  }

  /**
   * Deletes a specified ballot from the candidates ArrayList of ballots.
   *
   * @param ballot The ballot to be deleted.
   */
  public void deleteBallot(Ballot ballot) {
    this.ballots.remove(ballot);
  }

  /**
   * @return An ArrayList of all the ballots.
   */
  public ArrayList<Ballot> getBallots() {
    return ballots;
  }

  /**
   * @return The total number of ballots a candidate has received.
   */
  public Integer getNumBallots() {
    return ballots.size();
  }
}
