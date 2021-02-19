package amg.team.runningpp.services;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

import amg.team.runningpp.data.Constants;
import amg.team.runningpp.entities.Korisnik;
import amg.team.runningpp.view.FragmentLogin;

public class CheckEmailService extends AsyncTask<String,Void,String> {
    Fragment fragment;
    Korisnik korisnik;

    public CheckEmailService(Fragment fragment, Korisnik korisnik){
        this.fragment = fragment;
        this.korisnik = korisnik;
    }

    @Override
    protected void onPostExecute(String s) {
        if(s.equals("Success")){
            ((FragmentLogin)fragment).emailCheckSuccess(korisnik);
        }
        else
            ((FragmentLogin)fragment).emailCheckFailed(korisnik);

    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            String post_data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(strings[0], "UTF-8");
            URL url = new URL(Constants.URL+"/"+Constants.EMAIL_CHECK);
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
        catch (MalformedURLException e){
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  "Failed";
    }
}


