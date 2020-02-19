package main.multiThreading;

import main.service.AEF.RegEx;
import main.service.KMP.KMP;
import main.service.utils.Helper;
import main.service.utils.ResearchResult;

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

    public static List<ResearchResult> getResultsResearch(String pattern){

        if (Helper.isRegEx(pattern)){
            regEx = new RegEx(pattern);
        }else {
            retenue = KMP.calculRetenue(pattern.toCharArray());
        }
        ArrayList<String> books = Helper.readBooks(Helper.INDEXES_TABLES_PATH);
        futuresMatched = new ArrayList<>();
        for (int i=0; i < books.size(); i++) {
            String book = books.get(i);
            MatchingBook matchingBook = new MatchingBook(pattern, book, regEx, retenue);
            futuresMatched.add(pool.submit(matchingBook));
        }

        List<ResearchResult> results = futuresMatched
                .stream()
                .map(futureMatched -> {
                    try {
                        ResearchResult researchResult = futureMatched.get();
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
