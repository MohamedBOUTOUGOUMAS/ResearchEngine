package main.service.utils;

public class ResearchResult {

    public Book book;
    public int nbMatched;

    public ResearchResult(Book book, int nbMatched){
        this.book = book;
        this.nbMatched = nbMatched;
    }
}
