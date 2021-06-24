import java.util.*;

/**
 * An abstract class for the election that inherits to OPL and
 * IR elections.
 *
 * @author Ioana Munteanu
 */
abstract class Election {
  /**
   * The number of candidates running in an election.
   */
  protected int numCandidates;

  /**
   * The total number ballots that were cast during an election.
   */
  protected int numBallots;

  /**
   * The total number of seats available to be won.
   */
  protected int numSeats;

  /**
   * An ArrayList containing the candidate(s) who won a seat.
   */
  protected ArrayList<Candidate> winners;

  /**
   * An ArrayList to hold the candidates before being assigned to the Party objects.
   */
  protected ArrayList<Candidate> candidates;

  /**
   * @return The number of candidates in the election
   */
  public int getNumCandidates() {
    return numCandidates;
  }

  /**
     * @return The candidates in the election.
     */
  public abstract ArrayList<Candidate> getCandidates();


  /**
   * @param numCandidates The number of candidates to be set
   */
  public void setNumCandidates(int numCandidates) {
    this.numCandidates = numCandidates;
  }

  /**
   * @return The number of seats available in an election
   */
  public int getNumSeats() {
    return numSeats;
  }

  /**
   * @param numSeats The number of seats to be set
   */
  public void setNumSeats(int numSeats) {
    this.numSeats = numSeats;
  }

  /**
   * @return The number of ballots cast
   */
  public int getNumBallots() {
    return numBallots;
  }

  /**
   * @param numBallots The total number of ballots cast
   */
  public void setNumBallots(int numBallots) {
    this.numBallots = numBallots;
  }

  /**
   * @return An ArrayList of the winner(s) of the election
   */
  public ArrayList<Candidate> getWinners(){
    return winners;
  }


  /**
   * Used to break ties that happen in the election.
   * Generates a random int to decide between however many choices.
   *
   * @param numEquals The amount of things that are tied.
   * @return A random int between 0 and numEquals inclusively.
   */
  public static int flipCoin(int numEquals){
    Random rand = new Random();
    return rand.nextInt(numEquals);
  }

  /**
   * Abstract method to be used to run the specified election.
   */
  public abstract void runElection();

  /**
   * Abstract method to be used to parse through a line containing a ballot and assign the ballot to a candidate.
   * 
   * @param line a line of the file containing information about a ballot
   */
  protected abstract void assignBallots(String line);

  /**
   * Abstract method to be used to parse through the inputted file for the specified election.
   */
  protected abstract void processFile();
}
