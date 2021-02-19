package amg.team.runningpp.services;


import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import amg.team.runningpp.R;
import amg.team.runningpp.data.Constants;
import amg.team.runningpp.data.ListAktivnost;
import amg.team.runningpp.entities.Aktivnost;
import amg.team.runningpp.view.ActivityStart;


public class AktivnostsServices extends AsyncTask<Void,Void,String> {

    Activity activity;
    ListAktivnost listAktivnost = ListAktivnost.getInstance();

    public AktivnostsServices(Activity activity){
        this.activity = activity;
    }
    @Override
    protected String doInBackground(Void... voids) {
        try {

            URL url = new URL(Constants.URL+"/"+Constants.VRATI_AKTIVNOST);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            StringBuilder sb=new StringBuilder();
            InputStreamReader isr=new InputStreamReader(con.getInputStream());
            BufferedReader br=new BufferedReader(isr);
            String json;

            while((json=br.readLine())!=null)
            {
                sb.append(json+"\n");

            }
            return sb.toString().trim();
        }

        catch(Exception e)
        {

            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        try {
            JSONArray jsonArray = new JSONArray(s);
            Aktivnost aktivnost;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                aktivnost = new Aktivnost();
                aktivnost.setId(Integer.parseInt(jsonObject.getString("id")));
                aktivnost.setVidljivost(Integer.parseInt(jsonObject.getString("vidljivost")));
                aktivnost.setIdKorisnika(Integer.parseInt(jsonObject.getString("idKorisnika")));
                aktivnost.setKilometraza(Float.parseFloat(jsonObject.getString("kilometraza")));
                aktivnost.setProsecnaBrzina(Float.parseFloat(jsonObject.getString("prosecnaBrzina")));
                aktivnost.setMaxBrzina(Float.parseFloat(jsonObject.getString("maxBrzina")));
                aktivnost.setNaziv(jsonObject.getString("naziv"));
                aktivnost.setVremePocetka(jsonObject.getString("vremePocetka"));
                aktivnost.setVremeZavrsetka(jsonObject.getString("vremeZavrsetka"));
                aktivnost.setUrlSlika(jsonObject.getString("urlSlika"));
                listAktivnost.addAktivnost(aktivnost);
            }
            ((ActivityStart)activity).dialogHide();
            for (Aktivnost a: ListAktivnost.getInstance().getList()) {
                if(a.getUrlSlika().equals("slika.jpg")) {
                    a.setSlika(BitmapFactory.decodeResource(activity.getResources(), R.drawable.slika));
                }
                else {
                    GetImageService getImageService = new GetImageService(a);
                    getImageService.execute(a.getUrlSlika());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
