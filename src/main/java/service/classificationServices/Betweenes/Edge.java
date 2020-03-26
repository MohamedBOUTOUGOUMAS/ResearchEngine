package service.classificationServices.Betweenes;

public class Edge {
    public int p;
    public int q;
    public double dist;

    public Edge(int p, int q) {
        this.p = p;
        this.q = q;
    }

    public Edge(int p, int q, double dist) {
        this.p = p;
        this.q = q;
        this.dist = dist;
    }

    public double dist() {
        return dist;
    }

    public String toString() {
        return "{" + p + " " + q + "}";
    }

}