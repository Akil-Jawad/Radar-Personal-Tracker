package com.example.summittracker;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import AppConstant.CustomHelplineAdapter;
import Model.HelplineNumberModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class HelplineFragment extends Fragment {
    public  static final int RequestPermissionCode  = 2 ;
    ListView lvHelpline;
    CustomHelplineAdapter listAdapter;
    private String helpNumber = "";


    public HelplineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_helpline, container, false);
        lvHelpline = view.findViewById(R.id.lvhelpline);
        final List<HelplineNumberModel> helpline = getHelplineNumber();

        listAdapter = new CustomHelplineAdapter(getActivity(), helpline);
        lvHelpline.setAdapter(listAdapter);

        lvHelpline.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(),"LONG PRESSED",Toast.LENGTH_SHORT).show();
                HelplineNumberModel helplineNumberModel = helpline.get(position);
                helpNumber = helplineNumberModel.getHelplineNumber();
                if(ContextCompat.checkSelfPermission(getContext(),Manifest.permission.CALL_PHONE)==PackageManager.PERMISSION_GRANTED){
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:"+helpNumber));
                    startActivity(callIntent);
                }else{
                    enableRuntimePermission();
                }
                return false;
            }
        });
        return view;
    }

    public void enableRuntimePermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                getActivity(),
                Manifest.permission.CALL_PHONE))
        {
            new AlertDialog.Builder(getContext())
                    .setTitle("Permission needed")
                    .setMessage("Need contacts permission to access")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(getActivity(),new String[]{
                                    Manifest.permission.CALL_PHONE}, RequestPermissionCode);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(getActivity(),new String[]{
                    Manifest.permission.CALL_PHONE}, RequestPermissionCode);
        }
    }
    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {
        switch (RC) {
            case RequestPermissionCode:
                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:"+helpNumber));
                    startActivity(callIntent);
                } else {
                    Toast.makeText(getContext(),"Permission Canceled", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
    private List<HelplineNumberModel> getHelplineNumber() {
        ArrayList<HelplineNumberModel> arrayList = new ArrayList<>();
        HelplineNumberModel contact = new HelplineNumberModel();

        contact.setHelplineName("Rayhan Parvez");
        contact.setHelplineNumber("01711082742");
        arrayList.add(contact);

        contact = new HelplineNumberModel();
        contact.setHelplineName("Ahnaf Muttaki");
        contact.setHelplineNumber("01710396902");
        arrayList.add(contact);

        contact = new HelplineNumberModel();
        contact.setHelplineName("Shafayet Bin Mahmud");
        contact.setHelplineNumber("01868903697");
        arrayList.add(contact);

        return arrayList;

    }

}
