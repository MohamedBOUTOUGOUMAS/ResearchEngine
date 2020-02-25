package main.service.utils;

import java.io.*;

public class Serialization {
    public static void serialize(String path, String filename, Object objectToSerialize){
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(new File(path+"/"+filename));
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            System.out.println("Write the object to be serialized");
            oos.writeObject(objectToSerialize);
            fos.close();
            System.out.println("Serialization end");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object deserialize(String path, String filename){
        System.out.println("Deserialization of "+path+"/"+filename);
        FileInputStream fs;
        Object map = null;
        try {
            fs = new FileInputStream(new File(path+"/"+filename));
            ObjectInputStream ss = new ObjectInputStream(fs);
            map = ss.readObject();
            ss.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return map;
    }
}
