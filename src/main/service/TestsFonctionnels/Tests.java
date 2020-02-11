package main.service.TestsFonctionnels;

import main.service.AEF.RegExTree;
import main.service.KMP.KMP;
import main.service.AEF.*;
import main.service.RadixTree.*;
import main.service.utils.Position;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class Tests {

	@Test
	public void KMPtest1() {

		String facteur = "Egyptian";
		String text = "has been found necessary to modify the view that Egyptian culture in";
		int index = KMP.match(facteur.toCharArray(), KMP.calculRetenue(facteur.toCharArray()),
				text.toCharArray());

		Assert.assertEquals(49, index);
	}

	@Test
	public void KMPtest2() {

		String facteur = " ";
		String text = "has been found necessary to modify the view that Egyptian culture in";
		int index = KMP.match(facteur.toCharArray(), KMP.calculRetenue(facteur.toCharArray()),
				text.toCharArray());

		Assert.assertEquals(3, index);

	}
	
	
	@Test
	public void AEFDeterministeTest1() {

		RegExTree sargon = main.service.AEF.Test.exampleSargon();
		Automat a = sargon.makeAuto();
		
		AefDeterministe aefD = new AefDeterministe(a);

		String text = "But the excavation of the pre-Sargonic strata, so far as it has yet";
		//end's index of the matched word
		ArrayList<Position> positions = aefD.matchAll(text, 0);
		
		Assert.assertEquals(1, positions.size());
		Assert.assertEquals(30, positions.get(0).initPos);
	}

	@Test
	public void AEFDeterministeTest2() {

		AefDeterministe aefD = new RegEx("(as)+").aef;

		String text = "But the excavation of the pre-Sargonic strata, so far as it has yet";
		//end's index of the matched word
		ArrayList<Position> positions = aefD.matchAll(text, 0);

		Assert.assertEquals(2, positions.size());
		Assert.assertEquals(54, positions.get(0).initPos);
		Assert.assertEquals(61, positions.get(1).initPos);
	}

	@Test
	public void AEFDeterministeTest3() {

		AefDeterministe aefD = new RegEx(" ").aef;

		String text = "Sargonic strata, so far";
		//end's index of the matched word
		ArrayList<Position> positions = aefD.matchAll(text,0);

		//the word doesn't match
		Assert.assertEquals(3, positions.size());
		Assert.assertEquals(8, positions.get(0).initPos);
		Assert.assertEquals(16, positions.get(1).initPos);
		Assert.assertEquals(19, positions.get(2).initPos);
	}
	
	
	@Test
	public void RadixTreeTests() {

		RadixTree tree = new RadixTree();
		tree.insertWord("bulle",0,new Position());
        tree.insertWord("bus",0,new Position());
        tree.insertWord("zoo",0, new Position());
        Assert.assertTrue(tree.isPresent("bulle") != null && tree.isPresent("bulle").size() == 1);
        Assert.assertTrue(tree.isPresent("bus") != null && tree.isPresent("bus").size() == 1);
        Assert.assertTrue(tree.isPresent("zoo") != null && tree.isPresent("zoo").size() == 1);
	}
}