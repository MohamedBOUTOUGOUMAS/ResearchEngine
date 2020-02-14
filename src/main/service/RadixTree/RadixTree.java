package main.service.RadixTree;

import main.service.KMP.KMP;
import main.service.utils.Book;
import main.service.utils.Helper;
import main.service.utils.Position;

import java.io.*;
import java.util.*;

public class RadixTree {
	private char root;
	private ArrayList<Position> positions;
	private boolean isWord;
	private ArrayList<RadixTree> children;

	public RadixTree() {
		children = new ArrayList<RadixTree>();
		isWord = false;
		positions = new ArrayList<Position>();
	}

	public RadixTree childIfPresent(char c) {
		for (RadixTree r : this.children) {
			if (r.root == c)
				return r;
		}
		return null;
	}

	public void insertWord(String word, int i, Position pos) {
		if (i < word.length()) {
			RadixTree child = this.childIfPresent(word.charAt(i));
			if (child == null) {
				child = new RadixTree();
				child.root = word.charAt(i);
				this.children.add(child);
			}
			if (i == word.length() - 1) {
				child.positions.add(pos);
				child.isWord = true;
			} else {
				i += 1;
				child.insertWord(word, i, pos);
			}
		}
	}

	public ArrayList<Position> matchAll() {
		ArrayList<Position> positions = new ArrayList<Position>();
		for (RadixTree child : this.children) {
			positions.addAll(child.matchAll());
			positions.addAll(child.positions);
		}
		return positions;
	}

	public ArrayList<Position> isPresent(String word) {
		RadixTree current = this;
		ArrayList<Position> positions = new ArrayList<Position>();
		char c;
		int i = 0;
		while (i < word.length()) {
			c = word.charAt(i);
			current = current.childIfPresent(c);
			if (current == null || current.root != c)
				return null;
			i++;
		}
		positions.addAll(current.matchAll());
		positions.addAll(current.positions);

		for (Position p : positions) {
			p.endPos = p.initPos + word.length();
		}
		return positions;
	}

	public void printer() {
		System.out.println("Root : " + root);
		System.out.println("I have " + children.size() + " children, position : " + positions + ", isWord : " + isWord);
		for (RadixTree r : children)
			r.printer();
	}

	public void makeTree(String p1, Book book) {
		BufferedReader lecteurAvecBuffer;
		try {
			lecteurAvecBuffer = new BufferedReader(new FileReader(p1));
			String ligne;
			int l = 1;
			boolean findTitle = false;
			boolean findAuthor = false;
			boolean findReleaseDate = false;
			while ((ligne = lecteurAvecBuffer.readLine()) != null) {
				if (!findTitle) findTitle = Helper.decorateBookWithTitle(ligne, book, findTitle);
				if (!findAuthor) findAuthor = Helper.decorateBookWithAuthor(ligne, book, findAuthor);
				if (!findReleaseDate) findReleaseDate = Helper.decorateBookWithReleaseDate(ligne, book, findReleaseDate);
				if (!ligne.equals("") && !ligne.equals("\n")) {
					String[] array = ligne.split("[^a-zA-Z]");
					if (array.length != 0) {
						for (int i = 0; i < array.length; i++) {
							String word = array[i];
							if (!word.equals("") && !word.equals("\n")) {
								int[] retenue = {}; //= KMP.calculRetenue(word.toCharArray());
								//int index = KMP.match_Fast(word.toCharArray(), ligne.toCharArray());
								int index = KMP.match(word.toCharArray(), retenue, ligne.toCharArray());
								Position pos = new Position(ligne, l, index);
								this.insertWord(word, 0, pos);
							}
						}
					}
				}
				l++;
			}

			lecteurAvecBuffer.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}