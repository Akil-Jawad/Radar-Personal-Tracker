package Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class HistoryModel implements Parcelable {
    private List<UserLocationModel> list;
    private String date;

    public HistoryModel(List<UserLocationModel> list, String date) {
        this.list = list;
        this.date = date;
    }

    public HistoryModel(){

    }
    public List<UserLocationModel> getList() {
        return list;
    }

    public void setList(List<UserLocationModel> list) {
        this.list = list;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    protected HistoryModel(Parcel in) {
        list = in.createTypedArrayList(UserLocationModel.CREATOR);
        date = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(list);
        dest.writeString(date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<HistoryModel> CREATOR = new Creator<HistoryModel>() {
        @Override
        public HistoryModel createFromParcel(Parcel in) {
            return new HistoryModel(in);
        }

        @Override
        public HistoryModel[] newArray(int size) {
            return new HistoryModel[size];
        }
    };
}
