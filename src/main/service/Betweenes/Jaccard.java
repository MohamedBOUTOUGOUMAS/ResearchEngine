package main.service.Betweenes;

import main.service.utils.Helper;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static main.service.utils.CleanData.getAllWordsFromFile;

public class Jaccard {

    private static void createJaccardGraph() {
        Map<String, Map<String, Integer>> mapFiles = new HashMap<>();
        Map<String, Double> distanceMap = new HashMap<>();
        ArrayList<String> files = Helper.readBooks(Helper.BOOKS_PATH);
        FileWriter fw;
        BufferedWriter bw;
        String file1;
        String file2;
        Map<String, Integer> words1;
        Map<String, Integer> words2;

        try {
            double dist;
            for (int i = 0; i < files.size(); i++) {
                file1 = files.get(i);

                if (mapFiles.containsKey(file1))
                    words1 = mapFiles.get(file1);
                else {
                    words1 = getAllWordsFromFile(file1);
                    mapFiles.put(file1, words1);
                }

                fw = new FileWriter(Helper.JACCARD_PATH +"/"+file1);
                bw = new BufferedWriter(fw);
                for (int j = 0; j < files.size(); j++) {
                    if(i==j) continue;
                    System.out.println(i+" "+j);
                    file2 = files.get(j);

                    if (mapFiles.containsKey(file2))
                        words2 = mapFiles.get(file2);
                    else {
                        words2 = getAllWordsFromFile(file2);
                        mapFiles.put(file2, words2);
                    }

                    if (distanceMap.containsKey(file1+file2)){
                        dist = distanceMap.get(file1+file2);
                    }else if (distanceMap.containsKey(file2+file1)){
                        dist = distanceMap.get(file2+file1);
                    }else {
                        dist = getDistance(words1, words2);
                        distanceMap.put(file1+file2, dist);
                        distanceMap.put(file2+file1, dist);
                    }

                    bw.write(file2+" "+dist+"\n");
                }
                bw.close();
                fw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static Map<String, Integer> getAllWordsFromIndex(String filename) {
        Map<String, Integer> words = new HashMap<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("index" +"/"+filename));
            words = reader.lines()
                    .map(line -> line.split(" "))
                    .map(line -> new AbstractMap.SimpleEntry<>(line[0], Integer.parseInt(line[1])))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;
    }

    public static double getDistance(Map<String, Integer> words1, Map<String, Integer> words2) {
        Set<String> all_words = Stream.of(words1.keySet(), words2.keySet())
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        Integer delta = all_words.parallelStream()
                .map(word -> Math.max(words1.getOrDefault(word, 0), words2.getOrDefault(word,0)) -
                        Math.min(words1.getOrDefault(word, 0), words2.getOrDefault(word,0)))
                .mapToInt(Integer::intValue).sum();

        Integer total = all_words.parallelStream()
                .map(word -> Math.max(words1.getOrDefault(word, 0), words2.getOrDefault(word,0)))
                .mapToInt(Integer::intValue).sum();

        return (double) delta/total;
    }

    public static Map<String, Double> getDistanceFromJaccardGraph(String file1, double edgeThreshold) {
        Map<String, Double> res = new HashMap<>();
        FileReader fileReader;
        BufferedReader bufferedReader;
        try {
            fileReader = new FileReader(Helper.JACCARD_PATH+"/"+file1);
            bufferedReader = new BufferedReader(fileReader);

            res = bufferedReader.lines()
                    .map(line -> line.split(" "))
                    .map(lineArray -> new AbstractMap.SimpleEntry<>(lineArray[0], Double.parseDouble(lineArray[1])))
                    //.filter(entry -> entry.getValue() < edgeThreshold)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static void main(String[] args) {
        createJaccardGraph();
        return;
    }
}
