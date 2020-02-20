package main.service.AEF;

import java.util.*;
import java.util.Map.Entry;

import static main.service.utils.Helper.cleanText;

public class AefDeterministe {

	public Automat automat;

	static Set<Integer> visited = new HashSet<Integer>();

	public AefDeterministe(Automat a) {
		this.automat = makeDeterministe(a);
	}

	public String toString() {
		return automat.toString();
	}

	public static Set<Integer> epsilonFermeture(Automat a, Integer state) {
		Set<Integer> ef = new HashSet<Integer>();
		Integer e1 = null;
		Integer e2 = null;

		if (a.epsilon[state][0] != null) {
			e1 = a.epsilon[state][0];
			ef.add(e1);
			ef.addAll(epsilonFermeture(a, e1));
		}

		if (a.epsilon[state][1] != null) {
			e2 = a.epsilon[state][1];
			ef.add(e2);
			ef.addAll(epsilonFermeture(a, e2));
		}
		return ef;

	}

	public static Set<Integer> alphaFermeture(Automat a, Integer state, Integer alpha) {

		Set<Integer> af = new HashSet<Integer>();
		if (visited.contains(state))
			return af;

		visited.add(state);

		Integer e = a.auto[state][alpha];
		if (e != null) {
			af.add(e);
			af.addAll(epsilonFermeture(a, e));

			int i = 0;
			int siz = af.size();
			while (i < siz) {
				Iterator<Integer> it = af.iterator();
				for (int j = 0; j < i; j++) {
					it.next();
				}
				Integer item = it.next();
				af.addAll(alphaFermeture(a, item, alpha));
				siz = af.size();
				i++;
			}
		}

		return af;
	}

	public static String toStringMaison(Set<Integer> ef) {

		String et = "";
		for (Integer ie : ef) {
			et += ie + ",";
		}
		if (et.length() > 1) {
			et = et.substring(0, et.length() - 1);
		}

		return et;
	}

	public static boolean equalsStats(String e1, String e2) {

		char[] t1 = e1.trim().toCharArray();
		Arrays.sort(t1);
		char[] t2 = e2.trim().toCharArray();
		Arrays.sort(t2);

		if (t1.length != t2.length) {
			return false;
		}

		for (int i = 0; i < t1.length; i++) {
			if (t1[i] != t2[i]) {

				return false;

			}
		}
		return true;
	}

	public static boolean containsMaison(ArrayList<String> explored, String et) {
		for (String e : explored) {
			if (equalsStats(e, et)) {
				return true;
			}

		}
		return false;
	}

	public static Automat makeDeterministe(Automat a) {

		ArrayList<String> etats = new ArrayList<>();
		ArrayList<String> explored = new ArrayList<>();
		Set<Integer> ef = epsilonFermeture(a, 0);
		ef.add(0);
		String etat = toStringMaison(ef);
		Map<String, ArrayList<Tuple>> trans = new HashMap<>();
		etats.add(etat);
		int siz = etats.size();
		int k = 0;
		while (k < siz) {
			String et = etats.get(k);
			if (!containsMaison(explored, et)) {
				explored.add(et);
				for (int alpha = 0; alpha < a.auto[0].length; alpha++) {

					Set<Integer> af = new HashSet<>();
					String[] ets = et.split(",");

					for (int i = 0; i < ets.length; i++) {
						String c = ets[i];
						Set<Integer> tmp = alphaFermeture(a, Integer.parseInt(c), alpha);
						af.addAll(tmp);
					}

					visited.clear();
					String newEtat = toStringMaison(af);
					if (newEtat.length() > 0) {

						etats.add(newEtat);
						siz++;
						if (trans.containsKey(et)) {
							trans.get(et).add(new Tuple(newEtat, alpha));
						} else {
							ArrayList<Tuple> l = new ArrayList<>();
							l.add(new Tuple(newEtat, alpha));
							trans.put(et, l);
						}
					}

				}
			}
			k++;
		}

		Integer[][] auto = new Integer[explored.size()][a.auto[0].length];
		Automat automat = new Automat(a.root, null, null);
		automat.fin = new boolean[explored.size()];
		automat.init = new boolean[explored.size()];
		Map<String, Integer> dic = new HashMap<>();

		for (int i = 0; i < explored.size(); i++) {
			dic.put(explored.get(i), i);
			for (int j = 0; j < a.fin.length; j++) {
				String[] tab = explored.get(i).split(",");
				for (int l = 0; l < tab.length; l++) {
					if (tab[l].equals(String.valueOf(j)) && a.fin[j]) {
						automat.fin[i] = true;
					}
				}

			}
			if (!automat.fin[i]) {
				automat.fin[i] = false;
			}
		}

		for (Entry<String, ArrayList<Tuple>> e : trans.entrySet()) {
			String et = e.getKey();
			for (int i = 0; i < e.getValue().size(); i++) {
				int alpha = e.getValue().get(i).alpha;
				String etatDest = e.getValue().get(i).etat;
				auto[dic.get(et)][alpha] = dic.get(etatDest);
			}
		}

		automat.auto = auto;

		automat.init[0] = true;
		for (int i = 1; i < automat.auto.length; i++) {
			automat.init[i] = false;
		}

		return automat;

	}

	public int match(String word) {
		int currentState = 0;
		int index = 0;
		int matchLength = -1;
		if (automat.fin[currentState])
			matchLength = index;
		for (int i = 0; i < word.length(); i++) {
			Integer nextState = automat.auto[currentState][(int) word.charAt(i)];
			if (nextState == null)
				break;
			currentState = nextState;
			index++;
			if (automat.fin[currentState])
				matchLength = index;
		}
		return matchLength;
	}



	public int matchAll(String ligne, int l) {
		int matchResult = 0;
		ligne = cleanText(ligne);
		int i = 0;
		int size = ligne.length();
		while (i < size) {
			String tmp = ligne.substring(i, size);
			int resMatch = match(tmp);
			if(resMatch != -1) {
				matchResult += 1;
				i += resMatch;
			}
			else {
				i++;
			}
		}
		return matchResult;
	}
}
