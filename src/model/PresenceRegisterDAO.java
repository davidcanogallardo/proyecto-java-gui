package model;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.TreeSet;

public class PresenceRegisterDAO {
    public ArrayList<Presence> map = new ArrayList<>();

    public ArrayList<Presence> getMap() {
        return map;
    }

    public Presence add(Presence obj) {
        for (Presence presence : map) {
            if (presence.getId().equals(obj.getId()) && presence.getLeaveTime() == null) {
                System.out.println("no añado");
                return null;
            }
        }
        this.map.add(obj);
        System.out.println("añado");        
        return obj;
    }

    public boolean addLeaveTime(int id) {
        for (Presence presence : this.map) {
            if (presence.getId().equals(id) && presence.getLeaveTime() == null) {
                presence.setLeaveTime(LocalDateTime.now());
                return true;
            }
        }
        return false;
    }

    public void list() {
        for (Presence presence : map) {
            System.out.println(presence.toString());
        }
    }

    public void save() throws IOException {
        System.out.println("guardando222");
        try {
            FileOutputStream fos = new FileOutputStream("presence.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this.map);
            oos.close();
            System.out.println("presencia guardada");
         } catch(Exception e) {
            System.out.println("Error Occurred : " + e.getMessage());
         }
    }

    public void load() throws IOException {
        System.out.println("cargando....");
        FileInputStream fis = new FileInputStream("presence.dat");
        try {
            ObjectInputStream ois = new ObjectInputStream(fis);
            try {
                this.map = (ArrayList<Presence>) ois.readObject();
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            ois.close();
        } catch (Exception EOFException) {
            // TODO: handle exception
        }
    }
}
