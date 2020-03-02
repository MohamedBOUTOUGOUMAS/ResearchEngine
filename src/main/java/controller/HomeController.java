package main.java.controller;

import main.java.db.Metadata;
import main.java.multiThreading.ThreadPool;
import main.java.service.utils.Book;
import main.java.service.utils.Helper;
import main.java.service.utils.ResearchResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "https://research-engine-client.herokuapp.com/")
@RestController
public class HomeController {

    static List<ResearchResult> results = null;
    static String pattern = null;
    /*public static Map<String, Float> pageRang =
            (Map<String, Float>) Serialization.deserialize(Helper.PAGE_RANK_MAP, "map");
    public static Map<String, Map<String, Double>> jaccard_dists =
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
    public List<ResearchResult> getResearch(@RequestParam String pattern, @RequestParam String fast) {
        boolean speedMode = Boolean.parseBoolean(fast);
        List<ResearchResult> fromCach = getFromCache(pattern);
        if (fromCach != null) return fromCach;

        Metadata.addAutoCompleteSearch(pattern);

        if (speedMode) results = ThreadPool.getResultsResearchFast(pattern);
        else results = ThreadPool.getResultsResearch(pattern);

        //Collections.sort(results, (o1, o2) -> o2.nbMatched - o1.nbMatched);

        /*results.sort((o1, o2) -> {
            if (o2.pageRank > o1.pageRank) return 1;
            else if (o2.pageRank < o1.pageRank) return -1;
            return 0;
        });*/

        // Betweennes
        //results = Betweenness.sortByBetweenes(results, jaccard_dists, floydWarshall_map, fw_indexes);

        List<String> fileNames = results.stream().map(rr -> rr.book.fileName).collect(Collectors.toList());
        Map<String, Integer> nbClick = Metadata.getNbClickBooks(fileNames);
        results.sort((o1, o2) -> nbClick.get(o2.book.fileName) - nbClick.get(o1.book.fileName));
        return results;
    }
    @PostMapping("/advencedSearch")
    public List<ResearchResult> getAdvencedSearch(@RequestBody String body) {

        String pattern = Helper.getPatternFromJson(body);
        boolean speedMode = Helper.getFastFromJson(body);

        List<ResearchResult> fromCach = getFromCache(pattern);
        if (fromCach != null) return fromCach;

        Metadata.addAutoCompleteSearch(pattern);


        if (speedMode) results = ThreadPool.getResultsResearchFast(pattern);
        else results = ThreadPool.getResultsResearch(pattern);

        //Collections.sort(results, (o1, o2) -> o2.nbMatched - o1.nbMatched);

        /*results.sort((o1, o2) -> {
            if (o2.pageRank > o1.pageRank) return 1;
            else if (o2.pageRank < o1.pageRank) return -1;
            return 0;
        });*/

        //results = Betweenness.sortByBetweenes(results, jaccard_dists, floydWarshall_map, fw_indexes);

        List<String> fileNames = results.stream().map(rr -> rr.book.fileName).collect(Collectors.toList());
        Map<String, Integer> nbClick = Metadata.getNbClickBooks(fileNames);
        results.sort((o1, o2) -> nbClick.get(o2.book.fileName) - nbClick.get(o1.book.fileName));
        return results;
    }

    @GetMapping("/search/book")
    public Book getBook(@RequestParam String fileName) {
        Metadata.addClick(fileName);
        System.out.println(Metadata.getNbClickBooks());
        return Book.getBook(fileName);
    }

    @GetMapping("/autoComplete")
    public List<Object> getAutoComplete() {
        List<Object> autoComplete = Metadata.getAutoComplete();
        System.out.println("auto "+autoComplete);
        return autoComplete;
    }

    public List<ResearchResult> getFromCache(String pattern){
        if (HomeController.pattern != null && HomeController.pattern.equals(pattern)) return results;
        HomeController.pattern = pattern;
        return null;
    }
}
