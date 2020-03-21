package service.Betweenes;

import service.utils.Helper;
import service.utils.Serialization;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Betweenness {
	
	/*public static Integer nbPoints = Integer.MIN_VALUE;

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
			, Map<String, Map<String, Double>> jaccard_dists, Map<Integer, Map<Integer, ArrayList<Integer>>> fw_map
			, Map<String, Integer> fw_indexes) {
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
		tmp.stream().map(i -> new AbstractMap.SimpleEntry<>(i,
				computeBeetweenes(fw_indexes.get(hashedFiles.get(i).book.fileName), edges, fw, searchResult.size())))
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
			edges.addAll(jaccard_dists.get(file).entrySet().stream()
					.filter(entry -> mapResultId.containsKey(entry.getKey()))
					.map(entry -> new Edge(i, mapResultId.get(entry.getKey()), entry.getValue()))
					.filter(edge -> edge.dist() > 0.7)
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
		Map<String, Integer> indexes = results.entrySet().stream()
				.collect(Collectors.toMap(e -> e.getValue().book.fileName, Map.Entry::getKey));
		Serialization.serialize("floyd-warshall","indexes", indexes);
	}

	public static void getJaccardMap() {
		Map<String, Map<String, Double>> res = getAllDistances();
		Serialization.serialize("jaccard-map", "map", res);
	}


	public static void main(String[] args) {
		getJaccardMap();
		getFloydWarshallForAllBooks();
		return;
	}*/

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

    private static ArrayList<Edge> createGraph(Map<Integer, String> books
            , Map<String, Map<String, Double>> jaccard_dists) {
        ArrayList<Edge> edges = new ArrayList<>();
        String file;
        Map<String, Integer> mapResultId = books.keySet().stream()
                .collect(Collectors.toMap(books::get, Integer::intValue));

        for (Integer i : books.keySet()) {
            file = books.get(i);
            edges.addAll(jaccard_dists.get(file).entrySet().stream()
                    .filter(e -> mapResultId.containsKey(e.getKey()))
                    .map(entry -> new Edge(i, mapResultId.get(entry.getKey()), entry.getValue()))
                    .filter(edge -> edge.dist() > 0.9)
                    .collect(Collectors.toList()));
        }
        return edges;
    }

    public static Map<String, Float> betweennesMap(Map<String, Map<String, Double>> jaccard_dists,
                                                   Map<Integer, Map<Integer, ArrayList<Integer>>> floydWarshall_map,
                                                   Map<String, Integer> fw_indexes, ArrayList<String> books) {
        ArrayList<Edge> edges;

        // create all edges in the graph
        HashMap<Integer, String> hashedFiles = new HashMap<>();
        for(int i = 0; i < books.size(); i++) {
            hashedFiles.put(i, books.get(i));
        }
        edges = createGraph(hashedFiles, jaccard_dists);

        // compute shortestPaths
        FloydWarshall fw = new FloydWarshall();
        //fw.calculShortestPaths(edges, searchResult.size());
        fw.setMap(floydWarshall_map);

        // sort files by their betweenes
        ArrayList<String> tmp = new ArrayList<>(hashedFiles.values());
        Map<String, Float> res = tmp.stream().map(i -> new AbstractMap.SimpleEntry<>(i,
                computeBeetweenes(fw_indexes.get(i), edges, fw, books.size())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return res;
    }

    public static Map<String, Map<String, Double>> getJaccardMap() {
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

    public static Map<String, ArrayList<String>> suggestionMap(Map<String, Map<String, Double>> jaccard_dists,
                                                                  Map<String, Float> betweennes) {
        Map<String, ArrayList<String>> results = new HashMap<>();
        ArrayList<String> suggestions;

        for (String filename : jaccard_dists.keySet()) {
            suggestions = (ArrayList<String>) jaccard_dists.get(filename).keySet().parallelStream()
                    .sorted(Comparator.comparing(s -> jaccard_dists.get(filename).get(s)).reversed())
                    .limit(3)
                    .collect(Collectors.toList());
			results.put(filename, suggestions);
        }

        return results;
    }

    public static Map.Entry<Map<Integer, Map<Integer, ArrayList<Integer>>>, Map<String, Integer>>
    generateFloydWarshallAndIndexes(Map<String, Map<String, Double>> jaccard_dist, ArrayList<String> files) {
        Map<Integer, String> results = IntStream.range(0, files.size())
                .mapToObj(i -> new AbstractMap.SimpleEntry<>(i, files.get(i)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        ArrayList<Edge> edges = createGraph(results, jaccard_dist);

        /* Floyd-warshall serialization */
        FloydWarshall fw = new FloydWarshall();
        fw.calculShortestPaths(edges, files.size());

        /* Indexes serialization */
        Map<String, Integer> indexes = results.entrySet().stream()
                .collect(Collectors.toMap(e -> e.getValue(), Map.Entry::getKey));

        return new AbstractMap.SimpleEntry<>(fw.getMap(), indexes);
    }

    public static void main(String[] args) {
        ArrayList<String> files = Helper.readBooks(Helper.BOOKS_PATH);

        /* Jaccard */
        Map<String, Map<String, Double>> jaccard_dists = getJaccardMap();
        Serialization.serialize("jaccard-map", "map", jaccard_dists);
        System.out.println("End Jaccard");

        /* Floyd Warshall */
        Map.Entry<Map<Integer, Map<Integer, ArrayList<Integer>>>, Map<String, Integer>> res =
                generateFloydWarshallAndIndexes(jaccard_dists, files);
        System.out.println("End Entry FW");

        Map<Integer, Map<Integer, ArrayList<Integer>>> floydWarshall_map = res.getKey();
        Serialization.serialize("floyd-warshall", "map", floydWarshall_map);
        System.out.println("End keyFW");

        Map<String, Integer> fw_indexes = res.getValue();
        Serialization.serialize("floyd-warshall","indexes", fw_indexes);
        System.out.println("End indexFW");

        /* Betweennes */
        Map<String, Float> betweennes = betweennesMap(jaccard_dists, floydWarshall_map, fw_indexes, files);
        Serialization.serialize("betweennes-map", "map", betweennes);
        System.out.println("End betw");

        /* Suggestions */
        Map<String, ArrayList<String>> suggestions = suggestionMap(jaccard_dists, betweennes);
		Serialization.serialize("suggestions-map", "map", suggestions);
        System.out.println("End sugg");

        return;
    }
}
