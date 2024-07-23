package ru.kurochkin.hibernatephonebook.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class BaseResponse {
    private boolean success;
    private String message;

    public static BaseResponse getSuccessResponse() {
        return new BaseResponse(true, null);
    }

    public static BaseResponse getErrorResponse(String message) {
        return new BaseResponse(false, message);
    }
}
