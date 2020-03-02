package main.java.service.AEF;

import java.util.ArrayList;

public class RegExTree {
	protected int root;
	protected ArrayList<RegExTree> subTrees;

	public RegExTree(int root, ArrayList<RegExTree> subTrees) {
		this.root = root;
		this.subTrees = subTrees;
	}

	public Automat makeAuto() {
		if (root == RegEx.CONCAT) {
			Automat au1 = this.subTrees.get(0).makeAuto();
			Automat au2 = this.subTrees.get(1).makeAuto();
			return new Automat(root, au1, au2);

		} else if (root == RegEx.ETOILE) {
			Automat a = this.subTrees.get(0).makeAuto();
			return new Automat(root, a, null);

		} else if (root == RegEx.ALTERN) {

			Automat au1 = this.subTrees.get(0).makeAuto();
			Automat au2 = this.subTrees.get(1).makeAuto();
			return new Automat(root, au1, au2);

		} else if (root == RegEx.PLUS) {

			Automat etoile = new Automat(RegEx.ETOILE, this.subTrees.get(0).makeAuto(), null);
			Automat concat = new Automat(RegEx.CONCAT, this.subTrees.get(0).makeAuto(), etoile);
			return concat;

		} else {
			return new Automat(root, null, null);
		}
	}

	// FROM TREE TO PARENTHESIS
	public String toString() {
		if (subTrees.isEmpty())
			return rootToString();
		String result = rootToString() + "(" + subTrees.get(0).toString();
		for (int i = 1; i < subTrees.size(); i++)
			result += "," + subTrees.get(i).toString();
		return result + ")";
	}

	private String rootToString() {
		if (root == RegEx.CONCAT)
			return ".";
		if (root == RegEx.ETOILE)
			return "*";
		if (root == RegEx.ALTERN)
			return "|";
		if (root == RegEx.DOT)
			return ".";
		return Character.toString((char) root);
	}
}

