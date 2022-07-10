/**
 * Authors: Daniel Clark, Kenji Her, Sangwoo Park, Rick Wang
 */
import java.util.ArrayList;

public class DualSystemBallotCounter {
    
    /**
     * This is the driver for the application
     * 
     * @param args command line arguments
     */
    public static void main(String args[]) {
        // checking for csv file 
        if (args.length >= 1) {
            // collect ballot information
            String csvPath = args[0];
            ReadCSV read = new ReadCSV();
            read.readCSV(csvPath);
            String votingAlgorithm = "";
            votingAlgorithm = read.getVotingSystem();
            ArrayList<Candidate> candidates = new ArrayList<Candidate>();
            candidates = read.getCandidates();
            ArrayList<Ballot> ballots = new ArrayList<Ballot>();
            ballots = read.getBallots();
            int numSeats = read.getNumSeats();
            Audit results = new Audit();
        
            for(int i = 1; i < args.length; i++) {
                csvPath = args[i];
                ReadCSV read2 = new ReadCSV();
                read2.readCSV(csvPath);
                ArrayList<Candidate> candidates2 = new ArrayList<Candidate>();
                candidates2 = read2.getCandidates();
                if(read2.getVotingSystem().equals(votingAlgorithm) && sameCandidates(candidates, candidates2) && read2.getNumSeats() == numSeats) {
                    read.getBallots().addAll(read2.getBallots());
                } else {
                    System.out.println("Two Files cannot be combined.");
                    System.exit(0);
                }
            }

            // run the voting algorithm chosen
            if (votingAlgorithm.equals("IRV")) {
                IRV irv = new IRV(candidates, ballots, results, numSeats);
                irv.runAlgorithm();
                irv.displayResults();
            }

            else if (votingAlgorithm.equals("OPL")) {
                OPL opl = new OPL(candidates, ballots, results, numSeats);
                opl.runAlgorithm();
                opl.displayResults();
            }

            else if (votingAlgorithm.equals("PO")) {
                PO po = new PO(candidates, ballots);
                po.runAlgorithm();
                po.displayResults();
            }
            
            else {
                System.out.println("Invalid Command. Please try again");
            }

        }

        else {
            System.out.println("Error: Not enough arguments given");
            System.exit(0);
        }
    }
    // METHODS BELOW USED FOR TESTING
    /**
     * This method is used for using the JUNIT tests to check the argument count.
     * It is used to avoid having to check equivalency of print statments when
     * calling the main method directly. The logic is the same as the main function
     * @param args
     * @return
     */
    public String testArgsCount(String[] args) {
        if (args.length == 1)
            return "Correct";
        else if (args.length > 1) {
            return "Error: Too many arguments given";
        }
        return "Error: Not enough arguments given";
    }

    /**
     * This method is used for using the JUNIT tests to check for 
     * the type of algorithm to run. It is isued to avoid having
     * to check equivalency of print statements when calling the main method directly
     * The logic is the same as the main function
     * @param type
     * @return
     */
    public String testVotingMethod(String type) {
        if (type.equals("OPL")) {
            return "OPL METHOD";
        }
        return "IRV METHOD";
    }

    /**
     * This method is used for using the JUNIT tests to check for 
     * the multiple files to run. It is isued to avoid having
     * to check equivalency of print statements when calling the main method directly
     * The logic is the same as the main function
     * @param type
     * @return
     */
    public String testMultipleFiles(String[] args) {
        if (args.length >= 1) {     
            String csvPath = args[0];
            ReadCSV read = new ReadCSV();
            read.readCSV(csvPath);
            String votingAlgorithm = "";
            votingAlgorithm = read.getVotingSystem();
            ArrayList<Candidate> candidates = new ArrayList<Candidate>();
            candidates = read.getCandidates();
            ArrayList<Ballot> ballots = new ArrayList<Ballot>();
            ballots = read.getBallots();
            int numSeats = read.getNumSeats();

            for(int i = 1; i < args.length; i++) {
                csvPath = args[i];
                ReadCSV read2 = new ReadCSV();
                read2.readCSV(csvPath);
                ArrayList<Candidate> candidates2 = new ArrayList<Candidate>();
                candidates2 = read2.getCandidates();
                if(read2.getVotingSystem().equals(votingAlgorithm) && read2.getNumSeats() == numSeats) {
                    return "Combined Successfully";
                } else {
                    return "Two Files cannot be combined.";
                }
            }      
        }
        return "";
    }

    public static boolean sameCandidates(ArrayList<Candidate> cands1, ArrayList<Candidate> cands2) {
        if(cands1.size() != cands2.size()) {
            return false;
        }

        for(int i = 0; i < cands1.size(); i++) {
            if(!(cands1.get(i).getName().equals(cands2.get(i).getName()) && cands1.get(i).getParty().equals(cands2.get(i).getParty()))) {
                return false;
            }
        }
        return true;
    }
}