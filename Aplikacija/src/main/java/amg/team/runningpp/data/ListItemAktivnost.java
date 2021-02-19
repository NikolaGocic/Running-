package amg.team.runningpp.data;

import android.graphics.Bitmap;

public class ListItemAktivnost {

    private int icon;
    private String Tekst;
    private String imePrezime;
    private Bitmap bitmap;
    private String datum;

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public ListItemAktivnost(){

    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getImePrezime() {
        return imePrezime;
    }

    public void setImePrezime(String imePrezime) {
        this.imePrezime = imePrezime;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getTekst() {
        return Tekst;
    }

    public void setTekst(String tekst) {
        Tekst = tekst;
    }


}
