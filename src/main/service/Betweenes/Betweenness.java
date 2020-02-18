package main.service.Betweenes;

import main.service.utils.Helper;
import main.service.utils.ResearchResult;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static main.service.Betweenes.Jaccard.getDistance;

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

	public static float computeBeetweenes(Integer v, ArrayList<Edge> edges, FloydWarshall fw, Integer nbPoints) {
		//int [][] paths = fw.calculShortestPaths(edges, nbPoints);
		float res = 0;
		for(int i=0; i<nbPoints; i++) {
			if(i==v) continue;
			for(int j=0; j<nbPoints; j++) {
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

	public static ArrayList<ResearchResult> sortByBetweenes(List<ResearchResult> searchResult) {
		System.out.println("sortBetweeness");
		ArrayList<ResearchResult> sortedResult;
		ArrayList<Edge> edges;

		// create all edges in the graph
		HashMap<Integer, ResearchResult> hashedFiles = new HashMap<>();
		for(int i = 0; i < searchResult.size(); i++) {
			hashedFiles.put(i, searchResult.get(i));
		}
		System.out.println("BeforeCreatingGraph");
		edges = createGraph(hashedFiles);
		System.out.println("End reading files");
		// compute shortestPaths
		FloydWarshall fw = new FloydWarshall();
		fw.calculShortestPaths(edges, searchResult.size());
		System.out.println("End floydWarshall");
		for (int i = 0; i < searchResult.size(); i++) {
			for (int j = 0; j < searchResult.size(); j++) {
				if(i==j) continue;
				System.out.println(i+" "+j);
				System.out.println(fw.pathFinderMap(i, j));
			}
		}

		// sort files by their betweenes
		ArrayList<Integer> tmp = new ArrayList<>(hashedFiles.keySet());
		Collections.sort(tmp, ((i1, i2) ->  {
			float c1 = computeBeetweenes(i1, edges, fw, searchResult.size());
			float c2 = computeBeetweenes(i2, edges, fw, searchResult.size());
			if(c1 > c2) {
				return -1;
			} else if(c1 < c2) {
				return 1;
			} else {
				return 0;
			}
		}));
		for (Integer i : tmp) {
			System.out.println(i + " " +computeBeetweenes(i, edges, fw, searchResult.size()));
		}
		sortedResult = (ArrayList<ResearchResult>) tmp.stream()
				.map(f -> hashedFiles.get(f))
				.collect(Collectors.toList());

		return sortedResult;
	}

	private static ArrayList<Edge> createGraph(HashMap<Integer, ResearchResult> searchResult) {
		ArrayList<Edge> edges = new ArrayList<>();
		double dist;
		for (int i = 0; i < searchResult.keySet().size(); i++) {
			for (int j = i+1; j < searchResult.keySet().size(); j++) {
				dist = getDistance(searchResult.get(i).book.fileName, searchResult.get(j).book.fileName);
				if(dist > 0.25) {
					edges.add(new Edge(i, j, dist));
				}
			}
		}
		return edges;
	}

	private static void storeJaccardDistances(String filename) {
		try {
			FileWriter writer = new FileWriter(filename);
			BufferedWriter bw = new BufferedWriter(writer);
			ArrayList<String> books = Helper.readBooks(Helper.BOOKS_PATH);
			double dist;
			for (int i = 0; i < books.size(); i++) {
				String book1 = books.get(i);
				for (int j = i+1; j < books.size(); j++) {
					System.out.println(i+" "+j);
					String book2 = books.get(j);
					dist = getDistance(book1, book2);
					bw.write(book1+" "+book2+" "+dist+"\n");
				}
			}
			bw.close();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws Exception {
		storeJaccardDistances("distanceJaccard.txt");
	}

}
