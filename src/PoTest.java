/**
 * Authors: Daniel Clark, Kenji Her, Sangwoo Park, Rick Wang
 */
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
public class PoTest {
    public ArrayList<Candidate> testCands;
    public ArrayList<Ballot> testBals;
     
    @BeforeEach
    public void setUp() {
        this.testCands = new ArrayList<Candidate>();
        Candidate cand1 = new Candidate("Republican","Jason", 1);
        Candidate cand2 = new Candidate("Democrat", "Mark", 1);
        Candidate cand3 = new Candidate("Libertarian", "Cole", 1);
        Candidate cand4 = new Candidate("Independent", "Henry", 1);
        this.testCands.add(cand1);
        this.testCands.add(cand2);
        this.testCands.add(cand3);
        this.testCands.add(cand4);

        // initialize ballot list
        this.testBals = new ArrayList<Ballot>();
        for (int i = 0; i < 10; i++) {
            HashMap<String, Candidate> testCandNums = new HashMap<String, Candidate>();
            if (i < 1) {
                testCandNums.put("1", cand1);
            }
            else if (i < 3) {
                testCandNums.put("1", cand2);
            }
            else if (i < 6) {
                testCandNums.put("1", cand3);
            }
            else {
                testCandNums.put("1", cand4);
            }
            Ballot bal = new Ballot(testCandNums, i);
            this.testBals.add(bal);
        }
    }

    /**
     * Test PO Constructor
     */
    @Test
    public void testPOConstructor() {
        PO testPO = new PO(this.testCands, this.testBals);
        // check each candidates number of votes

        // cand1 has 1
        assertEquals(1, testPO.getCandidates().get(0).getNumVotes());
        // cand2 has 2
        assertEquals(2, testPO.getCandidates().get(1).getNumVotes());
        // cand3 has 3
        assertEquals(3, testPO.getCandidates().get(2).getNumVotes());
        // cand4 has 4
        assertEquals(4, testPO.getCandidates().get(3).getNumVotes());
    }

    @Test
    public void testRunAlgorithm() {
        PO testPO = new PO(this.testCands, this.testBals);
        testPO.runAlgorithm();
        // cand 1 not a winner with 1 vote
        assertEquals(false, testCands.get(0).getWinner());
        // cand 2 not a winner with 2 votes
        assertEquals(false, testCands.get(1).getWinner());
        // cand 3 not a winner with 3 votes
        assertEquals(false, testCands.get(2).getWinner());
        // cand4 has the most votes at 4 votes
        assertEquals(true, testCands.get(3).getWinner());
    }

    @Test
    public void testRunAlgorithmTie() {
        // add extra independent candidate and four ballots towards them
        Candidate extraIndCand = new Candidate("Independent", "Kram", 1);
        this.testCands.add(extraIndCand);
        HashMap<String, Candidate> testCandNums = new HashMap<String, Candidate>();
        for (int i = 0; i < 4; i++) {
            testCandNums.put("1", extraIndCand);
            Ballot extraIndBal = new Ballot(testCandNums, 10 + i);
            this.testBals.add(extraIndBal);
        }
        PO testPO = new PO(this.testCands, this.testBals);
        for (int i = 0; i < 10; i++) {
            // reset the winner status every round since it doesn't happen in the algorithm
            this.testCands.get(3).setWinner(false);
            this.testCands.get(4).setWinner(false);

            testPO.runAlgorithm();
            //testIRV.displayResults();
            System.out.printf("Independent Candidate 1: %b, ", this.testCands.get(3).getWinner());
            System.out.printf("Independent Candidate 2: %b\n", this.testCands.get(4).getWinner());
        }

    }

}
