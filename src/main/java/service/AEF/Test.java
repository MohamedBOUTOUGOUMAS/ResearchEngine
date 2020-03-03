package service.AEF;

import java.util.ArrayList;

public class Test {

    static final int CONCAT = 0xC04CA7;
    static final int ETOILE = 0xE7011E;
    static final int ALTERN = 0xA17E54;
    static final int PROTECTION = 0xBADDAD;
    static final int PLUS = 0x2B;

    public static RegExTree exampleAhoUllman() {
        RegExTree a = new RegExTree((int) 'a', new ArrayList<RegExTree>());
        RegExTree b = new RegExTree((int) 'b', new ArrayList<RegExTree>());
        RegExTree c = new RegExTree((int) 'c', new ArrayList<RegExTree>());
        ArrayList<RegExTree> subTrees = new ArrayList<RegExTree>();
        subTrees.add(c);
        RegExTree cEtoile = new RegExTree(ETOILE, subTrees);
        subTrees = new ArrayList<RegExTree>();
        subTrees.add(b);
        subTrees.add(cEtoile);
        RegExTree dotBCEtoile = new RegExTree(CONCAT, subTrees);
        subTrees = new ArrayList<RegExTree>();
        subTrees.add(a);
        subTrees.add(dotBCEtoile);
        return new RegExTree(ALTERN, subTrees);
    }

    public static RegExTree exampleSargon() {
        RegExTree s = new RegExTree((int) 'S', new ArrayList<RegExTree>());
        RegExTree a = new RegExTree((int) 'a', new ArrayList<RegExTree>());
        RegExTree r = new RegExTree((int) 'r', new ArrayList<RegExTree>());
        RegExTree g = new RegExTree((int) 'g', new ArrayList<RegExTree>());

        RegExTree o = new RegExTree((int) 'o', new ArrayList<RegExTree>());

        RegExTree n = new RegExTree((int) 'n', new ArrayList<RegExTree>());

        ArrayList<RegExTree> subTrees = new ArrayList<RegExTree>();
        subTrees.add(a);
        subTrees.add(r);
        RegExTree alt1 = new RegExTree(ALTERN, subTrees);
        subTrees = new ArrayList<RegExTree>();
        subTrees.add(alt1);
        subTrees.add(g);
        RegExTree alt2 = new RegExTree(ALTERN, subTrees);
        subTrees = new ArrayList<RegExTree>();
        subTrees.add(alt2);

        RegExTree plus = new RegExTree(PLUS, subTrees);

        subTrees = new ArrayList<RegExTree>();
        subTrees.add(s);
        subTrees.add(plus);
        RegExTree dot = new RegExTree(CONCAT, subTrees);

        subTrees = new ArrayList<RegExTree>();
        subTrees.add(dot);
        subTrees.add(o);
        RegExTree dotDotO = new RegExTree(CONCAT, subTrees);

        subTrees = new ArrayList<RegExTree>();
        subTrees.add(dotDotO);
        subTrees.add(n);
        RegExTree fin = new RegExTree(CONCAT, subTrees);

        return fin;
    }

    public static void main(String[] args) {
        // .(.(.(S,+(|(|(a,r),g))),o),n)
//		RegExTree example1 = exampleAhoUllman();
        RegExTree example = exampleSargon();

        System.out.println(example.toString());
        Automat a = example.makeAuto();
//		Automat a1 = example1.makeAuto();

//		a1.toString();

        AefDeterministe aefD = new AefDeterministe(a);

        String mot = "Saaaaaaon";
        System.out.println(aefD.match(mot));

        // newAut.toString();

    }

}
