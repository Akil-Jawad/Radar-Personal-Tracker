package Model;

import android.os.Parcel;
import android.os.Parcelable;

public class SubCenterList implements Parcelable {
    private int row_id;
    private String subcenter_name;
    private double latitude;
    private double longitude;
    private double distance;

    public SubCenterList(int row_id, String subcenter_name, double latitude, double longitude, double distance) {
        this.row_id = row_id;
        this.subcenter_name = subcenter_name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
    }

    public int getRow_id() {
        return row_id;
    }

    public void setRow_id(int row_id) {
        this.row_id = row_id;
    }

    public String getSubcenter_name() {
        return subcenter_name;
    }

    public void setSubcenter_name(String subcenter_name) {
        this.subcenter_name = subcenter_name;
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

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    protected SubCenterList(Parcel in) {
        row_id = in.readInt();
        subcenter_name = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        distance = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(row_id);
        dest.writeString(subcenter_name);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeDouble(distance);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SubCenterList> CREATOR = new Creator<SubCenterList>() {
        @Override
        public SubCenterList createFromParcel(Parcel in) {
            return new SubCenterList(in);
        }

        @Override
        public SubCenterList[] newArray(int size) {
            return new SubCenterList[size];
        }
    };
}
