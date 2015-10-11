package brobyn.com.employme;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;

public class DoActivity extends Activity  {

    private boolean DEV_MODE=true;
    TextView text;

    ListView myListView;
    ArrayAdapter myArrayAdapter;
    //ArrayList<String> myArrayList=new ArrayList<String>();
    ArrayList myArrayList=new ArrayList();

    public String readJSONFeed(String URL) {
        StringBuilder stringBuilder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(URL);
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
            } else {
                Log.e("JSON", "Failed to download file");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    private class ReadJSONFeedTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls) {
            return readJSONFeed(urls[0]);
        }

        protected void onPostExecute(String result) {
            try {
                JSONObject jObject = new JSONObject(result);
                String aJsonString = jObject.getString("text");
                text.setText(aJsonString);
                JSONArray jArray = jObject.getJSONArray("items");

                for (int i=0; i < jArray.length(); i++)
                {
                    try {
                        JSONObject oneObject = jArray.getJSONObject(i);
                        // Pulling items from the array
                        String title= oneObject.getString("title");
                        String content = oneObject.getString("content");
                        String datetime = oneObject.getString("datetime");
                        String item=datetime+"\n"+title+"\n"+content;
                        myArrayList.add(item);
                        if(DEV_MODE)Toast.makeText(getBaseContext(), item,Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        // Oops
                    }
                }
                myArrayAdapter.notifyDataSetChanged();
                /*
                JSONArray jsonArray = new JSONArray(result);
                Log.i("JSON", "Number of surveys in feed: " +
                        jsonArray.length());

                //---print out the content of the json feed---
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                */

    				/*
    				Toast.makeText(getBaseContext(), jsonObject.getString("appeId") +
    						" - " + jsonObject.getString("inputTime"),
    						Toast.LENGTH_SHORT).show();
    				*/
                    /*
                    Toast.makeText(getBaseContext(), jsonObject.getString("text") +
                                    " - " + jsonObject.getString("created_at"),
                            Toast.LENGTH_SHORT).show();
                    */

                //}
                Toast.makeText(getBaseContext(), aJsonString,Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do);
        text = (TextView) findViewById (R.id.text);

		/*
		new ReadJSONFeedTask().execute("http://extjs.org.cn/extjs/examples/grid/survey.html");
		*/
        /*
        new ReadJSONFeedTask().execute("https://twitter.com/statuses/user_timeline/weimenglee.json");
        */
        new ReadJSONFeedTask().execute("http://avail.mdx.ac.uk/items/");

        myListView=(ListView) findViewById(R.id.items);
        myArrayAdapter=new ArrayAdapter(this,
                //myArrayAdapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                myArrayList);

        myListView.setAdapter(myArrayAdapter);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                /*
                Item item=new Item();
                String text = "\n" + parent.toString() + "\n" + view.toString() + "\n" + position + "\n" + id;
                //makeText(getApplicationContext(), "SQLite\nonItemClick" + text, LENGTH_LONG).show();
                item=(Item) myListView.getItemAtPosition(position);
                */
                if(DEV_MODE)makeText(getApplicationContext(), "DoActivity\nonItemClick\n", Toast.LENGTH_SHORT).show();
                /*
                Intent i = new Intent(DoActivity.this, EditItemActivity.class);
                i.putExtra("id",item.id);
                startActivity(i);
                */
            }
        });
        myArrayAdapter.notifyDataSetChanged();

    }
}