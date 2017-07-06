package studio.crazybt.adventure.libs;

import android.app.Application;
import android.content.Context;

/**
 * Created by Brucelee Thanh on 25/10/2016.
 */

public class CommonConstants {
    public static final String KEY_DATA = "data";
    public static final String VAL_ID_DEFAULT = "default";
    public static final String VAL_USERNAME_DEFAUT = "Unknow";

    // relation
    public static final int VAL_IS_YOU = 1;
    public static final int VAL_FRIEND = 2;
    public static final int VAL_STRANGER = 3;


    public static final String KEY_ID_USER = "ID_USER";
    public static final String KEY_USERNAME = "USERNAME";
    public static final String KEY_TYPE_SHOW = "TYPE_SHOW";

    // Input activity
    public static final int ACT_CREATE_STATUS = 1;
    public static final int ACT_CREATE_TRIP = 2;
    public static final int ACT_CREATE_DIARY_TRIP = 3;
    public static final int ACT_CREATE_DISCUSS_TRIP = 4;
    public static final int ACT_EDIT_PROFILE_INFO = 5;
    public static final int ACT_VIEW_PROFILE_INFO = 6;
    public static final int ACT_CREATE_GROUP = 7;
    public static final int ACT_CREATE_STATUS_GROUP = 8;
    public static final int ACT_CREATE_TRIP_GROUP = 9;

    // Status activity
    public static final int ACT_STATUS_DETAIL = 1;
    public static final int ACT_STATUS_LIKE = 2;
    public static final int ACT_STATUS_COMMENT = 3;

    // Group member
    public static final int VAL_NONE_ACTIVITY_IN_GROUP = 0;
    public static final int VAL_REQUEST_MEMBER_OF_GROUP = 1;
    public static final int VAL_INVITE_MEMBER_OF_GROUP = 2;
    public static final int VAL_MEMBER_OF_GROUP = 3;
    public static final int VAL_BLOCKED_MEMBER_OF_GROUP = 4;
    public static final int VAL_ADMIN_OF_GROUP = 5;
    public static final int VAL_CREATOR_OF_GROUP = 6;

    // Group member permission
    public static final int VAL_PER_GROUP_CREATOR = 1;
    public static final int VAL_PER_GROUP_ADMIN = 2;
    public static final int VAL_PER_GROUP_MEMBER = 3;

    public static final int VAL_TYPE_STATUS_GROUP = 1;
    public static final int VAL_TYPE_TRIP_GROUP = 2;

    public static final int VAL_SECRET_GROUP = 1;
    public static final int VAL_CLOSE_GROUP = 2;
    public static final int VAL_OPEN_GROUP = 3;

    // Search user activity
    public static final int ACT_SEARCH_USER_GROUP_INVITE_MEMBER = 1;
    public static final int ACT_SEARCH_USER_MESSAGE = 2;

    // Message activity
    public static final int ACT_VIEW_CONVERSATION = 1;
    public static final int ACT_VIEW_MESSAGE = 2;

}
