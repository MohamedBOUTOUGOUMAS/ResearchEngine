package service.Betweenes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FloydWarshall {

    static Double[][] dist;
    static int[][] paths;
    static ArrayList<ArrayList<Integer>>[][] sh_paths;
    static Map<Integer, Map<Integer, ArrayList<ArrayList<Integer>>>> shortestPaths = new HashMap<>();
    static Map<Integer, Map<Integer, ArrayList<Integer>>> nextInPaths = new HashMap<>();

    public static ArrayList<Integer> pathFinder(int u, int v, int[][] paths) {
        ArrayList<Integer> chemin = new ArrayList<>();
        if (paths[u][v] == -1) {
            return chemin;
        }
        chemin.add(u);
        while (u != v) {
            u = paths[u][v];
            chemin.add(u);
        }
        return chemin;
    }

    public Map<Integer, Map<Integer, ArrayList<Integer>>> getMap() {
        return nextInPaths;
    }

    public void setMap(Map<Integer, Map<Integer, ArrayList<Integer>>> nextInPath) {
        nextInPaths = nextInPath;
    }

    public int[][] calculShortestPaths(ArrayList<Edge> edges, int nbPoints) {

        dist = new Double[nbPoints + 1][nbPoints + 1];
        paths = new int[nbPoints + 1][nbPoints + 1];

        for (Edge edge : edges) {
            dist[edge.p][edge.q] = edge.dist();
            paths[edge.p][edge.q] = edge.q;
            dist[edge.q][edge.p] = edge.dist();
            paths[edge.q][edge.p] = edge.p;
        }

        for (int i = 0; i < dist.length; i++) {
            for (int j = 0; j < dist.length; j++) {
                if (i == j) {
                    dist[i][j] = Double.valueOf(0);
                    paths[i][j] = i;
                } else if (dist[i][j] == null) {
                    dist[i][j] = Double.POSITIVE_INFINITY;
                    paths[i][j] = -1;
                }
            }
        }
        for (int k = 0; k < dist.length; k++) {
            for (int i = 0; i < dist.length; i++) {
                if (k == i) continue;
                for (int j = 0; j < dist.length; j++) {
                    if (k == j) continue;
                    if (dist[i][j] > dist[i][k] + dist[k][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                        paths[i][j] = paths[i][k];
                        ArrayList<Integer> p = new ArrayList<Integer>();
                        p.add(paths[i][k]);
                        Map<Integer, ArrayList<Integer>> subMap = new HashMap<>();
                        if (nextInPaths.containsKey(i)) subMap = nextInPaths.get(i);
                        subMap.put(j, p);
                        nextInPaths.put(i, subMap);

                    } else if (dist[i][j] == dist[i][k] + dist[k][j] &&
                            Double.valueOf(dist[i][j]) != Double.POSITIVE_INFINITY &&
                            Double.valueOf(dist[i][j]) != 0) {
                        paths[i][j] = paths[i][k];
                        nextInPaths.get(i).get(j).add(paths[i][k]);
                    }
                }
            }
        }

        return paths;
    }

    public ArrayList<ArrayList<Integer>> pathFinderMap(Integer curr, Integer dest) {
        ArrayList<ArrayList<Integer>> res = new ArrayList<>();

        if (nextInPaths.get(curr) != null) {
            ArrayList<Integer> next = nextInPaths.get(curr).get(dest);
            if (next == null) {
                ArrayList<Integer> r = new ArrayList<>();
                r.add(dest);
                r.add(curr);
                res.add(r);
                return res;
            }
            for (Integer i : next) {
                ArrayList<ArrayList<Integer>> tmp = pathFinderMap(i, dest);
                for (ArrayList<Integer> l : tmp) {
                    l.add(curr);
                }
                res.addAll(tmp);
            }
        }

        return res;
    }

}
