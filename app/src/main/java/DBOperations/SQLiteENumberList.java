package DBOperations;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import java.util.ArrayList;

import Model.UserEmergencyNumber;

public class SQLiteENumberList extends SQLiteOpenHelper {
    public SQLiteENumberList(Context context) {
        super(context, "user.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table IF NOT EXISTS e_number(_id INTEGER primary key AUTOINCREMENT,user_id INTEGER not null,e_contact_number VARCHAR(20) not null," +
                "e_contact_name VARCHAR(500) not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists e_number");
        onCreate(db);
    }

    public boolean insertContact(int user_id,String name,String number){
        SQLiteDatabase db  = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_id",user_id);
        contentValues.put("e_contact_name",name);
        contentValues.put("e_contact_number",number);
        Log.i("CheckValues",user_id+" "+name+" "+number);
        db.insert("e_number",null,contentValues);
        return true;
    }

    public ArrayList<UserEmergencyNumber> getAllNumbers(int user_id){
        ArrayList<UserEmergencyNumber> contacts = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("select e_contact_number,e_contact_name from e_number where user_id=?",new String[]{String.valueOf(user_id)});
        if(cursor.getCount()==0){
            return null;
        }
        while(cursor.moveToNext()){
            //byte[] img = cursor.getBlob(2);
            String e_number = cursor.getString(0);
            String e_name = cursor.getString(1);
            contacts.add(new UserEmergencyNumber(user_id,e_number,e_name));
            Log.i("TAG",e_name+" "+e_number);
        }
        cursor.close();
        return contacts;
    }
}
