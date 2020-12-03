package Model;

import android.os.Parcel;
import android.os.Parcelable;

public class UserLocationModel implements Parcelable {
    private int user_id;
    private double latitude;
    private double longitude;
    private String geolocation;

    public UserLocationModel(int user_id, double latitude, double longitude, String geolocation) {
        this.user_id = user_id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.geolocation = geolocation;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getGeolocation() {
        return geolocation;
    }

    public void setGeolocation(String geolocation) {
        this.geolocation = geolocation;
    }

    public static Creator<UserLocationModel> getCREATOR() {
        return CREATOR;
    }

    protected UserLocationModel(Parcel in) {
        user_id = in.readInt();
        latitude = in.readDouble();
        longitude = in.readDouble();
        geolocation = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(user_id);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(geolocation);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserLocationModel> CREATOR = new Creator<UserLocationModel>() {
        @Override
        public UserLocationModel createFromParcel(Parcel in) {
            return new UserLocationModel(in);
        }

        @Override
        public UserLocationModel[] newArray(int size) {
            return new UserLocationModel[size];
        }
    };
}
