import java.util.ArrayList;

public class Trie {

    // Wildcards
    final char WILDCARD = '.';
    final char STARTER = '*';

    TrieNode root;

    private class TrieNode {
        boolean completed = false;
        char data;
        TrieNode[] children = new TrieNode[62];

        public TrieNode(char data) {
            this.data = data;
        }

        public TrieNode insertChild(int pos, char data) {
            children[pos] = new TrieNode(data);
            return children[pos];
        }

        public TrieNode getChild(int pos) {
            return this.children[pos];
        }

        public void makeEnd() {
            this.completed = true;
        }

        public boolean isCompleted() {
            return this.completed;
        }
    }

    public Trie() {
        // TODO: Initialise a trie class here.
        this.root = new TrieNode(STARTER);
    }

    /**
     * Inserts string s into the Trie.
     *
     * @param s string to insert into the Trie
     */
    void insert(String s) {
        TrieNode curr = this.root;
        for (int i = 0; i < s.length(); i++) {
            int pos =  charToPos(s.charAt(i));
            TrieNode child = curr.getChild(pos);
            if (child == null) {
                child = curr.insertChild(pos, s.charAt(i));
            }

            curr = child;
        }
        curr.makeEnd();
    }

    private int charToPos(char c) {
        int value = (int) c;
        if (value >= 48 && value <= 57)
            return value - 48;
        else if (value >= 65 && value <= 90)
            return value - 65 + 10;
        else if (value >= 97 && value <= 122)
            return value - 97 + 26 + 10;
        return 0;
    }

    /**
     * Checks whether string s exists inside the Trie or not.
     *
     * @param s string to check for
     * @return whether string s is inside the Trie
     */
    boolean contains(String s) {
        TrieNode curr = this.root;
        for (int i = 0; i < s.length(); i++) {
            int pos =  charToPos(s.charAt(i));
            TrieNode child = curr.getChild(pos);
            if (child == null) {
                return false;
            }
            curr = child;
        }
        return curr.isCompleted();
    }

    /**
     * Searches for strings with prefix matching the specified pattern sorted by lexicographical order. This inserts the
     * results into the specified ArrayList. Only returns at most the first limit results.
     *
     * @param s       pattern to match prefixes with
     * @param results array to add the results into
     * @param limit   max number of strings to add into results
     */
    void prefixSearch(String s, ArrayList<String> results, int limit) {
        prefixSearch(s, 0, results, this.root, new StringBuilder(), new int[] {limit});
    }

    void prefixSearch(String s, int pos, ArrayList<String> results, TrieNode node, StringBuilder stringBuilder, int[] limit) {
        if (pos >= s.length()) {
            if (node.isCompleted() && limit[0] > 0) {
                results.add(stringBuilder.toString());
                limit[0]--;
            }

            for (TrieNode child : node.children) {
                if (child != null) {
                    prefixSearch(s, pos, results, child, new StringBuilder(stringBuilder).append(child.data), limit);
                }

                if (limit[0] <= 0) {
                    break;
                }
            }
            return;
        }

        char c = s.charAt(pos);

        if (c != this.WILDCARD) {
            TrieNode child = node.getChild(this.charToPos(c));
            if (child != null)
                prefixSearch(s, pos + 1, results, child, new StringBuilder(stringBuilder).append(child.data), limit);
            return;
        }

        for (TrieNode child : node.children) {
            if (child != null) {
                prefixSearch(s, pos + 1, results, child, new StringBuilder(stringBuilder).append(child.data), limit);
            }
        }
    }

    // Simplifies function call by initializing an empty array to store the results.
    // PLEASE DO NOT CHANGE the implementation for this function as it will be used
    // to run the test cases.
    String[] prefixSearch(String s, int limit) {
        ArrayList<String> results = new ArrayList<String>();
        prefixSearch(s, results, limit);
        return results.toArray(new String[0]);
    }


    public static void main(String[] args) {
        Trie t = new Trie();
        t.insert("peter");
        t.insert("piper");
        t.insert("picked");
        t.insert("a");
        t.insert("peck");
        t.insert("of");
        t.insert("pickled");
        t.insert("peppers");
        t.insert("pepppito");
        t.insert("pepi");
        t.insert("pik");

        String[] result1 = t.prefixSearch("pi", 10);
        String[] result2 = t.prefixSearch("pi.", 10);
        System.out.println(result1[2]);
        // result1 should be:
        // ["peck", "pepi", "peppers", "pepppito", "peter"]
        // result2 should contain the same elements with result1 but may be ordered arbitrarily
    }
}
