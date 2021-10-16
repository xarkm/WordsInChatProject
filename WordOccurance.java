public class WordOccurance implements Comparable<WordOccurance> {
    String word;
    int freq;

    public WordOccurance(String word, int freq) {
        this.word = word;
        this.freq = freq;
    }

    public int getFrequency() {
        return freq;
    }

    @Override
    public String toString() {
        return word + ": " + freq;
    }

    @Override
    public int compareTo(WordOccurance other) {
        return Integer.compare(this.freq, other.freq);
    }
}
