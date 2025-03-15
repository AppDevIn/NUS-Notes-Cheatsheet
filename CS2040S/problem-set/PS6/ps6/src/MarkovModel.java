import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This is the main class for your Markov Model.
 *
 * Assume that the text will contain ASCII characters in the range [1,255].
 * ASCII character 0 (the NULL character) will be treated as a non-character.
 *
 * Any such NULL characters in the original text should be ignored.
 */
public class MarkovModel {

	// Use this to generate random numbers as needed
	private Random generator = new Random();

	private int order;

	private HashMap<String, HashMap<Character, Integer>> hashMap;

	// This is a special symbol to indicate no character
	public static final char NOCHARACTER = (char) 0;

	/**
	 * Constructor for MarkovModel class.
	 *
	 * @param order the number of characters to identify for the Markov Model sequence
	 * @param seed the seed used by the random number generator
	 */
	public MarkovModel(int order, long seed) {
		// Initialize your class here
		this.order = order;

		// Initialize the random number generator
		generator.setSeed(seed);

		//Inti Hash Table
		hashMap = new HashMap<String, HashMap<Character, Integer>>();

	}

	/**
	 * Builds the Markov Model based on the specified text string.
	 */
	public void initializeText(String text) {
		int i = 0;
		while (i + order < text.length()) {
			String key = text.substring(i, i + order);
			char value = text.charAt(i + order);

			hashMap.putIfAbsent(key, new HashMap<>());
			hashMap.get(key).put(value, hashMap.get(key).getOrDefault(value, 0) + 1);
			i++;
		}
	}

	/**
	 * Returns the number of times the specified kgram appeared in the text.
	 */
	public int getFrequency(String kgram) {
		return  hashMap.get(kgram).values().stream().mapToInt(Integer::intValue).sum();
	}

	/**
	 * Returns the number of times the character c appears immediately after the specified kgram.
	 */
	public int getFrequency(String kgram, char c) {
		return hashMap.get(kgram).getOrDefault(c, 0);
	}

	/**
	 * Generates the next character from the Markov Model.
	 * Return NOCHARACTER if the kgram is not in the table, or if there is no
	 * valid character following the kgram.
	 */
	public char nextCharacter(String kgram) {
		// See the problem set description for details
		// on how to make the random selection.
		HashMap<Character, Integer> freq = hashMap.get(kgram);

		if (freq == null) return this.NOCHARACTER;

		int size =  freq.values().stream().mapToInt(Integer::intValue).sum();
		int randomNum = this.generator.nextInt(size);


		AtomicInteger curr = new AtomicInteger(0);
		return freq.entrySet().stream()
				.sorted(Map.Entry.comparingByKey())
				.filter(entry -> curr.addAndGet(entry.getValue()) > randomNum)
				.map(Map.Entry::getKey)
				.findFirst()
				.orElse(this.NOCHARACTER);
	}
}
