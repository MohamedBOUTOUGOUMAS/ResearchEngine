package multiThreading;

import controller.HomeController;
import db.Database;
import service.AEF.RegEx;
import service.KMP.KMP;
import service.utils.Book;
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
                        if (researchResult.nbMatched == 0) return null;
                        Float rank = HomeController.pageRang.get(researchResult.book.fileName);
                        researchResult.pageRank = rank != null ? rank : 0F;
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


    public static List<ResearchResult> getResultsResearchFast(String pattern, String searchSource) {

        if (Helper.isRegEx(pattern)) {
            regEx = new RegEx(pattern);
        } else {
            retenue = KMP.calculRetenue(pattern.toCharArray());
        }

        ArrayList<String> books = Helper.readBooks(Helper.INDEXES_TABLES_PATH);
        futuresMatched = new ArrayList<>();

        int asciiCode = (pattern.toUpperCase().charAt(0) - 65);

        List<ResearchResult> results = new ArrayList<>();

        if (searchSource != null && searchSource.equals("db")){
            for(String book : Database.matchDB(asciiCode, pattern)){
                ResearchResult rr = new ResearchResult(Book.getEmptyBook(book), (int) (Math.random() * 100));
                Float rank = HomeController.pageRang.get(book);
                rr.pageRank = rank != null ? rank : 0F;
                results.add(rr);
            }
            return results;
        }


        futuresMatched.addAll(books.stream()
                .map(fileName -> {
                    MatchingBook matchingBook = new MatchingBook(pattern, fileName, asciiCode, regEx, retenue);
                    return pool.submit(matchingBook);
                })
                .collect(Collectors.toList())
        );

        results = futuresMatched
                .stream()
                .map(futureMatched -> {
                    try {
                        ResearchResult researchResult = futureMatched.get();
                        if (researchResult.nbMatched == 0) return null;
                        Float rank = HomeController.pageRang.get(researchResult.book.fileName);
                        researchResult.pageRank = rank != null ? rank : 0F;
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
