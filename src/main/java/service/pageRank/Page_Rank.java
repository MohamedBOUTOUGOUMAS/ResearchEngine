package main.java.service.pageRank;

import main.java.service.utils.Helper;

import java.io.*;
import java.util.*;

import static main.java.service.utils.Serialization.serialize;

public class Page_Rank {

	public static Map<String, Sommet> adjacencyArray = new HashMap<>();
	public static ArrayList<String> indexTableList;
	public static Set<String> indexTableSet = new HashSet<>();
	public static Map<String, Integer> fileNameIndex = new HashMap<>();

	public static Map<String, Float> bookRank = new HashMap<>();

	public static void readFile() {
		BufferedReader lecteurAvecBuffer;
	    String ligne;
	    ArrayList<String> jaccard = Helper.readBooks(Helper.JACCARD_PATH);
		try {
			for (String book : jaccard) {
				lecteurAvecBuffer = new BufferedReader(new FileReader(Helper.JACCARD_PATH+"/"+book));
				while((ligne = lecteurAvecBuffer.readLine()) != null) {
					String [] aretes;
					aretes = ligne.split(" ", 2);

					String fileNeighbor = aretes[0];
					Double distance = Double.parseDouble(aretes[1]);

					if (distance > 0.8) continue;
					if(adjacencyArray.containsKey(book)) {
						adjacencyArray.get(book).neighbor.put(fileNeighbor, distance);
					}else {
						Map<String, Double> nei = new HashMap<>();
						nei.put(fileNeighbor, distance);
						adjacencyArray.put(book, new Sommet(book, nei));
					}
					indexTableSet.add(book);
					indexTableSet.add(fileNeighbor);
				}
				lecteurAvecBuffer.close();
			}

			System.out.println("reading finished");
			indexTableList = new ArrayList<>(indexTableSet);
			for(int i = 0; i<indexTableList.size();i++) {
				fileNameIndex.put(indexTableList.get(i), i);
			}
		}
	    catch(IOException exc){
	    	System.out.println("Erreur d'ouverture");
	    }
	}
	
	
	public static float [] prodMatVect(Map<String, Sommet> mat, float [] A){

		float [] B = new float[indexTableList.size()];

		for (Map.Entry<String, Sommet> e: mat.entrySet()){
			float degSortant = (float)e.getValue().neighbor.size();
			int i = fileNameIndex.get(e.getKey());
            for (Map.Entry<String, Double> nei: e.getValue().neighbor.entrySet()) {
                int ii = fileNameIndex.get(nei.getKey());
                B[ii] += (float)(A[i] / degSortant);
            }
		}
		return B;
	}
	
	
	public static float[] powerIteration(float alpha, int t) {

		float [] A = new float[indexTableList.size()];
		
		for(int i=0; i<A.length; i++) {
			A[i] = (float)1/adjacencyArray.size();
		}
		
		float[] produit = new float[indexTableList.size()];
		
		for(int i=0; i<A.length; i++) {
			produit[i] = 0;
		}
		
		for(int k =0; k<t; k++) {
			
			produit = prodMatVect(adjacencyArray,A);

			float sommeDesEltDuTabProduit = 0;
			
			for(int i=0; i<produit.length; i++) {
				produit[i] = (1-alpha) * produit[i] + (alpha/adjacencyArray.size());
				sommeDesEltDuTabProduit += produit[i];
			}
			
			
			//Normalisation
			for(int i=0; i<produit.length; i++) {
				produit[i] += (1-sommeDesEltDuTabProduit) / adjacencyArray.size();
				A[i] = produit[i];
			}
						
			
		}
		return produit;
	}

	public static void getRank(){
		readFile();
		ArrayList<Float> res = new ArrayList<>();
		float [] p = powerIteration((float) 0.15, 4);
		for(int i=0; i<p.length; i++) res.add(p[i]);
		ArrayList<Float> clone = (ArrayList<Float>) res.clone();
		Collections.sort(clone);
		System.out.println("make map");
		for (int i = 1; i < clone.size(); i++) {
			Float highest = clone.get(clone.size() - i - 1);
			int ind = res.indexOf(highest);
			res.remove(highest);
			String book = indexTableList.get(ind);
			indexTableList.remove(book);
			bookRank.put(book, highest);
		}
	}
	/*
	public static void serialize(){
		File pgrk = new File(Helper.PAGE_RANK_MAP);
		pgrk.mkdir();
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(new File(Helper.PAGE_RANK_MAP+"/map"));
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			System.out.println("write the map");
			oos.writeObject(bookRank);
			fos.close();
			System.out.println("End");

			for (Map.Entry<String, Float> e: bookRank.entrySet()) {
				System.out.println(e.getKey()+" "+e.getValue());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Map<String, Float> deserialize(){
		System.out.println("Deserialisation");
		FileInputStream fs;
		Map<String, Float> map = new HashMap<>();

		try {
			fs = new FileInputStream(new File(Helper.PAGE_RANK_MAP+"/map"));
			ObjectInputStream ss = new ObjectInputStream(fs);
			map = (Map<String, Float>) ss.readObject();
			ss.close();
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}

	 */

	public static void main(String [] args){
		getRank();
		serialize("page-rank-map", "map", bookRank);
	}
}
