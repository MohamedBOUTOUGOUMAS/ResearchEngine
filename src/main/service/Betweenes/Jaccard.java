package main.service.Betweenes;

import main.service.utils.Helper;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Jaccard {

    public static double getDistance(String file1, String file2) {
        List<String> words1 = getAllWordsFromFile(file1);
        List<String> words2 = getAllWordsFromFile(file2);

        List<String> intersection = words1.stream()
                .filter(words2::contains)
                .collect(Collectors.toList());

        List<String> union = new ArrayList<>(Stream.of(words1, words2)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet()));

        if (!union.isEmpty()) {
            return (double) intersection.size()/union.size();
        } else {
            return Double.POSITIVE_INFINITY;
        }
    }

    private static ArrayList<String> getAllWordsFromFile(String filename) {
        Set<String> words = new HashSet<>();
        String ligne;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(Helper.BOOKS_PATH +"/"+filename));
            while ((ligne = reader.readLine()) != null) {
                if (!ligne.equals("") && !ligne.equals("\n")) {
                    String[] array = ligne.split("[^a-zA-Z]");
                    if (array.length != 0) {
                        for (int i = 0; i < array.length; i++) {
                            if (!array[i].equals("") && !array[i].equals("\n")) {
                                words.add(array[i]);
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
        return new ArrayList<>(words);
    }

    public static void main(String[] args) {
        try {
            double dist = getDistance("91-0.txt", "36-0.txt");
            System.out.println("Distance de jaccard : "+dist);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
