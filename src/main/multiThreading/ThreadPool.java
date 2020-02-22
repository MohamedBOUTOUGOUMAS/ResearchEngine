package main.multiThreading;

import main.service.AEF.RegEx;
import main.service.utils.Helper;
import main.service.utils.ResearchResult;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class ThreadPool {
    public static Map<String, ResearchResult> bookResearch;
    public static List<Future<ResearchResult>> futuresMatched;
    public static ExecutorService pool = Executors.newFixedThreadPool(50);
    public static RegEx regEx = null;

    public static List<ResearchResult> getResultsResearch(String pattern){

        if (Helper.isRegEx(pattern)) regEx = new RegEx(pattern);
        bookResearch = new HashMap<>();
        //ArrayList<String> books = Helper.readBooks(Helper.BOOKS_PATH);
        ArrayList<String> books = Helper.readBooks(Helper.TEST_PATH);

        futuresMatched = new ArrayList<>();
        futuresMatched.addAll(books.stream()
                .map(book -> {
                    MatchingBook matchingBook = new MatchingBook(pattern, book, regEx);
                    return pool.submit(matchingBook);
                })
                .collect(Collectors.toList())
        );
        List<ResearchResult> results = futuresMatched
                .stream()
                .map(futureMatched -> {
                    try {
                        ResearchResult researchResult = futureMatched.get();
                        bookResearch.put(researchResult.book.fileName, researchResult);
                        if (researchResult.nbMatched == 0) return null;
                        //System.out.println(researchResult.book.fileName);
                        return researchResult;
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (regEx != null) regEx = null;
        return results;
    }
}
