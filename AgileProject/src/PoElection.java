import java.util.*;
import java.io.*;


/**
 * Used to run and hold the methods needed for an PO Election.
 *
 * @author Trevor Guy
 *
 */
public class PoElection extends Election{
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
     * Constructor for PoElection used to assign the class' private variables.
     *
     * @param files The files inputted by the user to be parsed.
     */
    public PoElection(ArrayList<File> files){ // constructor
        this.files = files;
        this.candidates = new ArrayList<Candidate>();
        this.rankedCandidates = new ArrayList<Candidate>();
        this.winners = new ArrayList<Candidate>();
    }

    /**
     * Creates all of the Candidate objects from the information in the election file.
     *
     * @param line String containing all of the candidates names and parties.
     */
    private void createCandidates(String line){
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
            this.rankedCandidates.add(myCandidate);
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
        candidates.get(voteIndex).addBallot(myBallot);
    }

    /**
     * Parses through the election file from and assigns the information in it accordingly.
     * Contains information saying what the election is, how many candidates there are, the names of
     * the candidates and what party the belong to, how many seats are available, how many total ballots
     * were cast, and all of the ballots themselves.
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
                  }
                  if (lineCounter >= 5 && lineCounter < getNumBallots() + 5){
                      assignBallots(line);
                  }
                  lineCounter++;
              }
              counter++;

              fr.close();
          } catch(Exception e) {
              e.printStackTrace();
          }
        }
    }

    /**
     * Runs a Po Election, calling methods to parse the input file and to assign seats.
     */
    @Override
    public void runElection(){
        processFile();
    }

    /**
     * Gets the candidates that run into the elextion.
     *
     * @return array of candidates that run in the election
     */
    public ArrayList<Candidate> getCandidates(){
        return this.candidates;
      }

      /**
     * Gets the files used to run the election.
     *
     * @return array of files used to run the election
     */
    public ArrayList<File> getFile(){
        return this.files;
      }
    
}
