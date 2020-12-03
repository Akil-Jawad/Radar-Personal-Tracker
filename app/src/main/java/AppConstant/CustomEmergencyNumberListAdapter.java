package AppConstant;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.summittracker.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import Model.EmergencyNumberModel;

public class CustomEmergencyNumberListAdapter extends BaseAdapter {
    Activity activity;
    List<EmergencyNumberModel> users;
    List<EmergencyNumberModel> model=new ArrayList<>();


    private static LayoutInflater inflater = null;

    public CustomEmergencyNumberListAdapter(Activity context) {
        this.activity = context;
    }

    public CustomEmergencyNumberListAdapter(Activity context, List<EmergencyNumberModel> users) {
        this.activity = context;
        this.users = users;
        model.addAll(users);
        //Log.i("model", String.valueOf(users.size()));
        Log.i("modelmodel", String.valueOf(model.size()));
        inflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.listview_contacts_item_row, parent, false);
            holder = new ViewHolder();
            holder.tvContactName = convertView.findViewById(R.id.tv_user_name);
            holder.tvContactNumber = convertView.findViewById(R.id.tv_contact_number);
            holder.ivCheckBox = convertView.findViewById(R.id.iv_check_box);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        EmergencyNumberModel contactNumberModel = users.get(position);

        holder.tvContactName.setText(contactNumberModel.getContactName());
        holder.tvContactNumber.setText(contactNumberModel.getContactNumber());


        if(contactNumberModel.isChecked()){
            holder.ivCheckBox.setBackgroundResource(R.mipmap.checked);
        }else{
            holder.ivCheckBox.setBackgroundResource(R.mipmap.check);
        }

        return convertView;
    }

    public void filter(String chartext){

        chartext = chartext.toLowerCase(Locale.getDefault());
        users.clear();
        if(chartext.length()==0){
            users.addAll(model);
        }else {
            for(EmergencyNumberModel m : model){
                if(m.getContactName().toLowerCase(Locale.getDefault()).contains(chartext)){
                    users.add(m);
                }

            }
        }

        notifyDataSetChanged();
    }

    public void updateRecords(List<EmergencyNumberModel> users){
        this.users=users;

        notifyDataSetChanged();
    }
    class ViewHolder{
        TextView tvContactName;
        TextView tvContactNumber;
        ImageView ivCheckBox;
    }
}
