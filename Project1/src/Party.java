import java.util.*;

/**
 * Represents a party in the election.
 *
 * @author Cole Thompson
 */
public class Party {
    /**
     * Name of the party.
     */
    private char name;

    /**
     * ArrayList of candidates that belong to the party.
     */
    private ArrayList<Candidate> candidates;

    /**
     * Total number of votes that the candidates in the party received.
     */
    private int votes;

    /**
     * Remainder of votes the party has after the first distribution.
     */
    private int remainder;
    
    /**
     * The number of seats the party won to be distributed to its candidates.
     */
    private int seats;

    /**
     * Constructor used to assign the class' private variables.
     *
     * @param name The name of the party, represented by a character.
     */
    public Party(char name) {
        this.name = name;
        this.candidates = new ArrayList<Candidate>();
    }

    /**
     * @param candidate Candidate to be added to the ArrayList
     */
    public void addCandidate(Candidate candidate) {
        this.candidates.add(candidate);
    }

    /**
     * @return The ArrayList of candidates belonging to the party
     */
    public ArrayList<Candidate> getCandidates() {
        return candidates;
    }

    /**
     * Ranks the candidates in the candidates ArrayList in order based on how many
     * votes they each received.
     */
    public void rankCandidates() {
        AuditService.appendToAuditFile("Begin ranking candidates in order based on votes received.");
        ArrayList<Candidate> temp = new ArrayList<Candidate>();
        ArrayList<Integer> indexes = new ArrayList<Integer>();
        while (!candidates.isEmpty()) {
            int numVotes = candidates.get(0).getNumBallots();
            indexes.add(0);
            for (int i = 1; i < candidates.size(); i++) {
                if (candidates.get(i).getNumBallots() > numVotes) {
                    numVotes = candidates.get(i).getNumBallots();
                    indexes.clear();
                    indexes.add(i);
                } else if (candidates.get(i).getNumBallots() == numVotes) {

                    indexes.add(i);
                }
            }
            if (indexes.size() > 1) {
                ArrayList<Candidate> candidatesToRemove = new ArrayList<Candidate>();
                while (!indexes.isEmpty()) {
                    AuditService.appendToAuditFile("Flip a coin for a tie between candidates: ");
                    for (int i = 0; i < indexes.size(); i++) {
                        AuditService.appendToAuditFile(candidates.get(indexes.get(i)).getName());
                    }
                    int tieBreaker = Election.flipCoin(indexes.size());
                    temp.add(candidates.get(indexes.get(tieBreaker)));
                    AuditService.appendToAuditFile("Candidate " + candidates.get(indexes.get(tieBreaker)).getName() + " won the tie, and was ranked higher.");
                    candidatesToRemove.add(candidates.get(indexes.get(tieBreaker)));
                    indexes.remove(tieBreaker);
                    if (indexes.size() == 1) {
                        temp.add(candidates.get(indexes.get(0)));
                        indexes.remove(0);
                    }
                }
                for (int i = 0; i < candidatesToRemove.size(); i++) {
                    candidates.remove(candidatesToRemove.get(i));
                }
                candidatesToRemove.clear();
            } else {
                temp.add(candidates.get(indexes.get(0)));
                candidates.remove(indexes.get(0).intValue());
                indexes.remove(0);
            }
        }
        candidates = temp;
    }

    /**
     * @return The name of the party.
     */
    public char getName() {
        return name;
    }

    /**
     * @return The total number of votes the party received.
     */
    public int getVotes() {
        return votes;
    }

    /**
     * Calculates the total number of votes the party received calculated from each
     * of the candidates.
     */
    public void setVotes() {
      for (Candidate candidate :candidates) {
        votes += candidate.getNumBallots();
      }
    }

    /**
     * @return The remainder of votes the party has left after the first distribution.
     */
    public int getRemainder() {
        return remainder;
    }

    /**
     * @param temp The remainder of votes the party has left after the first distribution.
     */
    public void setRemainder(int temp) {
        remainder = temp;
    }

    /**
     * @return The total number of seats that the party has to assign
     */
    public int getSeats() {
        return seats;
    }

    /**
     * @param temp The total number of seats that the party has won.
     */
    public void setSeats(int temp) {
        seats = temp;
    }

    /**
     * Method used to add 1 seat to the parties total, awarded from the second
     * distribution, and redistribution of seats from having the highest remainder.
     */
    public void addSeat() {
        seats++;
    }

    /**
     * Ranks the candidates then awards the highest voted ones with a seat, until
     * no seats are available.
     *
     * @return The candidates from the party that high the highest votes and won the seats.
     */
    public ArrayList<Candidate> assignSeats() {
        rankCandidates();
        ArrayList<Candidate> partyWinners = new ArrayList<Candidate>();
        while (seats > 0) {
            partyWinners.add(candidates.get(0));
            candidates.remove(0);
            seats--;
        }
        return partyWinners;
    }
}
