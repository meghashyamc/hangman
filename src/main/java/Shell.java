package main.java;

import java.io.IOException;

import asg.cliche.Command;
import asg.cliche.Param;

public class Shell {

	private Word currentWord;
	private Dictionary dictionary;
	private static final String CORRECT = "Correct!";
	private static final String WRONG = "Wrong!";
	private static final String END = "Congrats! You've guessed this word!";
	private static final String SET = "set";
	private static final String GET = "get";
	public static final int DEFAULTWORDLENGTH = 10;

	public Shell() throws IOException {
		this.dictionary = new Dictionary();
	}

	@Command(description = "Get the difficulty level of the game")
	public String level(@Param(name = "get", description = "Use 'get' to get the current level") String get) {
		String validationResult = validateLevel(get, "");
		if (validationResult.length() > 0) {
			return validationResult;
		}
		return String.format("The current level is %s", Word.currentLevel);
	}

	@Command(description = "Set the difficulty level of the game")
	public String level(@Param(name = "set", description = "Use 'set' to set the difficulty level") String set,
			@Param(name = "level", description = "The difficulty level can be 'easy', 'medium' or 'hard')") String levelName) {
		String validationResult = validateLevel(set, levelName);
		if (validationResult.length() > 0) {
			return validationResult;
		}
		Word.currentLevel = levelName;
		Word.minBlankPercentage = Word.LEVELSMAP.get(levelName);
		return String.format("The level has been set to %s. Changes will take effect from the next word.", levelName);
	}

	@Command(description = "Starts the game with a new word")
	public String start() {
		return start(DEFAULTWORDLENGTH);
	}

	@Command(description = "Starts the game with a new word while also specifying the word length of the new word")
	public String start(
			@Param(name = "word-length", description = "The word length of the word to be used in the game") int wordlength) {
		String validationResult = validateWordLength(wordlength);
		if (validationResult.length() > 0) {
			return validationResult;
		}
		this.currentWord = new Word(this.dictionary.getNewWord(wordlength));

		return formWordToDisplayWithIndices();

	}

	@Command(description = "Gives a hint by filling up one blank")
	public String hint() {
		String validationResult = validateWordHasBeenGenerated();
		if (validationResult.length() > 0)
			return validationResult;
		if (this.currentWord.hasBeenGuessed())
			return "The word has already been guessed - so I can't give you a hint";

		StringBuilder newWordStringWithBlanksBuilder = new StringBuilder(this.currentWord.getWordStringWithBlanks());

		for (int i = 0; i < newWordStringWithBlanksBuilder.length(); i++) {
			if (newWordStringWithBlanksBuilder.charAt(i) == Word.BLANK) {
				newWordStringWithBlanksBuilder.setCharAt(i, this.currentWord.getWordString().charAt(i));
				break;
			}
		}
		this.currentWord.setWordStringWithBlanks(newWordStringWithBlanksBuilder.toString());
		return formWordToDisplayWithIndices();

	}

	@Command(description = "Guess one letter in the word")
	public String guess(
			@Param(name = "position", description = "The position of the letter being guessed") int position,
			@Param(name = "letter", description = "The letter being guessed") String letterString) {

		String validationResult = validateGuess(position, letterString);
		if (validationResult.length() > 0) {
			return validationResult;
		}
		char letter = letterString.charAt(0);
		boolean correct = false;
		if (this.currentWord.getWordString().charAt(position) == letter) {
			StringBuilder newWordStringWithBlanksBuilder = new StringBuilder(
					this.currentWord.getWordStringWithBlanks());
			newWordStringWithBlanksBuilder.setCharAt(position, letter);
			this.currentWord.setWordStringWithBlanks(newWordStringWithBlanksBuilder.toString());
			correct = true;
		}

		return formGuessResponse(correct, this.currentWord.hasBeenGuessed());

	}

	private String validateGuess(int position, String letterString) {
		String validationResult = validateWordHasBeenGenerated();
		if (validationResult.length() > 0)
			return validationResult;

		String wordStringWithBlanks = this.currentWord.getWordStringWithBlanks();
		if (position < 0 || position > wordStringWithBlanks.length()) {
			return String.format("Please specify a valid position between %d and %d", 0, wordStringWithBlanks.length());
		}

		if (wordStringWithBlanks.charAt(position) != Word.BLANK) {
			return String.format("The letter at position %d is not blank!", position);
		}

		return "";
	}

	private String validateWordLength(int wordlength) {

		if (wordlength <= 1)
			return "Word length must be greater than 1";

		if (!dictionary.doWordsOfGivenLengthExist(wordlength))
			return "I do not have words of the given length. Can you try another word length?";

		return "";
	}

	private String validateLevel(String getset, String levelName) {

		if (!getset.contentEquals(GET) && !getset.contentEquals(SET))
			return String.format("The command %s is not valid", getset);

		if (!levelName.contentEquals("") && !Word.LEVELSMAP.containsKey(levelName))
			return "The specified level name is not valid";

		return "";
	}

	private String validateWordHasBeenGenerated() {
		if (this.currentWord == null) {
			return "Please start the game to generate a word first";
		}
		return "";
	}

	private String formGuessResponse(boolean isGuessCorrect, boolean areWeDone) {
		StringBuilder responseBuilder = new StringBuilder();

		responseBuilder.append(isGuessCorrect ? CORRECT : WRONG);
		responseBuilder.append(System.lineSeparator());
		appendWordToDisplayWithIndices(responseBuilder);
		if (areWeDone) {
			responseBuilder.append(System.lineSeparator());
			responseBuilder.append(END);
		}
		return responseBuilder.toString();
	}

	private String formWordToDisplayWithIndices() {
		StringBuilder responseBuilder = new StringBuilder();
		appendWordToDisplayWithIndices(responseBuilder);
		return responseBuilder.toString();
	}

	private void appendWordToDisplayWithIndices(StringBuilder responseBuilder) {
		responseBuilder.append(this.currentWord.getWordStringWithBlanks());
		responseBuilder.append(System.lineSeparator());
		responseBuilder.append(this.currentWord.getWordIndices());
	}

}
