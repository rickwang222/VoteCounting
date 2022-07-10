/**
 * Authors: Daniel Clark, Kenji Her, Sangwoo Park, Rick Wang
 */
public class ReadCSVTest {
    /**
     * The main method is used to test ReadCSV by manually comparing the output
     * @param args
     */
    public static void main(String[] args) {
        ReadCSV r = new ReadCSV();
        r.readCSV("../testing/IRV_test.csv");
        
        // Go through all the ballots
        for(int i = 0; i < r.getBallots().size(); i++) {
            System.out.print("Order of Ballot#" + r.getBallots().get(i).getBallotID() + ":\t");
            // Go through the candidates array
            for(int j = 1; j <= r.getCandidates().size(); j++){
                // Get the candidate that was voted as the jth option by the ballot
                Candidate cand = r.getBallots().get(i).getCandidateNumbers().get(Integer.toString(j));
                // Make sure the ballot voted for this candidate exists
                if(cand != null) {
                    System.out.print(r.getBallots().get(i).getCandidateNumbers().get(Integer.toString(j)).getName());
                    System.out.print(" ");
                }
            }
            System.out.println();
        }

        // Go through all the invalid ballots
        for(int i = 0; i < r.getInvalidBallots().size(); i++) {
            System.out.print("Order of Invalid Ballot#" + r.getInvalidBallots().get(i).getBallotID() + ":\t");
            // Go through the candidates array
            for(int j = 1; j <= r.getCandidates().size(); j++){
                // Get the candidate that was voted as the jth option by the ballot
                Candidate cand = r.getInvalidBallots().get(i).getCandidateNumbers().get(Integer.toString(j));
                // Make sure the ballot voted for this candidate exists
                if(cand != null) {
                    System.out.print(r.getInvalidBallots().get(i).getCandidateNumbers().get(Integer.toString(j)).getName());
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
        ReadCSVTest po = new ReadCSVTest();
        System.out.println("TestReadPO Begins Here: ");
        po.testReadPO();
    }
     /**
     * The testReadPO ensures that a PO csv file is read properlly and manually checking the output
     */
    public void testReadPO() {
        ReadCSV r = new ReadCSV();
        r.readCSV("../testing/PO_test.csv");
        // Go through all the ballots
        for(int i = 0; i < r.getBallots().size(); i++) {
            System.out.print("Order of Ballot#" + r.getBallots().get(i).getBallotID() + ":\t");
            // Go through the candidates array
            for(int j = 1; j <= r.getCandidates().size(); j++){
                // Get the candidate that was voted as the jth option by the ballot
                Candidate cand = r.getBallots().get(i).getCandidateNumbers().get(Integer.toString(j));
                // Make sure the ballot voted for this candidate exists
                if(cand != null) {
                    System.out.print(r.getBallots().get(i).getCandidateNumbers().get(Integer.toString(j)).getName());
                    System.out.print(" ");
                }
            }
            System.out.println();
        }

        // Go through all the invalid ballots
        for(int i = 0; i < r.getInvalidBallots().size(); i++) {
            System.out.print("Order of Invalid Ballot#" + r.getInvalidBallots().get(i).getBallotID() + ":\t");
            // Go through the candidates array
            for(int j = 1; j <= r.getCandidates().size(); j++){
                // Get the candidate that was voted as the jth option by the ballot
                Candidate cand = r.getInvalidBallots().get(i).getCandidateNumbers().get(Integer.toString(j));
                // Make sure the ballot voted for this candidate exists
                if(cand != null) {
                    System.out.print(r.getInvalidBallots().get(i).getCandidateNumbers().get(Integer.toString(j)).getName());
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }
}
