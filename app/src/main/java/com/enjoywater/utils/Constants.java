package com.enjoywater.utils;

public class Constants {
    public static class Url {
        //Google
        public static final String GOOGLE_OAUTH2_TOKEN = "https://www.googleapis.com/oauth2/v4/token";
        // DOMAIN
        public static final String DOMAIN = "http://157.230.38.81:8081/api/app/";
        //UserInfo
        public static final String LOGIN = "user/login";
        public static final String LOGIN_BY_TOKEN = "user/login-by-token";
        public static final String LOGIN_BY_SOCIAL_ACCESS_TOKEN = "user/login-with-social";
        public static final String REGISTER = "user/register";
        public static final String REGISTER_DEVICE = "device/register";
        public static final String GET_USER_INFO = "user/profile";
        public static final String UPDATE_USER_INFO = "user/update-info";
        public static final String GET_ACTIVE_CODE = "user/resend-mail-activate";
        public static final String ACTIVE_BY_CODE = "user/active-by-code";
        public static final String FORGET_PASSWORD = "user/forget-password";
        public static final String RESET_PASSWORD = "user/reset-password";
        public static final String ADD_NEW_ADDRESS = "user/add-address";
        public static final String SUBMIT_REF_CODE = "user/update-referral-code";
        //Home
        public static final String GET_LIST_NEWS = "news/list";
        public static final String GET_NEWS_DETAILS = "news/info/{newsId}";
        //Product
        public static final String GET_LIST_PRODUCTS = "product/list";
        public static final String GET_PRODUCT_DETAILS = "product/info";
        //Order
        public static final String CREATE_ORDER = "order/create-order";
        public static final String CANCEL_ORDER = "order/cancel-order/{orderId}";
        public static final String CONFIRM_RECEIVED = "order/confirm-received/{orderId}";
        public static final String RATE_ORDER = "order/rate/{orderId}";
        public static final String GET_LIST_ORDER = "order/list";
        public static final String GET_ORDER_DETAILS = "order/info/{orderId}";
        //Promotion
        public static final String GET_COUPON_DETAILS = "promotion/coupon-info/{code}";
        //Notify
        public static final String GET_LIST_NOTIF = "notification/list";
        public static final String GET_NOTIF_DETAILS = "notification/info/{notifyId}";
        public static final String UPDATE_STATUS_NOTIF = "notification/update-status/{notifyId}";
    }

    public static class Key {
        public static final String TOKEN_LOGIN = "Authorization";
        public static final String USER = "user";
        public static final String USERNAME = "username";
        public static final String NAME = "name";
        public static final String EMAIL = "email";
        public static final String PHONE = "phone";
        public static final String PASSWORD = "password";
        public static final String AVATAR = "avatar";
        public static final String GENDER = "gender";
        public static final String BIRTHDAY = "birthday";
        public static final String ADDRESS = "address";
        public static final String PASSPORT = "passport";
        public static final String REF_ID = "refId";
        public static final String TOKEN = "token";
        public static final String CODE = "code";
        public static final String LIMIT = "limit";
        public static final String PAGE = "page";
        public static final String SEARCH_KEY = "q";
        public static final String NEWS_ID = "newsId";
        public static final String PRODUCT_ID = "productId";
        public static final String CITY = "province";
        public static final String CITY_ID = "provinceId";
        public static final String DISTRICT = "district";
        public static final String DISTRICT_ID = "districtId";
        public static final String WARD = "ward";
        public static final String WARD_ID = "wardId";
        public static final String IS_DEFAULT = "isDefault";
        public static final String NOTES = "notes";
        public static final String RECEIVER_NAME = "receiverName";
        public static final String ORDER_BY_SCHEDULE = "orderBySchedule";
        public static final String COUPON_CODE = "couponCode";
        public static final String DELIVERY_OPTION = "deliveryOpts";
        public static final String DELIVERY_CLIMB = "deliveryClimb";
        public static final String PAYMENT_METHOD = "paymentMethod";
        public static final String ITEMS = "items";
        public static final String ORDER_ID = "orderId";
        public static final String NOTIFY_ID = "notifyId";
        public static final String STATUS = "status";
        public static final String SOCIAL_TYPE = "socialType";
        public static final String ACCESS_TOKEN = "accessToken";
        public static final String CLIENT_ID = "client_id";
        public static final String CLIENT_SECRET = "client_secret";
        public static final String GRANT_TYPE = "grant_type";
        public static final String REFRESH_TOKEN = "refresh_token";
        public static final String PLATFORM = "platform";
        public static final String DEVICE_ID = "deviceId";
        public static final String ENABLE_NOTIFICATION = "enableNotification";
        public static final String USER_ID = "userId";
        public static final String DEVICE_TOKEN = "deviceToken";
        public static final String DATA = "data";
        public static final String TYPE = "type";
        public static final String NEWS = "news";
        public static final String NEW_PASSWORD = "newPass";
        public static final String NEW_PASSWORD_REPEATED = "newPassRepeated";
        public static final String INSERT_ORDER = "insertOrder";
        public static final String INSERT_NEWS = "insertNews";
        public static final String ORDER_UPDATED = "orderUpdated";
        public static final String BONUS_UPDATED = "bonusUpdated";
        public static final String NEWS_UPDATED = "newsUpdated";
        public static final String PROFILE_UPDATED = "profileUpdated";
        public static final String ORDER = "order";
    }

    public static class Value {
        public static final String SECRET_HEADER = "X-API-TOKEN: abcxyz";
        public static final int ACTION_CLOSE = 0;
        public static final int ACTION_SUCCESS = 1;
        public static final String PENDING = "pending";
        public static final String VERIFIED = "verified";
        public static final String DELIVERING = "delivering";
        public static final String DELIVERED = "delivered";
        public static final String CANCELED = "canceled";
        public static final String CASH = "cash";
        public static final String COIN = "coin";
        public static final String BILL = "bill";
        public static final String READ = "read";
        public static final String UNREAD = "unread";
        public static final String ORDER = "order";
        public static final String BONUS = "bonus";
        public static final String NEWS = "news";
        public static final String FACEBOOK = "facebook";
        public static final String GOOGLE = "google";
        public static final String AUTHORIZATION_CODE = "authorization_code";
        public static final String REFRESH_TOKEN = "refresh_token";
        public static final String ANDROID = "android";
        public static final String SALE = "sale";
        public static final String HOME = "home";
        public static final String ANDROID_LINK = "https://play.google.com/store/apps/details?id=com.enjoywater";
        public static final String IOS_LINK = "https://itunes.apple.com/vn/app/enjoy-water/id959449140";
        public static final String WEBSITE_LINK = "http://www.nuocsach.online";
        public static final String HOTLINE = "0966555779";
        public static final String MALE = "male";
        public static final String FEMALE = "female";
        public static final String UNKNOWN = "unknown";
    }

    public static class DataNotify {
        public static final String DATA_ERROR = "Không tìm thấy dữ liệu.";
        public static final String NO_CONNECTION = "Không có kết nối mạng.";
        public static final String DATA_ERROR_TRY_AGAIN = "Lỗi dữ liệu, xin thử lại.";
        public static final String NOT_LOGIN_YET = "Vui lòng đăng nhập để sử dụng chức năng này.";
    }
}
