package Model;

import android.os.Parcel;
import android.os.Parcelable;

public class UserGroups implements Parcelable {
    private int group_row_id;
    private String group_name;

    public UserGroups(int group_row_id, String group_name) {
        this.group_row_id = group_row_id;
        this.group_name = group_name;
    }

    public int getGroup_row_id() {
        return group_row_id;
    }

    public void setGroup_row_id(int group_row_id) {
        this.group_row_id = group_row_id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    protected UserGroups(Parcel in) {
        group_row_id = in.readInt();
        group_name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(group_row_id);
        dest.writeString(group_name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserGroups> CREATOR = new Creator<UserGroups>() {
        @Override
        public UserGroups createFromParcel(Parcel in) {
            return new UserGroups(in);
        }

        @Override
        public UserGroups[] newArray(int size) {
            return new UserGroups[size];
        }
    };
}
