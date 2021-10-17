import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;

public class WordsInChat {
    public static void main(String[] args) throws IOException {
        // string prefix for file name
        String filePrefix = args[0].substring(0, args[0].length() - 4) + "_" + args[1];
        filePrefix = filePrefix.replaceAll(" ", "_");
        filePrefix = filePrefix.toLowerCase();

        Scanner scanner = null;
        FileWriter writer = null;
        HashMap<String, Integer> map = new HashMap<>(); // stores frequency of each word
        PriorityQueue<WordOccurance> queue = new PriorityQueue<>(Comparator.reverseOrder()); // create priority queue to sort words by highest frequency once done mapping

        // check each line for correct person, and process line if so, updating map and all-words file
        try {
            // create/reset file to contain all the words the person has typed
            File file = new File(filePrefix + "_all_words.txt");
            if(file.exists()){
                file.delete();
            }
            file.createNewFile();

            writer = new FileWriter(file);
            scanner = new Scanner(new File(args[0]));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                int lengthToTrim = Integer.parseInt(args[2]) + args[1].length() + 2;
                if (line.contains(args[1] + ":")) {
                    line = line.substring(lengthToTrim); // remove timestamp and name
                    line = line.replaceAll("’", "'"); // replace all slanted apostrophes with standard '
                    line = line.replaceAll("\\s", " "); // replace all whitespace with space
                    line = line.replaceAll("((https?|ftp|gopher|telnet|file|Unsure|http):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)", " "); // remove all links
                    line = line.replaceAll("[.?!]", " "); // place all sentence stoppers with space
                    // replace some strings with longer form so either appearance counts as one
                    line = line.replaceAll(" '|' |^[']|[']$", " "); // replace all apostrophes as quotations with space
                    line = line.replaceAll(" w/ |^w/|w/$", " with ");
                    line = line.replaceAll(" w/e |^w/e|w/e$", " whatever ");
                    line = line.replaceAll(" w/o |^w/o|w/o$", " without ");
                    line = line.replaceAll(" w/e |^w/e|w/e$", " whatever ");
                    line = line.replaceAll(" s/o |^s/o|s/o$", " significant other ");
                    line = line.replaceAll("'ve |'ve$", " have ");
                    line = line.replaceAll("'re |'re$", " are ");
                    line = line.replaceAll("[^a-zA-Z0-9']", " "); // replace all remaining special char except apostrophe with space
                    line = line.toLowerCase();
                    String[] words = line.split(" ");
                    for (String word : words) {
                        // for when there are multiple commas in a row
                        if (!word.equals("")) {
                            try {
                                writer.write(word + " ");
                                if (map.containsKey(word)) {
                                    map.put(word, map.get(word) + 1);
                                } else {
                                    map.put(word, 1);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        } 
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }  finally {
            scanner.close();
            writer.close();
        }

        // create new WordOccurance objects to add to the priority queue
        for (String word : map.keySet()) {
            queue.add(new WordOccurance(word, map.get(word)));
        }

        // go through queue to process count file 
        try {
            File file = new File(filePrefix + "_count.txt");
            // create/reset file for count
            if(file.exists()){
                file.delete();
            }
            file.createNewFile();

            int sum = 0;
            writer = new FileWriter(file);
            while(!queue.isEmpty()) {
                // increment the sum for each next highest freq word
                sum += queue.peek().getFrequency();
                writer.write(queue.poll().toString() + "\n");
            }
            writer.write(String.valueOf(sum));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
    }
}