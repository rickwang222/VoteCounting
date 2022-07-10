/**
 * Authors: Daniel Clark, Kenji Her, Sangwoo Park, Rick Wang
 */
public class PartyTest {

    /**
     * The main method will test to make sure that the sort candidates
     * method in Party will work as intended.
     * @param args
     */
    public static void main(String[] args) {
        ReadCSV r = new ReadCSV();
        r.readCSV("../testing/OPL_test.csv");
        Party republican = new Party("R");
        
        // Give candidates their votes
        for (int i = 0; i < r.getBallots().size(); i++) {
            for (int j = 0; j < r.getCandidates().size(); j++) {
                if (r.getBallots().get(i).getCandidateNumbers().get("1").getName().equals(r.getCandidates().get(j).getName())) {
                    r.getCandidates().get(j).incrementNumVotes();
                }
            }
        }

        // Populate republican party with republican candidates
        for(int i = 0; i < r.getCandidates().size(); i++) {
            // this also tests to make sure only republicans get added
            if(r.getCandidates().get(i).getParty().equals(republican.getNameParty())) {
                republican.addCandidate(r.getCandidates().get(i));
            }
        }

        // Testing sort
        for(int i = 0; i < republican.getPartyCandidates().size(); i++) {
            System.out.println(republican.getPartyCandidates().get(i).getName() + " " + republican.getPartyCandidates().get(i).getNumVotes());         
        }
        republican.sortCandidates();
        System.out.println();
        for(int i = 0; i < republican.getPartyCandidates().size(); i++) {
            System.out.println(republican.getPartyCandidates().get(i).getName() + " " + republican.getPartyCandidates().get(i).getNumVotes());         
        }
    }
}