package main.service;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;

import main.service.AEF.RegEx;
import main.service.KMP.KMP;
import main.service.RadixTree.RadixTree;
import main.service.utils.Book;
import main.service.utils.Helper;
import main.service.utils.Position;
import main.service.utils.ResearchResult;


public class Egrep {

	public static ResearchResult matchAllWords(String word, String fileName, int firstLetter, RegEx regEx, int[] retenue){
		int nbMatched = 0;
		/*String[] array = word.split("[^a-zA-Z]");
		if (array.length == 1) {
			RadixTree rt = RadixTree.makeTree(fileName);
			if (rt != null){
				ArrayList<Position> pos = rt.isPresent(word);
				if (pos != null && pos.size() > 0) {
					linesMatched.addAll(pos);
					System.out.println("RadixTree");
					return new ResearchResult(Book.getEmptyBook(fileName), linesMatched);
				}
			}

		}*/
		Book book = Book.getEmptyBook(fileName);
		BufferedReader lecteurAvecBuffer;
		String ligne;
		try {
			lecteurAvecBuffer = new BufferedReader(new FileReader(Helper.INDEXES_TABLES_PATH+"/"+fileName+"/"+firstLetter+".txt"));
			while ((ligne = lecteurAvecBuffer.readLine()) != null) {
				if (!ligne.equals("") && !ligne.equals("\n")) {
					String [] ar = ligne.split(" ");
					if (ar.length != 0) {
						String toMatch = ar[0];
						int nbOccurs = Integer.parseInt(ar[1]);
						if (regEx != null){
							int nbWords = matchLineWithAutomat(regEx, toMatch, nbOccurs);
							if (nbMatched != 0 && nbWords == 0) {
								System.out.println(fileName);
								System.out.println("AEF");
								break;
							}
							nbMatched += nbWords;
						}
						else {
							int nbWords = matchLineWithKMP(word, retenue, toMatch, nbOccurs);
							if (nbMatched != 0 && nbWords == 0) {
								System.out.println(fileName);
								System.out.println("KMP");
								break;
							}
							nbMatched += nbWords;
						}
					}
				}
			}
			lecteurAvecBuffer.close();

		} catch (IOException exc) {
			exc.printStackTrace();
		}

		return new ResearchResult(book, nbMatched);
	}

	public static int matchLineWithAutomat(RegEx regEx, String word, int nbOccurs) {
		return regEx.aef.matchAll(word, nbOccurs);
	}

	public static int matchLineWithKMP(String pattern, int[] retenue, String word, int nbOccurs) {
		return KMP.matchAll(pattern.toCharArray(), retenue, word, nbOccurs);
	}
}
