package amg.team.runningpp.services;

import android.app.Activity;
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
import amg.team.runningpp.view.ActivityStart;
import amg.team.runningpp.view.FragmentLogin;

public class LoginKorisnikService extends AsyncTask<String,Void,String> {

    Activity activity = null;
    Fragment fragment = null;
    String type = "";//tip 1 aktivnost, tip 2 fragment, moze i sa instaceof ali ovako smo se mi osigurali

    public LoginKorisnikService(Activity activity,String type){
        this.activity = activity;
        this.type = type;
    }
    public LoginKorisnikService(Fragment fragment,String type){
        this.fragment = fragment;
        this.type = type;
    }

    @Override
    protected void onPostExecute(String s) {
        try {
            if (s.equals("Success")) {
                if (type.equals("1")) {
                    ((ActivityStart) activity).successLogIn();
                } else if (type.equals("2")) {
                    ((FragmentLogin) fragment).successLogIn();
                }
            } else {
                if (type.equals("1")) {
                    ((ActivityStart) activity).failedLogin();
                } else if (type.equals("2")) {
                    ((FragmentLogin) fragment).failedLogin();
                }
            }
            ((ActivityStart)activity).dialogHide();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            String post_data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(strings[0], "UTF-8") + "&" +
                    URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(strings[1], "UTF-8");
            URL url = new URL(Constants.URL+"/"+Constants.LOGIN);
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
