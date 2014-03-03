package abacus.tutorial.applet;

import acm.graphics.*;
import acm.program.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

import javax.swing.JButton;

public class TutorialApplet extends GraphicsProgram {

	/* This method set the size of the application. */
	public void init() {

		setSize(APPLICATION_WIDTH, APPLICATION_HEIGHT);
		setAbacus();

		startButton = new JButton("Start Lesson!");
		add(startButton, SOUTH);

		endButton = new JButton("End Lesson!");
		add(endButton, SOUTH);

		addActionListeners();
		addMouseListeners();

	}

	/* Run method calls the setEnvironment to set up the program. */
	public void run() {

		setEnvironment();
		countingAbacus();
		// testingExercise();
	}

	/* Set the abacus's count variables. */
	private void countingAbacus() {

		highCount = countHigh();
		lowCount = countLow();
	}

	/* Testing the abacusSetter and displayNumber method. */
	private void testingExercise() {

		abacusSetter(4444);
		displayNumber();
		pause(DELAY);
		abacusReset();
		displayNumber();
		pause(DELAY);
	}

	/*
	 * Initiate the lesson by creating the lesson object and setting it as a
	 * instance object for this class. The action to start or to end the
	 * exercise depends on the clicked button.
	 */
	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand().equals("Start Lesson!")) {

			if (runExercise == false) {
				BackOperation lesson = new BackOperation();
				this.lesson = lesson;
				runExercise = true;
				abacusReset();
				message_prompt.setLabel("");
				beginLesson();
			}

		}

		if (e.getActionCommand().equals("End Lesson!")) {

			if (runExercise == true) {
				runExercise = false;
				message_prompt.setLabel("Exercise ended. Thank you!");
				addMessagePrompt();
				removeInstruction();
				lesson = null;
				abacusReset();
				questionCount = 0;
				question.setLabel("");
			}
		}
	}

	/*
	 * Check if the last level is already done, if not then set the lesson
	 * accordingly.
	 */
	private void beginLesson() {

		if (lesson.getQuestionLevel() < 6) {
			mistake = 0;
			exerciseIndex = 0;
			abacusReset();
			lesson.reset();
			setLesson();
		} else {
			message_prompt
					.setLabel("You have finished the tutorial! Press End button to reset.");
			addMessagePrompt();
			removeInstruction();
		}
	}

	/*
	 * Set the questions by calling the object setQuestion method and pass on
	 * the instances to setExercise method to set the abacus accordingly.
	 */
	private void setLesson() {

		questionCount++;
		lesson.setQuestion();
		setInstruction();
		abacusSetter(lesson.getsetNumber());
		setExercise(lesson.getOperator(), lesson.getsetNumber(),
				lesson.getOperatorNumber());
		lastCount = totalCount;
	}

	/* Check whether the move made by the student is the right one. */
	private void checkMovement() {

		if (totalCount == lesson.stepGetter(exerciseIndex)) {
			isRightMove = true;
		} else {
			isRightMove = false;
		}

		setMoveVariable();
	}

	/*
	 * Set the variables for next question or reset beads position if the move
	 * was wrong.
	 */
	private void setMoveVariable() {

		if (isRightMove == true) {
			exerciseIndex++;
			lastCount = totalCount;
			message_prompt.setLabel("");
		} else {
			mistake++;
			message_prompt.setLabel("Wrong! Please try again.");
			addMessagePrompt();
			abacusReset();
			abacusSetter(lastCount);
		}

		checkExerciseCount();
	}

	/* Check if the question has ended. */
	private void checkExerciseCount() {

		if (exerciseIndex == lesson.getSolutionCount()) {
			checkPerformance();
		}
	}

	/*
	 * Check the level of proficiency and set higher level when enough
	 * proficiency is reached.
	 */
	private void checkPerformance() {

		double performance = lesson.evaluator(questionCount, mistake);

		if (performance >= PROFICIENCY) {
			lesson.setLevel();
			questionCount = 0;
			beginLesson();
		} else {
			beginLesson();
		}
	}

	/* Set the question display and show the instruction message. */
	private void setExercise(boolean operator, int setNumber, int operatorNumber) {

		String sign = "-";
		if (operator)
			sign = "+";

		String num1 = Integer.toString(setNumber);
		String num2 = Integer.toString(operatorNumber);

		question.setLabel("Level-" + lesson.getQuestionLevel() + " Question "
				+ questionCount + ": " + num1 + " " + sign + " " + num2);
	}

	/* Set the instruction messages accordingly. */
	private void setInstruction() {

		message_line1.setLabel(lesson.getInstructionLine1());
		add(message_line1, (APPLICATION_WIDTH - message_line1.getWidth()) / 2,
				BOTTOM_DISPLAY_Y + message_line1.getHeight() + 3);

		message_line2.setLabel(lesson.getInstructionLine2());
		add(message_line2, (APPLICATION_WIDTH - message_line2.getWidth()) / 2,
				BOTTOM_DISPLAY_Y + (message_line2.getHeight() * 2) + 3);

		message_line3.setLabel(lesson.getInstructionLine3());
		add(message_line3, (APPLICATION_WIDTH - message_line3.getWidth()) / 2,
				BOTTOM_DISPLAY_Y + (message_line3.getHeight() * 3) + 3);
	}

	/* Set the instruction messages to blank. */
	private void removeInstruction() {

		message_line1.setLabel("");
		message_line2.setLabel("");
		message_line3.setLabel("");
	}

	/* Register the location where the mouse is clicked. */
	public void mouseClicked(MouseEvent e) {

		GPoint last = new GPoint(e.getPoint());
		moveBeads(last);
		displayNumber();

		if (runExercise) {
			checkMovement();
		}

	}

	/* Set the count variables and get the total value represent on the abacus. */
	private void countBeads() {

		highCount = countHigh();
		lowCount = countLow();
		totalCount = highCount + lowCount;
	}

	/* Set the abacus's beads to the position that reflect the input numbers. */
	private void abacusSetter(int n) {

		abacusDivider(n);

		for (int i = 1; i < 8; i++) {
			int num = valueDivider[i];

			if (num == 5) {
				abacusMoveFives(i);
			} else if (num == 1) {
				abacusMoveOnes(i, num);
			} else if (num == 2) {
				abacusMoveOnes(i, num);
			} else if (num == 3) {
				abacusMoveOnes(i, num);
			} else if (num == 4) {
				abacusMoveOnes(i, num);
			} else if (num > 5) {

				int remain = num % 5;
				abacusMoveOnes(i, remain);
				abacusMoveFives(i);
			}
		}

		displayNumber();
	}

	/* Reset all beads to its original position. i.e. set all rods to 0. */
	private void abacusReset() {

		abacusDivider(totalCount);

		for (int i = 1; i < 8; i++) {
			int num = valueDivider[i];

			if (num == 5) {
				abacusResetFives(i);
			} else if (num == 1) {
				abacusResetOnes(i, num);
			} else if (num == 2) {
				abacusResetOnes(i, num);
			} else if (num == 3) {
				abacusResetOnes(i, num);
			} else if (num == 4) {
				abacusResetOnes(i, num);
			} else if (num > 5) {

				int remain = num % 5;
				abacusResetOnes(i, remain);
				abacusResetFives(i);
			}
		}

		displayNumber();

	}

	/* Reset the beads in the lower half of the abacus. */
	private void abacusResetOnes(int rod, int value) {

		GPoint resetOnes = new GPoint(
				(((ABACUS_WIDTH - (BEAM_WIDTH * 2)) / 8 * rod) - (PILLAR_WIDTH / 2))
						+ ABACUS_OFFSET_X + 6,
				((ABACUS_HEIGHT / 4) + BEAM_WIDTH * 2) + ABACUS_OFFSET_Y + 20);

		moveBeads(resetOnes);
	}

	/* Reset the beads in the upper half of the abacus. */
	private void abacusResetFives(int rod) {

		GPoint resetFive = new GPoint(
				(((ABACUS_WIDTH - (BEAM_WIDTH * 2)) / 8 * rod) - (PILLAR_WIDTH / 2))
						+ ABACUS_OFFSET_X + 6,
				((ABACUS_HEIGHT / 4) + BEAM_WIDTH * 2) + ABACUS_OFFSET_Y - 5);

		moveBeads(resetFive);
	}

	/* Move the beads in the lower half of the abacus. i.e. the 1, 10, 100 etc. */
	private void abacusMoveOnes(int rod, int value) {

		GPoint moveOnes = new GPoint(
				(((ABACUS_WIDTH - (BEAM_WIDTH * 2)) / 8 * rod) - (PILLAR_WIDTH / 2))
						+ ABACUS_OFFSET_X + 6,
				((((ABACUS_HEIGHT - BEAM_WIDTH) + ABACUS_OFFSET_Y) - BEADS_OFFSET) - (BEADS_RADIUS / 2))
						- ((4 - value) * BEADS_RADIUS));

		moveBeads(moveOnes);

	}

	/* Move the beads in the upper half of the abacus. i.e. the 5, 50, 500 etc. */
	private void abacusMoveFives(int rod) {

		GPoint moveFive = new GPoint(
				(((ABACUS_WIDTH - (BEAM_WIDTH * 2)) / 8 * rod) - (PILLAR_WIDTH / 2))
						+ ABACUS_OFFSET_X + 6, ABACUS_OFFSET_Y + BEAM_WIDTH + 5);

		moveBeads(moveFive);
	}

	/*
	 * Separate the number into individual value i.e. 543 = 500, 40 and 3. These
	 * are then store within the valueDivider array.
	 */
	private void abacusDivider(int n) {

		resetDivider();
		String numbers = Integer.toString(n);
		int j = valueDivider.length - numbers.length();

		for (int i = 0; i < numbers.length(); i++) {
			char ch = numbers.charAt(i);
			String str = Character.toString(ch);
			int digit = Integer.parseInt(str);
			valueDivider[j++] = digit;
		}
	}

	/* Reset the valueDivider array by assigning 0 to all spaces. */
	private void resetDivider() {

		for (int i = 0; i < 8; i++) {
			valueDivider[i] = 0;
		}
	}

	/* Phrase the number integer into string variable and show it on the canvas. */
	private void displayNumber() {

		countBeads();
		String total = Integer.toString(totalCount);
		number.setLabel(total);
	}

	/*
	 * Move the beads by using GPoint variable to get the bead object and pass
	 * it on to the animation method.
	 */
	private void moveBeads(GPoint p) {

		bead = getElementAt(p);

		if (bead != null) {

			for (int i = 1; i < 8; i++) {
				if (bead.equals(highBeads[i])) {
					animationHighBeads(highBeads[i]);
					break;
				}
			}

			for (int i = 1; i < 32; i++) {
				if (bead.equals(lowBeads[i])) {
					checkBeadDirection(lowBeads[i]);
					animationLowBeads(lowBeads[i]);
					break;
				}
			}
		}
	}

	/* This method checks whether the beads should be move up or down. */
	private void checkBeadDirection(GOval b) {

		double x = b.getX() + (BEADS_RADIUS / 2);
		double y = (((ABACUS_HEIGHT / 4) + BEAM_WIDTH * 2) + ABACUS_OFFSET_Y);
		double empty = 0;

		for (int i = 1; i < 5; i++) {
			checkBead = getElementAt(x, y + (BEADS_RADIUS * i));
			if (checkBead == null) {
				empty = y + (BEADS_RADIUS * i);
				break;
			}
		}

		if (empty == 0 || empty > b.getY()) {
			isMoveUp = false;
		} else {
			isMoveUp = true;
		}

	}

	/* Move the beads at the lower half of the abacus. */
	private void animationLowBeads(final GOval b) {

		double x = b.getX() + (BEADS_RADIUS / 2);
		double y = b.getY();
		int arrayUsed = 0;

		moveBeads[0] = b;
		arrayUsed++;

		if (isMoveUp == true) {
			for (int i = 1; i < 4; i++) {
				checkBead = getElementAt(x, y - ((BEADS_RADIUS - 5) * i));
				if (checkBead == null)
					break;
				moveBeads[i] = checkBead;
				arrayUsed++;
			}
		}

		if (isMoveUp == false) {
			for (int i = 1; i < 4; i++) {
				checkBead = getElementAt(x, y + ((BEADS_RADIUS * i) + 10));
				if (checkBead == null)
					break;
				moveBeads[i] = checkBead;
				arrayUsed++;
			}
		}

		final int arrayCount = arrayUsed;

		Thread l = new Thread() {
			public void run() {

				if (isMoveUp == true) {

					for (int i = 0; i < 5; i++) {
						pause(DELAY);
						for (int j = arrayCount; j > 0; j--) {
							moveBeads[j - 1].move(0, -10);
						}
					}
					Arrays.fill(moveBeads, null);
				}

				if (isMoveUp == false) {

					for (int i = 0; i < 5; i++) {
						pause(DELAY);
						for (int j = arrayCount; j > 0; j--) {
							moveBeads[j - 1].move(0, +10);
						}
					}
					Arrays.fill(moveBeads, null);
				}
			}

		};
		l.start();

		try {
			l.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	/* Move the beads at the upper half of the abacus. */
	private void animationHighBeads(final GOval b) {

		Thread h = new Thread() {
			public void run() {

				checkBead = getElementAt(b.getX(), b.getY() - BEAM_WIDTH);

				if (checkBead != null) {

					for (int i = 0; i < 5; i++) {
						b.move(0, 10);
						pause(DELAY);
					}

				} else {

					for (int i = 0; i < 5; i++) {
						b.move(0, -10);
						pause(DELAY);
					}
				}
			}
		};
		h.start();

		try {
			h.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	/* The method calls each part of the environment to be set up. */
	private void setEnvironment() {

		setMessageBox();
		setNumberBox();
		setQuestionBox();
	}

	/* Set the question box. */
	private void setQuestionBox() {

		GLabel questionLabel = new GLabel("CURRENT QUESTION: ");
		questionLabel.setFont("Times New Roman-16");
		add(questionLabel, APPLICATION_WIDTH - (questionLabel.getWidth() * 2),
				TOP_DISPLAY_Y);

		question = new GLabel("");
		question.setFont("Times New Roman-16");
		question.setColor(Color.BLACK);
		add(question, APPLICATION_WIDTH - (questionLabel.getWidth() * 2),
				TOP_DISPLAY_Y + question.getHeight() + 3);
	}

	/* Set the number display box. */
	private void setNumberBox() {

		GLabel numberLabel = new GLabel("CURRENT VALUE:");
		numberLabel.setFont("Times New Roman-16");
		numberLabel.setColor(Color.BLACK);
		add(numberLabel, numberLabel.getWidth(), TOP_DISPLAY_Y);

		number = new GLabel("0");
		number.setFont("Times New Roman-16");
		number.setColor(Color.BLACK);
		add(number, numberLabel.getWidth(), TOP_DISPLAY_Y + number.getHeight()
				+ 3);

	}

	/*
	 * Count the beads at the upper half of the abacus on each rod and return
	 * the total value.
	 */
	private int countHigh() {

		int count = 0;

		double y = ((((ABACUS_HEIGHT / 4) + BEAM_WIDTH * 2) + ABACUS_OFFSET_Y) - 10);

		for (int i = 1; i < 8; i++) {

			GObject checkHighCount = getElementAt(
					((((ABACUS_WIDTH - (BEAM_WIDTH * 2)) / 8) * i) - (PILLAR_WIDTH / 2))
							+ ABACUS_OFFSET_X + 6, y);

			if (checkHighCount != null) {
				int value = getHigherRodPosition(i);

				count = count + value;

			}

		}
		return count;
	}

	/* Get the rod value in relation to its upper position within the abacus. */
	private int getHigherRodPosition(int i) {

		int value = 0;
		int rodPosition = i;
		switch (rodPosition) {
		case 1:
			value = 5000000;
			break;
		case 2:
			value = 500000;
			break;
		case 3:
			value = 50000;
			break;
		case 4:
			value = 5000;
			break;
		case 5:
			value = 500;
			break;
		case 6:
			value = 50;
			break;
		case 7:
			value = 5;
			break;
		}

		return value;
	}

	/* Count the beads at the lower half of the abacus. */
	private int countLow() {

		int count = 0;

		for (int i = 1; i < 8; i++) {

			int beads = 0;
			int rod = 0;
			boolean isRodCount = false;

			for (int j = 1; j < 5; j++) {

				GObject checkLowCount = getElementAt(
						((((ABACUS_WIDTH - (BEAM_WIDTH * 2)) / 8) * i) - (PILLAR_WIDTH / 2))
								+ ABACUS_OFFSET_X + 6,
						((((ABACUS_HEIGHT / 4) + BEAM_WIDTH * 2) + ABACUS_OFFSET_Y) + (BEADS_RADIUS * j)));

				if (checkLowCount == null)
					break;
				if (checkLowCount != null) {
					beads = j;
					isRodCount = true;
				}
			}

			if (isRodCount == true) {
				rod = getLowerRodPosition(i);
			}

			count = count + (rod * beads);
		}
		return count;
	}

	/* Get the rod value in relation to its lower position within the abacus. */
	private int getLowerRodPosition(int i) {

		int rod = 0;
		int rodPosition = i;

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

	/* Set the message label on the canvas. */
	private void setMessageBox() {

		GLabel messageLabel = new GLabel("INSTRUCTIONS.");
		messageLabel.setFont("Times New Roman-12");
		add(messageLabel, ABACUS_OFFSET_X, BOTTOM_DISPLAY_Y);

		message_prompt = new GLabel("");
		message_prompt.setFont("Times New Roman-16");
		message_prompt.setColor(Color.blue);
		addMessagePrompt();

		message_line1 = new GLabel("");
		message_line1.setFont("Times New Roman-16");
		message_line1.setColor(Color.BLACK);

		message_line2 = new GLabel("");
		message_line2.setFont("Times New Roman-16");
		message_line2.setColor(Color.BLACK);

		message_line3 = new GLabel("");
		message_line3.setFont("Times New Roman-16");
		message_line3.setColor(Color.BLACK);

	}

	/*
	 * Add the prompt message to the location where the length of the message is
	 * taking into account.
	 */
	private void addMessagePrompt() {

		add(message_prompt,
				(ABACUS_OFFSET_X + ABACUS_WIDTH - message_prompt.getWidth()),
				BOTTOM_DISPLAY_Y);
	}

	/* Set each component of the virtual abacus. */
	private void setAbacus() {

		setLeftBeam();
		setRightBeam();
		setTopBar();
		setLowBar();
		setMiddleBar();
		setPillars();
		setBeads();
	}

	/* Create the bead objects and set them within the abacus's frame. */
	private void setBeads() {

		int k = 1; // Used to add in the position for the lowBeads arrays.

		for (int i = 1; i < 8; i++) {

			highBeads[i] = new GOval(BEADS_RADIUS_Y, BEADS_RADIUS);
			highBeads[i].setFilled(true);
			highBeads[i].setColor(Color.green);

			add(highBeads[i], (((((ABACUS_WIDTH - (BEAM_WIDTH * 2)) / 8) * i)
					- (PILLAR_WIDTH / 2) - (BEADS_RADIUS_Y / 2)))
					+ ABACUS_OFFSET_X, BEAM_WIDTH + BEAM_OFFSET
					+ ABACUS_OFFSET_Y);
		}

		for (int i = 1; i < 5; i++) {
			for (int j = 1; j < 8; j++) {

				lowBeads[k] = new GOval(BEADS_RADIUS_Y, BEADS_RADIUS);
				lowBeads[k].setFilled(true);
				lowBeads[k].setColor(Color.green);

				add(lowBeads[k],
						(((((ABACUS_WIDTH - (BEAM_WIDTH * 2)) / 8) * j)
								- (PILLAR_WIDTH / 2) - (BEADS_RADIUS_Y / 2)))
								+ ABACUS_OFFSET_X,
						((ABACUS_HEIGHT - BEAM_WIDTH) - (BEADS_RADIUS * i)
								- BEAM_OFFSET - (BEADS_OFFSET * i))
								+ ABACUS_OFFSET_Y);
				k++;
			}
		}

	}

	/* Set the lower frame of the abacus. */
	private void setLowBar() {

		GRect lowBar = new GRect(ABACUS_WIDTH, BEAM_WIDTH);
		lowBar.setFilled(true);
		lowBar.setColor(Color.red);
		add(lowBar, ABACUS_OFFSET_X, (ABACUS_HEIGHT - BEAM_WIDTH)
				+ ABACUS_OFFSET_Y);
	}

	/* Set the middle frame of the abacus. */
	private void setMiddleBar() {

		GRect midBar = new GRect(ABACUS_WIDTH, BEAM_MIDDLE_WIDTH);
		midBar.setFilled(true);
		midBar.setColor(Color.red);
		add(midBar, ABACUS_OFFSET_X, ((ABACUS_HEIGHT / 4) + BEAM_WIDTH * 2)
				+ ABACUS_OFFSET_Y);
	}

	/* Set the rods of the abacus. */
	private void setPillars() {

		for (int i = 0; i < 8; i++) {
			GRect pillar = new GRect(PILLAR_WIDTH, ABACUS_HEIGHT);
			pillar.setFilled(true);
			pillar.setColor(Color.red);
			add(pillar,
					((((ABACUS_WIDTH - (BEAM_WIDTH * 2)) / 8) * i) - (PILLAR_WIDTH / 2))
							+ ABACUS_OFFSET_X, ABACUS_OFFSET_Y);
		}
	}

	/* Set the upper frame of the abacus. */
	private void setTopBar() {

		GRect topBar = new GRect(ABACUS_WIDTH, BEAM_WIDTH);
		topBar.setFilled(true);
		topBar.setColor(Color.red);
		add(topBar, ABACUS_OFFSET_X, ABACUS_OFFSET_Y);
	}

	/* Set the right frame of the abacus. */
	private void setRightBeam() {

		GRect rightBeam = new GRect(BEAM_WIDTH, ABACUS_HEIGHT);
		rightBeam.setFilled(true);
		rightBeam.setColor(Color.red);
		add(rightBeam, (ABACUS_WIDTH - BEAM_WIDTH) + ABACUS_OFFSET_X,
				ABACUS_OFFSET_Y);
	}

	/* Set the left frame of the abacus. */
	private void setLeftBeam() {

		GRect leftBeam = new GRect(BEAM_WIDTH, ABACUS_HEIGHT);
		leftBeam.setFilled(true);
		leftBeam.setColor(Color.red);
		add(leftBeam, ABACUS_OFFSET_X, ABACUS_OFFSET_Y);
	}

	// The dimension of Virtual Abacus in pixels.
	public static final int ABACUS_WIDTH = 550;
	public static final int ABACUS_HEIGHT = 320;
	public static final int BEADS_RADIUS = 35;
	public static final int BEAM_MIDDLE_WIDTH = 5;
	public static final int BEAM_WIDTH = 10;
	public static final int APPLICATION_WIDTH = 800;
	public static final int APPLICATION_HEIGHT = 600;
	public static final int PILLAR_WIDTH = 2;
	public static final int BEAM_OFFSET = 2;
	public static final int BEADS_RADIUS_Y = 46;
	public static final int BEADS_OFFSET = 2;
	public static final int ABACUS_OFFSET_X = 125;
	public static final int ABACUS_OFFSET_Y = 140;

	// Constant variables for the dimension of each boxes.
	public static final int TOP_DISPLAY_Y = 40;
	public static final int BOTTOM_DISPLAY_Y = 480;
	public static final int MESSAGE_BOX_WIDTH = 400;
	public static final int MESSAGE_BOX_HEIGHT = 130;
	public static final int START_BUTTON_WIDTH = 100;
	public static final int START_BUTTON_HEIGHT = 50;

	public static final int DELAY = 30;
	private static final double PROFICIENCY = 0.96;

	private JButton startButton;
	private JButton endButton;

	GOval[] highBeads = new GOval[8];
	GOval[] lowBeads = new GOval[32];
	GObject[] moveBeads = new GObject[5];
	int[] valueDivider = new int[8];
	private GObject bead;
	private GObject checkBead = null;
	private boolean isMoveUp = true;
	private int highCount;
	private int lowCount;
	private int totalCount;
	private int lastCount;
	private int mistake = 0;
	private int questionCount = 0;
	private boolean runExercise = false;
	private boolean isRightMove;
	private BackOperation lesson;
	private int exerciseIndex = 0;

	private GLabel message_line1;
	private GLabel message_line2;
	private GLabel message_line3;
	private GLabel message_prompt;

	private GLabel question;
	private GLabel number;
}
