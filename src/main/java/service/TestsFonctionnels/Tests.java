package service.TestsFonctionnels;

import service.AEF.*;
import service.AEF.RegExTree;
import service.KMP.KMP;
import service.RadixTree.*;
import service.utils.Position;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class Tests {

    @Test
    public void KMPtest1() {

        String facteur = "Egyptian";
        String text = "has been found necessary to modify the view that Egyptian culture in";
        int index = KMP.match(facteur.toCharArray(), KMP.calculRetenue(facteur.toCharArray()),
                text.toCharArray());

        assertEquals(49, index);
    }

    @Test
    public void KMPtest2() {

        String facteur = " ";
        String text = "has been found necessary to modify the view that Egyptian culture in";
        int index = KMP.match(facteur.toCharArray(), KMP.calculRetenue(facteur.toCharArray()),
                text.toCharArray());

        assertEquals(3, index);

    }


    @Test
    public void AEFDeterministeTest1() {

        RegExTree sargon = service.AEF.Test.exampleSargon();
        Automat a = sargon.makeAuto();

        AefDeterministe aefD = new AefDeterministe(a);

        String text = "But the excavation of the pre-Sargonic strata, so far as it has yet";
        String txt = "This eBook is for the use of anyone anywhere at no cost and with\n" +
                "almost no restrictions whatsoever.  You may copy it, give it away or\n" +
                "re-use it under the terSargonicms of the Project Gutenberg License included\n" +
                "with this eBook or online at www.gutenberg.org/license\n" +
                "\n" +
                "\n" +
                "Title: Mary Wollstonecraft and the beginnings of female emancipation in France and   England\n" +
                "\n" +
                "Author: Jacob Bouten\n" +
                "\n" +
                "Release Date: May 6, 2019 [EBook #59448]\n" +
                "\n" +
                "Language: English\n" +
                "\n" +
                "\n" +
                "*** START OF THIS PROJECT GUTENBERG EBOOK MARY WOLLSTONECRAFT ***";
        //end's index of the matched word
        int positions = aefD.matchAll(txt, 0);

        assertEquals(1, positions);
    }

    @Test
    public void AEFDeterministeTest2() {

        AefDeterministe aefD = new RegEx("(as)+").aef;

        String text = "But the excavation of the pre-Sargonic strata, so far as it has yet";
        //end's index of the matched word
        int positions = aefD.matchAll(text, 0);

        assertEquals(2, positions);
    }

    @Test
    public void AEFDeterministeTest3() {

        AefDeterministe aefD = new RegEx(" ").aef;

        String text = "Sargonic strata, so far";
        //end's index of the matched word
        int positions = aefD.matchAll(text, 0);

        //the word doesn't match
        assertEquals(3, positions);
    }


    @Test
    public void RadixTreeTests() {

        RadixTree tree = new RadixTree();
        ArrayList<Position> positions = new ArrayList<>();
        positions.add(new Position());
        tree.insertWord("bulle", 0, positions);
        tree.insertWord("bus", 0, positions);
        tree.insertWord("zoo", 0, positions);
        Assert.assertTrue(tree.isPresent("bulle") != null && tree.isPresent("bulle").size() == 1);
        Assert.assertTrue(tree.isPresent("bus") != null && tree.isPresent("bus").size() == 1);
        Assert.assertTrue(tree.isPresent("zoo") != null && tree.isPresent("zoo").size() == 1);
    }

	/*@Test
	public void whenGrepWithSimpleString_thenCorrect() {
		int expectedLineCount = 1;
		File file = new File("bref.txt");
		List<Line> lines = Unix4j.grep("c(o|p)+y", file).toLineList();
		System.out.println(lines);
		assertEquals(expectedLineCount, lines.size());
	}*/
}