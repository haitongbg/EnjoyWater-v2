package com.enjoywater.utils;

public class Constants {
    public static class Url {
        public static final String DOMAIN = "http://157.230.38.81:8081/api/app/";
        //UserInfo
        public static final String LOGIN = "user/login";
        public static final String LOGIN_BY_TOKEN = "user/login-by-token";
        public static final String LOGIN_BY_SOCIAL_ACCESS_TOKEN = "user/login-with-social";
        public static final String REGISTER = "user/register";
        public static final String GET_USER_INFO = "user/profile";
        public static final String UPDATE_USER_INFO = "user/update-info";
        public static final String GET_ACTIVE_CODE = "user/resend-mail-activate";
        public static final String ACTIVE_BY_CODE = "user/active-by-code";
        public static final String FORGET_PASSWORD = "user/forget-password";
        public static final String ADD_NEW_ADDRESS = "user/add-address";
        //Home
        public static final String GET_LIST_NEWS = "news/list";
        public static final String GET_NEWS_DETAILS = "news/info";
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
        public static final String FACEBOOK = "facebook";
        public static final String GOOGLE = "google";
    }

    public static class DataNotify {
        public static final String DATA_ERROR = "Không tìm thấy dữ liệu.";
        public static final String NO_CONNECTION = "Không có kết nối mạng.";
        public static final String DATA_ERROR_TRY_AGAIN = "Lỗi dữ liệu, xin thử lại.";
    }
}
