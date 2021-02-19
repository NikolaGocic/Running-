package amg.team.runningpp.services;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;

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
import amg.team.runningpp.view.FragmentEditProfile;
import amg.team.runningpp.view.FragmentKorisnikDeleteUpdate;

public class AdminUpdateKorisnikService extends AsyncTask<String,Void,String> {

    Fragment fragment;

    public AdminUpdateKorisnikService(Fragment fragment){
        this.fragment = fragment;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            String post_data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(params[0], "UTF-8") + "&" +
                    URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8")+ "&" +
                    URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(params[2], "UTF-8")+ "&" +
                    URLEncoder.encode("ime", "UTF-8") + "=" + URLEncoder.encode(params[3], "UTF-8")+ "&" +
                    URLEncoder.encode("prezime", "UTF-8") + "=" + URLEncoder.encode(params[4], "UTF-8")+ "&" +
                    URLEncoder.encode("datum", "UTF-8") + "=" + URLEncoder.encode(params[5], "UTF-8")+ "&" +
                    URLEncoder.encode("tezina", "UTF-8") + "=" + URLEncoder.encode(params[6], "UTF-8")+ "&" +
                    URLEncoder.encode("visina", "UTF-8") + "=" + URLEncoder.encode(params[7], "UTF-8")+ "&" +
                    URLEncoder.encode("pol", "UTF-8") + "=" + URLEncoder.encode(params[8], "UTF-8");
            URL url = new URL(Constants.URL+"/"+Constants.ADMIN_UPDATE_KORISNIK);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
            String result = "";
            String line;
            while( (line = bufferedReader.readLine()) != null){
                result += line;
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();

            return  result;
        }

        catch(Exception e)
        {

            e.printStackTrace();
            return "Failed";
        }
    }

    @Override
    protected void onPostExecute(String s) {
        if(s.equals("Success")){
            ((FragmentKorisnikDeleteUpdate)fragment).successUpdate();
        }
        else
            ((FragmentKorisnikDeleteUpdate)fragment).failedUpdate();
    }
}

