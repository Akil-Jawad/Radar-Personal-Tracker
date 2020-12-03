package Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class UserFullData implements Parcelable {
    private int row_id;
    private String user_name;
    private String email;
    private String password;
    private String mobile_number;
    private String present_address;
    private String supervisor_name;
    private ArrayList<UserGroups> user_groups=new ArrayList<>();
    private ArrayList<UserEmergencyNumber> user_emergency_numbers=new ArrayList<>();

    public UserFullData(int row_id, String user_name, String email, String password, String mobile_number, String present_address, String supervisor_name, ArrayList<UserGroups> user_groups, ArrayList<UserEmergencyNumber> user_emergency_numbers) {
        this.row_id = row_id;
        this.user_name = user_name;
        this.email = email;
        this.password = password;
        this.mobile_number = mobile_number;
        this.present_address = present_address;
        this.supervisor_name = supervisor_name;
        this.user_groups = user_groups;
        this.user_emergency_numbers = user_emergency_numbers;
    }

    public UserFullData() {

    }

    public int getRow_id() {
        return row_id;
    }

    public void setRow_id(int row_id) {
        this.row_id = row_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getPresent_address() {
        return present_address;
    }

    public void setPresent_address(String present_address) {
        this.present_address = present_address;
    }

    public String getSuprevisor_name() {
        return supervisor_name;
    }

    public void setSuprevisor_name(String supervisor_name) {
        this.supervisor_name = supervisor_name;
    }

    public ArrayList<UserGroups> getUser_groups() {
        return user_groups;
    }

    public void setUser_groups(ArrayList<UserGroups> user_groups) {
        this.user_groups = user_groups;
    }

    public ArrayList<UserEmergencyNumber> getUser_emergency_numbers() {
        return user_emergency_numbers;
    }

    public void setUser_emergency_numbers(ArrayList<UserEmergencyNumber> user_emergency_numbers) {
        this.user_emergency_numbers = user_emergency_numbers;
    }

    protected UserFullData(Parcel in) {
        row_id = in.readInt();
        user_name = in.readString();
        email = in.readString();
        password = in.readString();
        mobile_number = in.readString();
        present_address = in.readString();
        supervisor_name = in.readString();
        user_groups = in.createTypedArrayList(UserGroups.CREATOR);
        user_emergency_numbers = in.createTypedArrayList(UserEmergencyNumber.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(row_id);
        dest.writeString(user_name);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(mobile_number);
        dest.writeString(present_address);
        dest.writeString(supervisor_name);
        dest.writeTypedList(user_groups);
        dest.writeTypedList(user_emergency_numbers);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserFullData> CREATOR = new Creator<UserFullData>() {
        @Override
        public UserFullData createFromParcel(Parcel in) {
            return new UserFullData(in);
        }

        @Override
        public UserFullData[] newArray(int size) {
            return new UserFullData[size];
        }
    };
}
