import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.UIManager.LookAndFeelInfo;
import java.text.SimpleDateFormat;

/**
 * Main class for running both types of elections.
 *
 * @author Thomas Rooney
 */
public class SunshineApp {
  /**
   * Election object that is created to run the elections.
   */
  private Election election;
  /**
   * Name of the path of the election file.
   */
  private String file;
  /**
   * This is the outer frame for the UI.
   */
  private JFrame frame;
  /**
   * This is a panel that helps add the label to the UI.
   */
  private JPanel panel;
  /**
   * JLabel object is how we display text on the screen.
   */
  private JLabel label;
  /**
   * boolean that states what kind of election is being run.
   */
  private boolean isIR;
  /**
   * Boolean for if the user clicked cancelled.
   */
  private boolean cancel;

  /**
   * Constructor used to create the JFrame object.
   */
  public SunshineApp(){
    frame = new JFrame("Sunshine Vote Aggregation System");
    panel = new JPanel();
    label = new JLabel();
    isIR = false;
    cancel = false;
  }

  /**
   * Runs the specified election and passes through the file inputted
   * from the user.
   * Generates the media and audit files after the election has finished.
   */
  private void run() {
    generateAuditFile();
    loadGUI();
    if (!cancel) {
      if (inputFile()) {
         processFile();
      }
      election.runElection();
      AuditService.appendToAuditFile("Vote aggregation has now completed.");
      generateMediaFile();
      displayResults();
      AuditService.closeAuditFile();
    } else {
      AuditService.appendToAuditFile("Cancel was clicked and no election was run.");
      AuditService.closeAuditFile();
      System.exit(0);
    }
  }

  /**
   * Loads the GUI that prompts the user to input the election file.
   */
  private void loadGUI() {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
        e.printStackTrace();
    }
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().add(panel, BorderLayout.CENTER);
    frame.setSize(screenSize.width, screenSize.height);
    label.setPreferredSize(new Dimension(screenSize.width, screenSize.height));
    label.setHorizontalAlignment(SwingConstants.CENTER);
    label.setVerticalAlignment(SwingConstants.TOP);
    label.setFont(new Font("Comic Sans MS",1,20));
    panel.add(label);
    frame.setVisible(true);
    while (this.file == null) {
      String f = null;
      while (f == null && !cancel) {
        f = openDialog();
        if (f != null) {
          File checkFile = new File(f);
          if(checkFile.exists()) {
              this.file = f;
              label.setText("");
          } else {
            label.setText("Invalid file. Please input a valid one.");
          }
        } else {
          cancel = true;
          this.file = "No file";
        }
      }
    }
    if (cancel) {
      this.file = null;
    }
    //frame.setLocationRelativeTo(null);
  }

  /**
   * Opens the dialog to input a file.
   *
   * @return String that would be the file.
   */
  private String openDialog() {
    LayoutManager layout = new FlowLayout();
    panel.setLayout(layout);
    String file = (String)JOptionPane.showInputDialog(
       frame,
       "Input an Election File",
       "Sunshine Vote Aggregation System",
       JOptionPane.PLAIN_MESSAGE,
       null,
       null,
       ""
    );
    frame.setVisible(true);
    return file;
  }


  /**
   * Checks to see if the file exists or not.
   *
   * @return Boolean saying if the file was input successfully or not.
   */
  private boolean inputFile() {
    if (this.file != null) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Opens the file to be read from, and figures out what election is to be run,
   * passing the opened file through to the election classes to be parsed.
   */
  private void processFile() {
    try {
      AuditService.appendToAuditFile("Begin processing election file.");
      File file = new File(this.file);
      FileReader fr = new FileReader(file);
      BufferedReader br = new BufferedReader(fr);
      String line;
      line = br.readLine();
      if (line.equals("IR")) {
        AuditService.appendToAuditFile("Determine it is an IR election.");
        this.election = new IrElection(file);
        isIR = true;
      } else if (line.equals("OPL")){
        AuditService.appendToAuditFile("Determine it is an OPL election.");
        this.election = new OplElection(file);
      } else {
        AuditService.appendToAuditFile("Determine an invalid file was input.");
        System.out.println("error: Invalid file");
      }
      fr.close();
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Transforms the character representing the party into a string that shows
   * that the candidate belongs to that party.
   * @param c character representing the party
   * @return string that shows that the candidate belongs to that party
   */
  private String charToParty(char c){
    if (c == 'D') return ", representing the democratic party;";
    else if  (c == 'R') return ", representing the republican party;";
    else if (c== 'L') return ", representing the libertarian party;";
    else if (c == 'I') return ", independent;";
    return ", representing the " + c + " party;";
  }

  /**
   * Generates the message displayed on the screen and on the media file for both types of elections.
   * @param media boolean that is true if the result for the media file is generated and false if the result for the display on the screen is generated
   * @return sting containing the number of ballots, candidates, and names and parties of the candidates that will be displayed on the screen/in the media file
   */
  private String mutualDisplay(Boolean media){
    String newline = " <br><br>";
    if (media) {
      newline = " \n";
    }
    Election myElection = this.election;
    String results = "";
    results += "The number of ballots cast: " + myElection.getNumBallots() + newline;
    results += "The number of candidates that ran: " + myElection.getNumCandidates() + newline;
    if (!isIR) {
      results += "The number of seats available: " + myElection.getNumSeats() + newline;
    }
    results += "The candidates were: ";
    ArrayList<Candidate> electionCandidates = myElection.getCandidates();
    for (int i = 0; i < electionCandidates.size(); i++){
      if (i != electionCandidates.size() - 1) {
        results += electionCandidates.get(i).getName() + charToParty(electionCandidates.get(i).getParty()) + " ";
      }
      else{
        results += electionCandidates.get(i).getName() + charToParty(electionCandidates.get(i).getParty()).replace(";", ".");
      }
    }
    return results;
  }

  /**
   * Displays the results of the election to the screen after the election has
   * finished running.
   */
  private void displayResults() {
    AuditService.appendToAuditFile("Display the results to the user.");
    label.setVisible(false);
    label.setHorizontalAlignment(SwingConstants.CENTER);
    label.setVerticalAlignment(SwingConstants.TOP);
    ArrayList<Candidate> winners = this.election.getWinners();
    String results = "<html>";
    //load results
    if (isIR) {
      results += "An Instant Runoff election was run! Here are the results: <br><br>";
      results += mutualDisplay(false);
      Candidate winner = winners.get(0);
      results += "<br><br>The winner of the election was: " + winner.getName() + charToParty(winner.getParty()).replace(";", ",") + " with " + winner.getNumBallots() + " votes.<br><br>";
    } else {
      results += "An Open Party List election was run! Here are the results: <br><br>";
      results += mutualDisplay(false);
      results += "<br><br>The winners of the election were:<br><br>";
      for (int i = 0; i < winners.size(); i ++) {
        Candidate winner = winners.get(i);
        results += "&nbsp;&nbsp;&nbsp;&nbsp; *  " + winner.getName() + charToParty(winner.getParty()).replace(";", ", with ") + winner.getNumBallots() + " votes.<br><br>";
      }
    }
    results += "</html>";
    label.setText(results);
    label.setVisible(true);
  }

  /**
   * Calls AuditService that creates and opens a the audit file
   * which is written to throughout the execution of the program.
   */
  private void generateAuditFile() {
    try {
      AuditService.createAndOpenFile();
    } catch(Exception e) {
      System.out.println("Error creating audit file");
      e.printStackTrace();
    }
  }

  /**
   * Writes the results of the election to a file for the media.
   */
  private void generateMediaFile() {
    try {
      File file;
      SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy-hhmmss.SSS");
      String date = sdf.format(new Date());
      if (isIR) {
        file = new File("IrMedia" + date + ".txt");
      } else {
        file = new File("OplMedia" + date + ".txt");
      }
      FileWriter fr = new FileWriter(file, false);
      ArrayList<Candidate> winners = this.election.getWinners();
      if (isIR) {
        Candidate winner = winners.get(0);
        AuditService.appendToAuditFile("Generate media file for an IR election.");
        fr.write(mutualDisplay(true));
        fr.write("\nThe winner of the election is " + winner.getName() + charToParty(winner.getParty()).replace(";", ", with ") + winner.getNumBallots() + " votes.\n");
      } else {
        AuditService.appendToAuditFile("Generate media file for an OPL election.");
        fr.write(mutualDisplay(true));
        fr.write("\nThe winners of the election were:\n");
        for (Candidate winner : winners) {
          fr.write(winner.getName() + " of party " + charToParty(winner.getParty()).replace(";", ", with ") + winner.getNumBallots() + " votes.\n");
        }
      }
      fr.close();
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Runs the election.
   *
   * @param args
   */
  public static void main(String args[]){
    SunshineApp app = new SunshineApp();
    app.run();
  }
}
