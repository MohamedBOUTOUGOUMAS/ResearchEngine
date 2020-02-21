package main.service.pageRank;

import java.util.Map;

public class Sommet{
	
	String fileName;
	Map<String, Float> neighbor;
	
	public Sommet(String fileName, Map<String, Float> neighbor) {
		this.fileName = fileName;
		this.neighbor = neighbor;
	}
	

}
