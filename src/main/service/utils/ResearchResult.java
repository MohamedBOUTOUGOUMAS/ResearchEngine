package main.service.utils;

import java.util.ArrayList;

public class ResearchResult {

    public Book book;
    public ArrayList<Position> positions;

    public ResearchResult(Book book, ArrayList<Position> positions){
        this.book = book;
        this.positions = positions;
    }
}
