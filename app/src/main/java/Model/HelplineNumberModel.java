package Model;

import android.os.Parcel;
import android.os.Parcelable;

public class HelplineNumberModel implements Parcelable {
    private String helplineName;
    private String helplineNumber;

    public HelplineNumberModel(String helplineName, String helplineNumber) {
        this.helplineName = helplineName;
        this.helplineNumber = helplineNumber;
    }

    public HelplineNumberModel() {

    }

    public String getHelplineName() {
        return helplineName;
    }

    public void setHelplineName(String helplineName) {
        this.helplineName = helplineName;
    }

    public String getHelplineNumber() {
        return helplineNumber;
    }

    public void setHelplineNumber(String helplineNumber) {
        this.helplineNumber = helplineNumber;
    }

    public static Creator<HelplineNumberModel> getCREATOR() {
        return CREATOR;
    }

    protected HelplineNumberModel(Parcel in) {
        helplineName = in.readString();
        helplineNumber = in.readString();
    }

    public static final Creator<HelplineNumberModel> CREATOR = new Creator<HelplineNumberModel>() {
        @Override
        public HelplineNumberModel createFromParcel(Parcel in) {
            return new HelplineNumberModel(in);
        }

        @Override
        public HelplineNumberModel[] newArray(int size) {
            return new HelplineNumberModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(helplineName);
        dest.writeString(helplineNumber);
    }
}
