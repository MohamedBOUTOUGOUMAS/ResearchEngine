package main.controller;
import main.multiThreading.MatchingBook;
import main.service.utils.Helper;
import main.service.utils.Position;
import main.service.utils.ResearchResult;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class HomeController {
    public List<Future<ResearchResult>> futuresMatched;
    public ExecutorService pool = Executors.newFixedThreadPool(50);

    @RequestMapping("/")
    public String index() {
        return "Welcom";
    }
    @GetMapping("/search")
    public List<ResearchResult> getResearch(@RequestParam String pattern) {
        System.out.println(pattern);
        ArrayList<String> books = Helper.readBooks();
        futuresMatched = new ArrayList<>();
        futuresMatched.addAll(books.stream()
                .limit(1664)
                .map(book -> {
                    MatchingBook matchingBook = new MatchingBook(pattern, book);
                    return pool.submit(matchingBook);
                }).collect(Collectors.toList()));

        return futuresMatched
                .stream()
                .map(futureMatched -> {
                    try {
                        ResearchResult researchResult = futureMatched.get();
                        if (researchResult.positions.size() == 0) return null;
                        System.out.println(researchResult.book.fileName+" end");
                        return researchResult;
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @GetMapping("/search/book")
    public ArrayList<String> getBook(@RequestParam String fileName) {
        BufferedReader lecteurAvecBuffer;
        ArrayList<String> book = new ArrayList<>();

        try {
            lecteurAvecBuffer = new BufferedReader(new FileReader(Helper.BOOKS_PATH+"/"+fileName));
            String line;
            while ((line = lecteurAvecBuffer.readLine()) != null) {
                book.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return book;
    }
    
}
