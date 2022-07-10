/**
 * Authors: Daniel Clark, Kenji Her, Sangwoo Park, Rick Wang
 */
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
public class OPLTest {

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

        // initialize audit object
        this.testRes = new Audit();

        // initialize number of seats
        this.testNumSeats = 3;
    }

    /**
     * Test OPL constructor regularly
     */
    @Test 
    public void testOPLConstructor() {
        OPL testOPL = new OPL(this.testCands, this.testBals, this.testRes, this.testNumSeats);

        // check each candidates number of votes

        // cand1 has 1
        assertEquals(1, testOPL.getCandidates().get(0).getNumVotes());
        // cand2 has 2
        assertEquals(2, testOPL.getCandidates().get(1).getNumVotes());
        // cand3 has 3
        assertEquals(3, testOPL.getCandidates().get(2).getNumVotes());
        // cand4 has 4
        assertEquals(4, testOPL.getCandidates().get(3).getNumVotes());

        // check if there are 4 parties
        assertEquals(4, testOPL.getParties().size());

        // check each party's number of votes

        // party 1 has 1
        assertEquals(1, testOPL.getParties().get(0).getNumPartyVotes());
        // party 2 has 2
        assertEquals(2, testOPL.getParties().get(1).getNumPartyVotes());
        // party 3 has 3
        assertEquals(3, testOPL.getParties().get(2).getNumPartyVotes());
        // party 4 has 4
        assertEquals(4, testOPL.getParties().get(3).getNumPartyVotes());
    }

    /**
     * Test OPL constructor with additional democratic candidate
     */
    @Test 
    public void testOPLConstructorDemo() {
        // add extra democratic candidate and one ballot towards them
        Candidate extraDemoCand = new Candidate("Democrat", "Kram", 1);
        this.testCands.add(extraDemoCand);
        HashMap<String, Candidate> testCandNums = new HashMap<String, Candidate>();
        testCandNums.put("1", extraDemoCand);
        Ballot extraDemoBal = new Ballot(testCandNums, 10);
        this.testBals.add(extraDemoBal);

        OPL testOPL = new OPL(this.testCands, this.testBals, this.testRes, this.testNumSeats);

        // check each candidates number of votes

        // cand1 has 1
        assertEquals(1, testOPL.getCandidates().get(0).getNumVotes());
        // cand2 has 2
        assertEquals(2, testOPL.getCandidates().get(1).getNumVotes());
        // cand3 has 3
        assertEquals(3, testOPL.getCandidates().get(2).getNumVotes());
        // cand4 has 4
        assertEquals(4, testOPL.getCandidates().get(3).getNumVotes());
        // cand1 has 5
        assertEquals(1, testOPL.getCandidates().get(4).getNumVotes());

        // check if there are 4 parties
        assertEquals(4, testOPL.getParties().size());

        // check each party's number of votes

        // party 1 has 1
        assertEquals(1, testOPL.getParties().get(0).getNumPartyVotes());
        // party 2 has 2
        assertEquals(3, testOPL.getParties().get(1).getNumPartyVotes());
        // party 3 has 3
        assertEquals(3, testOPL.getParties().get(2).getNumPartyVotes());
        // party 4 has 4
        assertEquals(4, testOPL.getParties().get(3).getNumPartyVotes());
    }

    /**
     * Test OPL algorithm regularly
     */
    @Test
    public void testRunAlgorithm() {
        OPL testOPL = new OPL(this.testCands, this.testBals, this.testRes, this.testNumSeats);
        testOPL.runAlgorithm();

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
     * Test OPL algorithm with a tie
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
        OPL testOPL = new OPL(this.testCands, this.testBals, this.testRes, this.testNumSeats);
        for (int i = 0; i < 10; i++) {
            // reset the winner status every round since it doesn't happen in the algorithm
            this.testCands.get(3).setWinner(false);
            this.testCands.get(4).setWinner(false);

            testOPL.runAlgorithm();
            //testOPL.displayResults();
            System.out.printf("Ind Cand 1: %b, ", this.testCands.get(3).getWinner());
            System.out.printf("Ind Cand 2: %b\n", this.testCands.get(4).getWinner());
        }
    }

    /**
     * Test for display results
     */
    @Test
    public void testDisplayResults() {
        OPL testOPL = new OPL(this.testCands, this.testBals, this.testRes, this.testNumSeats);
        testOPL.runAlgorithm();
        String expected = "Open Party Listing\n" + "Quota 3\n";
        expected += "------------------------------------------------------------------------------------\n";
        expected += "                        First       Remaining   Second      Final       % of Vote   \n";
        expected += "Parties     Votes       Allocation  Votes       Allocation  Seat        to          \n";
        expected += "                        Of Seats                Of Seats    Total       % of Seats  \n";
        expected += "------------------------------------------------------------------------------------\n";
        expected += "Independent 4           1           1           0           1           40% / 33%\n";
        expected += "Libertarian 3           1           0           0           1           30% / 33%\n";
        expected += "Democrat    2           0           2           1           1           20% / 33%\n";
        expected += "Republican  1           0           1           0           0           10% / 0%\n";

        System.out.println(expected);
        testOPL.displayResults();
    }

    /**
     * Test OPL algorithm with more party seats than candidates
     */
    @Test 
    public void testRunAlgorithmMoreSeats() {
        // add more votes for the independent party
        HashMap<String, Candidate> testCandNums = new HashMap<String, Candidate>();
        for (int i = 0; i < 10; i++) {
            testCandNums.put("1", this.testCands.get(3));
            Ballot extraBal = new Ballot(testCandNums, 10 + i);
            this.testBals.add(extraBal);
        }

        OPL testOPL = new OPL(this.testCands, this.testBals, this.testRes, this.testNumSeats);
        testOPL.runAlgorithm();

        // first party should have no seats
        assertEquals(0, testOPL.getParties().get(0).getNumPartySeats());
        // second party should have one seat
        assertEquals(0, testOPL.getParties().get(1).getNumPartySeats());
        // third party should have one seat
        assertEquals(1, testOPL.getParties().get(2).getNumPartySeats());
        // fourth party should have one seat
        assertEquals(0, testOPL.getParties().get(3).getNumPartySeats());
    }

    /**
     * Test that the audit object is populated
     */
    @Test
    public void testCreateAuditFile() {
        OPL testOPL = new OPL(this.testCands, this.testBals, this.testRes, this.testNumSeats);
        testOPL.runAlgorithm();
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
