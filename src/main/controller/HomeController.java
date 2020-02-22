package main.controller;

import main.multiThreading.ThreadPool;
import main.service.pageRank.Page_Rank;
import main.service.utils.Book;
import main.service.utils.Helper;
import main.service.utils.ResearchResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class HomeController {

    static List<ResearchResult> results = null;
    static String pattern = null;

    @RequestMapping("/")
    public String index() {
        return "Welcome";
    }

    @GetMapping("/search")
    public List<ResearchResult> getResearch(@RequestParam String pattern) {

        List<ResearchResult> fromCach = getFromCache(pattern);
        if (fromCach != null) return fromCach;

        results = ThreadPool.getResultsResearch(pattern);

        Collections.sort(results, (o1, o2) -> o2.nbMatched - o1.nbMatched);

        //List<ResearchResult> r = Betweenness.sortByBetweenes(results);
        //System.out.println(r.stream().map(a -> a.book.fileName).collect(Collectors.toList()));

        /*Page_Rank.setResearchResults(results);
        ArrayList<String> booksRank = Page_Rank.getRank();
        List<ResearchResult> aferSort = new ArrayList<>();
        for(String book : booksRank){
            aferSort.add(ThreadPool.bookResearch.get(book));
        }
        return aferSort;*/
        return results;
    }
    @PostMapping("/advencedSearch")
    public List<ResearchResult> getAdvencedSearch(@RequestBody String body) {

        String pattern = Helper.getPatternFromJson(body);
        List<ResearchResult> fromCach = getFromCache(pattern);
        if (fromCach != null) return fromCach;

        results = ThreadPool.getResultsResearch(pattern);

        Collections.sort(results, (o1, o2) -> o2.nbMatched - o1.nbMatched);

        //List<ResearchResult> r = Betweenness.sortByBetweenes(results);
        //System.out.println(r.stream().map(a -> a.book.fileName).collect(Collectors.toList()));

        /*Page_Rank.setResearchResults(results);
        ArrayList<String> booksRank = Page_Rank.getRank();
        List<ResearchResult> aferSort = new ArrayList<>();
        for(String book : booksRank){
            aferSort.add(ThreadPool.bookResearch.get(book));
        }
        return aferSort;*/

        return results;
    }

    @GetMapping("/search/book")
    public Book getBook(@RequestParam String fileName) {
        return Book.getBook(fileName);
    }

    public List<ResearchResult> getFromCache(String pattern){
        if (HomeController.pattern != null && HomeController.pattern.equals(pattern)) return results;
        HomeController.pattern = pattern;
        return null;
    }
}
