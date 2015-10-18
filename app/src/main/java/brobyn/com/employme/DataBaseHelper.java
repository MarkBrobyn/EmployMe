//http://www.reigndesign.com/blog/using-your-own-sqlite-database-in-android-applications/

package brobyn.com.employme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;



public class DataBaseHelper {

    private static final String DB_NAME = "db";
    private static final int DB_VERSION = 1;

    final Context context;

    DBHelper db;
    SQLiteDatabase myDataBase;
    //static final String DBCREATE="CREATE TABLE items (_id INTEGER PRIMARY KEY  NOT NULL, datetime DATETIME DEFAULT CURRENT_TIMESTAMP, title TEXT DEFAULT Title, content TEXT DEFAULT Content, picture BLOB DEFAULT null);";
    static final String DBCREATE="CREATE TABLE items (datetime DATETIME DEFAULT CURRENT_TIMESTAMP, title TEXT DEFAULT Title, content TEXT DEFAULT Content, _id INTEGER PRIMARY KEY NOT NULL, picture BLOB DEFAULT NULL);";

    public DataBaseHelper(Context myContext) {
        this.context = myContext;
        db=new DBHelper(context);
    }


    private static class DBHelper extends SQLiteOpenHelper {

        DBHelper(Context context) {
            super(context, DB_NAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase myDataBase) {
            try {
                myDataBase.execSQL(DBCREATE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase myDataBase, int oldVersion, int newVersion) {
            myDataBase.execSQL("DROP TABLE IF EXISTS items");
            onCreate(myDataBase);
        }
    }

    public void close() {
        db.close();
        }


    public DataBaseHelper open() throws SQLException {
        myDataBase=db.getWritableDatabase();
        return this;
    }


    public Cursor showAll() {
        return myDataBase.query("items", new String[] {"datetime","title","content","_id"}, null, null, null, null, null);
    }

    public void deleteAll() {
        myDataBase.delete("items",null, null);
    }

    public long addItem(String title, String content, byte[] picture) {
        ContentValues initialValues=new ContentValues();
        initialValues.put("title",title);
        initialValues.put("content", content);
        if(picture!=null)initialValues.put("picture",picture);
        return myDataBase.insert("items",null, initialValues);
    }

    public boolean updateItem(String id, String title, String content, byte[] picture) {
        ContentValues values=new ContentValues();
        values.put("title",title);
        values.put("content", content);
        values.put("picture",picture);
        return myDataBase.update("items",values,"_id="+id,null)>0;
    }

    public Cursor getItem(String id) throws SQLException {
        Cursor mCursor = myDataBase.query(true, "items",
                new String[] {"datetime","title","content","_id"},
                "_id="+id,
                null,null,null,null,null);
        if(mCursor!=null) {mCursor.moveToFirst();}
        return mCursor;
    }

    public boolean deleteItem(String id) {
        return myDataBase.delete("items","_id="+id,null)>0;
    }

    /*
    public void open() throws SQLException {
        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }
    */


}