package AppConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Model.EmergencyNumberModel;
import Model.GetAllGroupMembersDetails;
import Model.HistoryModel;
import Model.SubCenterList;
import Model.UserFullData;
import Model.GroupListModel;
import Model.SupervisorNameModel;
import Model.UserLocationModel;

public class GlobalConstant {
    public static final String IP_ADDRESS = "http://192.168.0.100";
    private static final String SIGNUP_URL = IP_ADDRESS+"/scl_tracker_api/public/api/signup_post";
    private static final String SIGNIN_URL = IP_ADDRESS+"/scl_tracker_api/public/api/signin_user";
    private static final String SELECT_SUPERVISOR_URL = IP_ADDRESS+"/scl_tracker_api/public/api/select_supervisor";
    private static final String SELECT_GROUP_URL = IP_ADDRESS+"/scl_tracker_api/public/api/select_group";
    private static final String UPDATE_USER_LOCATION = IP_ADDRESS+"/scl_tracker_api/public/api/update_user_location";
    private static final String GROUP_MEMBERS = IP_ADDRESS+"/scl_tracker_api/public/api/get_group_members";
    private static final String SUBCENTER_LOCATION = IP_ADDRESS+"/scl_tracker_api/public/api/get_subcenter_location";
    private static final String UPDATE_COORDINATORS = IP_ADDRESS+"/scl_tracker_api/public/api/update_coordinators";
    private static final String LOCATION_HISTORY = IP_ADDRESS+"/scl_tracker_api/public/api/get_location_history";
    public static ArrayList<EmergencyNumberModel> eNumberArrayList= new ArrayList<>();
    public static ArrayList<SupervisorNameModel> sNameArrayList= new ArrayList<>();
    public static ArrayList<GroupListModel> gNameArrayList= new ArrayList<>();
    public static ArrayList<GetAllGroupMembersDetails> grpMembrsDetail = new ArrayList<>();
    public static HashMap<Integer, List<GetAllGroupMembersDetails>> mapGroups = new HashMap<>();
    public static UserFullData userFullData = new UserFullData();
    public static List<SupervisorNameModel> modelSupervisor=new ArrayList<>();
    public static List<GroupListModel> modelGroup=new ArrayList<>();
    public static ArrayList<SubCenterList> subCenterLists=new ArrayList<>();
    public static ArrayList<UserFullData> coordinatorList = new ArrayList<>();
    public static ArrayList<UserFullData> searchCoordinatorList = new ArrayList<>();
    public static HistoryModel historyWithDate = new HistoryModel();
    public static HashMap<String,HistoryModel> historyInMap = new HashMap<>();
    public static List<HistoryModel> historyModels = new ArrayList<>();

    public static List<HistoryModel> getHistoryModels() {
        return historyModels;
    }

    public static void setHistoryModels(List<HistoryModel> historyModels) {
        GlobalConstant.historyModels = historyModels;
    }

    public static HashMap<String, HistoryModel> getHistoryInMap() {
        return historyInMap;
    }
    public static HistoryModel getHistoryWithDate() {
        return historyWithDate;
    }

    public static String getLocationHistory() {
        return LOCATION_HISTORY;
    }

    public static ArrayList<UserFullData> getSearchCoordinatorList() {
        return searchCoordinatorList;
    }

    public static UserLocationModel userLocationModel = new UserLocationModel(0,0.0,0.0,null);
    public static boolean completeSignUp;

    public static ArrayList<UserFullData> getCoordinatorList() {
        return coordinatorList;
    }

    public static String getUpdateCoordinators() {
        return UPDATE_COORDINATORS;
    }

    public static String getGroupMembers() {
        return GROUP_MEMBERS;
    }

    public static ArrayList<GetAllGroupMembersDetails> getGrpMembrsDetail() {
        return grpMembrsDetail;
    }

    public static boolean isCompleteSignUp() {
        return completeSignUp;
    }

    public static void setCompleteSignUp(boolean completeSignUp) {
        GlobalConstant.completeSignUp = completeSignUp;
    }
    public static String getUpdateUserLocation() {
        return UPDATE_USER_LOCATION;
    }

    public static UserLocationModel getUserLocationModel() {
        return userLocationModel;
    }

    public static String getSubcenterLocation() {
        return SUBCENTER_LOCATION;
    }

    public static String getSigninUrl() {
        return SIGNIN_URL;
    }

    public static String getSignupUrl() {
        return SIGNUP_URL;
    }

    public static ArrayList<EmergencyNumberModel> geteNumberArrayList() {
        return eNumberArrayList;
    }

    public static ArrayList<SupervisorNameModel> getsNameArrayList() {
        return sNameArrayList;
    }

    public static ArrayList<GroupListModel> getgNameArrayList() {
        return gNameArrayList;
    }

    public static String getSelectGroupUrl() {
        return SELECT_GROUP_URL;
    }

    public static String getSelectSupervisorUrl() {
        return SELECT_SUPERVISOR_URL;
    }

    public static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }

    }



}
