/**
 * Authors: Daniel Clark, Kenji Her, Sangwoo Park, Rick Wang
 */
import java.util.*;
import java.io.*;
public class OPL implements VotingAlgorithm {
    
    private ArrayList<Candidate> candidates;
    private ArrayList<Ballot> ballots;
    private Audit results;
    private int numSeats;
    private ArrayList<Party> parties;
    private int quota;

    /**
     * Constructor for the OPL class
     * 
     * @param candidates the candidates in the election
     * @param ballots the ballots in the election
     * @param results the results of the election
     */
    public OPL(ArrayList<Candidate> candidates, ArrayList<Ballot> ballots, Audit results, int numSeats) {
        this.candidates = candidates;
        this.ballots = ballots;
        this.results = results;
        this.numSeats = numSeats;
        this.quota = 0;
        this.parties = new ArrayList<Party>();

        results.setVotingSystem("OPL");
        results.setNumCandidates(this.candidates.size());
        results.setCandidates(this.candidates);
        results.setNumBallots(this.ballots.size());
        results.setBallots(this.ballots);
        // give each candidate their number of votes
        for (int i = 0; i < this.ballots.size(); i++) {
            for (int j = 0; j < this.candidates.size(); j++) {
                if (this.ballots.get(i).getCandidateNumbers().get("1").getName().equals(this.candidates.get(j).getName())) {
                    this.candidates.get(j).incrementNumVotes();
                }
            }
        }
        // divide the candidates into parties
        for (int i = 0; i < this.candidates.size(); i++) {
            boolean contains = false;
            for (int j = 0; j < this.parties.size(); j++) {
                if (this.candidates.get(i).getParty().equals(this.parties.get(j).getNameParty())) {
                    this.parties.get(j).getPartyCandidates().add(this.candidates.get(i));
                    contains = true;
                    break;
                }
            }
            if (contains == false) {
                Party newParty = new Party(this.candidates.get(i).getParty());
                newParty.addCandidate(this.candidates.get(i));
                this.parties.add(newParty);
            }
            contains = false;
        }

        // populate the total number of party votes
        for (int i = 0; i < this.parties.size(); i++) {
            int numPartyVotes = 0;
            for (int j = 0; j < this.parties.get(i).getPartyCandidates().size(); j++) {
                numPartyVotes += this.parties.get(i).getPartyCandidates().get(j).getNumVotes();
            }
            this.parties.get(i).setNumPartyVotes(numPartyVotes);
        }
    }

    /**
     * Method for running the OPL algorithm
     */
    public void runAlgorithm() {
        // calculate the quota
        int numBallots = this.ballots.size();
        int numSeats = this.numSeats;
        this.quota = numBallots / numSeats;

        // calculate each party's number of seats and the remainder
        int numFirstRoundSeats = 0;
        for (int i = 0; i < this.parties.size(); i++) {
            int numPartySeats = this.parties.get(i).getNumPartyVotes() / this.quota;
            int remainder = this.parties.get(i).getNumPartyVotes() % this.quota; 
            // If first allocation of seats is greater than number of candidates available for the party,
            // then limit the number of seats to the number of candidates in the party
            // (prevents situations where there ends up being more seats than candidates per party)
            if(numPartySeats > this.parties.get(i).getPartyCandidates().size()){
                numPartySeats = this.parties.get(i).getPartyCandidates().size();
                this.parties.get(i).setNumPartySeats(numPartySeats);
                numFirstRoundSeats += numPartySeats;
            } else {
                this.parties.get(i).setNumPartySeats(numPartySeats);
                numFirstRoundSeats += numPartySeats;
            }       
            // Get the remaining votes for each party
            this.parties.get(i).setRemainder(remainder);
        }

        // account for remaining seats 
        if (numFirstRoundSeats < numSeats) {
            // get the remaining seats that are available to reallocate
            int remainingSeats = numSeats - numFirstRoundSeats;
            // sort parties based off remainder
            this.parties.sort((object1, object2) -> 
                (object2.getRemainder() - object1.getRemainder()));
            for (int i = 0; i < this.parties.size(); i++) {
                // Make sure there are remaining seats
                // Make sure that there are more party candidates than the seats that the party received 
                // (prevents situations where there ends up being more seats than candidates per party)
                if(this.parties.get(i).getPartyCandidates().size() > this.parties.get(i).getNumPartySeats() && remainingSeats > 0) {
                    // hypothetically parties can't have more than 1 additional seat
                    this.parties.get(i).setAdditionalSeats(1);
                    remainingSeats--;
                }      
            }
        }

        // determine the winners of each party
        for (int i = 0; i < this.parties.size(); i++) {
            // check if the party has seats
            int numPartySeats = this.parties.get(i).getNumPartySeats() + this.parties.get(i).getAdditionalSeats();
            // sort candidates from most to least number of votes
            this.parties.get(i).getPartyCandidates().sort((object1, object2) -> 
            (object2.getNumVotes() - object1.getNumVotes()));

            if (numPartySeats > 0) {       
                // make candidates winners

                // If the number of candidates is equal to the number of seats, then there is no need for a coinflip as
                // everybody would be a winner in the party (Note: it was made impossible for numSeats > numCandidates)
                if(numPartySeats == this.parties.get(i).getPartyCandidates().size()) {
                    for (int j = 0; j < this.parties.get(i).getPartyCandidates().size(); j++) {
                        this.parties.get(i).getPartyCandidates().get(j).setWinner(true);
                    }
                } else {
                    // Tie-breaker
                    // It is assumed that there can only be two way ties, so the edge case would always
                    // involve the two candidates at the index of numPartySeats - 1 and numPartySeats
                    for (int j = 0; j < numPartySeats - 1; j++) {
                        this.parties.get(i).getPartyCandidates().get(j).setWinner(true);          
                    }

                    for (int j = numPartySeats - 1; j < numPartySeats; j++) {
                        if  (this.parties.get(i).getPartyCandidates().get(j).getNumVotes() > 
                            this.parties.get(i).getPartyCandidates().get(j + 1).getNumVotes()) {
                            this.parties.get(i).getPartyCandidates().get(j).setWinner(true);          
                        } else {
                            int tieBreaker = this.coinFlip();
                            this.parties.get(i).getPartyCandidates().get(j + tieBreaker).setWinner(true);
                        }         
                    }
                }  
            }
        }        
        this.createAuditFile(results);
    }

    /**
     * Method for displaying the election results
     */
    public void displayResults() {
        Collections.reverse(this.parties);
        this.parties.sort((object1, object2) -> 
                (object2.getNumPartyVotes() - object1.getNumPartyVotes()));
        System.out.println("Open Party Listing");
        System.out.println("Quota: " + this.quota);
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("                        First       Remaining   Second      Final       % of Vote   ");
        System.out.println("Parties     Votes       Allocation  Votes       Allocation  Seat        to          ");
        System.out.println("                        Of Seats                Of Seats    Total       % of Seats  ");
        System.out.println("------------------------------------------------------------------------------------");
        for (int i = 0; i < this.parties.size(); i++) {
            System.out.printf("%-12s", this.parties.get(i).getNameParty());
            System.out.printf("%,-12d", this.parties.get(i).getNumPartyVotes());
            System.out.printf("%-12d", this.parties.get(i).getNumPartySeats());
            System.out.printf("%,-12d", this.parties.get(i).getRemainder());
            System.out.printf("%-12d", this.parties.get(i).getAdditionalSeats());
            System.out.printf("%-12d", this.parties.get(i).getNumPartySeats() + this.parties.get(i).getAdditionalSeats());
            System.out.printf("%-1.0f", (this.parties.get(i).getNumPartyVotes() / (double) this.ballots.size()) * 100);
            System.out.print("% / ");
            System.out.printf("%-1.0f", ((this.parties.get(i).getNumPartySeats() + this.parties.get(i).getAdditionalSeats()) / (double) this.numSeats) * 100);
            System.out.println("%");
        }
    }

    /**
     * Method to simulate a coin flip for tie breakers
     * 
     * @return randomly chosen true (1) or false (0)
     */
    private int coinFlip() {
        return (int) Math.floor(Math.random() * 2);
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
            myWriter.write("Ballot Distribution: " + System.getProperty("line.separator"));
            for(int i = 0; i < results.getCandidates().size(); i++) {
                myWriter.write("\t" + results.getCandidates().get(i).getName() + ": ");
                for(int j = 0; j < results.getBallots().size(); j++) {
                    if(results.getBallots().get(j).getCandidateNumbers().get("1").getName().equals(results.getCandidates().get(i).getName())) {
                        myWriter.write(Integer.toString(results.getBallots().get(j).getBallotID()));
                        if(j < results.getBallots().size() - 1) {
                            myWriter.write(", ");
                        }
                    }
                }
                myWriter.write(System.getProperty("line.separator"));
            }
            myWriter.write("Winners: ");
            for(int i = 0; i < results.getCandidates().size(); i++) {
                if(this.candidates.get(i).getWinner()) {
                    myWriter.write(results.getCandidates().get(i).getName());
                    if(i < results.getCandidates().size() - 1) {
                        myWriter.write(", ");
                    }
                }         
            }
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Created Getters for testing purposes
    // candidates, ballots, results, numSeats, parties
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

    public ArrayList<Party> getParties() {
        return this.parties;
    }
}
