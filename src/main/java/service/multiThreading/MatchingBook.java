package service.multiThreading;

import org.unix4j.Unix4j;
import org.unix4j.line.Line;
import service.matchingServices.AEF.RegEx;
import service.matchingServices.EgrepService;
import topics.Book;
import helpers.GenericHelper;
import topics.ResearchResult;

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

        if (GenericHelper.isRegEx(word) && retenue == null) {
            File file = new File(GenericHelper.BOOKS_PATH+"/"+fileName);
            List<Line> lines = Unix4j.grep(word, file).toLineList();
            return new ResearchResult(Book.getEmptyBook(fileName), lines.size());
        }
        if (retenue != null) return EgrepService.matchAllWordsFast(word, fileName, firstLetter, regEx, retenue);
        return EgrepService.matchAllWords(word, fileName, regEx);
    }
}
