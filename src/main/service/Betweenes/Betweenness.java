package main.service.Betweenes;

import main.service.utils.Book;
import main.service.utils.Helper;
import main.service.utils.ResearchResult;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static main.service.Betweenes.Jaccard.getDistance;

public class Betweenness {
	
	public static Integer nbPoints = Integer.MIN_VALUE;
	public static Map<String, Map<String, Double>> allDistances = getAllDistances();

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
		/*for (Integer i : tmp) {
			System.out.println(i + " " +computeBeetweenes(i, edges, fw, searchResult.size()));
		}*/
		sortedResult = (ArrayList<ResearchResult>) tmp.stream()
				.map(hashedFiles::get)
				.collect(Collectors.toList());

		return sortedResult;
	}

	private static ArrayList<Edge> createGraph(Map<Integer, ResearchResult> searchResult) {
		ArrayList<Edge> edges = new ArrayList<>();
		String file;
		Map<String, Integer> mapResultId = searchResult.keySet().stream()
				.collect(Collectors.toMap(key -> searchResult.get(key).book.fileName, Integer::intValue));

		for (Integer i : searchResult.keySet()) {
			file = searchResult.get(i).book.fileName;
			System.out.println("file : "+file);
			edges.addAll(allDistances.get(file).entrySet().parallelStream()
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
		System.out.println("start compute distances");
		try {
			for (String file : files) {
				fr = new FileReader(Helper.JACCARD_PATH+"/"+file);
				br = new BufferedReader(fr);
				//System.out.println(file);

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
		System.out.println("end compute distances");

		return result;
	}

	public static void main(String[] args) throws Exception {
		//storeJaccardDistances("distanceJaccard.txt");
		//Map<String, Map<String, Double>> res = getAllDistances();

		Map<Integer, ResearchResult> map = new HashMap<>();
		Book book1 = new Book("36-0.txt");
		ResearchResult res1 = new ResearchResult(book1, 1);
		Book book2 = new Book("61.txt.utf-8");
		ResearchResult res2 = new ResearchResult(book2, 1);
		map.put(0, res1);
		map.put(1, res2);
		ArrayList<Edge> l = createGraph(map);
		return;
	}

}
