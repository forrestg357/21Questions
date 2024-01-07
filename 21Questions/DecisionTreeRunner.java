import java.io.File;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class DecisionTreeRunner {

	private static Tree<String> qTree = new Tree<String>();
	
	private static boolean quit = false;
	private static int userWins = 0;
	private static int myWins = 0;
	private static File statementFile;
	private static PrintWriter treeWriter;
	private static Scanner treeScanner;

	public static void main(String[] args) {
		setUp();
		playGame();
		saveGame();
		
	}
	
	// Scans for user input, prompted by a given question
	public static String scan(String question) {

		Scanner scan = new Scanner(System.in);
		System.out.println(question);
		String text = scan.nextLine();

		return text; 

	}
	//Sets up the game
	private static void setUp() {
		
		makeFile();
		showInstructions(); 

		try {
			treeScanner = new Scanner(statementFile); //Creates scanner to read in file
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		importDecisionTree(qTree); //Reads the file into a binary tree

		if (qTree.getData() == null) { //Sets initial question and guesses if empty
			setInitialQA();
		}
	}

	//Makes the file containing statements (questions and guesses)
	//stored from previous rounds
	private static void makeFile() {
		
		try {
			statementFile = new File("StatementFile.txt");

			if (statementFile.createNewFile()) {
				System.out.println("File created: " + statementFile.getName());
				
			} 
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();

		}
	}
	
	//Instructions
	private static void showInstructions() {
		
		System.out.println("Welcome to 21 Questions!\n");
		System.out.println("Think of an item, and I will guess it\n"
				+ "using a series of [y]es-or-[n]o questions.\n"
				+ "I can also learn and grow smarter over time.\n");
		
	}
	
	//Imports the decision tree from the statementFile
	private static void importDecisionTree(Tree<String> currentTree) {
		
		if (treeScanner.hasNextLine()) {
			String nextLine = treeScanner.nextLine();
			
			//Base case and first step of recursion
			currentTree.setData(nextLine);
			
			//Checks if the next line is a question (not a leaf of the tree)
			if (nextLine.charAt(nextLine.length() - 1) == '?') {
	
				currentTree.setLeft(new Tree<String>());
				importDecisionTree(currentTree.getLeft());
				
				currentTree.setRight(new Tree<String>());
				importDecisionTree(currentTree.getRight());

			}
		}
	}

	// If the qTree is empty, sets the default questions and answers
	private static void setInitialQA() {
		
		qTree.setData("Is it an Animal?");
		qTree.setRight(new Tree<String>("Dolphin"));
		qTree.setLeft(new Tree<String>("Book"));

	}

	// Play the game
	private static void playGame() {
		while (quit == false) {
			gameAction();
			
		}
	}
	
	//The three actions in a game are either play, quit, or show game stats 
	private static void gameAction() {
		
		String input = scan("[P]lay, [Q]uit, or Show [S]tats:");
		while (!input.equalsIgnoreCase("q") && !input.equalsIgnoreCase("p") && !input.equalsIgnoreCase("s")) {
			input = scan("Continue [P]laying, [Q]uit, or Show [S]tats:");
		}
		if (input.equalsIgnoreCase("q")) {
			quit = true;
			
		}else if (input.equalsIgnoreCase("s")) {
			showStats();
			
		}else {
			questionCycle(qTree);
			
		}
	}
	
	//Shows game record against the computer for a particular cycle
	private static void showStats() {
		
		System.out.println("Games won by me: " + (myWins));
		System.out.println("Games won by you: " + userWins);
		
	}
	
	
	//The possibilities after posing a statement
	private static void questionCycle(Tree<String> currentTree) {
		
		String response = questionOrGuessResponse(currentTree); //Gets response to a question or guess
		
		if (response.equalsIgnoreCase("y") && currentTree.getRight() == null) {
			System.out.println("I guessed your object!");
			myWins++; //Computer wins

		} else if (response.equalsIgnoreCase("n") && currentTree.getLeft() == null) {
			addQuestion(currentTree);
			userWins++; //User wins
			
		} else if (response.equalsIgnoreCase("y") && currentTree.getRight() != null) {
			questionCycle(currentTree.getRight());

		} else if (response.equalsIgnoreCase("n") && currentTree.getLeft() != null) {
			questionCycle(currentTree.getLeft());

		} 
	}
	
	public static String questionOrGuessResponse(Tree<String> currentTree) {
		
		String currentStatement = currentTree.getData();
		String response;
		
		if (currentStatement.charAt(currentStatement.length() - 1) != '?') { 
			response = scan("Y/N My guess is: " + currentStatement); //Text for making a guess
			
			while (!response.equalsIgnoreCase("y") && !response.equalsIgnoreCase("n")) { //ensures user types y or n
				response = scan("Y/N My guess is: " + currentStatement);
				
			}
			
		} else {
			response = scan("Y/N " + currentStatement); //Text for asking a question
			
			while (!response.equalsIgnoreCase("y") && !response.equalsIgnoreCase("n")) { //ensures user types y or n
				response = scan(currentStatement);
				
			}
		}return response;
	}
	
	// Adds a question when user has traversed to the end of the tree and the program has not correctly guessed
	private static void addQuestion(Tree<String> currentTree) {
		
		String currentGuess = currentTree.getData();
		String correctAnswer = getAnswer();
		boolean setItemSide = makeNewQuestion(currentTree);

		if (setItemSide) { //if true, the side is right for the user's object
			currentTree.setRight(new Tree<String>(correctAnswer));
			currentTree.setLeft(new Tree<String>(currentGuess));

		} else { //otherwise, the side is left for the user's object
			currentTree.setLeft(new Tree<String>(correctAnswer));
			currentTree.setRight(new Tree<String>(currentGuess));

		}
	}
	
	//Makes a new question when the program fails to correctly guess the item
	private static boolean makeNewQuestion(Tree<String> currentTree) {
		
		String newQuestion = scan("What is a question that distinguishes "
				+ "between my guess and your object?");
		if (newQuestion.charAt(newQuestion.length() - 1) != '?') { //In case user forgets a "?"
			newQuestion += "?";
		}
		currentTree.setData(newQuestion);
		
		String answer = scan("And what is the answer (Y/N) to the above question for your item?");
		while (!answer.equalsIgnoreCase("Y") && !answer.equalsIgnoreCase("N")) {  //ensures yes or no answer
			answer = scan("And what is the answer (Y/N) to the above question for your item?");
			
		}
		if (answer.equalsIgnoreCase("y")){ //the question is true for the user's object
			return true;
			
		}return false;
	}

	//Prompts the user to give an answer
	private static String getAnswer() {
		return scan("What was your object?");

	}
	
	//Saves the game for the next round
	private static void saveGame() {
		
		try {
			treeWriter = new PrintWriter("StatementFile.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Saving for the next game...");
		printTree(qTree);
		treeWriter.close();
		System.out.println("Done!");
	}
	
	// Saves the decisions in qTree
	private static void printTree(Tree<String> currentTree) {
		
		if (currentTree.getRight() == null && currentTree.getLeft() == null) {
			treeWriter.println(currentTree.getData());
			
		} else {
			treeWriter.println(currentTree.getData());
			printTree(currentTree.getLeft());
			printTree(currentTree.getRight());

		}
	}
}