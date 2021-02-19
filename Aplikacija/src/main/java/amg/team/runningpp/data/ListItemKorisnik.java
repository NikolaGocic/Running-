package amg.team.runningpp.data;

public class ListItemKorisnik {
    String ime;
    String prezime;
    String username;
    public ListItemKorisnik() {
        ime= "";
        prezime="";
        username = "";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }
}
