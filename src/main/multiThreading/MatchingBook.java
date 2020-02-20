package main.multiThreading;

import main.service.AEF.RegEx;
import main.service.Egrep;
import main.service.utils.Book;
import main.service.utils.Helper;
import main.service.utils.ResearchResult;
import org.unix4j.Unix4j;
import org.unix4j.line.Line;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;

public class MatchingBook implements Callable<ResearchResult> {
    public String fileName;
    public String word;
    public RegEx regEx;

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
        return Egrep.matchAllWords(word, fileName, regEx);
    }
}
