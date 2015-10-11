package brobyn.com.employme;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import android.database.SQLException;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;


public class MainActivity extends AppCompatActivity {


    private static final String TAG="MainActivity";
    private boolean DEV_MODE=true;

    public void buttonClick(View view){
        Button button=(Button)view;
        if(DEV_MODE)
            makeText(getApplicationContext(), button.getText().toString(), LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View view=findViewById(R.id.main);
        Log.d(TAG, view.getTag().toString());
        if(DEV_MODE)
            makeText(getApplicationContext(), view.getTag().toString(), LENGTH_SHORT).show();


        //DataBaseHelper myDbHelper=new DataBaseHelper(this);
/*
        try {
            myDbHelper.createDataBase();
        }
        catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
*/
        Button button_me=(Button)findViewById(R.id.button_me);
        button_me.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //status.setText("button_add_item");
                //Intent i=new Intent("EditorActivity");
                Intent i = new Intent(MainActivity.this, SQLiteActivity.class);
                startActivity(i);
            }
        });

        Button button_do=(Button)findViewById(R.id.button_do);
        button_do.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //status.setText("button_add_item");
                //Intent i=new Intent("EditorActivity");
                Intent i = new Intent(MainActivity.this, DoActivity.class);
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
