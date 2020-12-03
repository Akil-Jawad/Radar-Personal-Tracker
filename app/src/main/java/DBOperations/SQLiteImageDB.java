package DBOperations;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.FileInputStream;
import java.io.IOException;

public class SQLiteImageDB extends SQLiteOpenHelper {

    public SQLiteImageDB(@Nullable Context context) {
        super(context, "image.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table IF NOT EXISTS images(_id INTEGER primary key AUTOINCREMENT,user_id INTEGER not null,img VARCHAR(500) not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists images");
        onCreate(db);
    }

    public Boolean insertImage(String x, Integer i){
        SQLiteDatabase db  = this.getWritableDatabase();
        try {
            FileInputStream fs = new FileInputStream(x);
            byte[] imgByte = new byte[fs.available()];
            fs.read(imgByte);
            ContentValues contentValues = new ContentValues();
            contentValues.put("user_id",i);
            contentValues.put("img",x);

            Cursor cursor = db.rawQuery("select _id from images where user_id=?",new String[]{String.valueOf(i)});
            if(cursor.getCount()>0){
                Log.i("Integer","USER EXISTS");
                db.update("images",contentValues,"user_id="+i,null);
                fs.close();
                cursor.close();
                return true;
            }else{
                db.insert("images",null,contentValues);
                fs.close();
                cursor.close();
                return true;
            }
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    public Bitmap getImage(int id){
        Log.i("Integer", String.valueOf(id));

        SQLiteDatabase db = this.getWritableDatabase();
                Bitmap bt = null;

        Cursor cursor = db.rawQuery("select img from images where user_id=?",new String[]{String.valueOf(id)});
        if(cursor.getCount()==0){
            return null;
        }

        while(cursor.moveToNext()){
            //byte[] img = cursor.getBlob(2);
            String img =cursor.getString(0);
            Log.i("Integer",img);
            bt = BitmapFactory.decodeFile(img);
        }
        cursor.close();
        return bt;
    }
}
