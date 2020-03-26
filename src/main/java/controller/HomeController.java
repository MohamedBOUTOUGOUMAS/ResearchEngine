package controller;

import com.google.gson.JsonObject;
import db.Metadata;
import service.multiThreading.ThreadPool;
import topics.Book;
import helpers.GenericHelper;
import topics.ResearchResult;
import org.springframework.web.bind.annotation.*;
import topics.Serialization;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "https://research-engine-client.herokuapp.com")
//@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class HomeController {
    static Set<String> autoComplete;
    public static Map<String, Float> pageRang =
            (Map<String, Float>) Serialization.deserialize(GenericHelper.PAGE_RANK_MAP, "map");

    public static Map<String, Float> betweennes =
            (Map<String, Float>) Serialization.deserialize(GenericHelper.BETWEENNES_MAP, "map");

    public static Map<String, List<String>> suggestions =
            (Map<String, List<String>>) Serialization.deserialize(GenericHelper.SUGGESTIONS_MAP, "map");

    @RequestMapping("/")
    public String index() {
        return "Welcome";
    }

    @GetMapping("/search")
    public List<ResearchResult> getResearch(@RequestParam String pattern, @RequestParam String fast,  @RequestParam String searchSource,  @RequestParam String classification) {
        List<ResearchResult> results;
        boolean speedMode = Boolean.parseBoolean(fast);

        if (autoComplete != null && !autoComplete.contains(pattern)) {
            Metadata.addAutoCompleteSearch(pattern);
            autoComplete.add(pattern);
        }
        if (speedMode) results = ThreadPool.getResultsResearchFast(pattern, searchSource);
        else results = ThreadPool.getResultsResearch(pattern);

        return GenericHelper.sortBy(results, classification);
    }

    @PostMapping("/advencedSearch")
    public List<ResearchResult> getAdvencedSearch(@RequestBody String body) {
        List<ResearchResult> results;

        JsonObject payload = GenericHelper.getJsonFromBody(body);

        String pattern = payload.get("regEx").getAsString();
        boolean speedMode = payload.get("fast").getAsBoolean();
        String searchSource = payload.get("searchSource").getAsString();
        String classification = payload.get("classification").getAsString();

        if (autoComplete != null && !autoComplete.contains(pattern)){
            Metadata.addAutoCompleteSearch(pattern);
            autoComplete.add(pattern);
        }

        if (speedMode) results = ThreadPool.getResultsResearchFast(pattern, searchSource);
        else results = ThreadPool.getResultsResearch(pattern);

        return GenericHelper.sortBy(results, classification);
    }

    @PostMapping("/advencedSearchPlus")
    public List<ResearchResult> getAdvencedPlusResearch(@RequestBody String body){
        List<ResearchResult> results;
        JsonObject payload = GenericHelper.getJsonFromBody(body);
        String pattern = payload.get("pattern").getAsString();
        String metadata = payload.get("metadata").getAsString();
        String classification = payload.get("classification").getAsString();

        results = ThreadPool.getResultsResearchPlus(pattern, metadata);

        return GenericHelper.sortBy(results, classification);
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

    @GetMapping("/suggestions")
    public List<ResearchResult> getSuggestions(@RequestParam String filename) {
        ArrayList<String> suggestedFiles = (ArrayList<String>) suggestions.get(filename);
        ArrayList<ResearchResult> result = (ArrayList<ResearchResult>) suggestedFiles.stream()
                .map(s -> new ResearchResult(Book.getEmptyBook(s), 0))
                .collect(Collectors.toList());
        return result;
    }
}
