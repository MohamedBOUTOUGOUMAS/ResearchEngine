package service.utils;

import org.apache.tomcat.util.http.fileupload.IOUtils;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class CleanData {

    private static void booksToIndex() {
        ArrayList<String> files = Helper.readBooks(Helper.BOOKS_PATH);
        for (String file : files) {
            System.out.println(file);
            storeIndexTable(getAllWordsFromFile(file), file);
        }
    }

    private static void storeIndexTable(Map<String, Integer> map, String file) {
        try {
            FileWriter fw = new FileWriter(Helper.BOOKS_INDEX + "/" + file);
            BufferedWriter bw = new BufferedWriter(fw);
            for (String word : map.keySet()) {
                bw.write(word + " " + map.get(word) + "\n");
            }
            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, Integer> getAllWordsFromFile(String filename) {
        Map<String, Integer> words = new HashMap<>();
        String ligne;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(Helper.BOOKS_PATH + "/" + filename));
            while ((ligne = reader.readLine()) != null) {
                if (!ligne.equals("") && !ligne.equals("\n")) {
                    String[] array = ligne.split("[^a-zA-Z]");
                    if (array.length != 0) {
                        for (int i = 0; i < array.length; i++) {
                            if (!array[i].equals("") && !array[i].equals("\n")) {
                                if (words.containsKey(array[i])) {
                                    words.put(array[i], words.get(array[i]) + 1);
                                } else {
                                    words.put(array[i], 1);
                                }
                            }
                        }
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;
    }

    public static void createBooks(){
        List<String> books = Helper.readBooks(Helper.BOOKS_PATH);
        int i = 1;
        for (String book: books){
            System.out.println("i "+i);
            if(i > Helper.NB_BOOKS) break;
            Book b = Book.getEmptyBook(book);
            if (b.fileName != null && b.author != null && b.title != null && b.releaseDate != null){
                try{
                    InputStream input = new FileInputStream(Helper.BOOKS_PATH+"/"+book);
                    OutputStream output = new FileOutputStream(Helper.BOOKS_CLEAN_PATH+"/"+book);
                    IOUtils.copy(input, output);
                    i++;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
    public static boolean hasTitle(String fileName) {
        BufferedReader lecteurAvecBuffer;
        try {
            lecteurAvecBuffer = new BufferedReader(new FileReader(Helper.BOOKS_PATH + "/" + fileName));
            String line;
            while ((line = lecteurAvecBuffer.readLine()) != null) {
                if (Helper.getTitleFromFile(line) != null) return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Set<String> rejectedFiles() {
        File repertoire = new File(Helper.BOOKS_PATH);
        ArrayList<String> files = (ArrayList<String>) Arrays.stream(repertoire.list()).collect(Collectors.toList());
        Set<String> rejectedFiles = files.stream()
                .filter(file -> !hasTitle(file))
                .collect(Collectors.toSet());
        return rejectedFiles;
    }

    public static void main(String[] args) {
        //booksToIndex();
        createBooks();
    }
}
