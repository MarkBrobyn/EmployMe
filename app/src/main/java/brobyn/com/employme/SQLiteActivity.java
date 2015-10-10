package brobyn.com.employme;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;

/**
 * Created by mark22 on 31/08/2015.
 */
public class SQLiteActivity extends AppCompatActivity {

    TextView status;


    //private SQLiteDatabase db;
    DataBaseHelper db=new DataBaseHelper(this); //

    ListView myListView;
    ArrayAdapter myArrayAdapter;
    //ArrayList<String> myArrayList=new ArrayList<String>();
    ArrayList myArrayList=new ArrayList();
    //ArrayList<Int> myArrayList=new ArrayList<String>();

    long addItemId;

    SimpleDateFormat dateFormatter = new SimpleDateFormat("E dd MMM K:mm aa");
    SimpleDateFormat dateFromSqlFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    class Item {
        private String title;
        private String id;
        //getters and setters;
        public String toString(){
            return title;
        }
        public void setTitle(String title){
            this.title = title;
        }
        public void setID(String id){
            this.id = id;
        }
        //public String getID() {return this.id;}
    }

    private void showAll() throws ParseException {
        int count = 0;
        Date date;
        Item item;
        String itemDate="";

        makeText(getApplicationContext(), "SQLite\nShow All", LENGTH_LONG).show();

        db.open();
        Cursor cursor = db.showAll();
        myArrayList.clear();
        if (cursor.moveToFirst()) {
            do {
                item=new Item();
                date=new Date();
                //myHolder.setTitle("Item "+cursor.getString(3)+": "+cursor.getString(1));
                try {
                    date=dateFromSqlFormatter.parse(cursor.getString(0));
                }
                catch (ParseException e) {
                    e.printStackTrace();
                }
                itemDate=dateFormatter.format(date);
                item.setTitle(itemDate + ": " + cursor.getString(1));
                //item.setTitle(cursor.getString(0) + ": " + cursor.getString(1));
                item.setID(cursor.getString(3));
                myArrayList.add(0,item); // add to start of list

                //myArrayList.add(0, cursor.getString(0) + " " + cursor.getString(1) + " " + cursor.getString(2) + " " + cursor.getString(3)); // add to start of list
                //myArrayList.add(cursor.getString(1));
                count++;
            } while (cursor.moveToNext());
        }
        db.close();
        myArrayAdapter.notifyDataSetChanged();
        status = (TextView) findViewById(R.id.sqlite_status);
        status.setText("Items = "+String.valueOf(count));
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            showAll();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite);
        //findViewById(R.id.sqlite_top_bar).setVisibility(View.GONE);
// list view stuff
        /*
        myArrayList.add("Mark");
        myArrayList.add("Gaby");
        myArrayList.add("Izzy");
        myArrayList.add("Paul");
        myArrayList.add("Viki");
        */

        myListView=(ListView) findViewById(R.id.sqlite_listview);
        myArrayAdapter=new ArrayAdapter(this,
        //myArrayAdapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                myArrayList);

        myListView.setAdapter(myArrayAdapter);
        myListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Item item=new Item();
                String text = "\n" + parent.toString() + "\n" + view.toString() + "\n" + position + "\n" + id;
                //makeText(getApplicationContext(), "SQLite\nonItemClick" + text, LENGTH_LONG).show();
                item=(Item) myListView.getItemAtPosition(position);
                makeText(getApplicationContext(), "SQLite\nonItemClick\n" + item.id, LENGTH_LONG).show();
                ///Intent i = new Intent(SQLiteActivity.this, EditItemActivity.class);
                ///i.putExtra("id",item.id);
                ///startActivity(i);
            }
        });
        myArrayAdapter.notifyDataSetChanged();
        /*
        ListView lstFrnd = (ListView) findViewById(R.id.frndLst);
ArrayList<String> listItems = new ArrayList<String>();
ArrayAdapter<String> adapter;
adapter = new ArrayAdapter<String>(this, android.R.layout.XXX, listItems);
for (int i = 0; i < ans.length(); i++) {
    int id = integer.parseInt(ans.getJSONObject(i).getString("UserID"));
    String disName = ans.getJSONObject(i).getString("DisplayName");
    DisNameID dis = new DisNameID(disName, id);
    adapter.add(disName + " - " + id);
}


class Holder {
    private String name;
    private String id;
    //getters and setters;
    public String toString(){ return name };
}
*/
// end list view stuff


        try {
            showAll();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Button button_sqlite_show_all=(Button)findViewById(R.id.button_sqlite_show_all);
        button_sqlite_show_all.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /*
                int count = 0;

                makeText(getApplicationContext(), "SQLite\nShow All", LENGTH_LONG).show();

                db.open();
                Cursor cursor = db.showAll();
                myArrayList.clear();
                if (cursor.moveToFirst()) {
                    do {
                        myArrayList.add(0, cursor.getString(0) + " " + cursor.getString(1) + " " + cursor.getString(2) + " " + cursor.getString(3)); // add to start of list
                        //myArrayList.add(cursor.getString(1));
                        count++;
                    } while (cursor.moveToNext());
                }
                db.close();
                myArrayAdapter.notifyDataSetChanged();
                status = (TextView) findViewById(R.id.sqlite_status);
                status.setText("Items = "+String.valueOf(count));
                */
                try {
                    showAll();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });



        Button button_sqlite_add_item=(Button)findViewById(R.id.button_sqlite_add_item);
        button_sqlite_add_item.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText title_text= (EditText) findViewById(R.id.title_edittext);
                EditText content_text= (EditText) findViewById(R.id.content_edittext);
                makeText(getApplicationContext(), "SQLite\nAdd item", LENGTH_LONG).show();
                db.open();
                addItemId = db.addItem(title_text.getText().toString(),content_text.getText().toString());
                db.close();
                try {
                    showAll();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });


        Button button_sqlite_delete_all=(Button)findViewById(R.id.button_sqlite_delete_all);
        button_sqlite_delete_all.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                makeText(getApplicationContext(), "SQLite\nDelete all", LENGTH_LONG).show();
                db.open();
                db.deleteAll();
                db.close();
                try {
                    showAll();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });




        db.open();
        Cursor c=db.showAll();
        if (c.moveToFirst()) makeText(getApplicationContext(),"SQLite\nshowAll()>0", LENGTH_LONG).show();
        else makeText(getApplicationContext(),"SQLite\nshowAll==0", LENGTH_LONG).show();
        db.close();
    }
}
