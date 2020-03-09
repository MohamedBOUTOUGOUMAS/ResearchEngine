package controller;

import db.Metadata;
import multiThreading.ThreadPool;
import service.utils.Book;
import service.utils.Helper;
import service.utils.ResearchResult;
import org.springframework.web.bind.annotation.*;
import service.utils.Serialization;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "https://research-engine-client.herokuapp.com")
//@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class HomeController {
    static Set<String> autoComplete = new HashSet<>();
    static List<ResearchResult> results = null;
    static String pattern = null;
    public static Map<String, Float> pageRang =
            (Map<String, Float>) Serialization.deserialize(Helper.PAGE_RANK_MAP, "map");
    /*public static Map<String, Map<String, Double>> jaccard_dists =
            (Map<String, Map<String, Double>>) Serialization.deserialize(Helper.JACCARD_MAP, "map");
    public static Map<Integer, Map<Integer, ArrayList<Integer>>> floydWarshall_map =
            (Map<Integer, Map<Integer, ArrayList<Integer>>>) Serialization.deserialize(Helper.FLOYD_WARSHALL, "map");
    public static Map<String, Integer> fw_indexes =
            (Map<String, Integer>) Serialization.deserialize(Helper.FLOYD_WARSHALL, "indexes");*/

    @RequestMapping("/")
    public String index() {
        return "Welcome";
    }

    @GetMapping("/search")
    public List<ResearchResult> getResearch(@RequestParam String pattern, @RequestParam String fast,  @RequestParam String searchSource,  @RequestParam String classification) {
        System.out.println("fast "+fast+" source "+searchSource+" classification "+classification);
        boolean speedMode = Boolean.parseBoolean(fast);
        List<ResearchResult> fromCache = getFromCache(pattern);
        if (fromCache != null) return fromCache;

        if (autoComplete != null && !autoComplete.contains(pattern)) {
            Metadata.addAutoCompleteSearch(pattern);
            autoComplete.add(pattern);
        }
        if (speedMode) results = ThreadPool.getResultsResearchFast(pattern, searchSource);
        else results = ThreadPool.getResultsResearch(pattern);

        if (classification != null && classification.equals("pgr")){
            results.sort((o1, o2) -> {
                if (o2.pageRank > o1.pageRank) return 1;
                else if (o2.pageRank < o1.pageRank) return -1;
                return 0;
            });
        }else if(classification.equals("btw")){
            /*results.sort((o1, o2) -> {
                if (o2.betweeness > o1.betweeness) return 1;
                else if (o2.betweeness < o1.betweeness) return -1;
                return 0;
            });*/
        }else if(classification.equals("nbOccurs")){
            Collections.sort(results, (o1, o2) -> o2.nbMatched - o1.nbMatched);
        }else if(classification.equals("nbClick")){
            List<String> fileNames = results.stream().map(rr -> rr.book.fileName).collect(Collectors.toList());
            Map<String, Integer> nbClick = Metadata.getNbClickBooks(fileNames);
            results.sort((o1, o2) -> nbClick.get(o2.book.fileName) - nbClick.get(o1.book.fileName));
        }



        // Betweennes
        //results = Betweenness.sortByBetweenes(results, jaccard_dists, floydWarshall_map, fw_indexes);
        return results;
    }

    @PostMapping("/advencedSearch")
    public List<ResearchResult> getAdvencedSearch(@RequestBody String body) {

        String pattern = Helper.getPatternFromJson(body);
        boolean speedMode = Helper.getFastFromJson(body);

        List<ResearchResult> fromCache = getFromCache(pattern);
        if (fromCache != null) return fromCache;

        if (autoComplete != null && !autoComplete.contains(pattern)){
            Metadata.addAutoCompleteSearch(pattern);
            autoComplete.add(pattern);
        }

        if (speedMode) results = ThreadPool.getResultsResearchFast(pattern, null);
        else results = ThreadPool.getResultsResearch(pattern);

        Collections.sort(results, (o1, o2) -> o2.nbMatched - o1.nbMatched);

        /*results.sort((o1, o2) -> {
            if (o2.pageRank > o1.pageRank) return 1;
            else if (o2.pageRank < o1.pageRank) return -1;
            return 0;
        });*/

        //results = Betweenness.sortByBetweenes(results, jaccard_dists, floydWarshall_map, fw_indexes);

        /*List<String> fileNames = results.stream().map(rr -> rr.book.fileName).collect(Collectors.toList());
        Map<String, Integer> nbClick = Metadata.getNbClickBooks(fileNames);
        results.sort((o1, o2) -> nbClick.get(o2.book.fileName) - nbClick.get(o1.book.fileName));*/
        return results;
    }

    @GetMapping("/search/book")
    public Book getBook(@RequestParam String fileName) {
        Metadata.addClick(fileName);
        System.out.println(Metadata.getNbClickBooks());
        return Book.getBook(fileName);
    }

    @GetMapping("/autoComplete")
    public Set<String> getAutoComplete() {
        if (autoComplete == null) autoComplete = Metadata.getAutoComplete();
        System.out.println("auto " + autoComplete);
        return autoComplete;
    }

    public List<ResearchResult> getFromCache(String pattern) {
        //if (HomeController.pattern != null && HomeController.pattern.equals(pattern)) return results;
        HomeController.pattern = pattern;
        return null;
    }
}
