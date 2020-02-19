package main.service.utils;

import java.io.Serializable;
import java.util.Comparator;

public class Position implements Comparator<Position>, Serializable {
	public int nbLine;
	public int initPos;
	public int endPos;
	public String word;

	public Position(int nbLine, int initPos) {
		this.nbLine = nbLine;
		this.initPos = initPos;
	}

	public Position(int nbLine, int initPos, int endPos) {
		this.nbLine = nbLine;
		this.initPos = initPos;
		this.endPos = endPos;
	}

	public Position(int nbLine, int initPos, int endPos, String word) {
		this.nbLine = nbLine;
		this.initPos = initPos;
		this.endPos = endPos;
		this.word = word;
	}

	public Position() {

	}

	@Override
	public int compare(Position p1, Position p2) {
		if (p1.nbLine == p2.nbLine) {
			return p1.word.length() - p2.word.length();
		}
		return (int) (((Position) p1).nbLine - ((Position) p2).nbLine);
	}
}