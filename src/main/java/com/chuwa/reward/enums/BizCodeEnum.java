package com.chuwa.reward.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum BizCodeEnum {

    /**
     * user error code start with 1
     */
    USER_NOT_FOUND(100001, "user not found", HttpStatus.NOT_FOUND),

    /**
     * purchase record error code start with 2
     */
    PURCHASE_RECORD_NOT_FOUND(200001, "purchase record not found", HttpStatus.NOT_FOUND),
    PERIOD_RECORD_NOT_FOUND(200002, "record not found in given period", HttpStatus.BAD_REQUEST);

    @Getter
    private final int code;

    @Getter
    private final String msg;

    @Getter
    private final HttpStatus httpStatus;

    BizCodeEnum(int code, String msg, HttpStatus httpStatus){
        this.code = code;
        this.msg = msg;
        this.httpStatus = httpStatus;
    }
}
