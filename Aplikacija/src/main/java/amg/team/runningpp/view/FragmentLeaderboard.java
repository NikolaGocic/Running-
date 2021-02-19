package amg.team.runningpp.view;

import android.icu.text.RelativeDateTimeFormatter;
import java.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.time.Period;
import java.util.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import amg.team.runningpp.R;
import amg.team.runningpp.controller.AdapterListItemAktivnostKorisnik;
import amg.team.runningpp.controller.AdapterListItemAktivnostLeaderboard;
import amg.team.runningpp.controller.AdapterListItemKorisnik;
import amg.team.runningpp.data.ListAktivnost;
import amg.team.runningpp.data.ListItemAktivnost;
import amg.team.runningpp.data.ListItemKorisnik;
import amg.team.runningpp.data.ListKorisnik;
import amg.team.runningpp.entities.Aktivnost;
import amg.team.runningpp.entities.Korisnik;

public class FragmentLeaderboard extends Fragment {

    Spinner spinnerSort;
    Spinner spinnerGroup;
    ListView listView;
    List<Aktivnost> listAktivnost;
    int tipSort = 0;
    int tipGroup = 0;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard,container,false);
        try {
            spinnerSort = view.findViewById(R.id.spinnerSort);
            spinnerGroup = view.findViewById(R.id.spinnerGroup);
            listView = view.findViewById(R.id.listView);
            listAktivnost = new ArrayList<>();


            final String podaciSort[] = new String[]{"Distance", "Average Speed", "Time"};
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, podaciSort);
            spinnerSort.setAdapter(adapter);

            final String podaciGroup[] = new String[]{"Day", "Week", "Month"};
            adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, podaciGroup);
            spinnerGroup.setAdapter(adapter);

            spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    tipSort = position;
                    doJob();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            spinnerGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    tipGroup = position;
                    doJob();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return view;
    }

    public void doJob(){
        try {
            List<ListItemAktivnost> list = new ArrayList<>();
            if (tipGroup == 0) {
                listAktivnost = ListAktivnost.getInstance().getAktivnostsByDay();
            }
            else if(tipGroup == 1){
                listAktivnost = ListAktivnost.getInstance().getAktivnostsByWeek();
            }
            else if (tipGroup == 2) {
                listAktivnost = ListAktivnost.getInstance().getAktivnostsByMonth();
            }
            if(tipSort == 0){
                Collections.sort(listAktivnost, new Comparator<Aktivnost>() {
                    @Override
                    public int compare(Aktivnost o1, Aktivnost o2) {
                        return Double.valueOf(o2.getKilometraza()).compareTo(Double.valueOf(o1.getKilometraza()));
                    }
                });
                for (int i = 0; i < listAktivnost.size(); i++) {
                    ListItemAktivnost listItemAktivnost = new ListItemAktivnost();
                    listItemAktivnost.setImePrezime(String.valueOf(i+1)+") " +
                            ListKorisnik.getInstance().getImePrezimeKorisnikID(listAktivnost.get(i).getIdKorisnika()));
                    listItemAktivnost.setTekst(String.valueOf(listAktivnost.get(i).getKilometraza()) + " km");
                    list.add(listItemAktivnost);
                }
            }
            else if(tipSort == 1){
                Collections.sort(listAktivnost, new Comparator<Aktivnost>() {
                    @Override
                    public int compare(Aktivnost o1, Aktivnost o2) {
                        return Double.valueOf(o2.getProsecnaBrzina()).compareTo(Double.valueOf(o1.getProsecnaBrzina()));
                    }
                });
                for (int i = 0; i < listAktivnost.size(); i++) {
                    ListItemAktivnost listItemAktivnost = new ListItemAktivnost();
                    listItemAktivnost.setImePrezime(String.valueOf(i+1)+") " +
                            ListKorisnik.getInstance().getImePrezimeKorisnikID(listAktivnost.get(i).getIdKorisnika()));
                    listItemAktivnost.setTekst(String.valueOf(listAktivnost.get(i).getProsecnaBrzina()) + " km/h");
                    list.add(listItemAktivnost);
                }
            }
            else if(tipSort == 2){
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
                for (int i = 0; i < listAktivnost.size(); i++) {
                    ListItemAktivnost listItemAktivnost = new ListItemAktivnost();
                    listItemAktivnost.setImePrezime(String.valueOf(i+1)+") " +
                            ListKorisnik.getInstance().getImePrezimeKorisnikID(listAktivnost.get(i).getIdKorisnika()));
                    String tekst = "";
                    try {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date d1 = simpleDateFormat.parse(listAktivnost.get(i).getVremePocetka());
                        Date d2 = simpleDateFormat.parse(listAktivnost.get(i).getVremeZavrsetka());
                        long diff = d2.getTime() -d1.getTime();
                        tekst = String.valueOf(diff/(1000*60));
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    listItemAktivnost.setTekst(tekst+" min");
                    list.add(listItemAktivnost);
                }

            }
            AdapterListItemAktivnostLeaderboard adapterListItemAktivnostLeaderboard = new AdapterListItemAktivnostLeaderboard(getContext(), R.layout.list_view_item_aktivnost_leaderboard, list);
            listView.setAdapter(adapterListItemAktivnostLeaderboard);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
