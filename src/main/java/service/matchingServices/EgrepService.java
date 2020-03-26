package service.matchingServices;

import service.matchingServices.AEF.RegEx;
import service.matchingServices.KMP.KMP;
import topics.Book;
import helpers.GenericHelper;
import topics.ResearchResult;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class EgrepService {

    public static ResearchResult matchAllWords(String word, String fileName, RegEx regEx) {
        String filePath = GenericHelper.BOOKS_PATH + "/" + fileName;
        int linesMatched = 0;

        Book book = new Book(fileName);
        BufferedReader lecteurAvecBuffer;
        try {
            lecteurAvecBuffer = new BufferedReader(new FileReader(filePath));
            String ligne;
            boolean findTitle = false;
            boolean findAuthor = false;
            boolean findReleaseDate = false;
            int l = 1;
            while ((ligne = lecteurAvecBuffer.readLine()) != null) {

                if (!findTitle) findTitle = GenericHelper.decorateBookWithTitle(ligne, book, findTitle);
                if (!findAuthor) findAuthor = GenericHelper.decorateBookWithAuthor(ligne, book, findAuthor);
                if (!findReleaseDate)
                    findReleaseDate = GenericHelper.decorateBookWithReleaseDate(ligne, book, findReleaseDate);

                if (regEx != null)
                    linesMatched += matchLineWithAutomat(ligne, l, regEx);
                else
                    linesMatched += matchLineWithKMP(word, ligne, l);

                l++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResearchResult(book, linesMatched);
    }

    public static int matchLineWithAutomat(String line, int nbLine, RegEx regEx) {
        return regEx.aef.matchAll(line, nbLine);
    }

    public static int matchLineWithKMP(String word, String line, int nbLine) {
        return KMP.matchAll(word.toCharArray(), line.toCharArray(), nbLine);
    }


    public static ResearchResult matchAllWordsFast(String word, String fileName, int firstLetter, RegEx regEx, int[] retenue) {
        int nbMatched = 0;
        Book book = Book.getEmptyBook(fileName);
        BufferedReader lecteurAvecBuffer;
        String ligne;
        try {
            lecteurAvecBuffer = new BufferedReader(new FileReader(GenericHelper.INDEXES_TABLES_PATH + "/" + fileName + "/" + firstLetter + ".txt"));
            while ((ligne = lecteurAvecBuffer.readLine()) != null) {
                if (!ligne.equals("") && !ligne.equals("\n")) {
                    String[] ar = ligne.split(" ");
                    if (ar.length != 0) {
                        String toMatch = ar[0];
                        int nbOccurs = Integer.parseInt(ar[1]);
                        if (regEx != null) {
                            int nbWords = matchLineWithAutomatFast(regEx, toMatch, nbOccurs);
                            if (nbMatched != 0 && nbWords == 0) break;
                            nbMatched += nbWords;
                        } else {
                            int nbWords = matchLineWithKMPFast(word, retenue, toMatch, nbOccurs);
                            if (nbMatched != 0 && nbWords == 0) break;
                            nbMatched += nbWords;
                        }
                    }
                }
            }
            lecteurAvecBuffer.close();

        } catch (IOException exc) {
        }

        return new ResearchResult(book, nbMatched);
    }

    public static int matchLineWithAutomatFast(RegEx regEx, String word, int nbOccurs) {
        return regEx.aef.matchAllFast(word, nbOccurs);
    }

    public static int matchLineWithKMPFast(String pattern, int[] retenue, String word, int nbOccurs) {
        return KMP.matchAllFast(pattern.toCharArray(), retenue, word, nbOccurs);
    }
}
