package main.java.service.pageRank;

import java.util.Map;

public class Sommet{
	
	String fileName;
	Map<String, Double> neighbor;
	
	public Sommet(String fileName, Map<String, Double> neighbor) {
		this.fileName = fileName;
		this.neighbor = neighbor;
	}
	

}
