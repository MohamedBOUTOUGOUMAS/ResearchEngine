package main.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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

	public static ResearchResult matchAllWords(String word, String fileName, RegEx regEx){
		String filePath = Helper.BOOKS_PATH+"/"+fileName;
		ArrayList<Position> linesMatched = new ArrayList<>();
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
		Map<String, ArrayList<Position>> index = Helper.getMapFromIndex(fileName);
		System.out.println(index);
		for (Map.Entry<String, ArrayList<Position>> lineFromIndex: index.entrySet()) {
			ArrayList<Position> p;
			if (regEx != null)
				p = matchLineWithAutomat(lineFromIndex.getKey(), lineFromIndex.getValue(), regEx);
			else
				p = matchLineWithKMP(word, lineFromIndex.getKey(), lineFromIndex.getValue());

			if (p.size() > 0) {
				linesMatched.addAll(p);
			}
		}

/*
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


				l++;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}*/
		return new ResearchResult(book, linesMatched);
	}

	public static ArrayList<Position> matchLineWithAutomat(String word, ArrayList<Position> positions, RegEx regEx) {
		return regEx.aef.matchAll(word, positions);
	}

	public static ArrayList<Position> matchLineWithKMP(String pattern, String word, ArrayList<Position> positions) {
		return KMP.matchAll(pattern.toCharArray(), word, positions);
	}
}
