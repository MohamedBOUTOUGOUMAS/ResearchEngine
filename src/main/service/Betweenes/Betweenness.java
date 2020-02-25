package main.service.Betweenes;

import main.service.utils.Book;
import main.service.utils.Helper;
import main.service.utils.ResearchResult;
import main.service.utils.Serialization;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

	public static ArrayList<ResearchResult> sortByBetweenes(List<ResearchResult> searchResult
			, Map<String, Map<String, Double>> jaccard_dists, Map<Integer, Map<Integer, ArrayList<Integer>>> fw_map) {
		System.out.println("sortBetweeness");
		ArrayList<ResearchResult> sortedResult;
		ArrayList<Edge> edges;

		// create all edges in the graph
		HashMap<Integer, ResearchResult> hashedFiles = new HashMap<>();
		for(int i = 0; i < searchResult.size(); i++) {
			hashedFiles.put(i, searchResult.get(i));
		}
		edges = createGraph(hashedFiles, jaccard_dists);

		// compute shortestPaths
		FloydWarshall fw = new FloydWarshall();
		//fw.calculShortestPaths(edges, searchResult.size());
        fw.setMap(fw_map);

		// sort files by their betweenes
		ArrayList<Integer> tmp = new ArrayList<>(hashedFiles.keySet());
		tmp.stream().map(i -> new AbstractMap.SimpleEntry<>(i, computeBeetweenes(i, edges, fw, searchResult.size())))
                .sorted(Comparator.comparingDouble(Map.Entry::getValue));

		sortedResult = (ArrayList<ResearchResult>) tmp.stream()
				.map(hashedFiles::get)
				.collect(Collectors.toList());

		return sortedResult;
	}

	private static ArrayList<Edge> createGraph(Map<Integer, ResearchResult> searchResult
			, Map<String, Map<String, Double>> jaccard_dists) {
		ArrayList<Edge> edges = new ArrayList<>();
		String file;
		Map<String, Integer> mapResultId = searchResult.keySet().stream()
				.collect(Collectors.toMap(key -> searchResult.get(key).book.fileName, Integer::intValue));

		for (Integer i : searchResult.keySet()) {
			file = searchResult.get(i).book.fileName;
			edges.addAll(jaccard_dists.get(file).entrySet().parallelStream()
					.filter(mapResultId::containsKey)
					.map(entry -> new Edge(i, mapResultId.get(entry.getKey()), entry.getValue()))
					.collect(Collectors.toList()));
		}
		return edges;
	}

	public static float computeBeetweenes(Integer v, ArrayList<Edge> edges, FloydWarshall fw, Integer nbPoints) {
		//int [][] paths = fw.calculShortestPaths(edges, nbPoints);
		float res = 0;
		int cpt;
		for(int i=0; i<nbPoints; i++) {
			if(i==v) continue;
			for(int j=i+1; j<nbPoints; j++) {
				if(j==v) continue;
				cpt = 0;
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

	public static Map<String, Map<String, Double>> getAllDistances() {
		Map<String, Map<String, Double>> result = new HashMap<>();
		Map<String, Double> file_dists;
		ArrayList<String> files = Helper.readBooks(Helper.JACCARD_PATH);
		FileReader fr;
		BufferedReader br;
		try {
			for (String file : files) {
				fr = new FileReader(Helper.JACCARD_PATH+"/"+file);
				br = new BufferedReader(fr);

				file_dists = br.lines()
						.map(line -> line.split(" ", 2))
						.map(array -> new AbstractMap.SimpleEntry<>(array[0], Double.parseDouble(array[1])))
						.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

				result.put(file, file_dists);

				br.close();
				fr.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void getFloydWarshallForAllBooks() {
		ArrayList<String> files = Helper.readBooks(Helper.BOOKS_PATH);
		Map<Integer, ResearchResult> results = IntStream.range(0, files.size())
				.mapToObj(i -> new AbstractMap.SimpleEntry<>(i, new ResearchResult(new Book(files.get(i)),1)))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		Map<String, Map<String, Double>> jaccard_dist =
				(Map<String, Map<String, Double>>) Serialization.deserialize("jaccard-map", "map");
		ArrayList<Edge> edges = createGraph(results, jaccard_dist);

		FloydWarshall fw = new FloydWarshall();
		fw.calculShortestPaths(edges, files.size());
		Serialization.serialize("floyd-warshall", "map", fw.getMap());
	}

	public static void getJaccardMap() {
		Map<String, Map<String, Double>> res = getAllDistances();
		Serialization.serialize("jaccard-map", "map", res);
	}


	public static void main(String[] args) {
		getJaccardMap();
		getFloydWarshallForAllBooks();
		return;
	}

}
