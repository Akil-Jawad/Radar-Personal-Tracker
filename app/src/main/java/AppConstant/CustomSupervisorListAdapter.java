package AppConstant;

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
import java.util.List;
import java.util.Locale;

import Model.EmergencyNumberModel;
import Model.SupervisorNameModel;

import static AppConstant.GlobalConstant.modelSupervisor;

public class CustomSupervisorListAdapter extends BaseAdapter {
    Activity activity;
    List<SupervisorNameModel> users;

    private static LayoutInflater inflater = null;
    public CustomSupervisorListAdapter(Activity activity){

    }

    public CustomSupervisorListAdapter(Activity activity, List<SupervisorNameModel> users) {
        this.activity = activity;
        this.users = users;
        Log.i("model", String.valueOf(this.users.size()));

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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.listview_supervisor_item_row, parent, false);
            holder = new CustomSupervisorListAdapter.ViewHolder();
            holder.tvContactName = convertView.findViewById(R.id.tv_user_name);
            holder.ivCheckBox = convertView.findViewById(R.id.iv_check_box);

            convertView.setTag(holder);
        }else{
            holder = (CustomSupervisorListAdapter.ViewHolder)convertView.getTag();
        }

        SupervisorNameModel supervisorNameModel = users.get(position);

        holder.tvContactName.setText(supervisorNameModel.getSupervisorName());

        if(supervisorNameModel.isChecked()){
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
            users.addAll(modelSupervisor);
        }else {
            for(SupervisorNameModel m : modelSupervisor){
                if(m.getSupervisorName().toLowerCase(Locale.getDefault()).contains(chartext)){
                    users.add(m);
                }

            }
        }

        notifyDataSetChanged();
    }
    public void updateRecords(List<SupervisorNameModel> users){
        this.users=users;
        notifyDataSetChanged();
    }
    class ViewHolder{
        TextView tvContactName;
        ImageView ivCheckBox;
    }
}
