package ObjectClass;

import java.util.HashMap;

public class Constants {
    public static final String KEY_COLLECTION_USER = "users";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_SEX = "sex";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_PREFERENCE_NAME = "chatAppPreference";
    public static final String KEY_IS_SIGN_IN = "isSigneIn";
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_FCM_TOKEN = "fcmToken";
    public static final String KEY_USER = "user";
    public static final String KEY_COLLECTION_CHAT = "chat";
    public static final String KEY_SENDER_ID = "senderId";
    public static final String KEY_RECEIVER_ID = "receiverId";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_TIMESTAMP = "timestamp";


    public static final String KEY_COLLECTION_CONVERSATIONS = "conversations";
    public static final String KEY_SENDER_NAME = "senderName";
    public static final String KEY_RECEIVER_NAME = "receiverName";
    public static final String KEY_SENDER_IMG = "senderImg";
    public static final String KEY_RECEIVER_IMG = "receiverImg";
    public static final String KEY_LAST_MSG = "lastMsg";
    public static final String KEY_AVAILABILITY = "availability";
    public static final String KEY_MSG_AUTHORIZATION = "Authorization";
    public static final String KEY_MSG_CONTENT_TYPE = "Content_Type";
    public static final String KEY_MSG_DATA = "data";
    public static final String KEY_MSG_REGISTRATION_IDS = "registration_ids";

    public static HashMap<String, String> remoteMsgHeaders = null;
    public static HashMap<String, String> getRemoteMsgHeaders(){
        if(remoteMsgHeaders == null){
            remoteMsgHeaders = new HashMap<>();
            remoteMsgHeaders.put(KEY_MSG_AUTHORIZATION,
                    "key=AAAArWGuBMM:APA91bFqVmVLXfqFAPN4IiwLYIJ_49x_bl-Rqyd0fO3BRupFFP0hD08M177A4Y1aNR-NaGFjLfVHLYEuGABQ5FGrtrtceZugcw3g_ixXc1AaJztAnC5TiD72WtXbOxYVdV50W1apihVo");
            remoteMsgHeaders.put(KEY_MSG_CONTENT_TYPE, "application/json");
        }
        return remoteMsgHeaders;
    }
}
