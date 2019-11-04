package http;

import java.util.HashMap;

/**
 * Created by Administrator on 2018/3/9.
 */
public class Result {
    public Result() {
    }

    public static ApiResponse success() {
        return new ApiResponse(201, null);
    }

    public static <T> ApiResponse<T> success(T t, HashMap headers) {
        return new ApiResponse<T>(201, headers ,t);
    }

    public static <T> ApiResponse<T> success(int code, T t, HashMap headers) {
        return new ApiResponse<T>(code, headers ,t);
    }



    public static ApiResponse error() {
        return new ApiResponse(400, null);
    }


    public static ApiResponse error(int code, HashMap headers) {
        return new ApiResponse(code, headers);
    }

    public static <T> ApiResponse<T> error(int code, T t, HashMap headers) {
        return new ApiResponse(code, headers, t);
    }
}
