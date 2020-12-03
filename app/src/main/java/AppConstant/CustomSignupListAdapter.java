package AppConstant;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.summittracker.R;
import com.example.summittracker.SignUpActivity;

import java.util.List;

import Model.SignUpAllListViewModel;

public class CustomSignupListAdapter extends BaseAdapter{
    Activity activity;
    List<SignUpAllListViewModel> users;
    private static LayoutInflater inflater = null;

    public CustomSignupListAdapter(Activity activity, List<SignUpAllListViewModel> users) {
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
    public View getView(final int position, View convertView, final ViewGroup parent) {

        CustomSignupListAdapter.ViewHolder holder = null;
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.listview_item_signup_row, parent, false);
            holder = new ViewHolder();
            holder.tvContactName = convertView.findViewById(R.id.tv_signup_user_name);
            holder.ivCheckBox = convertView.findViewById(R.id.iv_signup_check_box);

            convertView.setTag(holder);
        }else{
            holder = (CustomSignupListAdapter.ViewHolder)convertView.getTag();
        }
        final SignUpAllListViewModel signUpAllListViewModel = users.get(position);
//
        holder.tvContactName.setText(signUpAllListViewModel.getPersonName());

        holder.ivCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(signUpAllListViewModel.getFlag()==1){
                    users.remove(position);
                    notifyDataSetChanged();
                    GlobalConstant.geteNumberArrayList().remove(position);
                    SignUpActivity.setListViewHeightBasedOnChildren(SignUpActivity.eNumberList,users.size());
                }
                if(signUpAllListViewModel.getFlag()==2){
                    users.remove(position);
                    notifyDataSetChanged();
                    if(!GlobalConstant.getsNameArrayList().isEmpty()){
                        GlobalConstant.getsNameArrayList().remove(position);
                    }
                    SignUpActivity.setListViewHeightBasedOnChildren(SignUpActivity.supervisorList,users.size());
                }
                if(signUpAllListViewModel.getFlag()==3){
                    users.remove(position);
                    notifyDataSetChanged();
                    if(!GlobalConstant.getgNameArrayList().isEmpty()){
                        GlobalConstant.getgNameArrayList().remove(position);
                    }
                    SignUpActivity.setListViewHeightBasedOnChildren(SignUpActivity.groupList,users.size());
                }
            }
        });

        return convertView;
    }
    class ViewHolder{
        TextView tvContactName;
        ImageView ivCheckBox;
    }
}
