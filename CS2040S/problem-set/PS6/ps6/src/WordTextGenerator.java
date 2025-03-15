import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * This class generates text using a Word-Based Markov Model.
 */
public class WordTextGenerator {

    private static long seed;

    public static void setSeed(long s) {
        seed = s;
    }

    /**
     * Reads in the file and builds the Markov Model using words.
     *
     * @param order    the order of the Markov Model (number of words)
     * @param fileName the nameof the file to read
     * @param model    the Word-Based marko Model to build
     * @return the first {@code order} words of the file to be used as the seed text
     */
    public static String buildModel(int order, String fileName, WordMarkovModel model) {
        StringBuilder text = new StringBuilder();

        // Read the file into a single string
        try {
            Scanner scanner = new Scanner(new File(fileName));
            while (scanner.hasNext())
                text.append(scanner.next()).append(" ");

            scanner.close();

            // Ensure text has enough words for the Markov model
            String[] words = text.toString().trim().split("\\s+");
            if (words.length < order) {
                System.out.println("Textis shorter than specified Markov Order.");
                return null;
            }
        } catch (FileNotFoundException e) {
            System.out.println("Problem reading file " + fileName + ".");
            return null;
        }

        // Build Markov Model of order from text
        model.initializeText(text.toString());
        return String.join(" ",
                text.toString().trim().split("\\s+"))
                .substring(0, order);
    }

    /**
     * Generates text of the specified lengh using the given Markov Model.
     *
     * @param model    the Word-Based Markov Model to use
     * @param seedText the initial kgram used to generate text
     * @param order    the order of the Markov Model (number of words)
     * @param length   the number of words to generate
     */
    public static void generateText(WordMarkovModel model, String seedText, int order, int length) {
        StringBuilder generatedText = new StringBuilder(seedText);
        String kgram = seedText;

        // Generate words until we reach the desired length
        for (int i = 0; i < length - order; i++) {
            String nextWord = model.getNextWord(kgram);
            if (nextWord.equals(WordMarkovModel.NOWORD)) {
                System.out.println(generatedText.toString());
                return;
            }

            generatedText.append(" ").append(nextWord);

            // Shift window (keep last `order` words)
            String[] words = generatedText.toString().split("\\s+");
            int startIdx = Math.max(0, words.length - order);
            kgram = String.join(" ",
                    Arrays.copyOfRange(words, startIdx, words.length));
        }

        System.out.println(generatedText);
    }


    /**
     * The main routine. Takes 3 arguments:
     * args[0]: the order of the Markov Model
     * args[1]: the length of the text to generate (number of words)
     * args[2]: the filename for the input text
     */
    public static void main(String[] args) {
        // Check that we have three parameters
        if (args.length != 3) {
            System.out.println("Number of input parameters are wrong.");
            return;
        }

        // Get the input
        int order = Integer.parseInt(args[0]);
        int length = Integer.parseInt(args[1]);
        String fileName = args[2];

        // Create the model
        WordMarkovModel markovModel = new WordMarkovModel(order, seed);
        String seedText = buildModel(order, fileName, markovModel);

        // Generate text
        generateText(markovModel, seedText, order, length);
    }
}
