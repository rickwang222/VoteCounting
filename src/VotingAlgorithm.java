/**
 * Authors: Daniel Clark, Kenji Her, Sangwoo Park, Rick Wang
 */
public interface VotingAlgorithm {
    
    /**
     * Method for running the IRV algorithm
     * 
     * @param candidates the candidates in the election
     * @param ballots the ballots in the election
     */
    public void runAlgorithm();

    /**
     * Method for displaying the election results
     */
    public void displayResults();
}
