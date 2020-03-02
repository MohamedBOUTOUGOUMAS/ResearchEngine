package main.java.service.utils;

import java.util.ArrayList;

public class Line {

    public int lineNumber;
    public ArrayList<Position> positions;

    public Line(int lineNumber, ArrayList<Position> positions){
        this.lineNumber = lineNumber;
        this.positions = positions;
    }
    public String toString(){
        String line = "";
        for (Position position : this.positions){
            line += position.toString();
        }
        return line;
    }
}
