package main.controller;

import main.db.Metadata;
import main.multiThreading.ThreadPool;
import main.service.pageRank.Page_Rank;
import main.service.utils.Book;
import main.service.utils.Helper;
import main.service.utils.ResearchResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class HomeController {

    static List<ResearchResult> results = null;
    static String pattern = null;
    public static Map<String, Float> pageRang = Page_Rank.deserialize();

    @RequestMapping("/")
    public String index() {
        return "Welcome";
    }

    @GetMapping("/search")
    public List<ResearchResult> getResearch(@RequestParam String pattern) {

        List<ResearchResult> fromCach = getFromCache(pattern);
        if (fromCach != null) return fromCach;

        results = ThreadPool.getResultsResearch(pattern);
        //results = ThreadPool.getResultsResearchFast(pattern);

        //Collections.sort(results, (o1, o2) -> o2.nbMatched - o1.nbMatched);


        /*results.sort((o1, o2) -> {
            if (o2.pageRank > o1.pageRank) return 1;
            else if (o2.pageRank < o1.pageRank) return -1;
            return 0;
        });*/

        //List<ResearchResult> r = Betweenness.sortByBetweenes(results);
        //System.out.println(r.stream().map(a -> a.book.fileName).collect(Collectors.toList()));

        List<String> fileNames = results.stream().map(rr -> rr.book.fileName).collect(Collectors.toList());
        Map<String, Integer> nbClick = Metadata.getNbClickBooks(fileNames);
        results.sort((o1, o2) -> nbClick.get(o2.book.fileName) - nbClick.get(o1.book.fileName));
        return results;
    }
    @PostMapping("/advencedSearch")
    public List<ResearchResult> getAdvencedSearch(@RequestBody String body) {

        String pattern = Helper.getPatternFromJson(body);
        List<ResearchResult> fromCach = getFromCache(pattern);
        if (fromCach != null) return fromCach;

        results = ThreadPool.getResultsResearch(pattern);
        //results = ThreadPool.getResultsResearchFast(pattern);

        //Collections.sort(results, (o1, o2) -> o2.nbMatched - o1.nbMatched);

        /*results.sort((o1, o2) -> {
            if (o2.pageRank > o1.pageRank) return 1;
            else if (o2.pageRank < o1.pageRank) return -1;
            return 0;
        });*/

        //List<ResearchResult> r = Betweenness.sortByBetweenes(results);
        //System.out.println(r.stream().map(a -> a.book.fileName).collect(Collectors.toList()));

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

    public List<ResearchResult> getFromCache(String pattern){
        if (HomeController.pattern != null && HomeController.pattern.equals(pattern)) return results;
        HomeController.pattern = pattern;
        return null;
    }
}
