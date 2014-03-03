package abacus.tutorial.applet;

public class AbacusEvaluatorTest {
	
	/*Test the proficiency calculation of the BackOperation method. 
	 *The test print probability for each attempted question to show the
	 *progressive increase in percentage as student work through the question.
	 *Some question also registered mistake to see if the weight will be taken into account.
	 *The result is then compare to the expected result which have been prepared in the 
	 *SpreadSheet proficiency table. */

	public static void main(String args[]) {
		
		BackOperation backOp = new BackOperation();
		
		for (int i = 1; i < 11; i++){
			System.out.println(backOp.evaluator(1 * i, 0));
		}
		System.out.println("Expected result: achieved 96% at question 10.");
		
		/* Set new level to test if the instance variables will also get reset. */
		backOp.setLevel();
		System.out.println();
		System.out.println("New question set below:");
		
		/* Simulate 5 mistakes in question 1 and 2 mistakes in question 2. */
		System.out.println(backOp.evaluator(1, 5));
		System.out.println(backOp.evaluator(2, 2));
		
		for (int i = 3; i < 12; i++){
			System.out.println(backOp.evaluator(1 * i, 0));
		}
		System.out.println("Expected result: achieved 96% at question 11.");
		
	}
}
