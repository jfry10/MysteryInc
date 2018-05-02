package testcases;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunner {
   public static void main(String[] args) {
		
	  // first generic test
      Result result = JUnitCore.runClasses(TestJUnit.class);
      for (Failure failure : result.getFailures()) {
    	  	System.out.println(failure.toString());
      }
      
      // CardDeck test
      result = JUnitCore.runClasses(CardTests.class);
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
   }
}  