package main.service.RadixTree;

import main.service.utils.Helper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Indexing {

	static Map<String, Integer> dic;

	public static void makeMap() {
		ArrayList<String> files = Helper.readBooks(Helper.INDEXES_PATH);
		for (int i = 0; i < files.size(); i++) {
			dic = new HashMap<>();
			String index = files.get(i);
			System.out.println(index);
			BufferedReader lecteurAvecBuffer;
			try {
				lecteurAvecBuffer = new BufferedReader(new FileReader(Helper.INDEXES_PATH+"/"+index));
				String ligne;
				while ((ligne = lecteurAvecBuffer.readLine()) != null) {
					String [] arr = ligne.split(" ");
					String term = arr[0];

					if (dic.containsKey(ligne)) {
						dic.put(ligne, dic.get(ligne) + 1);
					} else {
						dic.put(ligne, 1);
					}
				}

				Map<String, Integer> result = dic.entrySet().stream().sorted(Entry.comparingByValue())
						.collect(Collectors.toMap(Entry::getKey, Entry::getValue, (oldValue, newValue) -> oldValue,
								LinkedHashMap::new));

				FileWriter writer = new FileWriter(Helper.INDEXES_TABLES_PATH+"/"+index);
				BufferedWriter bw = new BufferedWriter(writer);

				for (Entry<String, Integer> e : result.entrySet()) {
					if (e.getKey().length() > 3 && e.getValue() < 100)
						bw.write(e.getKey() + " " + e.getValue() + "\n");
				}

				lecteurAvecBuffer.close();
				bw.close();
				writer.close();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		makeMap();
	}

}
