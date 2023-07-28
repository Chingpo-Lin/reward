package com.chuwa.reward.exception;

import com.chuwa.reward.utils.JsonData;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public JsonData handler(Exception e, HttpServletResponse httpServletResponse) {
        if (e instanceof BizException) {
            BizException bizException = (BizException) e;
            log.error("[business exception] happened(" + new Date() + "):", e);
            httpServletResponse.setStatus(bizException.getHttpStatus().value());
            return JsonData.buildCodeAndMsg(bizException.getCode(), bizException.getMsg());
        } else if (e instanceof MethodArgumentNotValidException) {
            Map<String, String> errors = new HashMap<>();
            MethodArgumentNotValidException exception = (MethodArgumentNotValidException) e;
            log.error("[validation exception] happened(" + new Date() + "):", e);
            exception.getBindingResult().getAllErrors().forEach((error) -> {
                String fieldName = ((FieldError) error).getField();
                String message = error.getDefaultMessage();
                errors.put(fieldName, message);
            });
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return JsonData.buildError(errors);
        } else{
            log.error("non-business exception happened(" + new Date() + "):", e);
            return JsonData.buildError("global unknown err");
        }
    }
}