package amg.team.runningpp.services;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import amg.team.runningpp.data.Constants;
import amg.team.runningpp.data.ListKorisnik;

public class ListFriendsServices extends AsyncTask<Void,Void,String>  {

    ListKorisnik listKorisnik = ListKorisnik.getInstance();

    @Override
    protected String doInBackground(Void... params) {
        try{
            URL url = new URL(Constants.URL+"/"+Constants.LIST_FRIENDS);
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
        catch (Exception e){
            e.printStackTrace();
        }
        return "Failed";
    }


    @Override
    protected void onPostExecute(String s) {
        try {
            JSONArray jsonArray = new JSONArray(s);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                listKorisnik.addFriend(Integer.parseInt(obj.getString("PrijateljID")),Integer.parseInt(obj.getString("KorisnikID")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
