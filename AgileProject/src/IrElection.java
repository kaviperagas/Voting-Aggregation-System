import java.util.*;
import java.io.*;

/**
 * Used to run and hold the methods needed for an IR Election.
 *
 * @author Trevor Guy
 * @author Ioana Munteanu
 */
public class IrElection extends Election{
    /**
     * An ArrayList that contains all of the Candidates in the election.
     */
    protected ArrayList<Candidate> candidates;

    /**
     * An ArrayList of the candidates ranked in order of how many ballots they have.
     */
    private ArrayList<Candidate> rankedCandidates;

    /**
     * File containing the inputted election file to be read from
     */
    private ArrayList<File> files;

    /**
     * The number of votes necessary to reach majority.
     * */
    private Integer majority;

    /**
     * Constructor for IrElection used to assign the class' private variables.
     *
     * @param files The election files inputted by the user to be parsed.
     */
    public IrElection(ArrayList<File> files){ // constructor
        this.files = files;
        this.candidates = new ArrayList<Candidate>();
        this.rankedCandidates = new ArrayList<Candidate>();
        this.winners = new ArrayList<Candidate>();
    }

    /**
     * Eliminates the candidate with the least number of votes in candidates.
     *
     * @return The candidate that was eliminated.
     */
    private Candidate eliminateCandidate(){
        Integer lowestNumBallots = rankedCandidates.get(rankedCandidates.size() - 1).getNumBallots();
        Integer countLosers = 0;
        // Count how many candidates still in the election have the least number of votes
        AuditService.appendToAuditFile("Candidate(s) with the lowest number of votes:");
        for (int i = 0; i<rankedCandidates.size(); i++){
            if (rankedCandidates.get(i).getNumBallots() == lowestNumBallots){
                countLosers++;
                AuditService.appendToAuditFile(rankedCandidates.get(i).getName());
            }
        }
        //  Randomly decide who will be eliminated if there is more than 1 loser.
        if (countLosers > 1){
          AuditService.appendToAuditFile("Flip a coin to decide who will be eliminated.");
          Integer randomLoser = flipCoin(countLosers);
          Candidate removedCandidate = rankedCandidates.get(rankedCandidates.size() - randomLoser - 1);
          AuditService.appendToAuditFile("Remove " + removedCandidate.getName());
          this.rankedCandidates.remove(rankedCandidates.size() - randomLoser - 1);
          return removedCandidate;
        }
        // Eliminate the loser if there is only one loser.
        else{
          Candidate removedCandidate = rankedCandidates.get(rankedCandidates.size() - 1);
          AuditService.appendToAuditFile("Remove " + removedCandidate.getName());
          this.rankedCandidates.remove(rankedCandidates.size() - 1);
          return removedCandidate;
        }
    }

    /**
     * Checks to see if any candidate has achieved a majority and won the election.
     *
     * @return Boolean saying if a candidate has reached majority or not.
     */
    private Boolean isMajority(){
        Candidate leader = this.rankedCandidates.get(0);
        Integer leaderVotes = leader.getNumBallots();
        if (leaderVotes >= getMajority()){
            winners.add(rankedCandidates.get(0));
            AuditService.appendToAuditFile(rankedCandidates.get(0).getName() + " has the majority.");
            AuditService.appendToAuditFile("\nThe winner is " + winners.get(0).getName() + " of party " + winners.get(0).getParty()+ "\n");
            return true;
        }
        AuditService.appendToAuditFile("There is no majority.");
        return false;
    }

    /**
     * Ranks the candidates in order of how many ballots they've received.
     *
     * @param roundNumber the number corresponding to the current round of the election
     */
    private void rankCandidates(int roundNumber){
        AuditService.appendToAuditFile("\nRound "+ roundNumber);
        this.rankedCandidates.sort(Comparator.comparing(Candidate::getNumBallots).reversed());
        AuditService.appendToAuditFile("Rank the candidates.");
        for (int i = 0; i<rankedCandidates.size(); i++){
          String candInfo = "";
          candInfo += rankedCandidates.get(i).getName() +":";
          candInfo += " " + rankedCandidates.get(i).getNumBallots() + " votes";
          AuditService.appendToAuditFile(candInfo);
        }
        AuditService.appendToAuditFile("End rank the candidates.");
    }

    /**
     * Redistributes the ballots of a candidate who lost to the next preferred candidate listed on
     * the ballot.
     *
     * @param candidate The candidate whose votes are to be redistributed.
     */
    private void redistributeVotes(Candidate candidate){
      AuditService.appendToAuditFile("Redistribute votes.");
      ArrayList<Ballot> ballotsOwned = candidate.getBallots();
      Integer i = 0;
      while (i < ballotsOwned.size()){
          Ballot temp = ballotsOwned.get(i);
          //Check if we should still keep the ballot in the election
          if (temp.getChoices().size() > 1) {
            temp.getChoices().remove(0);
            Boolean ballotAssigned = false;
            //Assign the ballot to the next choice
            while ((temp.getChoices().size() > 0) && (!ballotAssigned)){
                if (this.rankedCandidates.contains(temp.getChoices().get(0))){
                    temp.getChoices().get(0).addBallot(temp);
                    ballotAssigned = true;
                } else {
                    temp.getChoices().remove(0);
                }
            }
          } else {
            candidate.deleteBallot(temp);
          }
          i++;
      }
      for (int j = 0; j<rankedCandidates.size(); j++){
        String candInfo = "";
        candInfo += rankedCandidates.get(j).getName();
        candInfo += " " + rankedCandidates.get(j).getNumBallots() + " votes";
        AuditService.appendToAuditFile(candInfo);
      }
      AuditService.appendToAuditFile("End redistribute votes.");
  }

    /**
     * Runs an IR Election, calling methods to parse the file, rank the candidates,
     * and check for a majority winner.
     */
    @Override
    public void runElection(){
        AuditService.appendToAuditFile("\nStart running an IR election.");
        processFile();
        rankCandidates(1);
        int idx = 2;
        while (!isMajority()){
          if (rankedCandidates.size() == 2) {
            String cand0 = rankedCandidates.get(0).getName();
            String cand1 = rankedCandidates.get(1).getName();
	          AuditService.appendToAuditFile("There is a tie between " + cand0 + " and " + cand1 + ".");
            AuditService.appendToAuditFile("Flip a coin to decide the winner of the election.");
	          int winnerIndex = flipCoin(2);
	          winners.add(rankedCandidates.get(winnerIndex));
            AuditService.appendToAuditFile("\nThe winner is " + winners.get(0).getName() + " of party " + winners.get(0).getParty()+"\n");
	          break;
          } else {
            redistributeVotes(eliminateCandidate());
            rankCandidates(idx);
            idx ++;
          }
        }

    }

    /**
     * Method used to read through a ballot and assign it to the correct candidate.
     * A Ballot object is created and added to an ArrayList in Candidate
     *
     * @param line A String containing information from the ballot.
     */
    @Override
    protected void assignBallots(String line){
       Integer[] voterPreference = new Integer[getNumCandidates()];
       Integer candidateIndex = 0;
       Candidate[] myCandidateList = new Candidate[getNumCandidates()];
       ArrayList<Candidate> myCandidateArray = new ArrayList<Candidate>();

       for (int j = 0; j < voterPreference.length; j++){
           voterPreference[j] = -1;
       }

       for(int i = 0; i < line.length(); i++){
           char myChar = line.charAt(i);
           if (myChar == ','){
               candidateIndex++;
           }
           if (java.lang.Character.isDigit(myChar)){
               voterPreference[candidateIndex] = myChar - '0';
           }
       }

       for (int j = 0; j < voterPreference.length; j++){
          if (voterPreference[j] - 1 >= 0) {
           myCandidateList[voterPreference[j] - 1] = candidates.get(j);
          }
       }

       for (int k = 0; k < myCandidateList.length; k++) {
         if (myCandidateList[k] != null) {
           myCandidateArray.add(myCandidateList[k]);
         }
       }

       Ballot myBallot = new Ballot(myCandidateArray);
       if (myBallot.isValid(getNumCandidates())){
         myBallot.getChoices().get(0).addBallot(myBallot);
         numValidBallots++;
       }
       AuditService.appendToAuditFile("Assign ballot to " + myBallot.getChoices().get(0).getName());
    }

    /**
     * Creates all of the Candidate objects from the information in the election file.
     *
     * @param line String containing all of the candidates names and parties.
     */
    private void createCandidates(String line){
        String[] lines = line.split(",");
        String [] candInfo = new String[2];
        String candName;
        char candParty;

        for (int i = 0; i < lines.length; i++){
            candInfo = lines[i].split("\\(");
            candName = candInfo[0].strip();
            candParty = candInfo[1].charAt(0);
            Candidate myCandidate = new Candidate(candName, candParty);
            this.candidates.add(myCandidate);
            this.rankedCandidates.add(myCandidate);
            AuditService.appendToAuditFile("Add candidate " + myCandidate.getName() + " of party " +myCandidate.getParty());
        }
    }

    /**
     * Parses through the election file and assigns the information in it accordingly.
     * Contains information saying what the election is, the number of candidates there are,
     * the names of the candidates and what party they belong to, the number of ballots cast,
     * and the ballots themselves.
     */
    @Override
    protected void processFile() {
        int counter = 0; // which file in the files array
        int numFiles = files.size();
        while (counter < numFiles) {
          try {
              AuditService.appendToAuditFile("\nProcess the rest of the file.");
              FileReader fr = new FileReader(files.get(counter));
              BufferedReader br = new BufferedReader(fr);
              String line;
              Integer lineCounter = 1;

              if (counter > 0) { // skip candidates
                for (int i = 0; i < 3; i++) {
                    line = br.readLine();
                }
                lineCounter = 4;
              }
              while((line = br.readLine()) != null) {
                  if (lineCounter == 2){
                      setNumCandidates(Integer.parseInt(line));
                      AuditService.appendToAuditFile("There are " + getNumCandidates() + " candidates.");
                  }
                  if (lineCounter == 3){
                      createCandidates(line);
                  }

                  if (lineCounter == 4){
                      addNumBallots(Integer.parseInt(line));
                      AuditService.appendToAuditFile("There are " + getNumBallots() + " ballots.");
                      Integer majority = getNumBallots() / 2 +1;
                      setMajority(majority);
                      AuditService.appendToAuditFile("To win the election by a majority of votes, a candidate needs " + majority + " votes.");
                  }
                  if (lineCounter >= 5 && lineCounter < getNumBallots() + 5){
                      assignBallots(line);
                  }
                  lineCounter++;
              }
              counter++;
              AuditService.appendToAuditFile("End processing the file.");
              fr.close();
          } catch(Exception e) {
              e.printStackTrace();
          }
        }
    }

    /**
     * Gets the file of the election.
     * @return the file of the election
     */
    public ArrayList<File> getFile(){
      return this.files;
    }

    /**
     * Gets an array of ranked candidates that are still in the election.
     *
     * @return array of ranked candidates that are still in the election
     */
    public ArrayList<Candidate> getRankedCandidates(){
      return this.rankedCandidates;
    }

    /**
     * Sets the majority to the number of votes necessary to with the eletion.
     *
     * @param majority the number of votes necessary to with the eletion
     */
    public void setMajority(Integer majority){
      this.majority = majority;
    }

    /**
     * Gets the number of votes necessary to with the eletion.
     *
     * @return the number of votes necessary to with the eletion
     */
    public Integer getMajority(){
      return this.majority;
    }

    /**
     * Gets the candidates that run into the elextion.
     *
     * @return array of candidates that run in the election
     */
    public ArrayList<Candidate> getCandidates(){
      return this.candidates;
    }
}
