package com.chuwa.reward.exception;

import com.chuwa.reward.enums.BizCodeEnum;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class BizException extends RuntimeException {

    private int code;
    private String msg;
    private HttpStatus httpStatus;

//    public BizException(int code, String msg, HttpStatus httpStatus) {
//        super(msg);
//        this.code = code;
//        this.msg = msg;
//        this.httpStatus = httpStatus;
//    }

    public BizException(BizCodeEnum bizCodeEnum) {
        super(bizCodeEnum.getMsg());
        this.code = bizCodeEnum.getCode();
        this.msg = bizCodeEnum.getMsg();
        this.httpStatus = bizCodeEnum.getHttpStatus();
    }
}