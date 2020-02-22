package main.service.Betweenes;

import main.service.utils.Helper;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Jaccard {

    private static void createJaccardGraph() {
        ArrayList<String> files = Helper.readBooks(Helper.BOOKS_PATH);
        FileWriter fw;
        BufferedWriter bw;
        String file1;
        String file2;
        try {
            double dist;
            for (int i = 0; i < files.size(); i++) {
                System.out.println(i);
                file1 = files.get(i);
                fw = new FileWriter(Helper.JACCARD_PATH +"/"+file1);
                bw = new BufferedWriter(fw);
                for (int j = 0; j < files.size(); j++) {
                    if(i==j) continue;
                    file2 = files.get(j);
                    dist = getDistance(file1, file2);
                    bw.write(file2+" "+dist+"\n");
                }
                bw.close();
                fw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static double getDistance(String file1, String file2) {
        Map<String, Integer> words1 = getAllWordsFromFile(file1);
        Map<String, Integer> words2 = getAllWordsFromFile(file2);
        Set<String> all_words = Stream.of(words1.keySet(), words2.keySet())
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        for (String f : all_words) {
            if (! words1.containsKey(f)) words1.put(f,0);
            else if (! words2.containsKey(f)) words2.put(f,0);
        }

        Integer delta = words1.keySet().stream()
                .map(word -> Math.max(words1.get(word), words2.get(word)) - Math.min(words1.get(word), words2.get(word)))
                .mapToInt(Integer::intValue).sum();

        Integer total = words1.keySet().stream()
                .map(word -> Math.max(words1.get(word), words2.get(word)))
                .mapToInt(Integer::intValue).sum();

        return (double) delta/total;
    }

    private static Map<String, Integer> getAllWordsFromFile(String filename) {
        Map<String, Integer> words = new HashMap<>();
        String ligne;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(Helper.BOOKS_PATH +"/"+filename));
            while ((ligne = reader.readLine()) != null) {
                if (!ligne.equals("") && !ligne.equals("\n")) {
                    String[] array = ligne.split("[^a-zA-Z]");
                    if (array.length != 0) {
                        for (int i = 0; i < array.length; i++) {
                            if (!array[i].equals("") && !array[i].equals("\n")) {
                                if(words.containsKey(array[i])) {
                                    words.put(array[i], words.get(array[i])+1);
                                } else {
                                    words.put(array[i], 1);
                                }
                            }
                        }
                    }
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;
    }

    public static double getDistanceFromJaccardGraph(String file1, String file2) {
        double res = 0;
        FileReader fileReader;
        BufferedReader bufferedReader;
        String matchedLine;
        try {
            fileReader = new FileReader(Helper.JACCARD_PATH+"/"+file1);
            bufferedReader = new BufferedReader(fileReader);

            matchedLine = bufferedReader.lines().filter(line -> line.contains(file2)).collect(Collectors.toList()).get(0);
            res = Double.parseDouble(matchedLine.split(" ")[1]);

            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static void main(String[] args) {
        //createJaccardGraph();
        double res = getDistanceFromJaccardGraph("49182-0.txt", "55467-0.txt");
        System.out.println(res);
    }
}
