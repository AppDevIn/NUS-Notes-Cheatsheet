import java.util.Random;

public class Guessing {

    // Your local variables here
    private int low = 0;
    private int high = 1000;

    private int lastGuess = -1;

    /**
     * Implement how your algorithm should make a guess here
     */
    public int guess() {
        lastGuess = (low + high) / 2;
        return lastGuess;
    }

    /**
     * Implement how your algorithm should update its guess here
     */
    public void update(int answer) {
        if (answer == -1)
            low = lastGuess + 1;
        else if (answer == 1)
            high = lastGuess - 1;

    }
}
