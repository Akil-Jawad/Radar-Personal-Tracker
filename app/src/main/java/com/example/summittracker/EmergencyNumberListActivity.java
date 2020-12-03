package com.example.summittracker;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import AppConstant.CustomEmergencyNumberListAdapter;
import AppConstant.GlobalConstant;
import Model.EmergencyNumberModel;

public class EmergencyNumberListActivity extends AppCompatActivity {

    ListView listview;
    Cursor cursor;
    String name, phonenumber ;
    SearchView searchView;
    int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_number_list);
        count=GlobalConstant.geteNumberArrayList().size();
        setTitle("Emergency Number List ("+count+"/5)");
        listview = findViewById(R.id.listview);
        searchView = findViewById(R.id.searchView);
        final List<EmergencyNumberModel> contacts = readContacts();
//        Collections.sort(contacts, new Comparator<EmergencyNumberModel>() {
//            @Override
//            public int compare(EmergencyNumberModel o1, EmergencyNumberModel o2) {
//                return o1.getContactName().compareToIgnoreCase(o2.contactName);
//            }
//        });
        Log.i("Size", String.valueOf(contacts.size()));
        final CustomEmergencyNumberListAdapter adapter = new CustomEmergencyNumberListAdapter(this,contacts);
        listview.setAdapter(adapter);
        listview.setTextFilterEnabled(true);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EmergencyNumberModel model = contacts.get(position);
                if(model.isChecked()){
                    model.setChecked(false);
                    count--;
                    setTitle("Select Emergency Number ("+count+"/5)");
                    for (int i = 0; i < GlobalConstant.geteNumberArrayList().size(); i++) {
                        EmergencyNumberModel checkObjects = GlobalConstant.geteNumberArrayList().get(i);
                        if(model.getContactName().equals(checkObjects.getContactName())){
                            GlobalConstant.geteNumberArrayList().remove(checkObjects);
                        }
                    }
                    //Log.i("Check Remove:", String.valueOf(GlobalConstant.geteNumberArrayList()));
                }else{
                    if(count>4){
                        Snackbar snackbar = Snackbar.make(findViewById(R.id.cle),"You can select maximum 5 numbers",Snackbar.LENGTH_LONG);
                        snackbar.show();
                        //Toast.makeText(EmergencyNumberListActivity.this,"You can select maximum 5 numbers", Toast.LENGTH_LONG).show();
                    }else{
                        model.setChecked(true);
                        count++;
                        setTitle("Select Emergency Number ("+count+"/5)");
                        GlobalConstant.geteNumberArrayList().add(model);
                        Log.i("Size", String.valueOf(GlobalConstant.geteNumberArrayList()));
                    }

                }
                contacts.set(position,model);

                adapter.updateRecords(contacts);
            }
        });

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmergencyNumberListActivity.this,SignUpActivity.class);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(TextUtils.isEmpty(newText)){
                    adapter.filter("");
                    listview.clearTextFilter();
                }else{
                    adapter.filter(newText);
                }
                return true;
            }
        });

    }

    public ArrayList<EmergencyNumberModel> readContacts(){
        ArrayList<EmergencyNumberModel> contactList = new ArrayList<>();

        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null, null, null);

        while (cursor.moveToNext()) {

            name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

            phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            boolean isChecked=false;
            for (int i = 0; i < GlobalConstant.geteNumberArrayList().size(); i++) {
                EmergencyNumberModel checkObjects = GlobalConstant.geteNumberArrayList().get(i);
                if(phonenumber.equals(checkObjects.getContactNumber())){
                    isChecked=true;
                    //draft.remove(i);
                }
            }
            contactList.add(new EmergencyNumberModel(name,phonenumber,isChecked));
        }

        cursor.close();


        return contactList;
    }

    @Override
    protected void onStop() {
        super.onStop();
        Intent intent = new Intent(EmergencyNumberListActivity.this,SignUpActivity.class);
        setResult(Activity.RESULT_OK,intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(),"Back Press disabled.",Toast.LENGTH_SHORT).show();
    }
}
