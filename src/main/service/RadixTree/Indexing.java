package main.service.RadixTree;

import main.service.utils.Helper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Indexing {



	public static void makeMap() {

		BufferedReader lecteurAvecBuffer;
		ArrayList<String> files = Helper.readBooks(Helper.INDEXES_PATH);

		for (int f=0; f<files.size(); f++){
			Map<String, Integer> dic = new HashMap<>();
			String book = files.get(f);
			System.out.println(book);
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

				FileWriter writer = new FileWriter(Helper.INDEXES_TABLES_PATH+"/"+book);
				BufferedWriter bw = new BufferedWriter(writer);

				for (Entry<String, Integer> e : dic.entrySet()) {
					if (e.getKey().length() > 3 && e.getValue() < 100)
						bw.write(e.getKey() + " " + e.getValue().toString() + ",");
				}

				lecteurAvecBuffer.close();
				bw.close();
				writer.close();

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
