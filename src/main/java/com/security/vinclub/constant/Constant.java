package com.security.vinclub.constant;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Constant {

    public static final String PREFIX_ERROR_CODE = "010-";
    public static final String GENERAL_ERROR_CODE = "999999";
    public static final String NOT_FOUND_ERROR_CODE = "000001";
    public static final String DUPLICATED_ERROR_CODE = "000002";

    public static final String VCC_BOOKING_PREFIX = "VCC::BOOKING::";

    public static final Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static final String WARNING_CXL_DEADLINE_SUBJECT = "Cancellation deadline warning for booking : ";
    public static final String GUARANTEED_CXL_DEADLINE_SUBJECT = "DEADLINE APPROACHING (Automatic guaranteed) ";
    public static final String CANCELLED_CXL_DEADLINE_SUBJECT = "CANCELLED (Deadline Exceeded) ";

    public static final String COMMA = ",";
    public static final String NEW_LINE = "\n";
    public static final String EMPTY_SPACE = " ";
    public static final String THB = "THB";
    public static final String USER_REPORT_TYPE = "userReport";
}
