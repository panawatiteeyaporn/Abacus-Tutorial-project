package abacus.tutorial.applet;

import java.util.ArrayList;

import acm.util.RandomGenerator;

public class BackOperation {

	// Contain all steps for the solution.
	ArrayList<Integer> stepList = new ArrayList<Integer>();

	/* BackOperation constructor. */
	public BackOperation() {

		instruction();
	}

	/* Set the operator and the two numbers for the question. */
	public void setQuestion() {

		setQuestionType(1); // replace with rgen.nextInt(0, 1)
		questionGenerator();
		checkQuestionSteps();
	}

	/*
	 * Check if the stepList variable have any value. i.e. the two number
	 * require no movement.
	 */
	private void checkQuestionSteps() {

		if (stepList.size() == 0) {
			setQuestion();
		}
	}

	/*
	 * Taking in two numbers for the question and simplify it for abacus's
	 * operations.
	 */
	private void abacusTranslator(int num1, int num2) {

		abacusDivider(num2);
		stepSolution(num1);
	}

	/*
	 * Reset the arrays for the next question. - May not need this as each class
	 * call have their own solution attached and there is no need to reset a
	 * question. If student request the same question then just input the
	 * numbers required.
	 */
	public void reset() {

		stepList.removeAll(stepList);

		for (int i = 0; i < abacusValue.length; i++) {
			abacusValue[i] = 0;
		}

		isCase5 = false;
		isCase10 = false;
		temp = 0;

	}

	/*
	 * Work out the abacus's steps by adding/subtracting each component value of
	 * num2 (contain within abacusValue array) to the value of num1, and record
	 * those steps in stepList array. Also check for case5 & case10 techniques
	 * and modify the steps as needed.
	 */
	private void stepSolution(int num1) {

		temp = num1;

		for (int i = 0; i < 8; i++) {

			if (abacusValue[i] != 0) {

				// case10 should also deal exclusively with case10/5 scenario.
				checkCase10(temp, abacusValue[i], i);

				if (isCase10 == false) {
					checkCase5(temp, abacusValue[i], i);
				}

				if (isCase5 == false && isCase10 == false) {

					int rod = getRodValue(i);

					if (isOperatorAdd == true) {
						if (abacusValue[i] > 5 * rod) {
							refineNormalSteps(abacusValue[i], rod);
						} else {
							temp = temp + abacusValue[i];
							stepList.add(temp);
						}

					} else if (isOperatorAdd == false) {

						if (abacusValue[i] > 5 * rod) {
							refineNormalSteps(abacusValue[i], rod);
						} else {
							temp = temp - abacusValue[i];
							stepList.add(temp);
						}
					}
				}

				isCase5 = false;
				isCase10 = false;
			}
		}

	}

	/*
	 * The method refine normal operation that include 5th value i.e. 8 - 7 = 1
	 * but the abacus operational sequences are 8 - 5 - 2 = 1. or 3 + 6 = 9 with
	 * abacus sequences as 3 + 5 + 1 = 9.
	 */
	private void refineNormalSteps(int add_num, int rod) {

		int fifthRod = 5 * rod;
		int remainder = add_num % fifthRod;

		if (isOperatorAdd == true) {
			temp = temp + fifthRod;
			stepList.add(temp);
			temp = temp + remainder;
			stepList.add(temp);
		} else {
			temp = temp - fifthRod;
			stepList.add(temp);
			temp = temp - remainder;
			stepList.add(temp);
		}
	}

	/*
	 * Determine which case 10 is used for the subtraction questions, and set
	 * additional steps accordingly to stepList. Note: The method provides only
	 * the first part of the check.
	 */
	private void abacusCase10MinusSolution(int add_num, int rod_index) {

		int rod = getRodValue(rod_index);

		if (add_num == 1) {
			temp = temp + (5 * rod);
			stepList.add(temp);
			temp = temp + (4 * rod);
			stepList.add(temp);

		} else if (add_num == 2) {
			temp = temp + (5 * rod);
			stepList.add(temp);
			temp = temp + (3 * rod);
			stepList.add(temp);

		} else if (add_num == 3) {
			temp = temp + (5 * rod);
			stepList.add(temp);
			temp = temp + (2 * rod);
			stepList.add(temp);

		} else if (add_num == 4) {
			temp = temp + (5 * rod);
			stepList.add(temp);
			temp = temp + (1 * rod);
			stepList.add(temp);

		} else if (add_num == 5) {
			temp = temp + (5 * rod);
			stepList.add(temp);

		} else {
			moreCheckCase10Minus(add_num, rod_index);
		}

		rollOverCheckCase10Minus(rod_index);

		if (rollOver == false) {
			temp = temp - (10 * rod);
			stepList.add(temp);
		}

		rollOver = false;

	}

	/*
	 * Check the abacus's steps for the special case where subtraction rollOver
	 * case10 occurs. This special case cause roll over effect and require every
	 * rods on the abacus to be adjusted. i.e. (1000 - 1 =
	 * 1005,1009,1059,1099,1599,1999,999)
	 */
	private void rollOverCheckCase10Minus(int rod_index) {

		int quest_num = getSingleDigit(temp, rod_index - 1);

		if (quest_num == 5) {
			rollOver = true;
			checkCase5Minus(quest_num, 1, rod_index - 1);
		} else if (quest_num == 0) {
			rollOver = true;
			rollOverCaseAdjust_Minus(rod_index - 1);
		}
	}

	/*
	 * Adjust the abacus's steps for the special roll over case in subtraction
	 * questions.
	 */
	private void rollOverCaseAdjust_Minus(int rod_index) {

		int rod = getRodValue(rod_index);
		int index = rod_index;
		int nextPosition_num;

		do {

			temp = temp + (5 * rod);
			stepList.add(temp);

			temp = temp + (4 * rod);
			stepList.add(temp);

			index = index - 1;

			nextPosition_num = getSingleDigit(temp, index);

			if (nextPosition_num == 5) {
				checkCase5Minus(nextPosition_num, 1, index);
				break;
			}

			rod = rod * 10;

		} while (nextPosition_num == 0);

		if (nextPosition_num != 5) {
			temp = temp - (1 * rod);
			stepList.add(temp);
		}
	}

	/* Continue second part of case10 check for subtraction questions. */
	private void moreCheckCase10Minus(int add_num, int rod_index) {

		int quest_num = getSingleDigit(temp, rod_index);
		int rod = getRodValue(rod_index);

		if (add_num == 6) {
			if (quest_num < 5 && quest_num > 0) {
				checkCase5Plus(quest_num, 4, rod_index);
			} else {
				temp = temp + (4 * rod);
				stepList.add(temp);
			}

		} else if (add_num == 7) {
			if (quest_num < 5 && quest_num > 1) {
				checkCase5Plus(quest_num, 3, rod_index);
			} else {
				temp = temp + (3 * rod);
				stepList.add(temp);
			}

		} else if (add_num == 8) {
			if (quest_num < 5 && quest_num > 2) {
				checkCase5Plus(quest_num, 2, rod_index);
			} else {
				temp = temp + (2 * rod);
				stepList.add(temp);
			}
		} else if (add_num == 9) {
			if (quest_num == 4) {
				checkCase5Plus(quest_num, 1, rod_index);
			} else {
				temp = temp + (1 * rod);
				stepList.add(temp);
			}
		}
	}

	/*
	 * Determine which case 10 is used for the addition questions, and set
	 * additional steps accordingly to stepList. Note: The method provides only
	 * the first part of the check.
	 */
	private void abacusCase10PlusSolution(int add_num, int rod_index) {

		int rod = getRodValue(rod_index);

		if (add_num == 1) {
			temp = temp - (5 * rod);
			stepList.add(temp);
			temp = temp - (4 * rod);
			stepList.add(temp);

		} else if (add_num == 2) {
			temp = temp - (5 * rod);
			stepList.add(temp);
			temp = temp - (3 * rod);
			stepList.add(temp);

		} else if (add_num == 3) {
			temp = temp - (5 * rod);
			stepList.add(temp);
			temp = temp - (2 * rod);
			stepList.add(temp);

		} else if (add_num == 4) {
			temp = temp - (5 * rod);
			stepList.add(temp);
			temp = temp - (1 * rod);
			stepList.add(temp);

		} else if (add_num == 5) {
			temp = temp - (5 * rod);
			stepList.add(temp);

		} else {
			moreCheckCase10Plus(add_num, rod_index);
		}

		rollOverCheckCase10Plus(rod_index);

		if (rollOver == false) {
			temp = temp + (10 * rod);
			stepList.add(temp);
		}

		rollOver = false;

	}

	/*
	 * Check the abacus's steps for the special case where addition rollOver
	 * case10 occurs. This special case cause roll over effect and require every
	 * rods on the abacus to be adjusted. i.e. (999 + 1 =
	 * 994,990,940,900,400,0,1000)
	 */
	private void rollOverCheckCase10Plus(int rod_index) {

		int quest_num = getSingleDigit(temp, rod_index - 1);

		if (quest_num == 4) {
			rollOver = true;
			checkCase5Plus(quest_num, 1, rod_index - 1);
		} else if (quest_num == 9) {
			rollOver = true;
			rollOverCaseAdjust_Plus(rod_index - 1);
		}

	}

	/*
	 * Adjust the abacus's steps for the special roll over case in addition
	 * questions.
	 */
	private void rollOverCaseAdjust_Plus(int rod_index) {

		int rod = getRodValue(rod_index);
		int index = rod_index;
		int nextPosition_num;

		do {

			temp = temp - (5 * rod);
			stepList.add(temp);

			temp = temp - (4 * rod);
			stepList.add(temp);

			index = index - 1;

			nextPosition_num = getSingleDigit(temp, index);

			if (nextPosition_num == 4) {
				checkCase5Plus(nextPosition_num, 1, index);
				break;
			}

			rod = rod * 10;

		} while (nextPosition_num == 9);

		if (nextPosition_num != 4) {
			temp = temp + (1 * rod);
			stepList.add(temp);
		}
	}

	/* Continue second part of case10 check for addition questions. */
	private void moreCheckCase10Plus(int add_num, int rod_index) {

		int quest_num = getSingleDigit(temp, rod_index);
		int rod = getRodValue(rod_index);

		if (add_num == 6) {
			if (quest_num > 4 && quest_num < 9) {
				checkCase5Minus(quest_num, 4, rod_index);
			} else {
				temp = temp - (4 * rod);
				stepList.add(temp);
			}

		} else if (add_num == 7) {
			if (quest_num > 4 && quest_num < 8) {
				checkCase5Minus(quest_num, 3, rod_index);
			} else {
				temp = temp - (3 * rod);
				stepList.add(temp);
			}

		} else if (add_num == 8) {
			if (quest_num > 4 && quest_num < 7) {
				checkCase5Minus(quest_num, 2, rod_index);
			} else {
				temp = temp - (2 * rod);
				stepList.add(temp);
			}

		} else if (add_num == 9) {
			if (quest_num == 5) {
				checkCase5Minus(quest_num, 1, rod_index);
			} else {
				temp = temp - (1 * rod);
				stepList.add(temp);
			}
		}

		isCase5 = false;
	}

	/* Check for case 10 technique. */
	private void checkCase10(int a, int b, int rod_index) {

		int quest_num = getSingleDigit(a, rod_index);
		int add_num = getSingleDigit(b, rod_index);

		if (isOperatorAdd == true) {
			if (quest_num + add_num > 9) {
				isCase10 = true;
				abacusCase10PlusSolution(add_num, rod_index);
			}
		} else if (isOperatorAdd == false) {
			if (quest_num < add_num) {
				isCase10 = true;
				abacusCase10MinusSolution(add_num, rod_index);
			}
		}
	}

	/*
	 * Check for case 5 technique by first determine the type of question
	 * (subtraction/addition) and single out the values that are needed to
	 * perform the check.
	 */
	private void checkCase5(int a, int b, int rod_index) {

		int quest_num = getSingleDigit(a, rod_index);
		int add_num = getSingleDigit(b, rod_index);

		if (isOperatorAdd == true) {
			checkCase5Plus(quest_num, add_num, rod_index);
		} else {
			checkCase5Minus(quest_num, add_num, rod_index);
		}
	}

	/* Check case 5 in addition type questions. */
	private void checkCase5Plus(int quest_num, int add_num, int rod_index) {

		if (quest_num < 5 && add_num < 5) {
			if (quest_num + add_num >= 5) {
				isCase5 = true;
				abacusCase5PlusSolution(add_num, rod_index);
			}
		}
	}

	/* Check case 5 in subtraction type questions. */
	private void checkCase5Minus(int quest_num, int add_num, int rod_index) {

		if (quest_num >= 5 && quest_num < 9 && add_num < 5 && add_num > 0) {
			int remainingBeads = quest_num - 5;
			if (remainingBeads - add_num < 0) {
				isCase5 = true;
				abacusCase5MinusSolution(add_num, rod_index);
			}
		}
	}

	/* Get a unit value from the abacus's digit. */
	private int getSingleDigit(int digit, int rod_index) {

		String num = Integer.toString(digit);
		int rodValue = getRodValue(rod_index);
		String rodString = Integer.toString(rodValue);

		if (rodString.length() > num.length()) {
			return 0;
		} else {

			char ch = num.charAt(num.length() - rodString.length());
			String num1 = Character.toString(ch);

			int singleDigit = Integer.parseInt(num1);

			return singleDigit;
		}
	}

	/*
	 * The method determine which case5 is used for the addition questions, and
	 * set additional steps accordingly to stepList.
	 */
	private void abacusCase5PlusSolution(int add_num, int rod_index) {

		int rod = getRodValue(rod_index);

		// Add 5 first before minus excess value. This should simulate the
		// actual
		// movement of fingers when work on actual abacus.
		temp = temp + (5 * rod);
		stepList.add(temp);

		if (add_num == 1) {
			temp = temp - (4 * rod);
			stepList.add(temp);
		} else if (add_num == 2) {
			temp = temp - (3 * rod);
			stepList.add(temp);
		} else if (add_num == 3) {
			temp = temp - (2 * rod);
			stepList.add(temp);
		} else if (add_num == 4) {
			temp = temp - (1 * rod);
			stepList.add(temp);
		}

	}

	/*
	 * The method determine which case 5 is used for subtraction questions, and
	 * set additional steps accordingly.
	 */
	private void abacusCase5MinusSolution(int add_num, int rod_index) {

		int rod = getRodValue(rod_index);

		temp = temp - (5 * rod);
		stepList.add(temp);

		if (add_num == 1) {
			temp = temp + (4 * rod);
			stepList.add(temp);
		} else if (add_num == 2) {
			temp = temp + (3 * rod);
			stepList.add(temp);
		} else if (add_num == 3) {
			temp = temp + (2 * rod);
			stepList.add(temp);
		} else if (add_num == 4) {
			temp = temp + (1 * rod);
			stepList.add(temp);
		}

	}

	/* Calls to get the number of steps needed to complete the question. */
	public int getSolutionCount() {

		return stepList.size();
	}

	/*
	 * Calls to get the value representing the steps that student is suppose to
	 * get on the abacus.
	 */
	public int stepGetter(int index) {

		return stepList.get(index);
	}

	/* The method return the value corresponding to the rod position. */
	private int getRodValue(int r) {

		int rodPosition = r;
		int rod = 0;

		switch (rodPosition) {
		case 1:
			rod = 1000000;
			break;
		case 2:
			rod = 100000;
			break;
		case 3:
			rod = 10000;
			break;
		case 4:
			rod = 1000;
			break;
		case 5:
			rod = 100;
			break;
		case 6:
			rod = 10;
			break;
		case 7:
			rod = 1;
			break;
		}

		return rod;
	}

	/*
	 * This method single out the individual number and its value, then input it
	 * into abacusValue array at the position relative to its 10th value.
	 */
	private void abacusDivider(int n) {

		String numbers = Integer.toString(n);
		int j = abacusValue.length - numbers.length();

		for (int i = 0; i < numbers.length(); i++) {
			char ch = numbers.charAt(i);
			String str = Character.toString(ch);
			int digit = Integer.parseInt(str);
			int rodValue = getRodValue(j++);
			int divideValue = digit * rodValue;
			abacusValue[j - 1] = divideValue;

		}
	}

	/* The class set the type of question variable, true = (+), false = (-) */
	private void setQuestionType(int x) {

		if (x == 1) {
			isOperatorAdd = true;
		} else {
			isOperatorAdd = false;
		}
	}

	/* Set question level one higher every time the method is call. */
	public void setLevel() {

		questionLevel = questionLevel + 1;
		instruction();
		streak = 0;
		totalMistake = 0;
	}

	/* Select the question type base on the question level. */
	private void questionGenerator() {

		switch (questionLevel) {
		case 1:
			typeOneQuestion();
			break;
		case 2:
			typeTwoQuestion();
			break;
		case 3:
			typeThreeQuestion();
			break;
		case 4:
			typeFourQuestion();
			break;
		case 5:
			typeFiveQuestion();
			break;
		}
	}

	/*
	 * Determine which set of type 5 questions to use base on the
	 * operator(addition/subtraction).
	 */
	private void typeFiveQuestion() {

		if (isOperatorAdd == true) {
			typeFiveAddition();
		} else {
			typeFiveSubtraction();
		}
	}

	/*
	 * Create two numbers with 9999999 limit which when subtract would result
	 * more than 0.
	 */
	private void typeFiveSubtraction() {

		int num1 = 0;
		int num2 = 0;

		while (true) {

			num1 = rgen.nextInt(1, 9999999);
			num2 = rgen.nextInt(1, 9999999);

			if (num1 - num2 > 0)
				break;
		}

		setNumber = num1;
		operatorNumber = num2;

		abacusTranslator(num1, num2);
	}

	/* Create two numbers which when add would not result more than 9999999. */
	private void typeFiveAddition() {

		int num1 = 0;
		int num2 = 0;

		while (true) {

			num1 = rgen.nextInt(1, 9999999);
			num2 = rgen.nextInt(1, 9999999);

			if (num1 + num2 <= 9999999)
				break;
		}

		setNumber = num1;
		operatorNumber = num2;

		abacusTranslator(num1, num2);
	}

	/*
	 * Determine which set of type 4 questions to use base on the
	 * operator(addition/subtraction).
	 */
	private void typeFourQuestion() {

		if (isOperatorAdd == true) {
			typeFourAddition();
		} else {
			typeFourSubtraction();
		}
	}

	/*
	 * Create two numbers with 9999 limit which when subtract would result more
	 * than 0.
	 */
	private void typeFourSubtraction() {

		int num1 = 0;
		int num2 = 0;

		while (true) {

			num1 = rgen.nextInt(1, 9999);
			num2 = rgen.nextInt(1, 9999);

			if (num1 - num2 > 0)
				break;
		}

		setNumber = num1;
		operatorNumber = num2;

		abacusTranslator(num1, num2);
	}

	/* Create two numbers which when add would not result more than 9999. */
	private void typeFourAddition() {

		int num1 = 0;
		int num2 = 0;

		while (true) {

			num1 = rgen.nextInt(1, 9999);
			num2 = rgen.nextInt(1, 9999);

			if (num1 + num2 <= 9999)
				break;
		}

		setNumber = num1;
		operatorNumber = num2;

		abacusTranslator(num1, num2);

	}

	/*
	 * Determine which set of type 3 questions to use base on the
	 * operator(addition/subtraction).
	 */
	private void typeThreeQuestion() {

		if (isOperatorAdd == true) {
			typeThreeAddition();
		} else {
			typeThreeSubtraction();
		}
	}

	/* Create two numbers which when subtract would require case10 technique. */
	private void typeThreeSubtraction() {

		int num1 = 0;
		int num2 = 0;

		int baseValue = rgen.nextInt(2, 7);

		for (int i = baseValue; i < MAXIMUM_BASE_VALUE; i++) {
			int digit1 = rgen.nextInt(0, 8);
			int rod = getRodValue(i);
			int digit2 = typeThreeSubtractionSet(digit1);

			num1 = num1 + (digit1 * rod);
			num2 = num2 + (digit2 * rod);

		}

		num1 = num1 + 1000000; // ensure that num1 is always more than num2.

		setNumber = num1;
		operatorNumber = num2;

		abacusTranslator(num1, num2);

	}

	/* Return the counter value for type 3 subtraction. */
	private int typeThreeSubtractionSet(int set) {

		int respondSet = set;
		int respondDigit = 0;

		if (respondSet == 0) {
			respondDigit = rgen.nextInt(1, 9);
		} else if (respondSet == 1) {

			if (rgen.nextBoolean() == true) {
				respondDigit = rgen.nextInt(2, 5);
			} else {
				respondDigit = rgen.nextInt(7, 9);
			}

		} else if (respondSet == 2) {

			if (rgen.nextBoolean() == true) {
				respondDigit = rgen.nextInt(3, 5);
			} else {
				respondDigit = rgen.nextInt(8, 9);
			}

		} else if (respondSet == 3) {

			if (rgen.nextBoolean() == true) {
				respondDigit = rgen.nextInt(4, 5);
			} else {
				respondDigit = 9;
			}

		} else if (respondSet == 4) {
			respondDigit = 5;
		} else if (respondSet == 5) {
			respondDigit = rgen.nextInt(6, 9);
		} else if (respondSet == 6) {
			respondDigit = rgen.nextInt(7, 9);
		} else if (respondSet == 7) {
			respondDigit = rgen.nextInt(8, 9);
		} else if (respondSet == 8) {
			respondDigit = 9;
		}

		return respondDigit;
	}

	/* Create two numbers which when add would require case10 technique. */
	private void typeThreeAddition() {

		int num1 = 0;
		int num2 = 0;

		int baseValue = rgen.nextInt(2, 7);

		for (int i = baseValue; i < MAXIMUM_BASE_VALUE; i++) {
			int digit1 = rgen.nextInt(1, 9);
			int rod = getRodValue(i);
			int digit2 = typeThreeAdditionSet(digit1);

			num1 = num1 + (digit1 * rod);
			num2 = num2 + (digit2 * rod);

		}

		setNumber = num1;
		operatorNumber = num2;

		abacusTranslator(num1, num2);
	}

	/* Return the counter value for type 3 addition. */
	private int typeThreeAdditionSet(int set) {

		int respondSet = set;
		int respondDigit = 0;

		if (respondSet == 1) {
			respondDigit = 9;
		} else if (respondSet == 2) {
			respondDigit = rgen.nextInt(8, 9);
		} else if (respondSet == 3) {
			respondDigit = rgen.nextInt(7, 9);
		} else if (respondSet == 4) {
			respondDigit = rgen.nextInt(6, 9);
		} else if (respondSet == 5) {
			respondDigit = 5;
		} else if (respondSet == 6) {

			if (rgen.nextBoolean() == true) {
				respondDigit = rgen.nextInt(4, 5);
			} else {
				respondDigit = 9;
			}

		} else if (respondSet == 7) {

			if (rgen.nextBoolean() == true) {
				respondDigit = rgen.nextInt(3, 5);
			} else {
				respondDigit = rgen.nextInt(8, 9);
			}

		} else if (respondSet == 8) {

			if (rgen.nextBoolean() == true) {
				respondDigit = rgen.nextInt(2, 5);
			} else {
				respondDigit = rgen.nextInt(7, 9);
			}

		} else if (respondSet == 9) {
			respondDigit = rgen.nextInt(1, 9);
		}

		return respondDigit;
	}

	/*
	 * Determine which set of type 2 questions to use base on the
	 * operator(addition/subtraction).
	 */
	private void typeTwoQuestion() {

		if (isOperatorAdd == true) {
			typeTwoAddition();
		} else {
			typeTwoSubtraction();
		}
	}

	/* Create two numbers which when subtract would require case5 technique. */
	private void typeTwoSubtraction() {

		int num1 = 0;
		int num2 = 0;

		int baseValue = rgen.nextInt(1, 7);

		for (int i = baseValue; i < MAXIMUM_BASE_VALUE; i++) {
			int digit1 = rgen.nextInt(5, 8);
			int rod = getRodValue(i);
			int digit2 = typeTwoSubtractionSet(digit1);

			num1 = num1 + (digit1 * rod);
			num2 = num2 + (digit2 * rod);

		}

		setNumber = num1;
		operatorNumber = num2;

		abacusTranslator(num1, num2);
	}

	/* Return the counter value for type 2 subtraction. */
	private int typeTwoSubtractionSet(int set) {

		int respondSet = set;
		int respondDigit = 0;

		if (respondSet == 5) {
			respondDigit = rgen.nextInt(1, 4);
		} else if (respondSet == 6) {
			respondDigit = rgen.nextInt(2, 4);
		} else if (respondSet == 7) {
			respondDigit = rgen.nextInt(3, 4);
		} else if (respondSet == 8) {
			respondDigit = 4;
		}

		return respondDigit;
	}

	/* Create two numbers which when add would require case5 technique. */
	private void typeTwoAddition() {

		int num1 = 0;
		int num2 = 0;

		int baseValue = rgen.nextInt(1, 7);

		for (int i = baseValue; i < MAXIMUM_BASE_VALUE; i++) {
			int digit1 = rgen.nextInt(1, 4);
			int rod = getRodValue(i);
			int digit2 = typeTwoAdditionSet(digit1);

			num1 = num1 + (digit1 * rod);
			num2 = num2 + (digit2 * rod);

		}

		setNumber = num1;
		operatorNumber = num2;

		abacusTranslator(num1, num2);
	}

	/* Return the counter value for type 2 addition. */
	private int typeTwoAdditionSet(int set) {

		int respondSet = set;
		int respondDigit = 0;

		if (respondSet == 1) {
			respondDigit = 4;
		} else if (respondSet == 2) {
			respondDigit = rgen.nextInt(3, 4);
		} else if (respondSet == 3) {
			respondDigit = rgen.nextInt(2, 4);
		} else if (respondSet == 4) {
			respondDigit = rgen.nextInt(1, 4);
		}

		return respondDigit;
	}

	/*
	 * Determine which set of type 1 questions to use base on the
	 * operator(addition/subtraction).
	 */
	private void typeOneQuestion() {

		if (isOperatorAdd == true) {
			typeOneAddition();
		} else {
			typeOneSubtraction();
		}
	}

	/* Create two numbers which when subtract require no special cases. */
	private void typeOneSubtraction() {

		int num1 = 0;
		int num2 = 0;

		int baseValue = rgen.nextInt(1, 7);

		for (int i = baseValue; i < MAXIMUM_BASE_VALUE; i++) {
			int digit1 = rgen.nextInt(1, 9);
			int rod = getRodValue(i);
			int digit2 = typeOneSubtractionSet(digit1);

			num1 = num1 + (digit1 * rod);
			num2 = num2 + (digit2 * rod);

		}

		setNumber = num1;
		operatorNumber = num2;

		abacusTranslator(num1, num2);
	}

	/* Return the counter value for type 1 subtraction. */
	private int typeOneSubtractionSet(int set) {

		int respondSet = set;
		int respondDigit = 0;

		if (respondSet == 1) {
			respondDigit = rgen.nextInt(0, 1);
		} else if (respondSet == 2) {
			respondDigit = rgen.nextInt(0, 2);
		} else if (respondSet == 3) {
			respondDigit = rgen.nextInt(0, 3);
		} else if (respondSet == 4) {
			respondDigit = rgen.nextInt(0, 4);
		} else if (respondSet == 5) {

			if (rgen.nextBoolean() == true) {
				respondDigit = 0;
			} else {
				respondDigit = 5;
			}

		} else if (respondSet == 6) {

			if (rgen.nextBoolean() == true) {
				respondDigit = 1;
			} else {
				respondDigit = rgen.nextInt(5, 6);
			}

		} else if (respondSet == 7) {

			if (rgen.nextBoolean() == true) {
				respondDigit = rgen.nextInt(1, 2);
			} else {
				respondDigit = rgen.nextInt(5, 7);
			}

		} else if (respondSet == 8) {

			if (rgen.nextBoolean() == true) {
				respondDigit = rgen.nextInt(1, 3);
			} else {
				respondDigit = rgen.nextInt(5, 8);
			}

		} else if (respondSet == 9) {
			respondDigit = rgen.nextInt(1, 9);
		}

		return respondDigit;
	}

	/* Create two numbers which when add require no special cases. */
	private void typeOneAddition() {

		int num1 = 0;
		int num2 = 0;

		int baseValue = rgen.nextInt(1, 7);

		for (int i = baseValue; i < MAXIMUM_BASE_VALUE; i++) {
			int digit1 = rgen.nextInt(0, 8);
			int rod = getRodValue(i);
			int digit2 = typeOneAdditionSet(digit1);

			num1 = num1 + (digit1 * rod);
			num2 = num2 + (digit2 * rod);
		}

		setNumber = num1;
		operatorNumber = num2;

		abacusTranslator(num1, num2);
	}

	/* Return the counter value for type 1 addition. */
	private int typeOneAdditionSet(int set) {

		int respondSet = set;
		int respondDigit = 0;

		if (respondSet == 0) {
			respondDigit = rgen.nextInt(0, 9);
		} else if (respondSet == 1) {

			if (rgen.nextBoolean() == true) {
				respondDigit = rgen.nextInt(0, 3);
			} else {
				respondDigit = rgen.nextInt(5, 8);
			}

		} else if (respondSet == 2) {

			if (rgen.nextBoolean() == true) {
				respondDigit = rgen.nextInt(0, 2);
			} else {
				respondDigit = rgen.nextInt(5, 7);
			}

		} else if (respondSet == 3) {

			if (rgen.nextBoolean() == true) {
				respondDigit = 1;
			} else {
				respondDigit = rgen.nextInt(5, 6);
			}

		} else if (respondSet == 4) {
			respondDigit = 5;
		} else if (respondSet == 5) {
			respondDigit = rgen.nextInt(0, 4);
		} else if (respondSet == 6) {
			respondDigit = rgen.nextInt(0, 3);
		} else if (respondSet == 7) {
			respondDigit = rgen.nextInt(0, 2);
		} else if (respondSet == 8) {
			respondDigit = rgen.nextInt(0, 1);
		}

		return respondDigit;

	}

	/* Get the set Number for virtual abacus's use. */
	public int getsetNumber() {

		return setNumber;
	}

	/* Get the operator number for virtual abacus's use. */
	public int getOperatorNumber() {

		return operatorNumber;
	}

	/* Will there be a need to reset level? */
	public void resetLevel() {

		questionLevel = 1;
	}

	/*
	 * Calculate the probability of the student performing the next question
	 * without any mistakes. The calculation is based on Logistic Regression
	 * Model (z = W0 + sum(Wi*Xi) where i = 1 to (n)) and Logistic function
	 * (h(z) = 1/1+e^(-z)). Three variables; the number of question done, the
	 * number of mistake made and the experience point are used in this case.
	 */
	public double evaluator(int questionAttempted, int questionMistake) {

		double log_num = Math.log(questionAttempted);
		double mistake_weight = mistakeAverage(questionAttempted,
				questionMistake);
		double experience = movementStreak(questionMistake);
		double respondVariable = log_num - mistake_weight + experience;
		setProbability(respondVariable);

		return mistake_free_probability;
	}

	/*
	 * Calculate the weight of mistakes by finding the percentage of mistaken
	 * moves made from the average moves needed to complete a question. Notice
	 * that the weight gets accumulated and so does the total average moves.
	 * This way the mistakes become less significant over time as the student go
	 * through more questions.
	 */
	private double mistakeAverage(int question, int mistake) {

		totalMistake = totalMistake + mistake;
		double weight = totalMistake / (question * AVERAGE_MOVES);

		return weight;
	}

	/*
	 * Calculate the experience points by rewarding streak points every time a
	 * question is done without any mistakes.
	 */
	private double movementStreak(int questionMistake) {

		if (questionMistake == 0) {
			streak++;
		}

		double experience = streak * STREAK_POINT;

		return experience;
	}

	/*
	 * Apply Logistic function to convert the respond variable into a percentage
	 * value.
	 */
	private void setProbability(double z) {

		double n = Math.exp(-z);
		mistake_free_probability = 1 / (1 + n);
	}

	/*
	 * Select the method to set the instruction messages base on the question
	 * level.
	 */
	private void instruction() {

		switch (questionLevel) {
		case 1:
			setInstructionOne();
			break;
		case 2:
			setInstructionTwo();
			break;
		case 3:
			setInstructionThree();
			break;
		case 4:
			setInstructionFour();
			break;
		case 5:
			setInstructionFive();
			break;
		}
	}

	/* Set instruction messages for type1 questions. */
	private void setInstructionOne() {

		explanation_line_1 = "The natural movement of the Soroban abacus is to work on a single rod from left to right. i.e. from highest number to the lowest.";
		explanation_line_2 = "Each rod represent base 10 value with the higher bead representing 5 units and lower beads 1 unit.";
		explanation_line_3 = "One important rule is to always move the higher bead first if needed. i.e. 2 + 6 = 2 + 5 + 1.";
	}

	/* Set instruction messages for type2 questions. */
	private void setInstructionTwo() {

		explanation_line_1 = "Case5 occurs where there is a need to carry (or borrow) the number from the higher beads to perform the calculation.";
		explanation_line_2 = "Case5 complementary numbers are: (4 & 1), (3 & 2). The technique apply by adding/subtracting 5 first, depending on";
		explanation_line_3 = "the operator, then apply the comp-value in reverse operator. Example: 3 + 4 => 3 + 5 - 1 = 7: 6 - 3 => 6 - 5 + 2 = 3.";
	}

	/* Set instruction messages for type3 questions. */
	private void setInstructionThree() {

		explanation_line_1 = "Case10 occurs where there is a need to carry (or borrow) the number from another rod to perform the calculation.";
		explanation_line_2 = "Case10 complementay numbers are: (1 & 9), (2 & 8), (3 & 7), (4 & 6), (5 & 5). The technique apply by using the reverse comp-value";
		explanation_line_3 = "first, before adding/subtracting the 10. Example: 4 + 8 => 4 - 2 + 10: 11 - 7 => 11 + 3 - 10 = 4.";
	}

	/* Set instruction messages for type4 questions. */
	private void setInstructionFour() {

		explanation_line_1 = "This level is where you have to apply all the techniques to finish the exercise. The questions are limited to using just 4 rods";
		explanation_line_2 = "(i.e. the range is between the thousandth or less). However, be on a lookout for a rollover effect!";
		explanation_line_3 = "This occurs when case10 can cause case5&10 in another rod. e.g. 99 + 1 => 99 - 5 - 4 - 50 - 40 + 100 = 100.";
	}

	/* Set instruction messages for type5 questions. */
	private void setInstructionFive() {

		explanation_line_1 = "This level is where all rods are in use so the question can be anywhere between units to millions";
		explanation_line_2 = "Remember the order of operations and always think about the technique.";
		explanation_line_3 = "Higher bead always move first and complentary value are applied in reverse. Good Luck!";
	}

	/* Get the first line instruction. */
	public String getInstructionLine1() {
		return explanation_line_1;
	}

	/* Get the second line instruction. */
	public String getInstructionLine2() {
		return explanation_line_2;
	}

	/* Get the third line instruction. */
	public String getInstructionLine3() {
		return explanation_line_3;
	}

	/* Get the level of the current question. */
	public int getQuestionLevel() {
		return questionLevel;
	}

	/* Get the operator boolean. */
	public boolean getOperator() {
		return isOperatorAdd;
	}

	boolean rollOver = false;
	boolean isOperatorAdd;
	boolean isCase5 = false;
	boolean isCase10 = false;
	private RandomGenerator rgen = RandomGenerator.getInstance();
	int[] abacusValue = new int[8];
	private int temp = 0;
	private int questionLevel = 1;
	private static final int MAXIMUM_BASE_VALUE = 8;
	private int setNumber;
	private int operatorNumber;
	private double mistake_free_probability;
	private static final int AVERAGE_MOVES = 12;
	private int totalMistake = 0;
	private int streak = 0;
	private static final double STREAK_POINT = 0.1;
	private String explanation_line_1 = "";
	private String explanation_line_2 = "";
	private String explanation_line_3 = "";

}
