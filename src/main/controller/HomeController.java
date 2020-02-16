package main.controller;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import main.multiThreading.ThreadPool;
import main.service.utils.Book;
import main.service.utils.ResearchResult;
import netscape.javascript.JSObject;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    @PostMapping("/advencedSearch")
    public List<ResearchResult> getAdvencedSearch(@RequestBody String body) {
        System.out.println(body);
        JsonObject payload = new JsonParser().parse(body).getAsJsonObject();
        String pattern = "";
        for (Map.Entry<String, JsonElement> e : payload.entrySet()){
            pattern = e.getValue().getAsString();
        }
        List<ResearchResult> matched = ThreadPool.getResultsResearch(pattern);
        Collections.sort(matched, (o1, o2) -> o2.positions.size() - o1.positions.size());
        return matched;
    }

    @GetMapping("/search/book")
    public Book getBook(@RequestParam String fileName) {
        return Book.getBook(fileName);
    }
    
}
