package brobyn.com.employme;

/**
 * Created by mark22 on 05/09/2015.
 */

//import android.support.v7.app.ActionBarActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;

public class EditItemActivity extends AppCompatActivity {
    private boolean DEV_MODE=false;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView myImageView;

    public void takePhoto(View view){
        dispatchTakePictureIntent();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            myImageView =(ImageView)findViewById(R.id.picture);
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            myImageView.setImageBitmap(imageBitmap);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        final String itemID = getIntent().getStringExtra("id");
if(DEV_MODE) makeText(getApplicationContext(), "EditItem\nonCreate\nitemID=" +itemID, LENGTH_LONG).show();

        TextView edit_item_datetime = (TextView) findViewById (R.id.edit_item_datetime);
        final TextView edit_item_title = (TextView) findViewById (R.id.edit_item_title);
        final TextView edit_item_content = (TextView) findViewById (R.id.edit_item_content);

        final DataBaseHelper db=new DataBaseHelper(this);
        db.open();
        Cursor c=db.getItem(itemID);
        if (c.moveToFirst()) {
            String text = "\n"+c.getString(0)
            +"\n"+c.getString(1)
            +"\n"+c.getString(2)
            +"\n"+c.getString(3);
if(DEV_MODE) makeText(getApplicationContext(),"EditItem\n_id="+itemID+" found:"+text, LENGTH_LONG).show();
            edit_item_datetime.setText(c.getString(0));
            edit_item_title.setText(c.getString(1));
            edit_item_content.setText(c.getString(2));
        }
        else makeText(getApplicationContext(),"EditItem\n_id="+itemID+" not found", LENGTH_LONG).show();
        db.close();

        Button button_edit_item_cancel=(Button)findViewById(R.id.button_edit_item_cancel);
        button_edit_item_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });


        Button button_edit_item_save=(Button)findViewById(R.id.button_edit_item_save);
        button_edit_item_save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                db.open();
                db.updateItem(itemID, edit_item_title.getText().toString(), edit_item_content.getText().toString());
                db.close();
                finish();
            }
        });

        Button button_edit_item_send=(Button)findViewById(R.id.button_edit_item_send);
        button_edit_item_send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String[] to={""};
                sendEmail(to, edit_item_title.getText().toString(), edit_item_content.getText().toString());
            }
        });

        Button button_edit_item_delete=(Button)findViewById(R.id.button_edit_item_delete);
        button_edit_item_delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
if(DEV_MODE) makeText(getApplicationContext(), "SQLite\nDelete Item\n"+itemID, LENGTH_LONG).show();
                db.open();
                db.deleteItem(itemID);
                db.close();
                finish();
            }
        });

        /**/
        final FrameLayout pictureframe=(FrameLayout) findViewById(R.id.pictureframe);

        pictureframe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto(v);
            }
        });
        /**/
    }





/*
    public void sendItem(View v){
        String[] to={"apps@brobyn.com"};
        //sendEmail(to,"Test","This is a test");

        sendEmail("",edit_item_title.getText().toString(),edit_item_content.getText().toString());
    }
*/
    private void sendEmail(String[] emailAddresses, String subject, String message){
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        String[] to = emailAddresses;
        //emailIntent.putExtra(Intent.EXTRA_EMAIL,to);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT,subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT,message);
        emailIntent.setType("message/rfc822");
        startActivity(Intent.createChooser(emailIntent,"email"));
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
