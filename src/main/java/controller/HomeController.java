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
    static Set<String> autoComplete;
    static List<ResearchResult> results = null;
    static String pattern = null;
    public static Map<String, Float> pageRang =
            (Map<String, Float>) Serialization.deserialize(Helper.PAGE_RANK_MAP, "map");

    public static Map<String, Float> betweennes =
            (Map<String, Float>) Serialization.deserialize(Helper.BETWEENNES_MAP, "map");

    public static Map<String, List<String>> suggestions =
            (Map<String, List<String>>) Serialization.deserialize(Helper.SUGGESTIONS_MAP, "map");

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
        }
        else if(classification.equals("btw")){
            results.sort((o1, o2) -> {
                if (o2.betweeness > o1.betweeness) return 1;
                else if (o2.betweeness < o1.betweeness) return -1;
                return 0;
            });
        }
        else if(classification.equals("nbClick")){
            List<String> fileNames = results.stream().map(rr -> rr.book.fileName).collect(Collectors.toList());
            Map<String, Integer> nbClick = Metadata.getNbClickBooks(fileNames);
            results.sort((o1, o2) -> nbClick.get(o2.book.fileName) - nbClick.get(o1.book.fileName));
        }
        else if(classification.equals("nbOccurs")){
            Collections.sort(results, (o1, o2) -> o2.nbMatched - o1.nbMatched);
        }

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

    @GetMapping("/suggestions")
    public List<ResearchResult> getSuggestions(@RequestParam String filename) {
        ArrayList<String> suggestedFiles = (ArrayList<String>) suggestions.get(filename);
        ArrayList<ResearchResult> result = (ArrayList<ResearchResult>) suggestedFiles.stream()
                .map(s -> new ResearchResult(Book.getEmptyBook(s), 0))
                .collect(Collectors.toList());
        return result;
    }
}
