package AppConstant;

import android.app.Activity;
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
import Model.GroupListModel;

import static AppConstant.GlobalConstant.modelGroup;

public class CustomGroupListAdapter extends BaseAdapter {
    Activity activity;
    List<GroupListModel> users;

    private static LayoutInflater inflater = null;

    public CustomGroupListAdapter(Activity activity, List<GroupListModel> users) {
        this.activity = activity;
        this.users = users;
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
            holder = new CustomGroupListAdapter.ViewHolder();
            holder.tvContactName = convertView.findViewById(R.id.tv_user_name);
            holder.ivCheckBox = convertView.findViewById(R.id.iv_check_box);

            convertView.setTag(holder);
        }else{
            holder = (CustomGroupListAdapter.ViewHolder)convertView.getTag();
        }

        GroupListModel groupListModel = users.get(position);

        holder.tvContactName.setText(groupListModel.getGroupName());

        if(groupListModel.isChecked()){
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
            users.addAll(modelGroup);
        }else {
            for(GroupListModel m : modelGroup){
                if(m.getGroupName().toLowerCase(Locale.getDefault()).contains(chartext)){
                    users.add(m);
                }

            }
        }

        notifyDataSetChanged();
    }
    public void updateRecords(List<GroupListModel> users){
        this.users=users;
        notifyDataSetChanged();
    }
    class ViewHolder{
        TextView tvContactName;
        ImageView ivCheckBox;
    }
}
