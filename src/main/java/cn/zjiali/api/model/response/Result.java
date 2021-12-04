package cn.zjiali.api.model.response;

import lombok.*;

/**
 * @author zJiaLi
 * @since 2021-12-04 10:32
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Result<T> {

    private int status;

    private String message;

    private T data;

    public static Result<Object> success() {
        Result<Object> objectResult = new Result<>();
        objectResult.setStatus(200);
        objectResult.setMessage("操作成功");
        return objectResult;
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setStatus(200);
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }

    public static <T> Result<T> success(String msg, T data) {
        Result<T> result = new Result<>();
        result.setStatus(200);
        result.setMessage(msg);
        result.setData(data);
        return result;
    }

    public static <T> Result<T> fail(Class<T> clazz) {
        Result<T> objectResult = new Result<>();
        objectResult.setStatus(500);
        objectResult.setMessage("操作失败");
        return objectResult;
    }

    public static Result<Object> fail() {
        Result<Object> objectResult = new Result<>();
        objectResult.setStatus(500);
        objectResult.setMessage("操作失败");
        return objectResult;
    }

    public static Result<Object> fail(String errMsg) {
        Result<Object> objectResult = new Result<>();
        objectResult.setStatus(500);
        objectResult.setMessage(errMsg);
        return objectResult;
    }
}
