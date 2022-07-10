/**
 * Authors: Daniel Clark, Kenji Her, Sangwoo Park, Rick Wang
 */
import java.io.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime; 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ReadCSV {

    private ArrayList<Candidate> candidates;
    private ArrayList<Ballot> ballots;
    private ArrayList<Ballot> invalidBallots;
    private String votingSystem;
    private int numSeats;

    /**
     * Constructor for ReadCSV
     */
    public ReadCSV() {
        votingSystem = "";
    }

    /**
     * Method for reading the CSV file
     * 
     * @param csv name of the csv file
     */
    public void readCSV(String csv) {
        try {
            File file = new File(csv);
            Scanner scanner = new Scanner(file, "UTF-8");
            // First 5 Lines 
            for (int i = 0; i < 5; i++) {
                if(scanner.hasNextLine()) {
                    switch (i) {
                        case 0: // Get the voting system    
                            votingSystem = scanner.nextLine();
                            break;
                        case 1: // Initialize candidates ArrayList
                            String numCandidatesString = scanner.nextLine();
                            int numCandidates = Integer.parseInt(numCandidatesString);
                            candidates = new ArrayList<Candidate>(numCandidates);
                            break;
                        case 2: // Get number of seats unless its a PO file. Then we get the candidates 
                            if (votingSystem.contains("PO")) {
                                String line = scanner.nextLine();
                                votingSystem = "PO";
                                line = line.substring(1, line.length() - 1);
                                String[] allCandidates = line.split("\\], \\[");
                                for(int j = 0; j < allCandidates.length; j++) {
                                    // Get the name of a candidate
                                    String candidateName = allCandidates[j].substring(0, allCandidates[j].indexOf(","));
                                    
                                    // Get the party of the candidate (one letter)
                                    String candidateParty = allCandidates[j].substring(allCandidates[j].indexOf(",") + 1, allCandidates[j].length());
                                    
                                    // Add each candidate to the ArrayList of candidates
                                    Candidate candidate = new Candidate(candidateParty, candidateName, 0);
                                    candidates.add(candidate);    
                                }
                                break;
                            } 
                            // else case for IRV and OPL files
                            String numSeatsString = scanner.nextLine();
                            numSeats = Integer.parseInt(numSeatsString);
                            break;
                        case 3: // Initialize ballots ArrayList
                            String numBallotsString = scanner.nextLine();
                            int numBallots = Integer.parseInt(numBallotsString);
                            ballots = new ArrayList<Ballot>(numBallots);
                            break;
                        case 4: // Populate candidates ArrayList 
                            
                            // List of candidates is different for the different ballots
                            if(votingSystem.contains("IRV")) { 
                                String line = scanner.nextLine();
                                votingSystem = "IRV";
                                String[] allCandidates = line.split(",");
                                for(int j = 0; j < allCandidates.length; j++) {
                                    // Get the name of a candidate
                                    String candidateName = allCandidates[j].substring(0, allCandidates[j].indexOf(" "));
                                    
                                    // Get the party of the candidate (one letter)
                                    String candidateParty = allCandidates[j].substring(allCandidates[j].indexOf("(") + 1, allCandidates[j].indexOf(")"));
                                    
                                    // Add each candidate to the ArrayList of candidates
                                    Candidate candidate = new Candidate(candidateParty, candidateName, 0);
                                    candidates.add(candidate);
                                } 
                            } else if (votingSystem.contains("OPL")) {
                                String line = scanner.nextLine();
                                votingSystem = "OPL";
                                line = line.substring(1, line.length() - 1);
                                String[] allCandidates = line.split("\\],\\[");
                                for(int j = 0; j < allCandidates.length; j++) {
                                    // Get the name of a candidate
                                    String candidateName = allCandidates[j].substring(0, allCandidates[j].indexOf(","));
                                   
                                    // Get the party of the candidate (one letter)
                                    String candidateParty = allCandidates[j].substring(allCandidates[j].indexOf(",") + 1, allCandidates[j].length());
        
                                    // Add each candidate to the ArrayList of candidates
                                    Candidate candidate = new Candidate(candidateParty, candidateName, 0);
                                    candidates.add(candidate);
                                } 
                            }     
                    }
                    
                }
            }           
            // make new array list for invalid ballots
            invalidBallots = new ArrayList<Ballot>();

            // Ballots
            
            // Set the ballotID to 1
            int ballotID = 1; 
           
            while (scanner.hasNextLine()) {
                // Get the individual ballot from csv file
                String line = scanner.nextLine();

                // initialize hashmap that corresponds each number in each individual ballot to a candidate
                HashMap<String, Candidate> candidateNumbers = new HashMap<String, Candidate>();

                // Convert individual ballots to an array of the numbers
                String[] votes = line.split(",", -1);

                // Check if voting system is IRV first
                if (this.votingSystem.equals("IRV")) {
                    // Get the number of ranked candidates
                    int numRanked = 0;
                    for (int i = 0; i < votes.length; i++) {
                        if (!votes[i].equals("")) {
                            numRanked++;
                        }
                    }

                    // The position of each number in votes array represents the candidate, and the number itself represents the ranking 
                    // of the candidates for each individual ballot
                    for(int i = 0; i < votes.length; i++) {
                        if(!votes[i].equals("")) {
                            candidateNumbers.put(votes[i], candidates.get(i));
                        }            
                    }
                    // Add each ballot to the ballots ArrayList and increment the ballotID
                    Ballot ballot = new Ballot(candidateNumbers, ballotID++);

                    // check if ballot has at least half the candidates ranked
                    if (numRanked >= (candidates.size() / 2.0)) {
                        ballots.add(ballot);
                    }
                    else {
                        this.invalidBallots.add(ballot);
                    }
                }
                else {
                    // The position of each number in votes array represents the candidate, and the number itself represents the ranking 
                    // of the candidates for each individual ballot
                    for(int i = 0; i < votes.length; i++) {
                        if(!votes[i].equals("")) {
                            candidateNumbers.put(votes[i], candidates.get(i));
                        }            
                    }

                    // Add each ballot to the ballots ArrayList and increment the ballotID
                    Ballot ballot = new Ballot(candidateNumbers, ballotID++);
                    ballots.add(ballot);
                }
            }
            scanner.close();

            this.createInvalidBallotFile(this.invalidBallots);
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * Method to write to a text file for invalid ballots
     * 
     * @param invalidBallots the invalid ballots
     */
    public void createInvalidBallotFile(ArrayList<Ballot> invalidBallots) {
        try {
            // check if invalidBallots actually has ballots
            if (invalidBallots.size() != 0) {
                // open up file to write to
                FileWriter myWriter = new FileWriter("invalidated_dateofelection.txt", false);
                myWriter.write("Invalid Ballot IDs:\n");

                // write the invalid ballot IDs
                for (int i = 0; i < invalidBallots.size(); i++) {
                    myWriter.write("" + invalidBallots.get(i).getBallotID() + "\n");
                }

                // close the file
                myWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method for store the candidate info of the csv file in the candidate class
     * 
     * @return arraylist with candidate
     */
    public ArrayList<Candidate> getCandidates() {
        return this.candidates;
    }

    /**
     * Method for store the ballot info of the csv file in the ballot class
     * 
     * @return arraylist of ballot
     */
    public ArrayList<Ballot> getBallots() {
        return this.ballots;
    }

    /**
     * Method for store the type of voting system in the string
     * 
     * @return String with the voting type
     */
    public String getVotingSystem() {
        return this.votingSystem;
    }

    /**
     * Method for getting the number of seats
     * 
     * @return number of seats
     */
    public int getNumSeats() {
        return this.numSeats;
    }

    /**
     * Method to get invalid ballots
     * 
     * @return arraylist of invalid ballot
     */
    public ArrayList<Ballot> getInvalidBallots() {
        return this.invalidBallots;
    }
}
