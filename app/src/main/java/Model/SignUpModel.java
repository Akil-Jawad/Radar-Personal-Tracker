package Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class SignUpModel implements Parcelable {
    private String user_name;
    private String email;
    private String password;
    private String mobile_number;
    private String present_address;
    private int supervisor_id;
    private ArrayList<Integer> group_row_id;
    private ArrayList<String> emergency_number;

    public SignUpModel() {

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

    public int getSuprevisor_id() {
        return supervisor_id;
    }

    public void setSuprevisor_id(int supervisor_id) {
        this.supervisor_id = supervisor_id;
    }

    public ArrayList<Integer> getGroup_row_id() {
        return group_row_id;
    }

    public void setGroup_row_id(ArrayList<Integer> group_row_id) {
        this.group_row_id = group_row_id;
    }

    public ArrayList<String> getEmergency_number() {
        return emergency_number;
    }

    public void setEmergency_number(ArrayList<String> emergency_number) {
        this.emergency_number = emergency_number;
    }

    public static Creator<SignUpModel> getCREATOR() {
        return CREATOR;
    }

    public SignUpModel(String user_name, String email, String password, String mobile_number, String present_address, int supervisor_id, ArrayList<Integer> group_row_id, ArrayList<String> emergency_number) {
        this.user_name = user_name;
        this.email = email;
        this.password = password;
        this.mobile_number = mobile_number;
        this.present_address = present_address;
        this.supervisor_id = supervisor_id;
        this.group_row_id = group_row_id;
        this.emergency_number = emergency_number;
    }

    protected SignUpModel(Parcel in) {
        user_name = in.readString();
        email = in.readString();
        password = in.readString();
        mobile_number = in.readString();
        present_address = in.readString();
        supervisor_id = in.readInt();
        emergency_number = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_name);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(mobile_number);
        dest.writeString(present_address);
        dest.writeInt(supervisor_id);
        dest.writeStringList(emergency_number);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SignUpModel> CREATOR = new Creator<SignUpModel>() {
        @Override
        public SignUpModel createFromParcel(Parcel in) {
            return new SignUpModel(in);
        }

        @Override
        public SignUpModel[] newArray(int size) {
            return new SignUpModel[size];
        }
    };
}
