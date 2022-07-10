/**
 * Authors: Daniel Clark, Kenji Her, Sangwoo Park, Rick Wang
 */
import java.util.*;
import java.io.*;

public class IRV implements VotingAlgorithm {
    
    private ArrayList<Candidate> candidates;
    private ArrayList<Ballot> ballots;
    private Audit results;
    private int numSeats;

    /**
     * Constructor for the IRV class
     * 
     * @param candidates the candidates in the election
     * @param ballots the ballots in the election
     * @param results the results of the election
     * @param numSeats the number of seats in the election
     */
    public IRV(ArrayList<Candidate> candidates, ArrayList<Ballot> ballots, Audit results, int numSeats) {
        this.candidates = candidates;
        this.ballots = ballots;
        this.results = results;
        this.numSeats = numSeats;
        results.setVotingSystem("IRV");
        results.setNumCandidates(this.candidates.size());
        results.setCandidates(this.candidates);
        results.setNumBallots(this.ballots.size());
        results.setBallots(this.ballots);
        //shuffle the ballots before processing
        shuffle();
    }

    /**
     * Method for running the IRV algorithm
     * 
     * @param candidates the candidates in the election
     * @param ballots the ballots in the election
     */
    public void runAlgorithm() {

        //candidates will be removed from currentCandidate if decleared a winner or loser
        ArrayList<Candidate> currentCandidates = new ArrayList<Candidate>();
        
        // create a deep copy of the candidates array
        for(int i = 0; i < this.candidates.size(); i++) {
            currentCandidates.add(this.candidates.get(i));
        }

        Stack<Candidate> losers = new Stack<>();
        Stack<Candidate> winners = new Stack<>();
        double winRate = 1.0/(((double)numSeats) + 1);
        int totalBallots = this.ballots.size();
        Candidate currentLoser;
        int round = 1;

        //loop through ballots untill all the seats are filled
        while(winners.size() != numSeats){

            //new ballots for loser
            ArrayList<Ballot> currentBallots = new ArrayList<Ballot>();

            //remove loser with the lowest votes, and redistribute the loser's ballots after the first round
            if(round > 1){
                int low = totalBallots;

                for (int j = 0; j < currentCandidates.size(); j++) {
                    if(currentCandidates.get(j).getNumVotes() < low){
                        low = currentCandidates.get(j).getNumVotes();
                    }
                }    

                int newLosers = 0;
                for (int j = 0; j < currentCandidates.size(); j++) {
                    if(currentCandidates.get(j).getNumVotes() == low){
                        losers.add(currentCandidates.get(j));
                        newLosers++;
                        currentCandidates.get(j).setLoser(true);
                        currentCandidates.remove(j--);
                    }
                }

                if(newLosers >= 2){
                    Candidate tie1;
                    Candidate tie2;
                    tie1 = losers.pop();
                    tie2 = losers.pop();

                    if (coinFlip() == 1){
                        losers.add(tie1);
                        tie2.setLoser(false);
                        currentCandidates.add(tie2);
                    }
                    else{
                        losers.add(tie2);
                        tie1.setLoser(false);
                        currentCandidates.add(tie1);
                    }
                }
                
                // find the current loser and go through all the ballot to find their ballots
                currentLoser = losers.peek();
                System.out.printf("loser name: %s %n", currentLoser.getName());
                System.out.printf("loser votes: %d %n", currentLoser.getNumVotes());
                for (int i = 0; i < currentLoser.getBallots().size(); i++) {
                    currentBallots.add(currentLoser.getBallots().get(i));
                }
                // re-allocate all votes of loser to their next highest rank
                for (int i = 0; i < currentBallots.size(); i++) {
                    System.out.print(currentBallots.get(i).getBallotID() + " ");
                    Candidate newCandidate = currentBallots.get(i).getCandidateNumbers().get(Integer.toString(currentBallots.get(i).getRank() + 1));
                    while(winners.contains(newCandidate) || losers.contains(newCandidate)) {                   
                        newCandidate = currentBallots.get(i).getCandidateNumbers().get(Integer.toString(currentBallots.get(i).getRank() + 1));
                        currentBallots.get(i).setRank(currentBallots.get(i).getRank() + 1);
                    }
                    newCandidate.addBallot(currentBallots.get(i));
                    newCandidate.incrementNumVotes();
                }

                // reset the loser's ballots
                currentBallots.clear();

                currentCandidates.sort((object1, object2) ->
                object2.getNumVotes() - object1.getNumVotes());

                //check if any candidate has won after each round of counting
                for (int j = 0; j < currentCandidates.size(); j++) {
                    if (((double)currentCandidates.get(j).getNumVotes()) / ((double)totalBallots) >= winRate) {
                        winners.add(currentCandidates.get(j));
                        currentCandidates.remove(j--);
                    }
                }
                       
                //check for tie between new winners
                if (winners.size() > numSeats){
                    Candidate tie1;
                    Candidate tie2;
                    tie1 = winners.pop();
                    tie2 = winners.pop();
                    if (tie1.getNumVotes() > tie2.getNumVotes()) {
                        winners.add(tie1);
                        currentCandidates.add(tie2);
                    } else if (tie1.getNumVotes() < tie2.getNumVotes()) {
                        winners.add(tie2);
                        currentCandidates.add(tie1);
                    } else {
                        if (coinFlip() == 1){
                            winners.add(tie1);
                            currentCandidates.add(tie2);
                        }
                        else{
                            winners.add(tie2);
                            currentCandidates.add(tie1);
                        }
                    }
                }

                // If the number of seats remaining matches the number of candidates remaining, then the rest are declared winners
                if(this.numSeats - winners.size() == currentCandidates.size()) {
                    for(int i = 0; i < currentCandidates.size(); i++) {
                        winners.add(currentCandidates.get(i));
                        currentCandidates.remove(i--);
                    }
                }

                for (int i = 0; i < this.candidates.size(); i++) {
                    if(this.candidates.get(i).getLoser()) {
                        System.out.print(this.candidates.get(i).getName() + " (L): " + 0 + ", ");      
                    } else {
                        System.out.print(this.candidates.get(i).getName() + " : " + this.candidates.get(i).getBallots().size() + ", ");
                    }
                }
                System.out.println();

                System.out.printf("%d round done ------------- %n", round);
            }

            //round 1
            //count all the ballot for the first round
            else{
                // get every ballot's number one choice
                for (int i = 0; i < totalBallots; i++) {
                    for (int j = 0; j < currentCandidates.size(); j++) {
                        if (this.ballots.get(i).getCandidateNumbers().get("1").getName().equals(currentCandidates.get(j).getName())) {
                            currentCandidates.get(j).incrementNumVotes();
                            currentCandidates.get(j).addBallot(this.ballots.get(i));
                        }
                    }
                }

                for (int j = 0; j < currentCandidates.size(); j++) {
                    currentCandidates.get(j).setInitialVotes(currentCandidates.get(j).getNumVotes());
                }

                currentCandidates.sort((object1, object2) ->
                object2.getNumVotes() - object1.getNumVotes());

                //check if any candidate has won after each round of counting
                for (int j = 0; j < currentCandidates.size(); j++) {
                    if ((currentCandidates.get(j).getNumVotes() / ((double)totalBallots)) >= winRate) {
                        winners.add(currentCandidates.get(j));
                        currentCandidates.remove(j--);
                    }
                }

                //check for tie between new winners
                if (winners.size() > numSeats){
                    Candidate tie1;
                    Candidate tie2;
                    tie1 = winners.pop();
                    tie2 = winners.pop();
                    if (tie1.getNumVotes() > tie2.getNumVotes()) {
                        winners.add(tie1);
                        currentCandidates.add(tie2);
                    } else if (tie1.getNumVotes() < tie2.getNumVotes()) {
                        winners.add(tie2);
                        currentCandidates.add(tie1);
                    } else {
                        if (coinFlip() == 1){
                            winners.add(tie1);
                            currentCandidates.add(tie2);
                        }
                        else{
                            winners.add(tie2);
                            currentCandidates.add(tie1);
                        }
                    }
                }
                
                // If the number of seats remaining matches the number of candidates remaining, then the rest are declared winners
                if(this.numSeats - winners.size() == currentCandidates.size()) {
                    for(int i = 0; i < currentCandidates.size(); i++) {
                        winners.add(currentCandidates.get(i));
                        currentCandidates.remove(i--);
                    }
                }

                for (int i = 0; i < this.candidates.size(); i++) {
                    if(this.candidates.get(i).getLoser()) {
                        System.out.print(this.candidates.get(i).getName() + " (L): " + 0 + ", ");      
                    } else {
                        System.out.print(this.candidates.get(i).getName() + " : " + this.candidates.get(i).getBallots().size() + ", ");
                    }
                }
                System.out.println();

                System.out.printf("%d round done -------------%n", round);
            }

            round++;
        }
    }

    /**
     * Method for displaying the election results
     */
    public void displayResults() {
        this.candidates.sort((object1, object2) ->
            object2.getInitialVotes() - object1.getInitialVotes());
        System.out.println("Instant Runoff Voting");
        System.out.println("--------------------------------------------------------------------------------------------------");
        System.out.println("                        First Count             Second Count                Third Count           ");
        System.out.println("--------------------------------------------------------------------------------------------------");
        System.out.println("     Candidates           Original          Transfer        New         Transfer        New       ");
        System.out.println("         &              First Choice           of          Totals          of          Totals     ");
        System.out.println("      Parties              Votes             Votes                       Votes                   ");
        System.out.println("--------------------------------------------------------------------------------------------------");
        for (int i = 0; i < this.candidates.size(); i++) {
            System.out.printf("%-15s", this.candidates.get(i).getName() + " (" + this.candidates.get(i).getParty() + ")");
            System.out.printf("%15d", this.candidates.get(i).getInitialVotes());

            if(this.candidates.get(i).getLoser()) {
                System.out.printf("%15s", "---");
            } else {
                System.out.printf("%15s", "---");
            }

            if(this.candidates.get(i).getLoser()) {
                System.out.printf("%15s", "---");
            } else {
                System.out.printf("%15s", "---");
            }
            
            if(this.candidates.get(i).getLoser()) {
                System.out.printf("%15s", "---");
            } else {
                System.out.printf("%15s", "---");
            }

            if(this.candidates.get(i).getLoser()) {
                System.out.printf("%15s", "---");
            } else {
                System.out.printf("%15s", this.candidates.get(i).getNumVotes() + " (W)");
            }
                
            System.out.println();
        }
    }

    /**
     * Method to simulate a coin flip for tie breakers
     * 
     * @return randomly chosen true or false
     */
    private int coinFlip() {

        int result = 0;
        //run random for 100 times 
        for(int i = 0; i < 100; i++){
            result = (int) Math.floor(Math.random() * 2);
        }

        return result;
    }

    /**
     * Method for shuffling the ballots
     */
    private void shuffle() {
        Random randomIndex = new Random();
        
        for (int i = this.ballots.size() - 1; i >= 1; i--) {
            Collections.swap(this.ballots, i, randomIndex.nextInt(i + 1));
        }
    }

    /**
     * Method to create and write to an audit file
     * 
     * @param results
     */
    private void createAuditFile(Audit results) {
        try {
            FileWriter myWriter = new FileWriter("Audit.txt", false);
            myWriter.write("Type of Voting: " + results.getVotingSystem() + System.getProperty("line.separator"));
            myWriter.write("Number of Candidates: " + results.getNumCandidates() + System.getProperty("line.separator"));
            myWriter.write("List of Candidates: ");
            for(int i = 0; i < results.getCandidates().size(); i++) {
                myWriter.write(results.getCandidates().get(i).getName());
                if(i < results.getCandidates().size() - 1) {
                    myWriter.write(", ");
                }
            }
            myWriter.write(System.getProperty("line.separator"));
            myWriter.write("Number of Votes Per Candidate: " + System.getProperty("line.separator"));
            for(int i = 0; i < results.getCandidates().size(); i++) {
                myWriter.write("\t" + results.getCandidates().get(i).getName() + ": " + results.getCandidates().get(i).getNumVotes() + System.getProperty("line.separator"));
            }
            myWriter.write("Number of Ballots: " + results.getNumBallots() + System.getProperty("line.separator"));
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Created Getters for testing purposes
    // candidates, ballots, results, numSeats
    public ArrayList<Candidate> getCandidates() {
        return this.candidates;
    }

    public ArrayList<Ballot> getBallots() {
        return this.ballots;
    }

    public Audit getResults() {
        return this.results;
    }

    public int getNumSeats() {
        return this.numSeats;
    }
}
