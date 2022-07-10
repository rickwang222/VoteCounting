/**
 * Authors: Daniel Clark, Kenji Her, Sangwoo Park, Rick Wang
 */
import java.util.ArrayList;
import java.util.HashMap;

public class Audit {
    private String votingSystem;
    private int numCandidates;
    private int numBallots;
    private ArrayList<Candidate> candidates;
    private ArrayList<Ballot> ballots;
    private ArrayList<String> calculations;
    private HashMap<Candidate, Integer> numVotesPerCandidate;
    private ArrayList<Candidate> winner;
    private HashMap<Candidate, ArrayList<Ballot>> ballotsPerCandidate;

    public String getVotingSystem() {
        return this.votingSystem;
    }

    public void setVotingSystem(String votingSystem) {
        this.votingSystem = votingSystem;
    }

    public int getNumCandidates() {
        return this.numCandidates;
    }

    public void setNumCandidates(int numCandidates) {
        this.numCandidates = numCandidates;
    }

    public int getNumBallots() {
        return this.numBallots;
    }

    public void setNumBallots(int numBallots) {
        this.numBallots = numBallots;
    }

    public ArrayList<Candidate> getCandidates() {
        return this.candidates;
    }

    public void setCandidates(ArrayList<Candidate> candidates) {
        this.candidates = candidates;
    }

    public ArrayList<Ballot> getBallots() {
        return this.ballots;
    }

    public void setBallots(ArrayList<Ballot> ballots) {
        this.ballots = ballots;
    }
    
    public ArrayList<String> getCalculations() {
        return this.calculations;
    }

    public void setCalculations(ArrayList<String> calculations) {
        this.calculations = calculations;
    }

    public HashMap<Candidate,Integer> getNumVotesPerCandidate() {
        return this.numVotesPerCandidate;
    }

    public void setNumVotesPerCandidate(HashMap<Candidate,Integer> numVotesPerCandidate) {
        this.numVotesPerCandidate = numVotesPerCandidate;
    }

    public ArrayList<Candidate> getWinner() {
        return this.winner;
    }

    public void setWinner(ArrayList<Candidate> winner) {
        this.winner = winner;
    }

    public HashMap<Candidate,ArrayList<Ballot>> getBallotsPerCandidate() {
        return this.ballotsPerCandidate;
    }

    public void setBallotsPerCandidate(HashMap<Candidate,ArrayList<Ballot>> ballotsPerCandidate) {
        this.ballotsPerCandidate = ballotsPerCandidate;
    }    
}