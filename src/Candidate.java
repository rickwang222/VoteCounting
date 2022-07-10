import java.util.ArrayList;

/**
 * Authors: Daniel Clark, Kenji Her, Sangwoo Park, Rick Wang
 */
public class Candidate {

    private String party;
    private String name;
    private int rank;
    private int initialVotes;
    private int numVotes;
    private boolean winner;
    private boolean loser;
    private ArrayList<Ballot> ballots;

    /**
     * Constructor for the Candidate class
     * 
     * @param party
     * @param name
     * @param rank
     */
    public Candidate(String party, String name, int rank) {
        this.party = party;
        this.name = name;
        this.rank = rank;
        this.numVotes = 0;
        this.winner = false;
        this.loser = false;
        this.ballots = new ArrayList<Ballot>();
    }

    public void setRank(int newRank) {
        this.rank = newRank;
    }

    public void setWinner(boolean newWinner) {
        this.winner = newWinner;
    }

    public void setLoser(boolean newLoser) {
        this.loser = newLoser;
    }

    public String getParty() {
        return this.party;
    }

    public String getName() {
        return this.name;
    }

    public int getRank() {
        return this.rank;
    }

    public int getNumVotes() {
        return this.numVotes;
    }

    public boolean getWinner() {
        return this.winner;
    }

    public void incrementNumVotes() {
        this.numVotes++;
    }
 
    public ArrayList<Ballot> getBallots() {
        return this.ballots;
    }

    public boolean getLoser() {
        return this.loser;
    }

    public int getInitialVotes() {
        return this.initialVotes;
    }

    public void setInitialVotes(int votes) {
        this.initialVotes = votes;
    }
    public void addBallot(Ballot b) {
        this.ballots.add(b);
    }
}