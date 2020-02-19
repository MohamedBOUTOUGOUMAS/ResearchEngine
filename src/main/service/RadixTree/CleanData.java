package main.service.RadixTree;

import main.service.utils.Helper;
import java.io.*;
import java.util.ArrayList;

public class CleanData {

	public static void main(String[] args) {
		BufferedReader lecteurAvecBuffer = null;
		String ligne;
		ArrayList<String> files = Helper.readBooks(Helper.BOOKS_PATH);
		for (int f=0; f<files.size(); f++){
			String book = files.get(f);
			System.out.println(book);

			try {
				FileWriter writer = new FileWriter(Helper.INDEXES_PATH+"/"+book);
				BufferedWriter bw = new BufferedWriter(writer);
				lecteurAvecBuffer = new BufferedReader(new FileReader(Helper.BOOKS_PATH+"/"+book));

				while ((ligne = lecteurAvecBuffer.readLine()) != null) {
					ligne = Helper.cleanText(ligne);
					if (!ligne.equals("") && !ligne.equals("\n")) {
						String[] array = ligne.split("[^a-zA-Z]");
						if (array.length != 0) {
							for (int i = 0; i < array.length; i++) {
								if (!array[i].equals("") && !array[i].equals("\n")) {
									bw.write(array[i] + "\n");
								}
							}
						}
					}
				}

				bw.close();
				writer.close();
				lecteurAvecBuffer.close();

			} catch (IOException exc) {
				exc.printStackTrace();
			}// TODO Auto-generated catch block


		}

	}

}
