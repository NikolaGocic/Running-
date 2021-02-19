package amg.team.runningpp.services;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import amg.team.runningpp.data.Constants;
import amg.team.runningpp.entities.Aktivnost;

public class GetImageService  extends AsyncTask<String, Bitmap,Void> {

    Aktivnost aktivnost;

    public GetImageService(Aktivnost aktivnost){
        this.aktivnost = aktivnost;
    }

    @Override
    protected Void doInBackground(String... strings) {
        try {
            URL url = new URL(Constants.URL + "/" + Constants.DOWNLOAD_IMAGE + "?URL=" + strings[0]);
            HttpURLConnection con = null;
            con = (HttpURLConnection) url.openConnection();
            StringBuilder sb = new StringBuilder();
            InputStreamReader isr = new InputStreamReader(con.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            String line;
            while( (line = br.readLine()) != null)
            {
                Bitmap pom= BitmapFactory.decodeStream((InputStream) new URL(line).getContent());
                publishProgress(pom);
            }
            br.close();
            isr.close();

            con.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Bitmap... values) {
        aktivnost.setSlika(values[0]);
        super.onProgressUpdate(values);
    }
}
