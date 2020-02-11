package main.controller;
import main.service.Egrep;
import main.service.utils.Helper;
import main.service.utils.ResearchResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class HomeController {
    @RequestMapping("/")
    public String index() {
        return "<input type='text'>" +
                "<button>rechercher</button>";
    }
    @GetMapping("/research")
    public List<ResearchResult> getResearch(@RequestParam String pattern) {
        ArrayList<String> books = Helper.readBooks();
        return books.stream()
                .limit(10)
                .map(book -> {
                    ResearchResult researchResult = Egrep.matchAllWords(pattern, Helper.BOOKS_PATH+"/"+book);
                    if (researchResult.positions.size()>0) return researchResult;
                    return null;
                })
                .filter(researchResult -> researchResult != null)
                .collect(Collectors.toList());
    }
}
