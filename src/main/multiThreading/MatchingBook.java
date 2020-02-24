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

    public MatchingBook(String word, String fileName, RegEx regEx){
        this.word = word;
        this.fileName = fileName;
        this.regEx = regEx;
    }

    @Override
    public ResearchResult call() {
        /*if (regEx != null) {
            File file = new File(Helper.BOOKS_PATH+"/"+fileName);
            List<Line> lines = Unix4j.grep(word, file).toLineList();
            return new ResearchResult(Book.getEmptyBook(fileName), lines.size());
        }*/
        //return Egrep.matchAllWordsFast(word, fileName, firstLetter, regEx, retenue);
        return Egrep.matchAllWords(word, fileName, regEx);
    }



    public MatchingBook(String word, String fileName, int firstLetter, RegEx regEx, int[] retenue){
        this.word = word;
        this.fileName = fileName;
        this.regEx = regEx;
        this.retenue = retenue;
        this.firstLetter = firstLetter;
    }

}
