package Model;

import android.os.Parcel;
import android.os.Parcelable;

public class GetAllGroupMembersDetails implements Parcelable {
    private int row_id;
    private String user_name;
    private String email;
    private String phone;
    private double lat;
    private double lon;
    private String geolocation;

    public GetAllGroupMembersDetails(int row_id, String user_name, String email, String phone, double lat, double lon, String geolocation) {
        this.row_id = row_id;
        this.user_name = user_name;
        this.email = email;
        this.phone = phone;
        this.lat = lat;
        this.lon = lon;
        this.geolocation = geolocation;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getGeolocation() {
        return geolocation;
    }

    public void setGeolocation(String geolocation) {
        this.geolocation = geolocation;
    }

    protected GetAllGroupMembersDetails(Parcel in) {
        row_id = in.readInt();
        user_name = in.readString();
        email = in.readString();
        phone = in.readString();
        lat = in.readDouble();
        lon = in.readDouble();
        geolocation = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(row_id);
        dest.writeString(user_name);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeDouble(lat);
        dest.writeDouble(lon);
        dest.writeString(geolocation);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GetAllGroupMembersDetails> CREATOR = new Creator<GetAllGroupMembersDetails>() {
        @Override
        public GetAllGroupMembersDetails createFromParcel(Parcel in) {
            return new GetAllGroupMembersDetails(in);
        }

        @Override
        public GetAllGroupMembersDetails[] newArray(int size) {
            return new GetAllGroupMembersDetails[size];
        }
    };
}
