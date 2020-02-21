package main.service.RadixTree;

import main.service.utils.Helper;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class CleanData {

	private static void books_to_words() {
		ArrayList<String> files = Helper.readBooks(Helper.BOOKS_PATH);
		Set<String> words;
		FileWriter fw;
		BufferedWriter bw;
		String res = "";
		try {
			for (String file : files) {
				System.out.println(file);
				fw = new FileWriter(Helper.BOOKS_BY_WORDS_PATH+"/"+file);
				bw = new BufferedWriter(fw);
				words = getAllWordsFromFile(file);
				res = words.stream().collect(Collectors.joining("\n"));
				bw.write(res);
				bw.close();
				fw.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static Set<String> getAllWordsFromFile(String filename) {
		Set<String> words = new HashSet<>();
		String ligne;

		FileWriter fwClean;
		BufferedWriter bwClean;

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
		return words;
	}

	public static void main(String[] args) {
		books_to_words();
	}
}
