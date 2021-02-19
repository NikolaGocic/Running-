package amg.team.runningpp.entities;

import java.util.ArrayList;
import java.util.List;

import amg.team.runningpp.R;
import amg.team.runningpp.data.ListAktivnost;

public class Korisnik {

    private int id;
    private String ime;
    private String prezime;
    private String datumRodjenja;
    private int tezina;
    private int visina;
    private String username;
    private String password;
    private String email;
    private String pol;
    private List<Integer> friendList;
    private List<Aktivnost> aktivnostList;
    private int avatar;

    public void setId(int id) {
        this.id = id;
    }

    public Korisnik() {
        this.id = 0;
        this.avatar = 0;
        this.ime = "";
        this.prezime = "";
        this.datumRodjenja = "";
        this.tezina = 0;
        this.visina = 0;
        this.username = "";
        this.password = "";
        this.email = "";
        this.pol = "";
        friendList = new ArrayList<>();
        aktivnostList = new ArrayList<>();
    }
    public String[] getListParams(){
        String[] p = new String[10];
        p[0] = this.username;
        p[1] = this.password;
        p[2] = this.email;
        p[3] = this.ime;
        p[4] = this.prezime;
        p[5] = this.datumRodjenja;
        p[6] = String.valueOf(this.tezina);
        p[7] = String.valueOf(this.visina);
        p[8] = this.pol;
        p[9] = String.valueOf(this.avatar);
        return  p;
    }
    public String getFormatedString(){
        return "?username="+this.username+"?password="+this.password+"?email="+this.email+
                "?ime="+this.ime+"?prezime="+this.prezime+"?datum="+this.datumRodjenja+"?tezina="+this.tezina+
                "?visina="+this.visina+"?pol="+this.pol+"?avatar="+this.avatar;
    }

    public int getResourceForImage(){
        if(this.avatar==1)
            return(R.drawable.s1);
        else if(this.avatar==2)
            return(R.drawable.s2);
        else if(this.avatar==3)
            return(R.drawable.s3);
        else if(this.avatar==4)
            return(R.drawable.s4);
        else if(this.avatar==5)
            return(R.drawable.s5);
        else if(this.avatar==6)
            return(R.drawable.s6);
        else if(this.avatar==7)
            return(R.drawable.s7);
        else if(this.avatar==8)
            return(R.drawable.s8);
        else if(this.avatar==9)
            return(R.drawable.s9);
        else
            return(R.drawable.s0);
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public String getImePrezime(){
        return this.getIme()+" "+this.getPrezime();
    }

    public List<Integer> getFriendList() {
        return friendList;
    }

    public void setFriendList(List<Integer> friendList) {
        this.friendList = friendList;
    }

    public void addFriend(Integer idKorisnik){
        friendList.add(idKorisnik);
    }

    public void findAktivnosts(){
        aktivnostList = new ArrayList<>();
        List<Aktivnost> pom = ListAktivnost.getInstance().getList();
        for(int i =0;i< pom.size();i++){
            if(this.id == pom.get(i).getIdKorisnika()){
                aktivnostList.add(pom.get(i));
            }
        }
    }

    public int getNumberAktivnost(){
        return aktivnostList.size();
    }

    public float getKilometers(){
        float pom = 0;
        for(int i = 0;i<aktivnostList.size();i++){
            pom+=aktivnostList.get(i).getKilometraza();
        }
        return pom;
    }

    public List<Aktivnost> getAktivnostList() {
        return aktivnostList;
    }

    public void setAktivnostList(List<Aktivnost> aktivnostList) {
        this.aktivnostList = aktivnostList;
    }

    public boolean isFrinedWith(Korisnik korisnik){
        for(int i = 0;i<friendList.size();i++){
            if(friendList.get(i) == korisnik.getId())
                return true;
        }
        return false;
    }

    public boolean isFrinedWithId(int id){
        for(int i = 0;i<friendList.size();i++){
            if(friendList.get(i) == id)
                return true;
        }
        return false;
    }

    public boolean removeFriend(int id){
        for(int i = 0;i<friendList.size();i++){
            if(friendList.get(i) == id) {
                friendList.remove(i);
                return true;
            }
        }
        return false;
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

    public String getDatumRodjenja() {
        return datumRodjenja;
    }

    public void setDatumRodjenja(String datumRodjenja) {
        this.datumRodjenja = datumRodjenja;
    }

    public int getTezina() {
        return tezina;
    }

    public void setTezina(int tezina) {
        this.tezina = tezina;
    }

    public int getVisina() {
        return visina;
    }

    public void setVisina(int visina) {
        this.visina = visina;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPol() {
        return pol;
    }

    public void setPol(String pol) {
        this.pol = pol;
    }

    public int getId() {
        return id;
    }
}
