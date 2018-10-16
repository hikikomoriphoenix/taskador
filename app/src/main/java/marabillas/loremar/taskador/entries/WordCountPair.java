package marabillas.loremar.taskador.entries;

/**
 * An entry containing one of the user's top words and its corresponding count indicating how
 * many times it was used in tasks.
 */
public class WordCountPair {
    public String word;
    public String count;

    public WordCountPair(String word, String count) {
        this.word = word;
        this.count = count;
    }
}
