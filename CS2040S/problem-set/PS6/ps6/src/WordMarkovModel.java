import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class WordMarkovModel {

    private Random generator;
    private int order;
    private HashMap<String, HashMap<String, Integer>> hashMap;
    public static final String NOWORD = "";

    public WordMarkovModel(int order, long seed) {
        this.order = order;
        this.generator = new Random(seed);
        this.hashMap = new HashMap<>();
    }

    public void initializeText(String text) {
        String[] words = text.split("\\s+");

        for (int i = 0; i <= words.length - order; i++) {
            String key = String.join(" ", Arrays.copyOfRange(words, i, i + order));
            String value = (i + order < words.length) ? words[i + order] : NOWORD;

            hashMap.putIfAbsent(key, new HashMap<>());
            hashMap.get(key).put(value, hashMap.get(key).getOrDefault(value, 0) + 1);
        }
    }

    public String getNextWord(String kgram) {
        HashMap<String, Integer> freq = hashMap.get(kgram);
        if (freq == null) return NOWORD;

        int size = freq.values().stream().mapToInt(Integer::intValue).sum();
        int randomNum = generator.nextInt(size);

        AtomicInteger curr = new AtomicInteger(0);
        return freq.entrySet().stream()
                .sorted(Map.Entry.comparingByKey()) // Sort in lexicographic order
                .filter(entry -> curr.addAndGet(entry.getValue()) >= randomNum)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(NOWORD);
    }
}
