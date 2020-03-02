package main.java.service.AEF;

public class Tuple {

	String etat;
	int alpha;

	public Tuple(String e, int a) {
		this.alpha = a;
		this.etat = e;
	}

	@Override
	public String toString() {
		return etat + " " + alpha;
	}
}
