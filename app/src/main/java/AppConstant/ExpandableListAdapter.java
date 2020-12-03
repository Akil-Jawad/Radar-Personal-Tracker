package AppConstant;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.summittracker.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import Model.EmergencyNumberModel;

import static AppConstant.GlobalConstant.getCoordinatorList;
import static AppConstant.GlobalConstant.getSearchCoordinatorList;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private List<String> _listDataHeader;
    private List<String> _modelHeader=new ArrayList<>();

    private HashMap<String, List<String>> _listDataChild;

    public ExpandableListAdapter(Context _context, List<String> _listDataHeader, HashMap<String, List<String>> _listDataChild) {
        this._context = _context;
        this._listDataHeader = _listDataHeader;
        this._listDataChild = _listDataChild;
        _modelHeader.addAll(_listDataHeader);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group_header, null);
        }

        TextView lblListHeader = convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);


        return convertView;
    }

    public void filter(String chartext){
        chartext = chartext.toLowerCase(Locale.getDefault());
        _listDataHeader.clear();
        //_listDataChild.clear();
        getSearchCoordinatorList().clear();
        if(chartext.length()==0){
            _listDataHeader.addAll(_modelHeader);
            getSearchCoordinatorList().addAll(getCoordinatorList());
        }else {
            for(String m : _modelHeader){
                if(m.toLowerCase().contains(chartext)){
                    _listDataHeader.add(m);
                    for (int i = 0; i < getCoordinatorList().size(); i++) {
                        if(m.equals(getCoordinatorList().get(i).getUser_name())){
                            getSearchCoordinatorList().add(getCoordinatorList().get(i));
                        }
                    }
                }
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expand_list_item, null);
        }

        TextView txtListChild = convertView
                .findViewById(R.id.lblListItem);

        txtListChild.setText(childText);
        return convertView;    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
