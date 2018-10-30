package com.enjoywater.utils;

public class Constants {
    public static class Url {
        public static final String DOMAIN = "http://35.240.146.185:8080/api/app/";
        //User
        public static final String LOGIN = "user/login";
        public static final String REGISTER = "user/register";
        public static final String GET_USER_INFO = "user/profile";
        public static final String UPDATE_USER_INFO = "user/update-info";
        public static final String ACTIVE_MAIL = "user/mail-activate";
        public static final String ACTIVE_BY_CODE = "user/active-by-code";
        public static final String FORGET_PASSWORD = "user/forget-password";
        //Home
        public static final String GET_LIST_NEWS = "news/list";
        public static final String GET_NEWS_DETAILS = "news/info";
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
    }

    public static class Value {
        public static final String SECRET_HEADER = "X-API-TOKEN: abcxyz";
    }
}
