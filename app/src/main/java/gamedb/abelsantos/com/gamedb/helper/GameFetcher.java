package gamedb.abelsantos.com.gamedb.helper;

import android.net.Uri;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import gamedb.abelsantos.com.gamedb.Database.Game;

/**
 * Created by Abel Cruz dos Santos on 16.02.2017.
 */

public class GameFetcher {
    private static final String TAG = "GameFetcher";

    private static final String API_KEY = "xJ5Il9taWGmshif2t3JgcAaZvASmp1CibhRjsnHCfFyh6r0nTa";


    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        try{
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK){
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlSpec);
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0){
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        }finally {
            connection.disconnect();
        }
    }
    public String getUrlString(String urlSpec) throws IOException{
        return new String(getUrlBytes(urlSpec));
    }

    public List<Game> fetchItems(){
        List<Game> items = new ArrayList<>();

        try{
            String url = Uri.parse("https://igdbcom-internet-game-database-v1.p.mashape.com/games/?fields=*&limit=10")
                    .buildUpon()
                    .appendQueryParameter("X-Mashape-Key", API_KEY)
                    .appendQueryParameter("Accept", "application/json")
                    .build().toString();
            String jsonString = getUrlString(url);
            Log.i(TAG, "Received JSON: " + jsonString);

            JSONObject jsonObject = new JSONObject(jsonString);

        }catch (JSONException je){
            Log.e(TAG, "Failed to parse JSON", je);
        }catch (IOException ioe){
            Log.e(TAG, "Failed to fetch items", ioe);
        }

        return items;
    }

}
