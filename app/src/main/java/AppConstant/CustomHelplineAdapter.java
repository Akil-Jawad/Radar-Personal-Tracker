package AppConstant;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.summittracker.R;

import java.util.List;

import Model.HelplineNumberModel;

public class CustomHelplineAdapter extends BaseAdapter {
    Activity activity;
    List<HelplineNumberModel> users;
    private static LayoutInflater inflater = null;

    public CustomHelplineAdapter(Activity activity, List<HelplineNumberModel> users) {
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
        CustomHelplineAdapter.ViewHolder holder = null;
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.listview_helpline_row, parent, false);
            holder = new CustomHelplineAdapter.ViewHolder();
            holder.tvHelplineName = convertView.findViewById(R.id.tv_helpline_name);
            holder.tvHelplineNumber = convertView.findViewById(R.id.tv_helpline_number);

            convertView.setTag(holder);
        }else{
            holder = (CustomHelplineAdapter.ViewHolder)convertView.getTag();
        }

        HelplineNumberModel helplineNumberModel = users.get(position);

        holder.tvHelplineName.setText(helplineNumberModel.getHelplineName());
        holder.tvHelplineNumber.setText(helplineNumberModel.getHelplineNumber());

        return convertView;
    }

    public void updateRecords(List<HelplineNumberModel> users){
        this.users=users;

        notifyDataSetChanged();
    }
    class ViewHolder{
        TextView tvHelplineName;
        TextView tvHelplineNumber;
    }
}
