package multiThreading;

import db.Database;
import org.unix4j.Unix4j;
import org.unix4j.line.Line;
import service.AEF.RegEx;
import service.Egrep;
import service.utils.Book;
import service.utils.Helper;
import service.utils.ResearchResult;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;

public class MatchingBook implements Callable<ResearchResult> {
    public String fileName;
    public String word;
    public RegEx regEx;
    public int[] retenue;
    public int firstLetter;

    public MatchingBook(String word, String fileName) {
        this.word = word;
        this.fileName = fileName;
    }

    public MatchingBook(String word, String fileName, int firstLetter, RegEx regEx, int[] retenue) {
        this.word = word;
        this.fileName = fileName;
        this.regEx = regEx;
        this.retenue = retenue;
        this.firstLetter = firstLetter;
    }

    @Override
    public ResearchResult call() {

        if (Helper.isRegEx(word) && retenue == null) {
            File file = new File(Helper.BOOKS_PATH+"/"+fileName);
            List<Line> lines = Unix4j.grep(word, file).toLineList();
            return new ResearchResult(Book.getEmptyBook(fileName), lines.size());
        }
        if (retenue != null) return Egrep.matchAllWordsFast(word, fileName, firstLetter, regEx, retenue);
        return Egrep.matchAllWords(word, fileName, regEx);
    }
}
