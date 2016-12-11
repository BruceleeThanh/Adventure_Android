package studio.crazybt.adventure.libs;

import android.net.Uri;


/**
 * Created by Brucelee Thanh on 07/10/2016.
 */

public class ApiConstants {

    public static Uri.Builder builder;

    // Request code & response code
    public static final int RESPONSE_CODE_SUCCESS = 200;

    // API Urls
    public static final String API_ROOT = "155.94.144.150:25763/api";
    public static final String API_SCHEME = "http";
    public static final String API_NORMAL_SIGNUP = "user/sign_up";
    public static final String API_NORMAL_LOGIN = "user/login";
    public static final String API_UPLOAD_IMAGE = "file/upload_image";
    public static final String API_CREATE_STATUS = "status/create";
    public static final String API_BROWSE_LIKE = "like_status/browse";
    public static final String API_LIKE_STATUS = "like_status/like";
    public static final String API_BROWSE_COMMENT = "comment_status/browse";
    public static final String API_COMMENT_STATUS = "comment_status/create";
    public static final String API_COMMENT_EDIT_CONTENT = "comment_status/edit_content";
    public static final String API_DELETE_COMMENT = "comment_status/delete";
    public static final String API_TIME_LINE = "news/time_line";
    public static final String API_NEWS_FEED = "news/news_feed";
    public static final String API_SUGGEST_FRIEND = "friend/suggest_friend";
    public static final String API_BROWSE_FRIEND = "friend/browse";
    public static final String API_SEND_REQUEST_FRIEND = "friend_request/send_request";
    public static final String API_BROWSE_REQUEST_FRIEND = "friend_request/browse";
    public static final String API_CONFIRM_REQUEST_FRIEND = "friend_request/confirm";

    // Default params
    public static final String DEF_CODE = "code";
    public static final String DEF_MSG = "message";
    public static final String DEF_DATA = "data";

    // Key params

    // user params
    public static final String KEY_ID = "_id";
    public static final String KEY_ID_STATUS = "id_status";
    public static final String KEY_ID_COMMENT = "id_comment";
    public static final String KEY_PHONE_NUMBER_EMAIL = "phone_number_email";
    public static final String KEY_PHONE_NUMBER = "phone_number";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_FIRST_NAME = "first_name";
    public static final String KEY_LAST_NAME = "last_name";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_RELIGION = "religion";
    public static final String KEY_INTRO = "intro";
    public static final String KEY_ID_FACEBOOK = "id_facebook";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_FILE = "file";
    public static final String KEY_LINK = "link";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_IMAGE_DESCRIPTION = "image_description";
    public static final String KEY_PERMISSION = "permission";
    public static final String KEY_TYPE = "type";
    public static final String KEY_LATEST_ACTIVE = "latest_active";
    public static final String KEY_AMOUNT_LIKE = "amount_like";
    public static final String KEY_AMOUNT_COMMENT = "amount_comment";
    public static final String KEY_IS_LIKE = "is_like";
    public static final String KEY_IS_COMMENT = "is_comment";
    public static final String KEY_IS_FRIEND = "is_friend";
    public static final String KEY_URL = "url";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_CREATED_AT = "created_at";
    public static final String KEY_OWNER = "owner";
    public static final String KEY_IMAGES = "images";
    public static final String KEY_AVATAR = "avatar";
    public static final String KEY_COVER = "cover";
    public static final String KEY_PAGE = "page";
    public static final String KEY_PERPAGE = "perPage";
    public static final String KEY_USER = "user";
    public static final String KEY_SENDER = "sender";
    public static final String KEY_RECIPIENT = "recipient";
    public static final String KEY_STATUS = "status";


    public ApiConstants() {
    }

    public static void setBaseUrl() {
        builder = new Uri.Builder();
        builder.scheme(API_SCHEME).encodedAuthority(API_ROOT);
    }

    public Uri.Builder getBaseApi() {
        this.setBaseUrl();
        return builder;
    }

    public static Uri.Builder getApi(String path) {
        setBaseUrl();
        if (path.contains("/")) {
            builder.appendEncodedPath(path);
        } else {
            builder.appendPath(path);
        }
        return builder;
    }

}
