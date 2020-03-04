package multiThreading;

import service.AEF.RegEx;
import service.KMP.KMP;
import service.utils.Helper;
import service.utils.ResearchResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class ThreadPool {
    public static List<Future<ResearchResult>> futuresMatched;
    public static ExecutorService pool = Executors.newFixedThreadPool(50);
    public static RegEx regEx = null;
    public static int[] retenue = {};

    public static List<ResearchResult> getResultsResearch(String pattern) {

        ArrayList<String> books = Helper.readBooks(Helper.BOOKS_PATH);

        futuresMatched = new ArrayList<>();
        futuresMatched.addAll(books.stream()
                .map(book -> {
                    MatchingBook matchingBook = new MatchingBook(pattern, book);
                    return pool.submit(matchingBook);
                })
                .collect(Collectors.toList())
        );
        List<ResearchResult> results = futuresMatched
                .stream()
                .map(futureMatched -> {
                    try {
                        ResearchResult researchResult = futureMatched.get();
                        //bookResearch.put(researchResult.book.fileName, researchResult);
                        if (researchResult.nbMatched == 0) return null;
                        //System.out.println(researchResult.book.fileName);
                        //Float rank = HomeController.pageRang.get(researchResult.book.fileName);
                        //researchResult.pageRank = rank != null ? rank : 0F;
                        return researchResult;
                    } catch (InterruptedException | ExecutionException e) {
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (regEx != null) regEx = null;
        return results;
    }


    public static List<ResearchResult> getResultsResearchFast(String pattern) {

        if (Helper.isRegEx(pattern)) {
            regEx = new RegEx(pattern);
        } else {
            retenue = KMP.calculRetenue(pattern.toCharArray());
        }

        ArrayList<String> books = Helper.readBooks(Helper.INDEXES_TABLES_PATH);
        futuresMatched = new ArrayList<>();
        futuresMatched.addAll(books.stream()
                .map(fileName -> {
                    int asciiCode = (pattern.toUpperCase().charAt(0) - 65);
                    MatchingBook matchingBook = new MatchingBook(pattern, fileName, asciiCode, regEx, retenue);
                    return pool.submit(matchingBook);
                })
                .collect(Collectors.toList())
        );

        List<ResearchResult> results = futuresMatched
                .stream()
                .map(futureMatched -> {
                    try {
                        ResearchResult researchResult = futureMatched.get();
                        if (researchResult.nbMatched == 0) return null;
                        //Float rank = HomeController.pageRang.get(researchResult.book.fileName);
                        //researchResult.pageRank = rank != null ? rank : 0F;
                        return researchResult;
                    } catch (InterruptedException | ExecutionException e) {
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (regEx != null) regEx = null;
        return results;
    }
}
