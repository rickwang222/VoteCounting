/**
 * Authors: Daniel Clark, Kenji Her, Sangwoo Park, Rick Wang
 */
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
public class CandidateTest {
    
    /**
     * Test file for all the methods in Candidate
     * This class is only getters/setters
     */
    @Test
    public void testSetRank() {
        Candidate cand = new Candidate("Democrat", "Mark", 1);
        cand.setRank(5);
        assertEquals(5,cand.getRank());
    }

    @Test
    public void testSetWinner() {
        Candidate cand = new Candidate("Democrat", "Mark", 1);
        cand.setWinner(true);
        assertEquals(true,cand.getWinner());
    }

    @Test
    public void testGetParty() {
        Candidate cand = new Candidate("Democrat", "Mark", 1);
        assertEquals("Democrat", cand.getParty());
    }

    @Test
    public void testGetName() {
        Candidate cand = new Candidate("Democrat", "Mark", 1);
        assertEquals("Mark", cand.getName());
    }

    @Test
    public void testGetRank() {
        Candidate cand = new Candidate("Democrat", "Mark", 1);
        assertEquals(1, cand.getRank());
    }

    @Test
    public void testGetNumVotes() {
        Candidate cand = new Candidate("Democrat", "Mark", 1);
        assertEquals(0, cand.getNumVotes());
        cand.incrementNumVotes();
        cand.incrementNumVotes();
        assertEquals(2, cand.getNumVotes());
    }

    @Test
    public void testGetWinner() {
        Candidate cand = new Candidate("Democrat", "Mark", 1);
        assertEquals(false, cand.getWinner());
        cand.setWinner(true);
        assertEquals(true, cand.getWinner());
    }

    @Test
    public void testIncrementNumVotes() {
        Candidate cand = new Candidate("Democrat", "Mark", 1);  
        for (int i = 0; i < 100; i++) {
            cand.incrementNumVotes();
        }
        assertEquals(100, cand.getNumVotes());
    }
}
