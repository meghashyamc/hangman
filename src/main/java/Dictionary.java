package main.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import com.jayway.jsonpath.JsonPath;

public class Dictionary {

	private HashMap<Integer, ArrayList<String>> wordLengthDictionary = new HashMap<>();
	public static String DICTIONARYFILEPATH = "src/main/resources/words_dictionary.json";

	public Dictionary() throws IOException {

		populateDictionaryFromFile(DICTIONARYFILEPATH);

	}

	public String getNewWord(int wordLength) {

		Random rand = new Random();

		ArrayList<String> possibleWords = wordLengthDictionary.get(wordLength);

		int wordIndex = rand.nextInt(possibleWords.size());

		return possibleWords.get(wordIndex);

	}

	public boolean doWordsOfGivenLengthExist(int wordlength) {

		return wordLengthDictionary.containsKey(wordlength);
	}

	private void populateDictionaryFromFile(String filepath) throws IOException {

		String fileDataString = new String(Files.readAllBytes(Paths.get(filepath)));

		Set<String> rawDictionary = JsonPath.read(fileDataString, "$..*.keys()");

		Iterator<String> wordsIterator = rawDictionary.iterator();

		while (wordsIterator.hasNext()) {
			ArrayList<String> words;
			String nextWord = wordsIterator.next();
			if (!wordLengthDictionary.containsKey(nextWord.length())) {
				words = new ArrayList<>();

			} else {
				words = wordLengthDictionary.get(nextWord.length());
			}
			words.add(nextWord);
			wordLengthDictionary.put(nextWord.length(), words);

		}

	}

}
