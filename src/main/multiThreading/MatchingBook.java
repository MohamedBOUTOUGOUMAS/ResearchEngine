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
    public int firstLetter;

    public MatchingBook(String word, String fileName, int firstLetter, RegEx regEx, int[] retenue){
        this.word = word;
        this.fileName = fileName;
        this.regEx = regEx;
        this.retenue = retenue;
        this.firstLetter = firstLetter;
    }

    @Override
    public ResearchResult call() {
        return Egrep.matchAllWords(word, fileName, firstLetter, regEx, retenue);
    }
}
