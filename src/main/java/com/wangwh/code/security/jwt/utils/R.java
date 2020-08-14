package com.wangwh.code.security.jwt.utils;

import lombok.Data;

import java.io.Serializable;

@Data
public class R<T> implements Serializable {
    private Boolean success;
    private Integer errorCode;
    private String errorMsg;
    private T data;

    public R() {
    }

    public R(boolean success) {
        this.success = success;
        this.errorCode = success ? RCode.SUCCESS.getCode() : RCode.FAILTER.getCode();
        this.errorMsg = success ? RCode.SUCCESS.getMessage() : RCode.FAILTER.getMessage();
    }

    public R(boolean success, RCode resultEnum) {
        this.success = success;
        this.errorCode = success ? RCode.SUCCESS.getCode() : (resultEnum == null ? RCode.FAILTER.getCode() : resultEnum.getCode());
        this.errorMsg = success ? RCode.SUCCESS.getMessage() : (resultEnum == null ? RCode.FAILTER.getMessage() : resultEnum.getMessage());
    }

    public R(boolean success, T data) {
        this.success = success;
        this.errorCode = success ? RCode.SUCCESS.getCode() : RCode.FAILTER.getCode();
        this.errorMsg = success ? RCode.SUCCESS.getMessage() : RCode.FAILTER.getMessage();
        this.data = data;
    }

    public R(boolean success, RCode resultEnum, T data) {
        this.success = success;
        this.errorCode = success ? RCode.SUCCESS.getCode() : (resultEnum == null ? RCode.FAILTER.getCode() : resultEnum.getCode());
        this.errorMsg = success ? RCode.SUCCESS.getMessage() : (resultEnum == null ? RCode.FAILTER.getMessage() : resultEnum.getMessage());
        this.data = data;
    }

	public static R success() {
		return new R(true);
	}

	public static <T> R<T> success(T data) {
		return new R(true, data);
	}

	public static R fail() {
		return new R(false);
	}

	public static R fail(RCode resultEnum) {
		return new R(false, resultEnum);
	}
}