package main.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import main.service.AEF.RegEx;
import main.service.KMP.KMP;
import main.service.utils.Book;
import main.service.utils.Helper;
import main.service.utils.Position;
import main.service.utils.ResearchResult;


public class Egrep {

	public static ResearchResult matchAllWords(String word, String fileName, RegEx regEx){
		//String filePath = Helper.BOOKS_PATH+"/"+fileName;
		String filePath = Helper.TEST_PATH+"/"+fileName;
		int linesMatched = 0;
		/*String[] array = word.split("[^a-zA-Z]");
		if (array.length == 1) {
			RadixTree rt = new RadixTree();
			rt.makeTree(fileName);
			ArrayList<Position> pos = rt.isPresent(word);
			if (pos != null && pos.size() > 0) {
				linesMatched.addAll(pos);
				return new ResearchResult(Book.getEmptyBook(fileName), linesMatched);
			}
		}*/

		Book book = new Book(fileName);
		BufferedReader lecteurAvecBuffer;
		try {
			lecteurAvecBuffer = new BufferedReader(new FileReader(filePath));
			String ligne;
			boolean findTitle = false;
			boolean findAuthor = false;
			boolean findReleaseDate = false;
			int l = 1;
			while ((ligne = lecteurAvecBuffer.readLine()) != null) {

				if (!findTitle) findTitle = Helper.decorateBookWithTitle(ligne, book, findTitle);
				if (!findAuthor) findAuthor = Helper.decorateBookWithAuthor(ligne, book, findAuthor);
				if (!findReleaseDate) findReleaseDate = Helper.decorateBookWithReleaseDate(ligne, book, findReleaseDate);

				if (regEx != null)
					linesMatched += matchLineWithAutomat(ligne, l, regEx);
				else
					linesMatched += matchLineWithKMP(word, ligne, l);

				l++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ResearchResult(book, linesMatched);
	}

	public static int matchLineWithAutomat(String line, int nbLine, RegEx regEx) {
		return regEx.aef.matchAll(line, nbLine);
	}

	public static int matchLineWithKMP(String word, String line, int nbLine) {
		return KMP.matchAll(word.toCharArray(), line.toCharArray(), nbLine);
	}
}
