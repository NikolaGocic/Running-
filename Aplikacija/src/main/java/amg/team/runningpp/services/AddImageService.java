package amg.team.runningpp.services;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import amg.team.runningpp.data.Constants;
import amg.team.runningpp.view.FragmentProfileFriend;
import amg.team.runningpp.view.FragmentSaveActivity;

public class AddImageService extends AsyncTask<String,Void,String> {

    Fragment fragment;
    Bitmap bitmap;

    public AddImageService(Fragment fragment, Bitmap bitmap){
        this.fragment = fragment;
        this.bitmap = bitmap;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            String encodedImage = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
            String post_data = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(encodedImage, "UTF-8") + "&" +
                    URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(params[0], "UTF-8");
            URL url = new URL(Constants.URL+"/"+Constants.ADD_IMAGE);
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
            ((FragmentSaveActivity)fragment).uploadImageSuccess();
        }
        else {
            ((FragmentSaveActivity)fragment).uploadImageFailed();
        }
    }
}
