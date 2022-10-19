package main.java;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Word {

	public static final Character BLANK = '_';
	public static final Character[] VOWELLIST = new Character[] { 'a', 'e', 'i', 'o', 'u' };
	public static final Set<Character> VOWELSET = new HashSet<>(Arrays.asList(VOWELLIST));
	public static final String LEVELEASY = "easy";
	public static final String LEVELMEDIUM = "medium";
	public static final String LEVELHARD = "hard";
	public static final Map<String, Integer> LEVELSMAP = Map.of(LEVELEASY, 30, LEVELMEDIUM, 50, LEVELHARD, 70);
	protected static String currentLevel = LEVELMEDIUM;
	protected static int minBlankPercentage = LEVELSMAP.get(LEVELMEDIUM);
	private String wordString;
	private String wordStringWithBlanks;
	private String wordIndices;

	public Word(String wordString) {
		this.wordString = wordString;
		this.wordStringWithBlanks = addBlanks(this.wordString);
		this.wordIndices = getWholeNumbersTill(wordString.length());
	}

	public String getWordStringWithBlanks() {
		return this.wordStringWithBlanks;
	}

	public String getWordString() {
		return this.wordString;
	}

	public String getWordIndices() {
		return this.wordIndices;
	}

	public void setWordStringWithBlanks(String s) {
		this.wordStringWithBlanks = s;
	}

	public boolean hasBeenGuessed() {
		return this.wordStringWithBlanks.contentEquals(this.wordString);
	}
	
	
	private static String addBlanks(String s) {

		int numOfBlanks = 0;
		StringBuilder sBuilder = new StringBuilder(s);
		for (int i = 0; i < sBuilder.length(); i++) {
			if (VOWELSET.contains(Character.valueOf(sBuilder.charAt(i)))) {
				sBuilder.setCharAt(i, BLANK);
				numOfBlanks++;
				if (getBlanksPercentage(numOfBlanks, s.length()) >= minBlankPercentage)
					break;
			}
		}

		String wordStringWithBlanks = sBuilder.toString();
		if (getBlanksPercentage(numOfBlanks, s.length()) >= minBlankPercentage) {
			return wordStringWithBlanks;
		}

		for (int i = 0; i < sBuilder.length(); i++) {
			if (i % 2 == 0) {
				sBuilder.setCharAt(i, BLANK);
				numOfBlanks++;
				if (getBlanksPercentage(numOfBlanks, s.length()) >= minBlankPercentage)
					break;
			}
		}

		return sBuilder.toString();

	}

	private static int getBlanksPercentage(int numOfBlanks, int stringLength) {
		return (100 * numOfBlanks) / stringLength;

	}

	private static String getWholeNumbersTill(int num) {

		StringBuilder sBuilder = new StringBuilder();

		for (int i = 0; i < num; i++) {
			sBuilder.append(i);
		}

		return sBuilder.toString();

	}

}
