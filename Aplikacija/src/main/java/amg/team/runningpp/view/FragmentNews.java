package amg.team.runningpp.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import amg.team.runningpp.R;
import amg.team.runningpp.controller.AdapterListItemAktivnost;
import amg.team.runningpp.data.Constants;
import amg.team.runningpp.data.ListAktivnost;
import amg.team.runningpp.data.ListItemAktivnost;
import amg.team.runningpp.data.ListKorisnik;

public class FragmentNews extends Fragment {
    ListView listView;
    SharedPreferences preferences;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news,container,false);

        listView = (ListView)view.findViewById(R.id.listView);
        preferences  = getActivity().getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);


        try{
            List<ListItemAktivnost> list = new ArrayList<ListItemAktivnost>();
            for(int i = ListAktivnost.getInstance().getLength()-1;i>=0;i--) {
                if (ListAktivnost.getInstance().getList().get(i).getSlika() != null) {
                    //proverava da li je aktivnost.getIdKorisnika prijatelj sa prijavljenim korisnikom
                    if(!preferences.getString(Constants.SHARED_PREFERENCES_USERNAME, "guest").equals("guest")) {
                        if (ListKorisnik.getInstance()
                                .getKorisnikWithUsername(preferences
                                        .getString(Constants.SHARED_PREFERENCES_USERNAME, "guest"))
                                .isFrinedWithId(ListAktivnost.getInstance().getList().get(i).getIdKorisnika())) {
                            ListItemAktivnost listItemAktivnost = new ListItemAktivnost();
                            listItemAktivnost.setImePrezime(ListKorisnik.getInstance()
                                    .getKorisnikWithID(ListAktivnost.getInstance().getList().get(i).getIdKorisnika())
                                    .getImePrezime());
                            listItemAktivnost.setBitmap(ListAktivnost.getInstance().getList().get(i).getSlika());
                            listItemAktivnost.setTekst(ListAktivnost.getInstance().getFormatedString(i));
                            listItemAktivnost.setDatum(ListAktivnost.getInstance().getStartDate(i));
                            listItemAktivnost.setIcon(ListKorisnik.getInstance().getKorisnikWithID
                                    (ListAktivnost.getInstance().getAktivnost(i).getIdKorisnika()).getResourceForImage());
                            list.add(listItemAktivnost);
                        }
                        //Proverava da li je aktivnost vidljiva svima
                        else if (ListAktivnost.getInstance().getList().get(i).getVidljivost() == 0) {
                            ListItemAktivnost listItemAktivnost = new ListItemAktivnost();
                            listItemAktivnost.setImePrezime(ListKorisnik.getInstance()
                                    .getKorisnikWithID(ListAktivnost.getInstance().getList().get(i).getIdKorisnika())
                                    .getImePrezime());
                            listItemAktivnost.setBitmap(ListAktivnost.getInstance().getList().get(i).getSlika());
                            listItemAktivnost.setTekst(ListAktivnost.getInstance().getFormatedString(i));
                            listItemAktivnost.setDatum(ListAktivnost.getInstance().getStartDate(i));
                            listItemAktivnost.setIcon(ListKorisnik.getInstance().getKorisnikWithID
                                    (ListAktivnost.getInstance().getAktivnost(i).getIdKorisnika()).getResourceForImage());
                            list.add(listItemAktivnost);
                        }

                    }
                    //Proverava da li je aktivnost vidljiva svima
                    else if (ListAktivnost.getInstance().getList().get(i).getVidljivost() == 0) {
                        ListItemAktivnost listItemAktivnost = new ListItemAktivnost();
                        listItemAktivnost.setImePrezime(ListKorisnik.getInstance()
                                .getKorisnikWithID(ListAktivnost.getInstance().getList().get(i).getIdKorisnika())
                                .getImePrezime());
                        listItemAktivnost.setBitmap(ListAktivnost.getInstance().getList().get(i).getSlika());
                        listItemAktivnost.setTekst(ListAktivnost.getInstance().getFormatedString(i));
                        listItemAktivnost.setDatum(ListAktivnost.getInstance().getStartDate(i));
                        listItemAktivnost.setIcon(ListKorisnik.getInstance().getKorisnikWithID
                                (ListAktivnost.getInstance().getAktivnost(i).getIdKorisnika()).getResourceForImage());
                        list.add(listItemAktivnost);
                    }
                }
            }
        AdapterListItemAktivnost adapterListItemAktivnost = new AdapterListItemAktivnost(getContext(),R.layout.list_view_item,list);
        listView.setAdapter(adapterListItemAktivnost);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return view;
    }
}
