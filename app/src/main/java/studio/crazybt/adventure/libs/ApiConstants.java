package studio.crazybt.adventure.libs;

import android.content.Context;
import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

import studio.crazybt.adventure.R;
import studio.crazybt.adventure.utils.RealmUtils;
import studio.crazybt.adventure.utils.SharedPref;


/**
 * Created by Brucelee Thanh on 07/10/2016.
 */

public class ApiConstants {
    
    private static Context context;

    public static Uri.Builder builder;

    // Request code & response code
    public static final int RESPONSE_CODE_SUCCESS = 200;

    // API Urls

    /* Remember: change both API_ROOT and API_ROOT_IMAGE */
    //private static final String API_ROOT = "155.94.144.150:25763/api";

    private static final String FIRST_API_ROOT = "192.168.1.83:25763/api";
    private static final String FIRST_API_ROOT_IMAGES = "192.168.1.83:25763";

//    private static final String API_ROOT = "192.168.1.9:25763/api";
//    private static final String API_ROOT_IMAGES = "192.168.1.9:25763";

//    private static final String API_ROOT = "192.168.7.125:25763/api";
//    private static final String API_ROOT_IMAGES = "192.168.7.125:25763";

    private static String API_ROOT;
    private static String API_ROOT_IMAGES;

    private static final String API_GOOGLE_MAP_DIRECTION = "https://maps.googleapis.com/maps/api/directions/json?origin=%1$s&destination=%2$s&key=%3$s";

    // apis
    private static final String API_SCHEME = "http";
    public static final String API_NORMAL_SIGNUP = "user/sign_up";
    public static final String API_NORMAL_LOGIN = "user/login";
    public static final String API_FB_LOGIN = "user/fb_login";
    public static final String API_PROFILE_USER = "user/profile";
    public static final String API_EDIT_PROFILE_USER = "user/edit_profile";
    public static final String API_UPLOAD_IMAGE = "file/upload_image";
    public static final String API_CREATE_STATUS = "status/create";
    public static final String API_FIND_ONE_STATUS = "status/find_one";
    public static final String API_BROWSE_LIKE = "like_status/browse";
    public static final String API_LIKE_STATUS = "like_status/like";
    public static final String API_BROWSE_COMMENT = "comment_status/browse";
    public static final String API_COMMENT_STATUS = "comment_status/create";
    public static final String API_COMMENT_EDIT_CONTENT = "comment_status/edit_content";
    public static final String API_DELETE_COMMENT = "comment_status/delete";
    public static final String API_BROWSE_NOTIFICATION = "notification/browse";
    public static final String API_VIEWED_NOTIFICATION = "notification/viewed";
    public static final String API_CLICKED_NOTIFICATION = "notification/clicked";
    public static final String API_TIME_LINE = "news/time_line";
    public static final String API_NEWS_FEED = "news/news_feed";
    public static final String API_PUBLIC_TRIP = "news/public_trip";
    public static final String API_SUGGEST_FRIEND = "friend/suggest_friend";
    public static final String API_BROWSE_FRIEND = "friend/browse";
    public static final String API_SEND_REQUEST_FRIEND = "friend_request/send_request";
    public static final String API_BROWSE_REQUEST_FRIEND = "friend_request/browse";
    public static final String API_CONFIRM_REQUEST_FRIEND = "friend_request/confirm";
    public static final String API_CREATE_TRIP = "trip/create";
    public static final String API_BROWSE_TRIP = "trip/browse";
    public static final String API_DETAIL_TRIP = "trip/detail";
    public static final String API_INTERESTED_TRIP = "trip/interested";
    public static final String API_UNINTERESTED_TRIP = "trip/uninterested";
    public static final String API_BROWSE_INTERESTED_TRIP = "trip/browse_interested";
    public static final String API_CREATE_PLACE_TRIP_MAP = "trip_map/create_place";
    public static final String API_CREATE_TRIP_DIARY = "trip_diary/create";
    public static final String API_BROWSE_TRIP_DIARY = "trip_diary/browse";
    public static final String API_DETAIL_TRIP_DIARY = "trip_diary/detail";
    public static final String API_BROWSE_TRIP_MEMBER = "trip_member/browse_member";
    public static final String API_REQUEST_TRIP_MEMBER = "trip_member/request";
    public static final String API_CANCEL_REQUEST_TRIP_MEMBER = "trip_member/cancel_request";
    public static final String API_BROWSE_REQUEST_TRIP_MEMBER = "trip_member/browse_request";
    public static final String API_ACCEPT_REQUEST_TRIP_MEMBER = "trip_member/accept_request";
    public static final String API_REJECT_REQUEST_TRIP_MEMBER = "trip_member/reject_request";
    public static final String API_LEAVE_TRIP_TRIP_MEMBER = "trip_member/leave_trip";
    public static final String API_SEARCH_FRIEND = "friend/search";
    public static final String API_CREATE_GROUP = "group/create";
    public static final String API_BROWSE_GROUP = "group/browse";
    public static final String API_DETAIL_GROUP = "group/detail";
    public static final String API_INFO_GROUP = "group/info";
    public static final String API_BROWSE_GROUP_MEMBER = "group_member/browse";
    public static final String API_REQUEST_GROUP_MEMBER = "group_member/request";
    public static final String API_BROWSE_REQUEST_GROUP_MEMBER = "group_member/browse_request";
    public static final String API_CANCEL_REQUEST_GROUP_MEMBER = "group_member/cancel_request";
    public static final String API_ACCEPT_REQUEST_GROUP_MEMBER = "group_member/accept_request";
    public static final String API_REJECT_REQUEST_GROUP_MEMBER = "group_member/reject_request";
    public static final String API_INVITE_GROUP_MEMBER = "group_member/invite";
    public static final String API_REMOVE_INVITE_GROUP_MEMBER = "group_member/remove_invite";
    public static final String API_SEARCH_INVITE_GROUP_MEMBER = "group_member/search_invite";
    public static final String API_LEAVE_GROUP_GROUP_MEMBER = "group_member/leave_group";
    public static final String API_REMOVE_MEMBER_GROUP_MEMBER = "group_member/remove_member";
    public static final String API_MAKE_ADMIN_GROUP_MEMBER = "group_member/make_admin";
    public static final String API_REMOVE_ADMIN_GROUP_MEMBER = "group_member/remove_admin";
    public static final String API_BLOCK_MEMBER_GROUP_MEMBER = "group_member/block_member";
    public static final String API_UNBLOCK_MEMBER_GROUP_MEMBER = "group_member/unblock_member";
    public static final String API_CONVERSATION_BROWSE = "conversation/browse";
    public static final String API_CONVERSATION_INITIALIZE = "conversation/initialize";
    public static final String API_STATUS_TRIP_BROWSE = "status/trip_browse";

    // socket
    public static final String SOCKET_USER_ONLINE = "user_online";
    public static final String SOCKET_JOIN_ROOM = "join_room";
    public static final String SOCKET_LEAVE_ROOM = "leave_room";
    public static final String SOCKET_CHAT_TO_ROOM = "chat_to_room";
    public static final String SOCKET_NEW_MESSAGE = "new_message";
    public static final String SOCKET_TYPING = "typing";
    public static final String SOCKET_STOP_TYPING = "stop_typing";

    // Default params
    public static final String DEF_CODE = "code";
    public static final String DEF_MSG = "message";
    public static final String DEF_DATA = "data";

    // Key params
    public static final String KEY_ID = "_id";
    public static final String KEY_ID_STATUS = "id_status";
    public static final String KEY_ID_COMMENT = "id_comment";
    public static final String KEY_ID_GROUP = "id_group";
    public static final String KEY_ID_TRIP = "id_trip";
    public static final String KEY_ID_USER = "id_user";
    public static final String KEY_PHONE_NUMBER_EMAIL = "phone_number_email";
    public static final String KEY_PHONE_NUMBER = "phone_number";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_FIRST_NAME = "first_name";
    public static final String KEY_LAST_NAME = "last_name";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_BIRTHDAY = "birthday";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_RELIGION = "religion";
    public static final String KEY_INTRO = "intro";
    public static final String KEY_ID_FACEBOOK = "id_facebook";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_FCM_TOKEN = "fcm_token";
    public static final String KEY_FB_TOKEN = "fb_token";
    public static final String KEY_FILE = "file";
    public static final String KEY_LINK = "link";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_IMAGE_DESCRIPTION = "image_description";
    public static final String KEY_PERMISSION = "permission";
    public static final String KEY_TYPE = "type";
    public static final String KEY_AMOUNT_LIKE = "amount_like";
    public static final String KEY_AMOUNT_COMMENT = "amount_comment";
    public static final String KEY_IS_LIKE = "is_like";
    public static final String KEY_IS_COMMENT = "is_comment";
    public static final String KEY_IS_FRIEND = "is_friend";
    public static final String KEY_URL = "url";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_CREATED_AT = "created_at";
    public static final String KEY_LAST_VISITED_AT = "last_visited_at";
    public static final String KEY_OWNER = "owner";
    public static final String KEY_IMAGES = "images";
    public static final String KEY_AVATAR = "avatar";
    public static final String KEY_AVATAR_ACTUAL = "avatar_actual";
    public static final String KEY_COVER = "cover";
    public static final String KEY_PAGE = "page";
    public static final String KEY_PERPAGE = "per_page";
    public static final String KEY_USER = "user";
    public static final String KEY_SENDER = "sender";
    public static final String KEY_SENDER_AVATAR = "sender_avatar";
    public static final String KEY_RECIPIENT = "recipient";
    public static final String KEY_STATUS = "status";
    public static final String KEY_OBJECT = "object";
    public static final String KEY_CLICKED = "clicked";
    public static final String KEY_VIEWED = "viewed";
    public static final String KEY_FCM_CONTENT = "fcm_content";
    public static final String KEY_NAME = "name";
    public static final String KEY_START_AT = "start_at";
    public static final String KEY_END_AT = "end_at";
    public static final String KEY_START_POSITION = "start_position";
    public static final String KEY_DESTINATION_SUMMARY = "destination_summary";
    public static final String KEY_EXPENSE = "expense";
    public static final String KEY_AMOUNT_PEOPLE = "amount_people";
    public static final String KEY_AMOUNT_MEMBER = "amount_member";
    public static final String KEY_AMOUNT_INTERESTED = "amount_interested";
    public static final String KEY_AMOUNT_RATING = "amount_rating";
    public static final String KEY_RATING = "rating";
    public static final String KEY_VEHICLES = "vehicles";
    public static final String KEY_ROUTES = "routes";
    public static final String KEY_PREPARE = "prepare";
    public static final String KEY_NOTE = "note";
    public static final String KEY_TITLE = "title";
    public static final String KEY_ORDER = "order";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_SCHEDULE = "schedule";
    public static final String KEY_MAP = "map";
    public static final String KEY_IS_MEMBER = "is_member";
    public static final String KEY_IS_INTERESTED = "is_interested";
    public static final String KEY_MEMBERS = "members";
    public static final String KEY_ID_TRIP_MEMBER = "id_trip_member";
    public static final String KEY_DATE = "date";
    public static final String KEY_DETAIL_DIARY = "detail_diary";
    public static final String KEY_DIARIES = "diaries";
    public static final String KEY_ID_TRIP_DIARY = "id_trip_diary";
    public static final String KEY_DISCUSS = "discuss";
    public static final String KEY_FRIENDS = "friends";
    public static final String KEY_STRANGERS = "strangers";
    public static final String KEY_TOTAL = "total";
    public static final String KEY_TOTAL_FRIEND = "total_friend";
    public static final String KEY_TOTAL_STRANGER = "total_stranger";
    public static final String KEY_TOTAL_MEMBER = "total_member";
    public static final String KEY_KEYWORD = "keyword";
    public static final String KEY_GROUP_CREATE = "group_create";
    public static final String KEY_GROUP_MANAGE = "group_manage";
    public static final String KEY_GROUP_JOIN = "group_join";
    public static final String KEY_GROUP_REQUEST = "group_request";
    public static final String KEY_GROUP_SUGGEST = "group_suggest";
    public static final String KEY_GROUP_INVITE = "group_invite";
    public static final String KEY_YOUR_STATUS = "your_status";
    public static final String KEY_GROUP_POST = "group_post";
    public static final String KEY_GROUP_IMAGES = "group_images";
    public static final String KEY_TYPE_ITEM = "type_item";
    public static final String KEY_GROUP = "group";
    public static final String KEY_REQUESTS = "requests";
    public static final String KEY_BLOCKS = "blocks";
    public static final String KEY_ADMINS = "admins";
    public static final String KEY_ID_GROUP_MEMBER = "id_group_member";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_SUMMARY_INFO = "summary_info";
    public static final String KEY_TIME_LINE = "time_line";
    public static final String KEY_INFO = "info";
    public static final String KEY_USER_POST = "user_post";
    public static final String KEY_COUNT_TRIP_CREATED = "count_trip_created";
    public static final String KEY_COUNT_TRIP_JOINED = "count_trip_joined";
    public static final String KEY_COUNT_PLACE_ARRIVED = "count_place_arrived";
    public static final String KEY_COUNT_FOLLOWER = "count_follower";
    public static final String KEY_USER_ONLINE = "user_online";
    public static final String KEY_TOTAL_USER_ONLINE = "total_user_online";
    public static final String KEY_CONVERSATION = "conversation";
    public static final String KEY_TOTAL_CONVERSATION = "total_conversation";
    public static final String KEY_NOTIFY = "notify";
    public static final String KEY_PARTNER = "partner";
    public static final String KEY_ID_PARTNER = "id_partner";
    public static final String KEY_LATEST_MESSAGE = "latest_message";
    public static final String KEY_PARTNER_SOCKET_ID = "partner_socket_id";
    public static final String KEY_PARTNER_VISITED_AT = "partner_visited_at";
    public static final String KEY_MESSAGES = "messages";
    public static final String KEY_TOTAL_MESSAGE = "total_message";
    public static final String KEY_ID_CONVERSATION = "id_conversation";
    public static final String KEY_TRIP_CREATE = "trip_create";
    public static final String KEY_TRIP_JOIN = "trip_join";
    public static final String KEY_TRIP_REQUEST = "trip_request";
    public static final String KEY_TRIP_INTERESTED = "trip_interested";
    public static final String KEY_RELATION = "relation";

    public ApiConstants() {

    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        ApiConstants.context = context;
    }

    public static void instance(Context cxt){
        context = cxt;
        API_ROOT = SharedPref.getInstance(context).getString("API_ROOT", null);
        API_ROOT_IMAGES = SharedPref.getInstance(context).getString("API_ROOT_IMAGES", null);
    }

    public static String getFirstApiRoot(){
        return FIRST_API_ROOT;
    }

    public static String getFirstApiRootImages(){
        return FIRST_API_ROOT_IMAGES;
    }

    public static void setApiRoot(String url){
        API_ROOT = url;
        SharedPref.getInstance(context).putString("API_ROOT", url);
    }

    public static String getApiRoot(){
        return API_ROOT;
    }

    public static void setApiRootImages(String urlImages){
        API_ROOT_IMAGES = urlImages;
        SharedPref.getInstance(context).putString("API_ROOT_IMAGES", urlImages);
    }

    public static String getApiRootImages(){
        return API_ROOT_IMAGES;
    }

    private static void setBaseUrl(String rootUrl) {
        builder = new Uri.Builder();
        builder.scheme(API_SCHEME).encodedAuthority(rootUrl);
    }

    public static String getBaseUrl(){ // http://ip:port
        String s = API_ROOT;
        String[] parts = s.split("/");
        setBaseUrl(parts[0]);
        return builder.build().toString();
    }

    public Uri.Builder getBaseApi(String rootUrl) {
        setBaseUrl(rootUrl);
        return builder;
    }

    private static Uri.Builder getApi(String rootUrl, String path) {
        setBaseUrl(rootUrl);
        if (path.contains("/")) {
            builder.appendEncodedPath(path);
        } else {
            builder.appendPath(path);
        }
        return builder;
    }

    private static Uri.Builder getImageApi(String rootUrl, String path) {
        StringBuilder sb = new StringBuilder(path);
        String subPath = sb.deleteCharAt(0).toString();
        setBaseUrl(rootUrl);
        if (path.contains("/")) {
            builder.appendEncodedPath(subPath);
        } else {
            builder.appendPath(subPath);
        }
        return builder;
    }

    public static String getUrl(String path){
        return getApi(API_ROOT, path).build().toString();
    }

    public static String getImageUrl(String path){
        if(path.contains("https://") || path.contains("http://")){
            return path;
        }else{
            return getImageApi(API_ROOT_IMAGES, path).build().toString();
        }
    }

    public static String getGoogleMapDirectionUrl(LatLng origin, LatLng destination){
        String strOrigin = origin.latitude + "," + origin.longitude;
        String strDestination = destination.latitude + "," + destination.longitude;
        return String.format(API_GOOGLE_MAP_DIRECTION, strOrigin, strDestination, context.getResources().getString(R.string.google_direction_api));
    }

}
