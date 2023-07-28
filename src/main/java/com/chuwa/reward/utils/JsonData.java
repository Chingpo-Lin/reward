package com.chuwa.reward.utils;

import com.chuwa.reward.enums.BizCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JsonData {

    /**
     * default code -1 means fail, 1 mean success */
    private Integer code;

    /**
     * data
     */
    private Object data;

    /**
     * description
     */
    private String msg;

    /**
     * default success
     * @return
     */
    public static JsonData buildSuccess() {
        return new JsonData(1, null, null);
    }

    /**
     * success with code, data, and msg
     * @param data
     * @return
     */
    public static JsonData buildSuccess(Object data) {
        return new JsonData(1, data, null);
    }

    /**
     * fail with msg
     * @param data
     * @return
     */
    public static JsonData buildError(Object data) {
        return new JsonData(-1, data, null);
    }

    /**
     * fail with custom code&msg
     * @param code
     * @param msg
     * @return
     */
    public static JsonData buildCodeAndMsg(int code, String msg) {
        return new JsonData(code, null, msg);
    }

    /**
     * success result return
     * @param codeEnum
     * @return
     */

    /**
     * error return
     * @param codeEnum
     * @return
     */
    public static JsonData buildErrorResult(BizCodeEnum codeEnum){
        return JsonData.buildCodeAndMsg(codeEnum.getCode(), codeEnum.getMsg());
    }
}
