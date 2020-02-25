package main.service.RadixTree;

import main.service.utils.CleanData;
import main.service.utils.Helper;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Indexing {

	public static void makeMap() {

		ArrayList<String> files = Helper.readBooks(Helper.BOOKS_PATH);

		for (int f=0; f<files.size(); f++){
			String book = files.get(f);
			try {

				Map<String, Integer> dic = CleanData.getAllWordsFromFile(book);
				Map<String, Integer> result = dic.entrySet().stream().sorted(Map.Entry.comparingByKey())
						.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue,
								LinkedHashMap::new));

				File indexDirectory = new File(Helper.INDEXES_TABLES_PATH+"/"+book);
				boolean createdDir = indexDirectory.mkdir();
				System.out.println(createdDir);
				if (createdDir){

					FileWriter [] fileWriters = new FileWriter[26];
					BufferedWriter [] bufferedWriters = new BufferedWriter[26];
					for (int i = 0; i < 26; i++) {
						fileWriters[i] = new FileWriter(Helper.INDEXES_TABLES_PATH+"/"+book+"/"+i+".txt");
						bufferedWriters[i] = new BufferedWriter(fileWriters[i]);
					}

					for (Entry<String, Integer> e : result.entrySet()) {
						if (e.getKey().length() > 3 && e.getValue() < 100){
							int asciiCode = (e.getKey().toUpperCase().charAt(0) - 65);
							bufferedWriters[asciiCode].write(e.getKey() + " " + e.getValue().toString() + "\n");
						}
					}

					for (int i = 0; i < 26; i++) {
						bufferedWriters[i].close();
						fileWriters[i].close();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("End");
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		makeMap();
	}

}
