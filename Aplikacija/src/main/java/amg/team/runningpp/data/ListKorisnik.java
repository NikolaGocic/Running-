package amg.team.runningpp.data;

import android.annotation.TargetApi;
import android.os.Build;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import amg.team.runningpp.entities.Korisnik;


public class ListKorisnik {

    private List<Korisnik> listKorisnik;

    private static ListKorisnik instance;

    private ListKorisnik() {
        this.listKorisnik = new ArrayList<Korisnik>();
    }

    public static ListKorisnik getInstance() {
        if(instance == null){
            instance = new ListKorisnik();
        }
        return instance;
    }

    public String getImePrezime(int i){
        return listKorisnik.get(i).getIme()+ " "+listKorisnik.get(i).getPrezime();
    }

    public String getImePrezimeKorisnikID(int i){
        return getKorisnikWithID(i).getIme()+ " "+getKorisnikWithID(i).getPrezime();
    }

    public void addKorisnik(Korisnik k){
        listKorisnik.add(k);
    }

    public Korisnik getKorisnikWithUsername(String username){
        for(int i = 0;i<listKorisnik.size();i++){
            if(listKorisnik.get(i).getUsername().equals(username))
                return listKorisnik.get(i);
        }
        return null;
    }

    public void removeKorisnikWithUsername(String username){
        for(int i = 0;i<listKorisnik.size();i++){
            if(listKorisnik.get(i).getUsername().equals(username))
                listKorisnik.remove(i);
        }
    }

    public void setKorisnik(Korisnik korisnik){
        for(int i = 0;i<listKorisnik.size();i++){
            if(listKorisnik.get(i).getUsername().equals(korisnik.getUsername())){
                listKorisnik.set(i,korisnik);
                return;
            }
        }
        listKorisnik.add(korisnik);
    }

    public void addFriend(Integer friednID,Integer korisnikID){
        for(int i = 0;i<listKorisnik.size();i++){
            if(listKorisnik.get(i).getId() == korisnikID){
                listKorisnik.get(i).addFriend(friednID);
                return;
            }
        }
        return;
    }

    public Korisnik getKorisnikWithID(Integer korisnikID){
        for(int i = 0;i<listKorisnik.size();i++){
            if(listKorisnik.get(i).getId() == korisnikID){
                return listKorisnik.get(i);
            }
        }
        return null;
    }

    public List<Korisnik> getListKorisnik(){
        return  this.listKorisnik;
    }

    public int getLength(){
        return listKorisnik.size();
    }

    public Korisnik getKorisnik(int i){
        return  listKorisnik.get(i);
    }

    public void clearList(){
        for(int i =listKorisnik.size()-1;i>=0;i--){
            listKorisnik.remove(i);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public String getHashedPassword(String password){
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.update(("running++").getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return generatedPassword;
    }
}
