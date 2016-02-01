package brobyn.com.employme;

/**
 * Created by mark22 on 05/09/2015.
 */

//import android.support.v7.app.ActionBarActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.ByteArrayOutputStream;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;

public class EditItemActivity extends AppCompatActivity {
    private boolean DEV_MODE=false;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView myImageView;
    Bitmap imageBitmap=null;
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
            imageBitmap = (Bitmap) extras.get("data");
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
        final ImageView picture = (ImageView) findViewById (R.id.picture);
        byte[] byteArray=null;
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
            byteArray=c.getBlob(4);
            if(byteArray!=null){
                picture.setImageBitmap(BitmapFactory.decodeByteArray(byteArray,0,byteArray.length));
            }
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
                byte[] imageBuffer=null;
                if(imageBitmap!=null) {
                  ByteArrayOutputStream out = new ByteArrayOutputStream();
                  imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                  imageBuffer=out.toByteArray();
                  db.open();
                  db.updateItem(itemID, edit_item_title.getText().toString(), edit_item_content.getText().toString(),imageBuffer);
                  db.close();
                }
                else {
                  db.open();
                  db.updateItem(itemID, edit_item_title.getText().toString(), edit_item_content.getText().toString());
                  db.close();
                }
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

    private void sendEmail(String[] emailAddresses, String subject, String message){
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        String[] to = emailAddresses;
        //emailIntent.putExtra(Intent.EXTRA_EMAIL,to);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT,subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT,message);
        emailIntent.setType("message/rfc822");
        startActivity(Intent.createChooser(emailIntent,"email"));

        /*
// http://stephendnicholas.com/archives/974
public static void createCachedFile(Context context, String fileName,
            String content) throws IOException {

    File cacheFile = new File(context.getCacheDir() + File.separator
                + fileName);
    cacheFile.createNewFile();
    cacheFile.setReadable(true, false);
    FileOutputStream fos = new FileOutputStream(cacheFile);
    OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF8");
    PrintWriter pw = new PrintWriter(osw);

    pw.println(content);

    pw.flush();
    pw.close();

    // shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(shareFile));
}

public static Intent getSendEmailIntent(Context context, String email,
            String subject, String body, String fileName) {

    final Intent emailIntent = new Intent(
                android.content.Intent.ACTION_SEND);

    //Explicitly only use Gmail to send
    emailIntent.setClassName("com.google.android.gm","com.google.android.gm.ComposeActivityGmail");

    emailIntent.setType("plain/text");
    //shareIntent.setType(“application/zip”);

    //Add the recipients
    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                new String[] { email });

    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);

    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);

    //Add the attachment by specifying a reference to our custom ContentProvider
    //and the specific file of interest
    emailIntent.putExtra(
            Intent.EXTRA_STREAM,
                Uri.parse("content://" + CachedFileProvider.AUTHORITY + "/"
                        + fileName));
    return emailIntent;
}
*/


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
