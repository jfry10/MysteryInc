package testcases;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunner {
	public static void main(String[] args) {
      
	   
	  // CardDeck test
	  Result result = JUnitCore.runClasses(CardTests.class);
	  for (Failure failure : result.getFailures()) {
		  	System.out.println(failure.toString());
	  }
	  System.out.print("CardDeck Tests Passed = ");
	  System.out.println(result.wasSuccessful());
	 
	      // DetectiveNotes test
	  result = JUnitCore.runClasses(DetectiveNotesTest.class);
	  for (Failure failure : result.getFailures()) {
		  	System.out.println(failure.toString());
	  }
	  System.out.print("DetectiveNotes Tests Passed = ");
	  System.out.println(result.wasSuccessful());
	
	  // Player test
	  result = JUnitCore.runClasses(PlayerTests.class);
	  for (Failure failure : result.getFailures())
	  {
		  	System.out.println(failure.toString());
	  }
	  System.out.print("Player Tests Passed = ");
	  System.out.println(result.wasSuccessful());
	  
	  // Accusation test
	  result = JUnitCore.runClasses(AccusationTest.class);
	  for (Failure failure : result.getFailures())
	  {
		  	System.out.println(failure.toString());
	  }
	  System.out.print("Accusation Tests Passed = ");
	  System.out.println(result.wasSuccessful());
	  
	  // Suggestion test
	  result = JUnitCore.runClasses(SuggestionTest.class);
	  for (Failure failure : result.getFailures())
	  {
		  	System.out.println(failure.toString());
	  }
	  System.out.print("Suggestion Tests Passed = ");
	  System.out.println(result.wasSuccessful());
	  
	  // Gameboard test
	  result = JUnitCore.runClasses(GameboardTests.class);
	  for (Failure failure : result.getFailures())
	  {
		  	System.out.println(failure.toString());
	  }
	  System.out.print("Gameboard Tests Passed = ");
	  System.out.println(result.wasSuccessful());
   }
  
}  