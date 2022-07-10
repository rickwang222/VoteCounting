/**
 * Authors: Daniel Clark, Kenji Her, Sangwoo Park, Rick Wang
 */
import java.util.HashMap;
public class Ballot {

    private HashMap<String, Candidate> candidateNumbers;
    private int ballotID;
    private int rank;
    /**
     * Constructor for the Ballot class
     * 
     * @param candidateNumbers
     * @param ballotID
     */
    public Ballot(HashMap<String, Candidate> candidateNumbers, int ballotID) {
        this.candidateNumbers = candidateNumbers;
        this.ballotID = ballotID;
        this.rank = 1;
    }
    
    public HashMap<String, Candidate> getCandidateNumbers() {
        return this.candidateNumbers;
    }

    public int getBallotID() {
        return this.ballotID;
    }

    public void setCandidateNumbers(HashMap<String, Candidate> candidateNumbers) {
        this.candidateNumbers = candidateNumbers;
    }

    public void setBallotNum(int newBallotID) {
        this.ballotID = newBallotID;
    }

    public int getRank() {
        return this.rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
