import java.util.*;
import java.io.*;

/**
 * Used to run and hold the methods needed for an OPL Election.
 *
 * @author Cole Thompson
 * @author Ioana Munteanu
 */
public class OplElection extends Election {
    /**
     * An ArrayList that contains all of the Party objects of the election.
     */
    private ArrayList<Party> parties;

    /**
     * Int holding the quota value calculated from the number of total votes and number of seats available.
     */
    private int quota;

    /**
     * Int holding the number of seats remaining that have yet to be distributed.
     */
    private int seatsLeft;

    /**
     * File containing the inputted election file to be read from.
     */
    private ArrayList<File> file;


    /**
     * Constructor for OplElection used to assign the class' private variables.
     *
     * @param file The files inputted by the user to be parsed.
     */
    public OplElection(ArrayList<File> file) {
        this.file = file;
        this.candidates = new ArrayList<Candidate>();
        this.parties = new ArrayList<Party>();
        this.winners = new ArrayList<Candidate>();
    }

    /**
     * Calculates the quota using the number of ballots cast and the number of seats available.
     */
    private void calculateQuota() {
        this.quota = getNumBallots() / getNumSeats();
        AuditService.appendToAuditFile("Calculate quota to be " + this.quota);
    }

    /**
     * Calculates the first distribution of seats to each party, using the calculated quota,
     * and how many total votes that party received.
     */
    private void firstDistribution() {
        AuditService.appendToAuditFile("Start of first distribution.");
        for (int i = 0; i < parties.size(); i++) {
            int seats = parties.get(i).getVotes() / quota;
            parties.get(i).setSeats(seats);
            AuditService.appendToAuditFile("Assign party " + parties.get(i).getName() + " " + seats + " seat(s) from " + parties.get(i).getVotes() + " votes.");
            seatsLeft -= seats;
            int remainder = parties.get(i).getVotes() - seats * quota;
            parties.get(i).setRemainder(remainder);
        }
        AuditService.appendToAuditFile("End of first distribution.");
    }

    /**
     * Ranks the party in the ArrayList parties, based on the remainder of votes
     * they have after the first distribution of seats.
     */
    private void rankParties() {
        AuditService.appendToAuditFile("Begin ranking parties in order based on remainder left.");
        ArrayList<Party> temp = new ArrayList<Party>();
        ArrayList<Integer> indexes = new ArrayList<Integer>();
        while (!parties.isEmpty()) {
            int numVotes = parties.get(0).getRemainder();
            indexes.add(0);
            for (int i = 1; i < parties.size(); i++) {
                if (parties.get(i).getRemainder() > numVotes) {
                    numVotes = parties.get(i).getRemainder();
                    indexes.clear();
                    indexes.add(i);
                } else if (parties.get(i).getRemainder() == numVotes) {
                    indexes.add(i);
                }
            }
            if (indexes.size() > 1) {
                ArrayList<Party> partiesToRemove = new ArrayList<Party>();
                while (!indexes.isEmpty()) {
                    AuditService.appendToAuditFile("Flip a coin for a tie between parties:");
                    for (int i = 0; i < indexes.size(); i++) {
                        AuditService.appendToAuditFile(parties.get(indexes.get(i)).getName() + "");
                    }
                    int tieBreaker = flipCoin(indexes.size());
                    temp.add(parties.get(indexes.get(tieBreaker)));
                    AuditService.appendToAuditFile("Party " + parties.get(indexes.get(tieBreaker)).getName() + " won the tie, and was ranked higher.");
                    partiesToRemove.add(parties.get(indexes.get(tieBreaker)));
                    indexes.remove(tieBreaker);
                    if (indexes.size() == 1) {
                        temp.add(parties.get(indexes.get(0)));
                        indexes.remove(0);
                    }
                }
                for (int i = 0; i < partiesToRemove.size(); i++) {
                    parties.remove(partiesToRemove.get(i));
                }
                partiesToRemove.clear();
            } else {
                temp.add(parties.get(indexes.get(0)));
                parties.remove(indexes.get(0).intValue());
                indexes.remove(0);
            }
        }
        parties = temp;
    }

    /**
     * Calculates the second distribution of seats based on which parties had the highest remainder.
     * Parties can receive at most one seat.
     */
    private void secondDistribution() {
        AuditService.appendToAuditFile("Start of second distribution.");
        while (seatsLeft > 0) {
            for (int i = 0; i < parties.size(); i++) {
                if (seatsLeft > 0) {
                    parties.get(i).addSeat();
                    seatsLeft--;
                    AuditService.appendToAuditFile("Assign party " + parties.get(i).getName() + " a seat from a remainder of " + parties.get(i).getRemainder() + ".");
                }
            }
        }
        AuditService.appendToAuditFile("End of second distribution.");
    }

    /**
     * Redistributes seats from parties that were awarded more seats then they have candidates for to other parties.
     *
     * @param tooManySeats The parties that were awarded more seats then they have candidates for.
     */
    private void redistributionOfSeats(ArrayList<Party> tooManySeats) {
        ArrayList<Party> spaceAvailable = new ArrayList<Party>();
        for (int i = 0; i < parties.size(); i++) {
            if (!tooManySeats.contains(parties.get(i))) {
                spaceAvailable.add(parties.get(i));
            }
        }
        while(seatsLeft > 0) {
            for (int i = 0; i < spaceAvailable.size(); i++) {
                if (seatsLeft > 0) {
                    spaceAvailable.get(i).addSeat();
                    AuditService.appendToAuditFile(spaceAvailable.get(i).getName() + " awarded a seat in the redistribution.");
                    seatsLeft--;
                    if (spaceAvailable.get(i).getCandidates().size() == spaceAvailable.get(i).getSeats()) {
                        spaceAvailable.remove(i);
                        i--;
                    }
                }
            }
            if (spaceAvailable.isEmpty()) {
                AuditService.appendToAuditFile("There are/is " + seatsLeft + " seat(s) still available.");
                break;
            }
        }
    }

    /**
     * Method to call the first and second distribution of seats, along with awarding the
     * highest voted candidates with seats available in each party.
     */
    private void assignSeats() {
        for (Party party: parties) {
          party.setVotes();
        }
        firstDistribution();
        rankParties();
        secondDistribution();
        ArrayList<Party> tooManySeats = new ArrayList<Party>();
        for (int i = 0; i < parties.size(); i++) {
            if (parties.get(i).getCandidates().size() <= parties.get(i).getSeats()) {
                tooManySeats.add(parties.get(i));
                int seatsToRemove = parties.get(i).getSeats() - parties.get(i).getCandidates().size();
                if (parties.get(i).getCandidates().size() < parties.get(i).getSeats()) {
                    AuditService.appendToAuditFile(parties.get(i).getName() + " awarded " + seatsToRemove + " too many seat(s).");
                }
                parties.get(i).setSeats(parties.get(i).getSeats() - seatsToRemove);
                seatsLeft += seatsToRemove;
            }
        }
        if (!tooManySeats.isEmpty()) {
            redistributionOfSeats(tooManySeats);
        }
        AuditService.appendToAuditFile("Distributing seats to candidates with the most votes.");
        for (int i = 0; i < parties.size(); i++) {
            if (parties.get(i).getSeats() > 0) {
                ArrayList<Candidate> partyWinners = parties.get(i).assignSeats();
                for (int j = 0; j < partyWinners.size(); j++) {
                    AuditService.appendToAuditFile("Add " + partyWinners.get(j).getName() + " as a winner with " + partyWinners.get(j).getNumBallots() + " votes.");
                    winners.add(partyWinners.get(j));
                }
            }
        }
    }

    /**
     * Runs an OPL Election, calling methods to parse the input file and to assign seats.
     */
    @Override
    public void runElection() {
        AuditService.appendToAuditFile("Start running an OPL election.");
        processFile();
        assignSeats();
    }

    /**
     * Finds and returns a specified party
     *
     * @param myPartyName the name of the party to search for
     * @return            the Party object that was to be searched for
     */
    private Party findParty (char myPartyName){
      for (Party party : parties) {
          if (party.getName() == myPartyName)
              return party;
          }
      return null;
    }

    /**
     * Method to create the Party and Candidate objects associated with the OPL Election.
     * The line argument is a string of text taken from the inputted file containing relevant information.
     *
     * @param line String containing the names of all the candidates and the party they belong to.
     */
    private void createPartiesAndCandidates(String line){
      String[] lines = line.split(",");
      String candName;
      for (int i = 0; i < lines.length; i += 2) {
        if (i == 0) {
          candName = lines[i].substring(1);
        } else {
          candName = lines[i].substring(2);
        }
        char partyName = lines[i + 1].charAt(0);
        Candidate myCandidate = new Candidate(candName, partyName);
        this.candidates.add(myCandidate);
        Party existingParty = findParty(partyName);
        if (existingParty != null){
            existingParty.addCandidate(myCandidate);
            AuditService.appendToAuditFile("Add " + myCandidate.getName() + " to party " + existingParty.getName());
        } else {
            Party myParty = new Party(partyName);
            AuditService.appendToAuditFile("Create party " + myParty.getName());
            myParty.addCandidate(myCandidate);
            AuditService.appendToAuditFile("Add " + myCandidate.getName() + " to party " + myParty.getName());
            parties.add(myParty);
        }
      }
    }

    /**
     * Method used to read through a ballot and assign it to the correct candidate.
     * A Ballot object is created and added to an ArrayList in Candidate
     *
     * @param line A String containing information from the ballot
     */
    @Override
    protected void assignBallots(String line){
      Ballot myBallot = new Ballot();
      line = line.strip();
      Integer voteIndex = 0;
      Boolean voteFound = false;
      for (int i = 0; i <= line.length() && voteFound == false; i++){
          if (line.charAt(i) == ','){
              voteIndex++;
          }
          if (line.charAt(i) == '1'){
              voteFound = true;
          }
      }
      Candidate votedCandidate = candidates.get(voteIndex);
      char votedParty = votedCandidate.getParty();
      String votedName = votedCandidate.getName();
      for (Party party : parties){
        if (party.getName() == votedParty){
          ArrayList<Candidate> currentCandidates = party.getCandidates();
          for (Candidate candidate : currentCandidates){
            if (candidate.getName().equals(votedName)){
              AuditService.appendToAuditFile("Assign ballot to " + candidate.getName());
              candidate.addBallot(myBallot);
            }
          }
        }
      }
    }

    /**
     * Parses through the election file from and assigns the information in it accordingly.
     * Contains information saying what the election is, how many candidates there are, the names of
     * the candidates and what party the belong to, how many seats are available, how many total ballots
     * were cast, and all of the ballots themselves.
     */
    @Override
    protected void processFile() {
        AuditService.appendToAuditFile("Start processing candidates and ballots.");
        int counter = 0;
        int numFiles = file.size();
        while (counter < numFiles) {
            try {
                FileReader fr = new FileReader(file.get(counter));
                BufferedReader br = new BufferedReader(fr);
                String line;
                int line_no = 1;
                if (counter > 0) {
                    for (int i = 0; i < 4; i++) {
                        line = br.readLine();
                    }
                    line_no = 5;
                }
                while ((line = br.readLine()) != null) {
                    if (line_no == 2) {
                        setNumCandidates(Integer.parseInt(line));
                        AuditService.appendToAuditFile("Set number of candidates to " + getNumCandidates());
                    }
                    if (line_no == 3) {
                        createPartiesAndCandidates(line);
                    }
                    if (line_no == 4) {
                        setNumSeats(Integer.parseInt(line));
                        AuditService.appendToAuditFile("Set number of seats to " + getNumSeats());
                        seatsLeft = getNumSeats();
                    }
                    if (line_no == 5) {
                        addNumBallots(Integer.parseInt(line));
                        AuditService.appendToAuditFile("Add " + getNumBallots() + " ballot(s) to the election");       // need to figure out how things will be different in the election files
                        calculateQuota();
                    }
                    if (line_no >= 6 && line_no < getNumBallots() + 6) {
                        assignBallots(line);
                    }
                    line_no += 1;
                }
                fr.close();
                counter++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @return The election files.
     */
    public ArrayList<File> getFile(){
        return this.file;
      }

    /**
     * @return The quota of the election.
     */
    public int getQuota(){
      return this.quota;
    }

    /**
     * @return The ArrayList of all the parties in the election.
     */
    public ArrayList<Party> getParties(){
        return this.parties;
      }

    /**
     * @return All of the winners of the election.
     */
    public ArrayList<Candidate> getWinners(){
        return this.winners;
      }

    /**
     * @return All of the candidates in the election.
     */
    public ArrayList<Candidate> getCandidates(){
      return this.candidates;
    }
}
