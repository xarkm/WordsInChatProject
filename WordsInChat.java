import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;

public class WordsInChat {
    public static void main(String[] args) {
        // scanner check each line for args[1] + ":"
        // if contains, do str = str.replaceAll("[^a-zA-Z0-9]", " "); 
        // convert line to lowercase split by space, add/update hashmap
        HashMap<String, Integer> map = new HashMap<>();
        String filePrefix = args[0].substring(0, args[0].length() - 4) + "_" + args[1];
        try {
            Scanner scanner = new Scanner(new File(args[0]));
            // Create/reset file to contain all the words the person has typed
            File file = new File(filePrefix + "_all_words.txt");
            try {
                if(file.exists()){
                    file.delete();
                }
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                try {
                    FileWriter writer = new FileWriter(file);
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        int lengthToTrim = Integer.parseInt(args[2]) + args[1].length() + 2;
                        if (line.contains(args[1] + ":")) {
                            line = line.substring(lengthToTrim); // remove timestamp and name
                            line = line.replaceAll("’", "'");
                            line = line.replaceAll("[^a-zA-Z0-9']", ","); // replace all special char except ' with ,
                            // replace some letters with longer form so either appearance counts as one
                            line = line.replaceAll(",u,", ",you,");
                            line = line.replaceAll(",ur,", ",you're,");
                            line = line.replaceAll(",r,", ",are,");
                            line = line.replaceAll(",b,", ",be,");
                            line = line.replaceAll(",y,", ",why,");
                            line = line.replaceAll(",w,", ",with,");
                            line = line.toLowerCase();
                            String[] words = line.split(",");
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
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            } finally {
                scanner.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //System.out.println(map.toString());
        PriorityQueue<WordOccurance> queue = new PriorityQueue<>(Comparator.reverseOrder());
        for (String word : map.keySet()) {
            queue.add(new WordOccurance(word, map.get(word)));
        }
        File file = new File(filePrefix + "_count.txt");
        try {
            if(file.exists()){
                file.delete();
            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            int sum = 0;
            FileWriter writer = new FileWriter(file);
            while(!queue.isEmpty()) {
                sum += queue.peek().getFrequency();
                writer.write(queue.poll().toString() + "\n");
            }
            writer.write(String.valueOf(sum));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } 
        
    }
}