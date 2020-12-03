package Model;

import android.os.Parcel;
import android.os.Parcelable;

public class EmergencyNumberModel implements Parcelable {
    public String contactName;
    public String contactNumber;
    public boolean isChecked;

    public EmergencyNumberModel(String contactName, String contactNumber, boolean isChecked) {
        this.contactName = contactName;
        this.contactNumber = contactNumber;
        this.isChecked = isChecked;
    }

    protected EmergencyNumberModel(Parcel in) {
        contactName = in.readString();
        contactNumber = in.readString();
        isChecked = in.readByte() != 0;
    }

    public static final Creator<EmergencyNumberModel> CREATOR = new Creator<EmergencyNumberModel>() {
        @Override
        public EmergencyNumberModel createFromParcel(Parcel in) {
            return new EmergencyNumberModel(in);
        }

        @Override
        public EmergencyNumberModel[] newArray(int size) {
            return new EmergencyNumberModel[size];
        }
    };

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(contactName);
        dest.writeString(contactNumber);
        dest.writeByte((byte) (isChecked ? 1 : 0));
    }
}
