package main.multiThreading;

import main.service.Egrep;
import main.service.utils.ResearchResult;

import java.util.concurrent.Callable;

public class MatchingBook implements Callable<ResearchResult> {
    public String fileName;
    public String word;

    public MatchingBook(String word, String fileName){
        this.word = word;
        this.fileName = fileName;
    }

    @Override
    public ResearchResult call() {
        return Egrep.matchAllWords(word, fileName);
    }
}
