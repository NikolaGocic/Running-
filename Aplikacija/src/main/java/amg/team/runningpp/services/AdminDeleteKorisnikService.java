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
import amg.team.runningpp.view.FragmentKorisnikDeleteUpdate;

public class AdminDeleteKorisnikService  extends AsyncTask<String,Void,String> {

    Fragment fragment;

    public AdminDeleteKorisnikService(Fragment fragment){
        this.fragment = fragment;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            String post_data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(params[0], "UTF-8") ;
            URL url = new URL(Constants.URL+"/"+Constants.ADMIN_DELETE_KORISNIK);
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
            ((FragmentKorisnikDeleteUpdate)fragment).removeKorisnikSuccess();
        }
        else {
            ((FragmentKorisnikDeleteUpdate)fragment).removeKorisnikFailed();
        }
    }
}
