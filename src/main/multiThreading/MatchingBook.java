package main.multiThreading;

import main.service.AEF.RegEx;
import main.service.Egrep;
import main.service.utils.ResearchResult;
import java.util.concurrent.Callable;

public class MatchingBook implements Callable<ResearchResult> {

    public String fileName;
    public String word;
    public RegEx regEx;
    public int[] retenue;

    public MatchingBook(String word, String fileName, RegEx regEx, int[] retenue){
        this.word = word;
        this.fileName = fileName;
        this.regEx = regEx;
        this.retenue = retenue;
    }

    @Override
    public ResearchResult call() {
        return Egrep.matchAllWords(word, fileName, regEx, retenue);
    }
}
