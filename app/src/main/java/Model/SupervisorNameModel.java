package Model;

import android.os.Parcel;
import android.os.Parcelable;

public class SupervisorNameModel implements Parcelable {
    public int id;
    public String supervisorName;
    public String email;
    public String phone;
    public boolean isChecked;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSupervisorName() {
        return supervisorName;
    }

    public void setName(String supervisorName) {
        this.supervisorName = supervisorName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public static Creator<SupervisorNameModel> getCREATOR() {
        return CREATOR;
    }

    public SupervisorNameModel(int id, String supervisorName, String email, String phone, boolean isChecked) {
        this.id = id;
        this.supervisorName = supervisorName;
        this.email = email;
        this.phone = phone;
        this.isChecked = isChecked;
    }

    protected SupervisorNameModel(Parcel in) {
        id = in.readInt();
        supervisorName = in.readString();
        email = in.readString();
        phone = in.readString();
        isChecked = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(supervisorName);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeByte((byte) (isChecked ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SupervisorNameModel> CREATOR = new Creator<SupervisorNameModel>() {
        @Override
        public SupervisorNameModel createFromParcel(Parcel in) {
            return new SupervisorNameModel(in);
        }

        @Override
        public SupervisorNameModel[] newArray(int size) {
            return new SupervisorNameModel[size];
        }
    };
}
