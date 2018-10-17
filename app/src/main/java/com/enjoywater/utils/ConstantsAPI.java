package com.enjoywater.utils;

public class ConstantsAPI {
    public static final String DOMAIN = "http://35.240.146.185:8080/api/app/";

    public static class Url {
        public static final String LOGIN = "user/login";
        public static final String REGISTER = "user/register";
        public static final String GET_USER_INFO = "user/profile";
        public static final String UPDATE_USER_INFO = "user/update-info";
        public static final String ACTIVE_MAIL = "user/mail-activate";
        public static final String FORGET_PASSWORD = "user/forget-password";
    }

    public static class Param {
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
    }
}
