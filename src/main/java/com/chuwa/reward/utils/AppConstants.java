package com.chuwa.reward.utils;

public class AppConstants {
    /**
     * user pagination constant
     */
    public static final String USER_DEFAULT_PAGE_NUMBER = "0";
    public static final String USER_DEFAULT_PAGE_SIZE = "10";
    public static final String USER_DEFAULT_SORT_BY = "id";
    public static final String USER_DEFAULT_SORT_DIR = "asc";

    /**
     * user DTO error
     */
    public static final String USER_NAME_EMPTY = "username cannot be empty or null";
    public static final int MIN_USER_NAME_LEN = 3;
    public static final String USER_NAME_SHORT = "username should have at least " + MIN_USER_NAME_LEN + " characters";
//    public static final String USER_POINT_NULL = "you can set initial point to 0 instead of empty or null";

    /**
     * record request constant
     */
    public static final String AMOUNT_NULL = "amount cannot be null in a purchase";

    /**
     * time key
     */
    public static String TIME_KEY = "%s month ago total reward";
}
