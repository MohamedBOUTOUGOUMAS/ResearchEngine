package main.service.KMP;

import main.service.utils.Position;
import java.util.ArrayList;

public class KMP {

	public static int match(char[] facteur, int[] retenue, char[] text) {
		int i = 0;
		int j = 0;

		while (i < text.length) {
			if (j == facteur.length)
				return (i - facteur.length);
			if (text[i] == facteur[j]) {
				i++;
				j++;
			} else if (retenue[j] == -1) {
				i++;
				j = 0;
			} else {
				j = retenue[j];
			}
		}
		if (j == facteur.length) {
			return i - j;
		} else {
			return -1;
		}
	}

	public static int match_Fast(char[] facteur, char[] text) {
		for (int i = 0; i <= text.length - facteur.length;) {
			int j = 0;
			int previ = i;
			for (; j < facteur.length; ++j) {
				if (facteur[j] != text[i]) {
					break;
				}
				i++;
			}
			if (j == facteur.length) {
				return previ;
			}

			i = previ + 1;
		}

		return -1;
	}

	public static int matchAll(char[] facteur, char[] text, int nbLine) {
		int matchResult = 0;
		int firstIndex = 0;
		int beginNewText = 0;
		while (firstIndex != -1 && beginNewText < text.length) {
			String str = new String(text);
			str = str.substring(beginNewText, text.length);
			char[] txt = str.toCharArray();
			int val = match_Fast(facteur, txt);
			//int val = match(facteur, retenue, txt);

			if (val == -1)
				break;
			firstIndex = beginNewText + val;
			beginNewText = firstIndex + facteur.length;
			matchResult += 1;
		}

		return matchResult;
	}

	public static int[] calculRetenue(char[] facteur) {
		String regex = "";
		int[] re = new int[facteur.length + 1];

		for (int i = 0; i < facteur.length; i++) {
			regex += facteur[i];
		}

		for (int i = 0; i < facteur.length; i++) {
			if (facteur[i] == facteur[0]) {
				re[i] = -1;
				continue;
			} else {
				re[i] = 0;
			}
			if (i > 2) {
				String prefSuf = "";
				for (int k = i - 1; k >= 0; k--) {
					int half = (k + 1) / 2;

					for (int nbcase = 1; nbcase <= half; nbcase++) {
						String str = "";
						for (int j = 0; j < nbcase + 1; j++) {
							str += facteur[k - nbcase + j];
						}
						String tmp = regex.substring(0, k - 1);

						if (tmp.contains(str) && prefSuf.length() < str.length()) {
							prefSuf = str;
							int index = regex.indexOf(prefSuf) + prefSuf.length();
							re[i] = index;
						}
					}
				}
			} else {
				re[i] = 0;
			}

		}

		for (int i = 0; i < facteur.length; i++) {
			int r = re[i];
			if (r != -1 && facteur[i] == facteur[r]) {
				re[i] = re[re[i]];
			}
		}

		return re;
	}


	public static int matchAllFast(char[] facteur, int[] retenue, String word, int nbOccurs) {
		char [] text = word.toCharArray();
		//int val = match_Fast(facteur, text);
		int val = match(facteur, retenue, text);
		if (val == -1) return 0;
		return nbOccurs;
	}

}
