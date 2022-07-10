/**
 * Authors: Daniel Clark, Kenji Her, Sangwoo Park, Rick Wang
 */
import java.util.*;
import java.io.*;

public class PO implements VotingAlgorithm {
    
    private ArrayList<Candidate> candidates;
    private ArrayList<Ballot> ballots;

    /**
     * Constructor for the PO class
     * 
     * @param candidates the candidates in the election
     * @param ballots the ballots in the election
     */
    public PO (ArrayList<Candidate> candidates, ArrayList<Ballot> ballots) {
        this.candidates = candidates;
        this.ballots = ballots;
    }

    /**
     * Method for running the OPL algorithm
     */
    public void runAlgorithm() {
        // give each candidate their number of votes
        for (int i = 0; i < this.ballots.size(); i++) {
            for (int j = 0; j < this.candidates.size(); j++) {
                if (this.ballots.get(i).getCandidateNumbers().get("1").getName().equals(this.candidates.get(j).getName())) {
                    this.candidates.get(j).incrementNumVotes();
                }
            }
        }

        // sort the candidates based off number of votes
        this.candidates.sort((object1, object2) -> 
            (object2.getNumVotes() - object1.getNumVotes()));

        // check for ties
        if (this.candidates.get(0).getNumVotes() == this.candidates.get(1).getNumVotes()) {
            int tiebreaker = this.coinFlip();
            // if tiebreaker is 1 then first candidate wins
            if (tiebreaker == 1) {
                this.candidates.get(0).setWinner(true);
            }
            else {
                this.candidates.get(1).setWinner(true);
            }
        }
    }

    /**
     * Method for displaying election results (DO NOT NEED FOR THIS PBI)
     */
    public void displayResults() {
        return;
    }

    /**
     * Method to simulate a coin flip for tie breakers
     * 
     * @return randomly chosen true (1) or false (0)
     */
    private int coinFlip() {
        return (int) Math.floor(Math.random() * 2);
    }
}
