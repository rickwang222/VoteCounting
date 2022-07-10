The program is run by typing "javac DualSystemBallotCounter.java" followed by java DualSystemBallotCounter arg is the path to the csv file and all csv files that we used are stored in the testing folder. A few tests such as partyTest and readCSVTest need to be compiled and run on their own since they do not use junit to test as it involved comparing print statements manually. 

There are numerous types of voting algorithms and in the United States, we typically use plurality voting where each 
voter is allowed to vote for only one candidate, and the candidate who polls the most votes is elected.  It is rare for an 
election to be tied but if that occurs, there is typically a runoff between the tied candidates.  For example, there have been 
three cases in history where there was a tie in the Electoral College for a presidential election.  The House of 
Representatives then decided who was president by voting.  For small sized, local elections a run-off may occur or even 
a coin flip can decide the outcome in some cases.  Much research has been performed on voting theory and some believe 
that the other types of voting are better than our style of voting.
This vote counting system is capable of performing two algorithms: instant runoff (IR) and open party listing (OPL).
