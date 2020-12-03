package AppConstant;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.summittracker.R;

import java.util.HashMap;
import java.util.List;

import Model.HelplineNumberModel;
import Model.UserLocationModel;

public class CustomHistoryAdapter extends BaseAdapter {
    Activity activity;
    List<String> historyWithDate;
    private static LayoutInflater inflater = null;

    public CustomHistoryAdapter(Activity activity, List<String> historyWithDate) {
        this.activity = activity;
        this.historyWithDate = historyWithDate;
        inflater = activity.getLayoutInflater();

    }

    @Override
    public int getCount() {
        return historyWithDate.size();
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        CustomHistoryAdapter.ViewHolder holder = null;
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.listview_history_row, viewGroup, false);
            holder = new CustomHistoryAdapter.ViewHolder();
            holder.tvFrom = convertView.findViewById(R.id.tv_from);

            convertView.setTag(holder);
        }else{
            holder = (CustomHistoryAdapter.ViewHolder)convertView.getTag();
        }

        String historyName = historyWithDate.get(position);
        holder.tvFrom.setText(historyName);

        return convertView;
    }

    public void updateRecords(List<String> historyWithDate){
        this.historyWithDate=historyWithDate;

        notifyDataSetChanged();
    }
    class ViewHolder{
        TextView tvFrom;
    }
}
