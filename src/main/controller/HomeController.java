package main.controller;
import main.multiThreading.ThreadPool;
import main.service.utils.Book;
import main.service.utils.Helper;
import main.service.utils.ResearchResult;
import org.springframework.web.bind.annotation.*;

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
        System.out.println(pattern);
        System.out.println(HomeController.pattern+" last search");

        List<ResearchResult> fromCach = getFromCach(pattern);
        if (fromCach != null) return fromCach;

        results = ThreadPool.getResultsResearch(pattern);
        Collections.sort(results, (o1, o2) -> o2.positions.size() - o1.positions.size());
        return results;
    }
    @PostMapping("/advencedSearch")
    public List<ResearchResult> getAdvencedSearch(@RequestBody String body) {
        System.out.println(body);
        System.out.println(HomeController.pattern+" last search");

        String pattern = Helper.getPatternFromJson(body);

        List<ResearchResult> fromCach = getFromCach(pattern);
        if (fromCach != null) return fromCach;

        results = ThreadPool.getResultsResearch(pattern);
        Collections.sort(results, (o1, o2) -> o2.positions.size() - o1.positions.size());
        return results;
    }

    @GetMapping("/search/book")
    public Book getBook(@RequestParam String fileName) {
        return Book.getBook(fileName);
    }

    public List<ResearchResult> getFromCach(String pattern){
        if (HomeController.pattern != null && HomeController.pattern.equals(pattern)) return results;
        HomeController.pattern = pattern;
        return null;
    }
}
