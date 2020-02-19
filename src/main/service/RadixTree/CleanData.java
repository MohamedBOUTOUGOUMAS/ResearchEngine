package main.service.RadixTree;

import main.service.utils.Helper;
import main.service.utils.Position;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CleanData {

	static Map<String, ArrayList<Position>> dic = new HashMap<>();

	public static void main(String[] args) {
		BufferedReader lecteurAvecBuffer = null;
		String ligne;
		ArrayList<String> files = Helper.readBooks(Helper.BOOKS_PATH);
		for (int f=0; f<files.size(); f++){
			String book = files.get(f);
			System.out.println(book);
			try {
				FileOutputStream fos = new FileOutputStream(Helper.INDEXES_PATH+"/"+book);
				ObjectOutputStream oos = new ObjectOutputStream(fos);

				lecteurAvecBuffer = new BufferedReader(new FileReader(Helper.BOOKS_PATH+"/"+book));
				int l = 1;
				while ((ligne = lecteurAvecBuffer.readLine()) != null) {
					if (!ligne.equals("") && !ligne.equals("\n")) {
						String[] array = ligne.split("[^a-zA-Z]");
						if (array.length != 0) {
							for (int i = 0; i < array.length; i++) {
								if (!array[i].equals("") && !array[i].equals("\n")) {
									String term = array[i];
									int initPos = ligne.indexOf(array[i]);
									int endPos = initPos+array[i].length();
									Position position = new Position(l, initPos, endPos);
									if (dic.containsKey(term)) {
										ArrayList<Position> positions = dic.get(term);
										positions.add(position);
										dic.put(term, positions);
									} else {
										ArrayList<Position> positions = new ArrayList<>();
										positions.add(position);
										dic.put(ligne, positions);
									}
								}
							}
						}
					}
					l++;
				}
				oos.writeObject(dic);
				dic.clear();
				oos.close();
				fos.close();
				lecteurAvecBuffer.close();

			} catch (IOException exc) {
				exc.printStackTrace();
			}

		}

	}

}
