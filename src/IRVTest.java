/**
 * Authors: Daniel Clark, Kenji Her, Sangwoo Park, Rick Wang
 */
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;
public class IRVTest {
    public ArrayList<Candidate> testCands;
    public ArrayList<Ballot> testBals;
    public Audit testRes;
    public int testNumSeats;    

    /**
     * Set up operations before each test run
     */
    @BeforeEach
    public void setUp() {
        // initialize candidate list
        this.testCands = new ArrayList<Candidate>();
        Candidate cand1 = new Candidate("Democrat","Rosen", 1);
        Candidate cand2 = new Candidate("Republican", "Kleinberg", 1);
        Candidate cand3 = new Candidate("Libertarian", "Royce", 1);
        Candidate cand4 = new Candidate("Independent", "Chou", 1);
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
                testCandNums.put("2", cand1);
                testCandNums.put("3", cand1);
                testCandNums.put("4", cand1);
            }
            else if (i < 3) {
                testCandNums.put("1", cand2);
                testCandNums.put("4", cand2);
                testCandNums.put("3", cand2);
                testCandNums.put("2", cand2);

            }
            else if (i < 6) {
                testCandNums.put("2", cand3);
                testCandNums.put("1", cand3);
                testCandNums.put("3", cand3);
                testCandNums.put("4", cand3);
            }
            else {
                testCandNums.put("4", cand4);
                testCandNums.put("2", cand4);
                testCandNums.put("3", cand4);
                testCandNums.put("1", cand4);
            }
            Ballot bal = new Ballot(testCandNums, i);
            this.testBals.add(bal);
            System.out.println(this.testBals.size());
        }

        // initialize audit object
        this.testRes = new Audit();

        // initialize number of seats
        this.testNumSeats = 2;
    }

    /**
     * Test IRV constructor regularly
     */
    @Test 
    public void testIRVConstructor() {
        IRV testIRV = new IRV(this.testCands, this.testBals, this.testRes, this.testNumSeats);

        // check each candidates number of votes

        assertEquals(1, testIRV.getCandidates().get(0).getNumVotes());
        assertEquals(2, testIRV.getCandidates().get(1).getNumVotes());
        assertEquals(3, testIRV.getCandidates().get(2).getNumVotes());
        assertEquals(4, testIRV.getCandidates().get(3).getNumVotes());

    }

    /**
     * Test IRV constructor with additional democratic candidate
     */
    @Test 
    public void testIRVConstructorDemo() {
        // add extra democratic candidate and one ballot towards them
        Candidate extraDemoCand = new Candidate("Democrat", "Kram", 1);
        this.testCands.add(extraDemoCand);
        HashMap<String, Candidate> testCandNums = new HashMap<String, Candidate>();
        testCandNums.put("1", extraDemoCand);
        Ballot extraDemoBal = new Ballot(testCandNums, 10);
        this.testBals.add(extraDemoBal);

        IRV testIRV = new IRV(this.testCands, this.testBals, this.testRes, this.testNumSeats);

        // check each candidates number of votes

        assertEquals(1, testIRV.getCandidates().get(0).getNumVotes());
        assertEquals(2, testIRV.getCandidates().get(1).getNumVotes());
        assertEquals(3, testIRV.getCandidates().get(2).getNumVotes());
        assertEquals(4, testIRV.getCandidates().get(3).getNumVotes());
        assertEquals(1, testIRV.getCandidates().get(4).getNumVotes());
    }

    /**
     * Test IRV algorithm regularly
     */
    @Test
    public void testRunAlgorithm() {
        IRV testIRV = new IRV(this.testCands, this.testBals, this.testRes, this.testNumSeats);
        testIRV.runAlgorithm();

        // cand1 is a loser
        assertEquals(false, testCands.get(0).getWinner());
        // cand2 is a winner
        assertEquals(true, testCands.get(1).getWinner());
        // cand3 is a winner
        assertEquals(true, testCands.get(2).getWinner());
        // cand4 is a winner
        assertEquals(true, testCands.get(3).getWinner());
    }

    /**
     * Test IRV algorithm with a tie
     */
    @Test
    public void testRunAlgorithmTie() {
        // change number of seats to 1 to make it easier
        this.testNumSeats = 1;

        // add extra independent candidate and four ballots towards them
        Candidate extraIndCand = new Candidate("Independent", "Kram", 1);
        this.testCands.add(extraIndCand);
        HashMap<String, Candidate> testCandNums = new HashMap<String, Candidate>();
        for (int i = 0; i < 4; i++) {
            testCandNums.put("1", extraIndCand);
            Ballot extraIndBal = new Ballot(testCandNums, 10 + i);
            this.testBals.add(extraIndBal);
        }

        // check that one and only one of the independent candidates is a winner
        // hypothetically it should be very hard for one of the candidates to win 10 times in a row
        // therefore I chose to loop 10 times to show that the coin flip works
        IRV testIRV = new IRV(this.testCands, this.testBals, this.testRes, this.testNumSeats);
        for (int i = 0; i < 10; i++) {
            // reset the winner status every round since it doesn't happen in the algorithm
            this.testCands.get(3).setWinner(false);
            this.testCands.get(4).setWinner(false);

            testIRV.runAlgorithm();
            //testIRV.displayResults();
            System.out.printf("Independent Candidate 1: %b, ", this.testCands.get(3).getWinner());
            System.out.printf("Independent Candidate 2: %b\n", this.testCands.get(4).getWinner());
        }
    }

    /**
     * Test that the audit object is populated
     */
    @Test
    public void testCreateAuditFile() {
        IRV testIRV = new IRV(this.testCands, this.testBals, this.testRes, this.testNumSeats);
        testIRV.runAlgorithm();
        System.out.println(this.testRes.getVotingSystem());
        System.out.println(this.testRes.getNumBallots());
        System.out.println(this.testRes.getNumCandidates());
        System.out.println(this.testRes.getBallots());
        System.out.println(this.testRes.getBallotsPerCandidate());
        System.out.println(this.testRes.getCalculations());
        System.out.println(this.testRes.getCandidates());
        System.out.println(this.testRes.getNumVotesPerCandidate());
        System.out.println(this.testRes.getWinner());
    }
}
    

