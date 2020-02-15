package main.controller;
import main.multiThreading.ThreadPool;
import main.service.utils.Book;
import main.service.utils.ResearchResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class HomeController {

    @RequestMapping("/")
    public String index() {
        return "Welcom";
    }
    @GetMapping("/search")
    public List<ResearchResult> getResearch(@RequestParam String pattern) {
        System.out.println(pattern);
        List<ResearchResult> matched = ThreadPool.getResultsResearch(pattern);
        Collections.sort(matched, (o1, o2) -> o2.positions.size() - o1.positions.size());
        return matched;
    }

    @GetMapping("/search/book")
    public Book getBook(@RequestParam String fileName) {
        return Book.getBook(fileName);
    }
    
}
