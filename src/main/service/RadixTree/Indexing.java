package main.service.RadixTree;

import main.service.utils.Helper;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Indexing {

	public static void makeMap() {

		BufferedReader lecteurAvecBuffer;
		ArrayList<String> files = Helper.readBooks(Helper.INDEXES_PATH);

		for (int f=0; f<files.size(); f++){
			Map<String, Integer> dic = new HashMap<>();
			String book = files.get(f);
			try {
				lecteurAvecBuffer = new BufferedReader(new FileReader(Helper.INDEXES_PATH+"/"+book));
				String ligne;
				while ((ligne = lecteurAvecBuffer.readLine()) != null) {
					if (dic.containsKey(ligne)) {
						dic.put(ligne, dic.get(ligne) + 1);
					} else {
						dic.put(ligne, 1);
					}
				}

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

					lecteurAvecBuffer.close();
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
