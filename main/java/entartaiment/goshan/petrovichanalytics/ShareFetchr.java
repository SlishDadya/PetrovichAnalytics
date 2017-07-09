package entartaiment.goshan.petrovichanalytics;


import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ShareFetchr {
    public static final String TAG_PRICE="tag_price";
    public static final String TAG_PROCENT="tag_procent";
    public static Bundle fetchItems(String name)
    {
        String url=String.format("https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=%s&apikey=G338JEZ2KMIHUOOW",name);
        Log.e("cyka",url);

        try {
            String JsonString = new String(getURLbytes(url));
          /*  JSONObject jsonBody = new JSONObject(JsonString);
            JSONObject jsonQuotes=jsonBody.getJSONObject("Realtime Global Securities Quote");
            Bundle bundle=new Bundle();
            bundle.putSerializable(TAG_PRICE,jsonQuotes.getDouble("03. Latest Price"));
            bundle.putSerializable(TAG_PROCENT,jsonQuotes.getDouble("08. Price Change"));

            return bundle;*/
            JSONObject jsonBody = new JSONObject(JsonString);
            JSONObject jsonQuotes=jsonBody.getJSONObject("Realtime Global Securities Quote");
            Bundle bundle=new Bundle();
            bundle.putSerializable(TAG_PRICE,jsonQuotes.getDouble("03. Latest Price"));
            bundle.putSerializable(TAG_PROCENT,jsonQuotes.getDouble("08. Price Change"));
            return bundle;


        }
        catch(IOException e){Log.e("IO",e.toString());}
        catch(JSONException e){Log.e("JSON",e.toString());}
        return null;

    }
    public static byte[] getURLbytes(String urlspec) throws IOException
    {
        URL url=new URL(urlspec);
        HttpURLConnection connection=(HttpURLConnection)url.openConnection();

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if(connection.getResponseCode()!= HttpURLConnection.HTTP_OK)
            {
                throw new IOException(connection.getResponseMessage()+": with"+ urlspec);
            }
            int bytesRead=0;
            byte[] buffer=new byte[1024];
            while((bytesRead=in.read(buffer))>0)
            {
                outputStream.write(buffer,0,bytesRead);
            }
            outputStream.close();
            return outputStream.toByteArray();


        }
        finally {
            connection.disconnect();
        }
    }
}
