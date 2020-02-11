package main.service.Betweenes;

public class Edge {
	public int p;
	public int q;
	public double dist;

	public Edge(int p, int q) {
		this.p = p;
		this.q = q;
	}

	public double dist() {
		return dist;
	}
	
	public String toString() {
		return "{"+p+" "+q+"}";
	}
	
}