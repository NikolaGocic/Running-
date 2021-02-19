package amg.team.runningpp.entities;

import android.graphics.Bitmap;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Aktivnost {
    int id;
    String vremePocetka;
    String vremeZavrsetka;
    String naziv;
    float kilometraza;
    float prosecnaBrzina;
    float maxBrzina;
    int vidljivost;
    String urlSlika;
    int idKorisnika;
    Bitmap slika;



    public Aktivnost() {
        this.id = 0;
        this.vremePocetka = "";
        this.vremeZavrsetka = "";
        this.naziv = "";
        this.kilometraza = 0;
        this.prosecnaBrzina = 0;
        this.maxBrzina = 0;
        this.vidljivost = 1;
        this.urlSlika = "";
        this.idKorisnika = 0;
    }

    public String getFormatedString(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = simpleDateFormat.parse(this.getVremePocetka());
            d2 = simpleDateFormat.parse(this.getVremeZavrsetka());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diff = d2.getTime() -d1.getTime();
        diff /= 1000;
        String tekst = String.format("%02d:%02d:%02d", diff / 3600, (diff % 3600) / 60, (diff % 60));
        String formatedText = "Average Speed : "+this.getProsecnaBrzina()+" km/h\n"+
                "Distance : "+this.getKilometraza()+" km\n"+
                "Max Speed : "+this.getMaxBrzina()+" km/h\n"+
                "Duration Of Activity : "+tekst+"\n";
        return formatedText;
    }

    public String getFormatedStringWihCalories(Korisnik korisnik){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = simpleDateFormat.parse(this.getVremePocetka());
            d2 = simpleDateFormat.parse(this.getVremeZavrsetka());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diff = d2.getTime() -d1.getTime();
        diff /= 1000;
        float cal = 0;
        int age = new Date().getYear() - Integer.parseInt(korisnik.getDatumRodjenja().substring(0,3));
        if (korisnik.getPol().equals("M"))
            cal  = getCalories(diff,korisnik.getTezina(),korisnik.getVisina(), 20,true);
        else
            cal  = getCalories(diff,korisnik.getTezina(),korisnik.getVisina(), 20,false);

        String stringCal = String.format("%.2f", cal);
        String tekst = String.format("%02d:%02d:%02d", diff / 3600, (diff % 3600) / 60, (diff % 60));
        String formatedText = "Average Speed : "+this.getProsecnaBrzina()+" km/h\n"+
                "Distance : "+this.getKilometraza()+" km\n"+
                "Max Speed : "+this.getMaxBrzina()+" km/h\n"+
                "Duration Of Activity : "+tekst+"\n"+
                "Calories : "+stringCal+" kcal\n";
        return formatedText;
    }

    public float getCalories(long time, int weight, int height, int age, boolean gender)
    {
        float calories=0;
        float METS = (float)(3.5+this.prosecnaBrzina*60*0.2)/3.5f;
        float BRM;
        if(gender)
            BRM=10*weight+6.25f*height-5*age+5;
        else
            BRM=10*weight+6.25f*height-5*age-161;
        calories=((METS*BRM*time)/3600)/24;
        Log.i("Counting calories  : ","Time : "+time+" Weight : "+weight+" Height : "+height+" Age : "+age+" Gender : "+gender
        +"Average Speed : "+prosecnaBrzina+" METS : "+METS+" BRM : "+BRM+" Calories : "+calories);
        return calories;
    }

    public Bitmap getSlika() {
        return slika;
    }

    public void setSlika(Bitmap slika) {
        this.slika = slika;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVremePocetka() {
        return vremePocetka;
    }

    public void setVremePocetka(String vremePocetka) {
        this.vremePocetka = vremePocetka;
    }

    public String getVremeZavrsetka() {
        return vremeZavrsetka;
    }

    public void setVremeZavrsetka(String vremeZavrsetka) {
        this.vremeZavrsetka = vremeZavrsetka;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public float getKilometraza() {
        return kilometraza;
    }

    public void setKilometraza(float kilometraza) {
        this.kilometraza = kilometraza;
    }

    public float getProsecnaBrzina() {
        return prosecnaBrzina;
    }

    public void setProsecnaBrzina(float prosecnaBrzina) {
        this.prosecnaBrzina = prosecnaBrzina;
    }

    public float getMaxBrzina() {
        return maxBrzina;
    }

    public void setMaxBrzina(float maxBrzina) {
        this.maxBrzina = maxBrzina;
    }

    public int getVidljivost() {
        return vidljivost;
    }

    public void setVidljivost(int vidljivost) {
        this.vidljivost = vidljivost;
    }

    public String getUrlSlika() {
        return urlSlika;
    }

    public void setUrlSlika(String urlSlika) {
        this.urlSlika = urlSlika;
    }

    public int getIdKorisnika() {
        return idKorisnika;
    }

    public void setIdKorisnika(int idKorisnika) {
        this.idKorisnika = idKorisnika;
    }
}
