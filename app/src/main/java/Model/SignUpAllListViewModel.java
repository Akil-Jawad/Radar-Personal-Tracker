package Model;

import android.os.Parcel;
import android.os.Parcelable;

public class SignUpAllListViewModel implements Parcelable {
    private String personName;
    public boolean isChecked;
    private int flag;

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public static Creator<SignUpAllListViewModel> getCREATOR() {
        return CREATOR;
    }

    public SignUpAllListViewModel(String personName, boolean isChecked, int flag) {
        this.personName = personName;
        this.isChecked = isChecked;
        this.flag = flag;
    }

    public SignUpAllListViewModel(Parcel in) {
        personName = in.readString();
        isChecked = in.readByte() != 0;
        flag = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(personName);
        dest.writeByte((byte) (isChecked ? 1 : 0));
        dest.writeInt(flag);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SignUpAllListViewModel> CREATOR = new Creator<SignUpAllListViewModel>() {
        @Override
        public SignUpAllListViewModel createFromParcel(Parcel in) {
            return new SignUpAllListViewModel(in);
        }

        @Override
        public SignUpAllListViewModel[] newArray(int size) {
            return new SignUpAllListViewModel[size];
        }
    };
}
