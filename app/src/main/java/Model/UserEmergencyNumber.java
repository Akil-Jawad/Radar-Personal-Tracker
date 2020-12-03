package Model;

import android.os.Parcel;
import android.os.Parcelable;

public class UserEmergencyNumber implements Parcelable {
    private int emp_row_id;
    private String contact_name;
    private String mobile_number;

    public UserEmergencyNumber(int emp_row_id, String mobile_number,String contact_name) {
        this.emp_row_id = emp_row_id;
        this.mobile_number = mobile_number;
        this.contact_name = contact_name;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public int getEmp_row_id() {
        return emp_row_id;
    }

    public void setEmp_row_id(int emp_row_id) {
        this.emp_row_id = emp_row_id;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    protected UserEmergencyNumber(Parcel in) {
        emp_row_id = in.readInt();
        mobile_number = in.readString();
        contact_name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(emp_row_id);
        dest.writeString(mobile_number);
        dest.writeString(contact_name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserEmergencyNumber> CREATOR = new Creator<UserEmergencyNumber>() {
        @Override
        public UserEmergencyNumber createFromParcel(Parcel in) {
            return new UserEmergencyNumber(in);
        }

        @Override
        public UserEmergencyNumber[] newArray(int size) {
            return new UserEmergencyNumber[size];
        }
    };
}
