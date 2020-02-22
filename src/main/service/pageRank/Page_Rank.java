package main.service.pageRank;

import main.service.utils.Helper;
import main.service.utils.ResearchResult;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Page_Rank {

	public static Map<String, Sommet> adjacencyArray = new HashMap<>();
	public static ArrayList<String> indexTableList;
	public static Set<String> indexTableSet = new HashSet<>();
	public static Map<String, Integer> fileNameIndex = new HashMap<>();

	public static List<ResearchResult> researchResults;

	public static void setResearchResults(List<ResearchResult> researchResults){
		Page_Rank.researchResults = researchResults;
	}

	public static void readFile() {
		BufferedReader lecteurAvecBuffer;
	    String ligne;
		try{
			for (ResearchResult researchResult : researchResults) {
				String book = researchResult.book.fileName;
				//System.out.println(book);
				lecteurAvecBuffer = new BufferedReader(new FileReader(Helper.JACCARD_PATH+"/"+book));
				while((ligne = lecteurAvecBuffer.readLine()) != null) {
					String [] aretes = new String[2];
					aretes = ligne.split("\\s+", 2);
					String fileNeighbor = aretes[0];
					Float distance = Float.valueOf(aretes[1]);
					if (distance > 0.8) continue;
					if(adjacencyArray.containsKey(book)) {
						adjacencyArray.get(book).neighbor.put(fileNeighbor, distance);
					}else {
						Map<String, Float> nei = new HashMap<>();
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
            for (Map.Entry<String, Float> nei: e.getValue().neighbor.entrySet()) {
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
			
//			System.out.println("iteration :"+k+" P[0] "+produit[0]);
//			System.out.println("iteration :"+k+" P[size - 1] "+produit[produit.length -1]);
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

	public static ArrayList<String> getRank(){

		ArrayList<String> rank = new ArrayList<>();
		readFile();
		//System.out.println(indexTableList);
		ArrayList<Float> res = new ArrayList<>();

		float [] p = powerIteration((float) 0.15, 4);

		for(int i=0; i<p.length; i++) {
			//System.out.println("p[i] : "+p[i]);
			res.add(p[i]);
		}

		ArrayList<Float> clone = (ArrayList<Float>) res.clone();
		Collections.sort(clone);


		for (int i = 1; i < clone.size(); i++) {
			Float highest = clone.get(clone.size() - i - 1);
			int ind = res.indexOf(highest);
			res.remove(highest);
			String book = indexTableList.get(ind);
			indexTableList.remove(book);
			rank.add(book);
		}
		return rank;
	}
}
