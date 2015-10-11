package brobyn.com.employme;

/**
 * Created by mark22 on 11/10/2015.
 */

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class MoActivity extends AppCompatActivity {


    private static final String TAG="MoActivity";
    private boolean DEV_MODE=true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mo);
        //View view=findViewById(R.id.main);
        //Log.d(TAG, view.getTag().toString());
        //if(DEV_MODE)
            //makeText(getApplicationContext(), view.getTag().toString(), LENGTH_SHORT).show();


        Button button_me=(Button)findViewById(R.id.button_mdx);
        button_me.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            String uri="http://www.mdx.ac.uk";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(uri));
                startActivity(i);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
