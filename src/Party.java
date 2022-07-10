/**
 * Authors: Daniel Clark, Kenji Her, Sangwoo Park, Rick Wang
 */
import java.util.*;

public class Party {
    
    private String nameParty;
    private int numPartySeats;
    private ArrayList<Candidate> partyCandidates;
    private int remainder;
    private int numPartyVotes;
    private int additionalSeats;

    /**
     * Party constructor
     * 
     * @param nameParty the name of the party
     * @param numPartySeats the number of seats the party has
     */
    public Party(String nameParty) {
        this.nameParty = nameParty;
        this.numPartySeats = 0;
        this.partyCandidates = new ArrayList<Candidate>();
        this.remainder = 0;
        this.numPartyVotes = 0;
        this.additionalSeats = 0;
    }

    /**
     * Method for adding a candidate to the party
     * 
     * @param candidate
     */
    public void addCandidate(Candidate candidate) {
        this.partyCandidates.add(candidate);
    }

    /**
     * Method for sorting the candidates by number of votes in descending order
     */
    public void sortCandidates() {
        this.partyCandidates.sort((object1, object2) -> 
            (object2.getNumVotes() - object1.getNumVotes()));
    }

    public String getNameParty() {
        return this.nameParty;
    }

    public int getNumPartySeats() {
        return this.numPartySeats;
    }

    public void setNumPartySeats(int numPartySeats) {
        this.numPartySeats = numPartySeats;
    }

    public ArrayList<Candidate> getPartyCandidates() {
        return this.partyCandidates;
    }

    public int getRemainder() {
        return this.remainder;
    }

    public void setRemainder(int remainder) {
        this.remainder = remainder;
    }

    public int getNumPartyVotes() {
        return this.numPartyVotes;
    }

    public void setNumPartyVotes(int numPartyVotes) {
        this.numPartyVotes = numPartyVotes;
    }

    public int getAdditionalSeats() {
        return this.additionalSeats;
    }

    public void setAdditionalSeats(int additionalSeats) {
        this.additionalSeats = additionalSeats;
    }
}