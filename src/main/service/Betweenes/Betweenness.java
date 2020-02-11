package main.service.Betweenes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Betweenness {
	
	public static Integer nbPoints = Integer.MIN_VALUE;

	public static ArrayList<Edge> read(String path) {
		BufferedReader lecteurAvecBuffer = null;
		String ligne;
		ArrayList<Edge> edges = new ArrayList<>();
		
		try {
			lecteurAvecBuffer = new BufferedReader(new FileReader(path));

			while ((ligne = lecteurAvecBuffer.readLine()) != null) {
				String[] aretes = new String[2];
				aretes = ligne.split("\\s+", 2);
				try {
					Integer a = Integer.parseInt(aretes[0]);
					Integer b = Integer.parseInt(aretes[1]);
					if(a > nbPoints) nbPoints = a;
					if(b > nbPoints) nbPoints = b;
					
					edges.add(new Edge(a, b));
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}

			lecteurAvecBuffer.close();

			return edges;
		} catch (IOException exc) {
			System.out.println("Erreur d'ouverture");
		}
		return edges;

	}

	public static float computeBeetweenes(Integer v, ArrayList<Edge> edges, FloydWarshall fw) {
		int [][] paths = fw.calculShortestPaths(edges, nbPoints);
		float res = 0;
		for(int i=0; i<nbPoints+1; i++) {
			if(i==v) continue;
			for(int j=0; j<nbPoints+1; j++) {
				if(j==v || j==i) continue;
				int cpt = 0;
				ArrayList<ArrayList<Integer>> shortestPaths = fw.pathFinderMap(i,j);
				for (ArrayList<Integer> path : shortestPaths) {
					if(path.contains(v)) cpt++;
				}
				if(shortestPaths.size() > 0) {
					res+=((float) cpt/shortestPaths.size());
				}
			}
		}
		return res;
	}

	public static void main(String[] args) {
		ArrayList<Edge> edges = read("TestsBeds/2025.nodup");
		System.out.println(nbPoints);
		FloydWarshall fw = new FloydWarshall();
		int [][] paths = fw.calculShortestPaths(edges, nbPoints); 
		/*for (int i = 0; i < nbPoints+1; i++) {
			for (int j = 0; j < nbPoints+1; j++) {
				if(i==j) continue;
				System.out.println(i+" "+j);
				System.out.println(fw.pathFinderMap(i, j));
			}
		}*/
		System.out.println("nb : "+computeBeetweenes(16, edges, fw));
	}

}
