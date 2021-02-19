package amg.team.runningpp.services;


import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import amg.team.runningpp.data.Constants;
import amg.team.runningpp.data.ListKorisnik;
import amg.team.runningpp.entities.Korisnik;


public class KorisniksServices extends AsyncTask<Void,Void,String> {
    ListKorisnik listKorisnik = ListKorisnik.getInstance();
    @Override
    protected String doInBackground(Void... voids) {
        try {

            URL url = new URL(Constants.URL+"/"+Constants.VRATI_KORISNIKE);
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
            Korisnik korisnik;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                korisnik = new Korisnik();
                korisnik.setId(Integer.parseInt(obj.getString("id")));
                korisnik.setIme(obj.getString("ime"));
                korisnik.setPrezime(obj.getString("prezime"));
                korisnik.setDatumRodjenja(obj.getString("datumRodjenja"));
                korisnik.setTezina(Integer.parseInt(obj.getString("tezina")));
                korisnik.setVisina(Integer.parseInt(obj.getString("visina")));
                korisnik.setUsername(obj.getString("username"));
                korisnik.setPassword(obj.getString("password"));
                korisnik.setEmail(obj.getString("email"));
                korisnik.setPol(obj.getString("pol"));
                korisnik.setAvatar(obj.getInt("avatar"));
                listKorisnik.addKorisnik(korisnik);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
