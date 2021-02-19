package amg.team.runningpp.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import amg.team.runningpp.entities.Aktivnost;


public class ListAktivnost {
    private List<Aktivnost> listAktivnost;

    private static ListAktivnost instance;

    private ListAktivnost(){
        listAktivnost = new ArrayList<Aktivnost>();
    }

    public static ListAktivnost getInstance() {
        if(instance == null)
            instance = new ListAktivnost();
        return instance;
    }

    public String getFormatedString(int i){
        return listAktivnost.get(i).getFormatedString();
    }

    public List<Aktivnost> getAktivnostsByDay() throws  Exception{
        List<Aktivnost> retAKtivnost = new ArrayList<>();
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datum = simpleDateFormat.format(date);
        for(int i = 0;i<listAktivnost.size();i++){
            if(listAktivnost.get(i).getVremePocetka().substring(5,7).equals(datum.substring(5,7))) {
                if (listAktivnost.get(i).getVremePocetka().substring(8, 10).equals(datum.substring(8, 10))) {
                    retAKtivnost.add(listAktivnost.get(i));
                }
            }
        }
        return retAKtivnost;
    }

    public List<Aktivnost> getAktivnostsByMonth() throws  Exception{
        List<Aktivnost> retAKtivnost = new ArrayList<>();
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datum = simpleDateFormat.format(date);
        for(int i = 0;i<listAktivnost.size();i++){
            if(listAktivnost.get(i).getVremePocetka().substring(5,7).equals(datum.substring(5,7))){
                retAKtivnost.add(listAktivnost.get(i));
            }
        }
        return retAKtivnost;
    }

    public List<Aktivnost> getAktivnostsByWeek() throws  Exception{
        List<Aktivnost> retAKtivnost = new ArrayList<>();
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for(int i = 0;i<listAktivnost.size();i++){
            Date d1 = simpleDateFormat.parse(listAktivnost.get(i).getVremePocetka());
            long diff = 8*24*60*60*1000;
            if(d1.getTime()<date.getTime())
                diff = date.getTime() - d1.getTime();
            if((diff/1000)<(date.getDay()*24*60*60)){
                retAKtivnost.add(listAktivnost.get(i));
            }
        }
        return retAKtivnost;
    }

    public void sortListByDate(){
        Collections.sort(listAktivnost, new Comparator<Aktivnost>() {
            @Override
            public int compare(Aktivnost o1, Aktivnost o2) {
                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date d11 = simpleDateFormat.parse(o1.getVremePocetka());
                    Date d12 = simpleDateFormat.parse(o1.getVremeZavrsetka());
                    Date d21 = simpleDateFormat.parse(o2.getVremePocetka());
                    Date d22 = simpleDateFormat.parse(o2.getVremeZavrsetka());
                    Long d1 = d12.getTime()-d11.getTime();
                    Long d2 = d22.getTime() - d21.getTime();
                    return d2.compareTo(d1);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                return 0;
            }
        });
    }

    public String getStartDate(int i){
        return  listAktivnost.get(i).getVremePocetka();
    }
    public void addAktivnost(Aktivnost a){
        listAktivnost.add(a);
    }

    public int getLength(){
        return listAktivnost.size();
    }

    public List<Aktivnost> getList(){
        return  listAktivnost;
    }

    public Aktivnost getAktivnost(int i){
        return listAktivnost.get(i);
    }

    public void clearList(){
        for(int i =listAktivnost.size()-1;i>=0;i--){
            listAktivnost.remove(i);
        }
    }
}
