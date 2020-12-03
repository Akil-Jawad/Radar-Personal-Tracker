package Model;

import android.os.Parcel;
import android.os.Parcelable;

public class GroupListModel implements Parcelable {
    public int id;
    private String groupName;
    public boolean isChecked;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public static Creator<GroupListModel> getCREATOR() {
        return CREATOR;
    }

    public GroupListModel(int id, String groupName, boolean isChecked) {
        this.id = id;
        this.groupName = groupName;
        this.isChecked = isChecked;
    }

    protected GroupListModel(Parcel in) {
        id = in.readInt();
        groupName = in.readString();
        isChecked = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(groupName);
        dest.writeByte((byte) (isChecked ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GroupListModel> CREATOR = new Creator<GroupListModel>() {
        @Override
        public GroupListModel createFromParcel(Parcel in) {
            return new GroupListModel(in);
        }

        @Override
        public GroupListModel[] newArray(int size) {
            return new GroupListModel[size];
        }
    };
}
