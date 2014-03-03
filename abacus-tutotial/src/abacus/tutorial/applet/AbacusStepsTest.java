package abacus.tutorial.applet;

public class AbacusStepsTest {
	
	/*Test the BackOperation constructor for accuracy of the abacus translator,
	 *the question generator and other get methods. Print out the two randomly 
	 *generated question numbers and each of their abacus steps. The steps then 
	 *get tested on the actual abacus manually. */
	
	public static void main(String args[]){
		
		BackOperation backOp = new BackOperation();
		
		backOp.setQuestion();
		System.out.println("The two numbers are:");
		System.out.println(backOp.getsetNumber());
		System.out.println(backOp.getOperatorNumber());
		System.out.println("Operator " + ((backOp.getOperator())?"(+)":"(-)"));
		System.out.println("The steps are as follow:");
		
		for (int i = 0; i < backOp.getSolutionCount(); i++){
			System.out.println(backOp.stepGetter(i));
		}
		
		System.out.println();
		backOp.reset();
		backOp.setLevel();
		backOp.setQuestion();
		System.out.println("The two numbers are:");
		System.out.println(backOp.getsetNumber());
		System.out.println(backOp.getOperatorNumber());
		System.out.println("Operator " + ((backOp.getOperator())?"(+)":"(-)"));
		System.out.println("The steps are as follow:");
		
		for (int i = 0; i < backOp.getSolutionCount(); i++){
			System.out.println(backOp.stepGetter(i));
		}
		
		System.out.println();
		backOp.reset();
		
		backOp.setLevel();
		backOp.setQuestion();
		System.out.println("The two numbers are:");
		System.out.println(backOp.getsetNumber());
		System.out.println(backOp.getOperatorNumber());
		System.out.println("Operator " + ((backOp.getOperator())?"(+)":"(-)"));
		System.out.println("The steps are as follow:");
		
		for (int i = 0; i < backOp.getSolutionCount(); i++){
			System.out.println(backOp.stepGetter(i));
		}
		
		System.out.println();
		backOp.reset();
		backOp.setLevel();
		backOp.setQuestion();
		System.out.println("The two numbers are:");
		System.out.println(backOp.getsetNumber());
		System.out.println(backOp.getOperatorNumber());
		System.out.println("Operator " + ((backOp.getOperator())?"(+)":"(-)"));
		System.out.println("The steps are as follow:");
		
		for (int i = 0; i < backOp.getSolutionCount(); i++){
			System.out.println(backOp.stepGetter(i));
		}
		
		System.out.println();
		backOp.reset();
		backOp.setLevel();
		backOp.setQuestion();
		System.out.println("The two numbers are:");
		System.out.println(backOp.getsetNumber());
		System.out.println(backOp.getOperatorNumber());
		System.out.println("Operator " + ((backOp.getOperator())?"(+)":"(-)"));
		System.out.println("The steps are as follow:");
		
		for (int i = 0; i < backOp.getSolutionCount(); i++){
			System.out.println(backOp.stepGetter(i));
		}
	}

}
