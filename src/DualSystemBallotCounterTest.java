/**
 * Authors: Daniel Clark, Kenji Her, Sangwoo Park, Rick Wang
 */
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;



import org.junit.jupiter.api.Test;
 /**
 * This is the test file for the main driver of the software
*/
public class DualSystemBallotCounterTest {

    
    /**
     * This is a test to check if the program behaves as expected with no arguments passed in
     */
    @Test
    public void testNoArgs() {
        String[] args = {

        };
        DualSystemBallotCounter testDriver = new DualSystemBallotCounter();
        assertEquals("Error: Not enough arguments given", testDriver.testArgsCount(args));
        
    }

    /**
     * This is a test to check if the program behaves as expected with 1 argument passed in
     */
    @Test
    public void testOneArg() {
        String[] args = {
            "../testing/OPL_test.csv"
        };
        DualSystemBallotCounter testDriver = new DualSystemBallotCounter();
        assertEquals("Correct", testDriver.testArgsCount(args));
    }

    /**
     * This is a test to check if the program behaves as expected with too many arguments passed in
     */
    @Test
    public void testTooManyArgs() {
        String[] args = {
            "../testing/OPL_test.csv",
            "extraArg",
            "Another"
        };
        DualSystemBallotCounter testDriver = new DualSystemBallotCounter();
        assertEquals("Error: Too many arguments given", testDriver.testArgsCount(args));
    }

    /**
     * This is a test to check if the program behaves as expected when IRV is selected
     */
    @Test
    public void testIRV() {
        DualSystemBallotCounter testDriver = new DualSystemBallotCounter();
        String csvPath = "../testing/IRV_test.csv";
        ReadCSV read = new ReadCSV();
        read.readCSV(csvPath);
        String votingAlgorithm = "";
        votingAlgorithm = read.getVotingSystem();
        assertEquals("IRV METHOD", testDriver.testVotingMethod(votingAlgorithm));
    }

    /**
     * This is a test to check if the program behaves as expected when OPL is selected
     */
    @Test
    public void testOPL() {
        DualSystemBallotCounter testDriver = new DualSystemBallotCounter();
        String csvPath = "../testing/OPL_test.csv";
        ReadCSV read = new ReadCSV();
        read.readCSV(csvPath);
        String votingAlgorithm = "";
        votingAlgorithm = read.getVotingSystem();
        assertEquals("OPL METHOD", testDriver.testVotingMethod("OPL"));
    }

    /**
     * This is a test to check if the program behaves as expected when OPL is selected
     */
    @Test
    public void testMultipleFiles() {
        String[] args = {
            "../testing/OPL_test.csv",
            "../testing/OPL_test2.csv",
        };
        DualSystemBallotCounter testDriver = new DualSystemBallotCounter();
        assertEquals("Combined Successfully", testDriver.testMultipleFiles(args));
    }
}