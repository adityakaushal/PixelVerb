package oxforddictionary.codeitstudio.com.dictionary;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by adityakaushal on 03/04/18.
 */

public class MyDictionaryRequest extends AsyncTask<String,Integer,String> {

    final String app_id = "";
    final String app_key ="";

String myUrl;

Context context;

MyDictionaryRequest(Context context){
    this.context = context;
}

    @Override
    protected String doInBackground(String... strings) {
        myUrl = strings[0];
        try {
            URL url = new URL(myUrl);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setRequestProperty("app_id",app_id);
            urlConnection.setRequestProperty("app_key",app_key);


            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }

            return stringBuilder.toString();

        }
        catch (Exception e) {
//
            e.printStackTrace();
            return e.toString();
            //Toast.makeText(context,e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

       String def;
        try{
            JSONObject j = new JSONObject(s);
            JSONArray results = j.getJSONArray("results");

            JSONObject lexicalEnteries = results.getJSONObject(0);
            JSONArray lArray = lexicalEnteries.getJSONArray("lexicalEntries");

            JSONObject entries = lArray.getJSONObject(0);
            JSONArray entry = entries.getJSONArray("entries");

            JSONObject jsonObject = entry.getJSONObject(0);
            JSONArray sensesArray = jsonObject.getJSONArray("senses");

            JSONObject d = sensesArray.getJSONObject(0);
            JSONArray de = d.getJSONArray("definitions");

            def = de.getString(0);


            final AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle("Definition of the Word...");
            builder.setMessage(def);
            builder.create();
            builder.show();

        }catch(Exception e){
            Toast.makeText(context,"Definition Not Found in the Dictionary",Toast.LENGTH_SHORT).show();
        }




    }


}
