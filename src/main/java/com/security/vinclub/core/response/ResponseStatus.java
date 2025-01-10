package com.security.vinclub.core.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseStatus {

    SUCCESSFUL("000", "Successful"),
    UNAUTHORIZED("001", "Unauthorized"),
    EMAIL_EXIST("002", "Email exists"),
    INVALID_REQUEST_PARAMETER("003", "Invalid request parameter: "),
    USER_NOT_FOUND("004", "User not found"),
    PHONE_NUMBER_EXIST("005", "Phone number exists"),
    CATEGORY_NOT_FOUND("006", "Category not found"),
    CATEGORY_NAME_EXIST("007", "Category name exists"),
    ROLE_NOT_FOUND("008", "Role not found"),
    FILE_NOT_FOUND("009", "File not found"),
    DEPOSIT_NOT_FOUND("010", "Deposit not found"),
    WITHDRAW_NOT_FOUND("011", "Withdraw not found"),
    TOKEN_EXPIRED("012", "Token is expired"),
    ACCOUNT_DEACTIVATED("013", "Account is deactivated"),
    REFERENCE_CODE_NOT_FOUND("014", "Reference code not found"),
    INVALID_CREDENTIALS("031", "Invalid Credentials"),
    INTERNAL_SERVER_ERROR("999", "Internal server error");

    private final String code;
    private final String message;

    public static final String SUCCESS = "SUCCESS";
    public static final String FAILURE = "FAILURE";

    public static ResponseStatus findResponseStatus(String code) {
        for (ResponseStatus v : values()) {
            if (v.getCode().equals(code)) {
                return v;
            }
        }

        return null;
    }
}
