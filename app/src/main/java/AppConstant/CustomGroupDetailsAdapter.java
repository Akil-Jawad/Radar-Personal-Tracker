package AppConstant;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.summittracker.R;

import java.util.List;

import Model.GetAllGroupMembersDetails;

public class CustomGroupDetailsAdapter extends BaseAdapter {
    Activity activity;
    List<GetAllGroupMembersDetails> users;
    private static LayoutInflater inflater = null;

    public CustomGroupDetailsAdapter(Activity activity, List<GetAllGroupMembersDetails> users) {
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
            convertView = inflater.inflate(R.layout.group_detail_list, parent, false);
            holder = new CustomGroupDetailsAdapter.ViewHolder();
            holder.tvContactName = convertView.findViewById(R.id.user_name);
            holder.tvContactEmail = convertView.findViewById(R.id.user_email);
            holder.tvContactNumber = convertView.findViewById(R.id.user_phone);

            convertView.setTag(holder);

        }else{
            holder = (CustomGroupDetailsAdapter.ViewHolder)convertView.getTag();
        }

        GetAllGroupMembersDetails testModel = users.get(position);
        holder.tvContactName.setText(testModel.getUser_name());
        holder.tvContactEmail.setText(testModel.getEmail());
        holder.tvContactNumber.setText(testModel.getPhone());

        return convertView;
    }

    class ViewHolder{
        TextView tvContactName;
        TextView tvContactEmail;
        TextView tvContactNumber;
    }
}
