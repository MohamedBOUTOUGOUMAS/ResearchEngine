package main.service.RadixTree;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CleanData {

	public static void main(String[] args) {
		BufferedReader lecteurAvecBuffer = null;
		String ligne;

		try {
			FileWriter writer = new FileWriter("49345.index");
			BufferedWriter bw = new BufferedWriter(writer);
			lecteurAvecBuffer = new BufferedReader(new FileReader("49345-0.txt"));

			while ((ligne = lecteurAvecBuffer.readLine()) != null) {
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

		} catch (FileNotFoundException exc) {
			exc.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
