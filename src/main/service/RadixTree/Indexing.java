package main.service.RadixTree;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Indexing {

	static Map<String, Integer> dic = new HashMap<>();

	public static void makeMap() {
		BufferedReader lecteurAvecBuffer;
		try {
			lecteurAvecBuffer = new BufferedReader(new FileReader("49345.index"));
			String ligne;
			while ((ligne = lecteurAvecBuffer.readLine()) != null) {
				if (dic.containsKey(ligne)) {
					dic.put(ligne, dic.get(ligne) + 1);
				} else {
					dic.put(ligne, 1);
				}
			}

			Map<String, Integer> result = dic.entrySet().stream().sorted(Entry.comparingByValue())
					.collect(Collectors.toMap(Entry::getKey, Entry::getValue, (oldValue, newValue) -> oldValue,
							LinkedHashMap::new));

			FileWriter writer = new FileWriter("indexTable");
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

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		makeMap();
	}

}
