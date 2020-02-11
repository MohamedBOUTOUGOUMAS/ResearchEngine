package main.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import main.service.AEF.RegEx;
import main.service.KMP.KMP;
import main.service.RadixTree.RadixTree;
import main.service.utils.Book;
import main.service.utils.Helper;
import main.service.utils.Position;
import main.service.utils.ResearchResult;


public class Egrep {

	public static ResearchResult matchAllWords(String word, String filePath){
		ResearchResult researchResult;
		ArrayList<Position> linesMatched = new ArrayList<>();
		Book book = new Book(filePath);
		String[] array = word.split("[^a-zA-Z]");
		if (array.length == 1) {
			RadixTree rt = new RadixTree();
			rt.makeTree(filePath, book);
			ArrayList<Position> pos = rt.isPresent(word);
			if (pos != null && pos.size() > 0) {
				linesMatched.addAll(pos);
				return new ResearchResult(book, linesMatched);
			}
		}

		BufferedReader lecteurAvecBuffer;
		try {
			lecteurAvecBuffer = new BufferedReader(new FileReader(filePath));
			String ligne;
			boolean findTitle = false;
			boolean findAuthor = false;
			boolean findReleaseDate = false;
			while ((ligne = lecteurAvecBuffer.readLine()) != null) {
				findTitle = Helper.decorateBookWithTitle(ligne, book, findTitle);
				findAuthor = Helper.decorateBookWithAuthor(ligne, book, findAuthor);
				findReleaseDate = Helper.decorateBookWithReleaseDate(ligne, book, findReleaseDate);
				ArrayList<Position> p = matchLine(word, ligne);
				if (p.size() > 0) linesMatched.addAll(p);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ResearchResult(book, linesMatched);
	}

	public static ArrayList<Position> matchLine(String word, String line) {
		if (word.contains("(") || word.contains("+") || word.contains("*") || word.contains(".")) {
			//System.out.println("AEF : ");
			RegEx regEx = new RegEx(word);
			return regEx.aef.matchAll(line, 0);
		} else {
				//System.out.println("KMP : ");
			return KMP.matchAll(word.toCharArray(), KMP.calculRetenue(word.toCharArray()), line.toCharArray());
		}
	}
}
