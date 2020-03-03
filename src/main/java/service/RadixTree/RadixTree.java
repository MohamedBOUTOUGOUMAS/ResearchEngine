package service.RadixTree;

import service.utils.Helper;
import service.utils.Position;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Map;

public class RadixTree {
    private char root;
    private ArrayList<Position> positions;
    private boolean isWord;
    private ArrayList<RadixTree> children;

    public RadixTree() {
        children = new ArrayList<>();
        isWord = false;
        positions = new ArrayList<>();
    }

    public RadixTree childIfPresent(char c) {
        for (RadixTree r : this.children) {
            if (r.root == c)
                return r;
        }
        return null;
    }

    public void insertWord(String word, int i, ArrayList<Position> pos) {
        if (i < word.length()) {
            RadixTree child = this.childIfPresent(word.charAt(i));
            if (child == null) {
                child = new RadixTree();
                child.root = word.charAt(i);
                this.children.add(child);
            }
            if (i == word.length() - 1) {
                child.positions.addAll(pos);
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

		/*for (Position p : positions) {
			p.endPos = p.initPos + word.length();
		}*/
        return positions;
    }

    public void printer() {
        System.out.println("Root : " + root);
        System.out.println("I have " + children.size() + " children, position : " + positions + ", isWord : " + isWord);
        for (RadixTree r : children)
            r.printer();
    }

    public void makeTree(String fileName) {
        try {
            FileInputStream fs = new FileInputStream(Helper.INDEXES_PATH + "/" + fileName);
            ObjectInputStream ois = new ObjectInputStream(fs);
            Map<String, ArrayList<Position>> dic = (Map<String, ArrayList<Position>>) ois.readObject();
            for (Map.Entry<String, ArrayList<Position>> e : dic.entrySet()) {
                this.insertWord(e.getKey(), 0, e.getValue());
            }
            ois.close();
            fs.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}