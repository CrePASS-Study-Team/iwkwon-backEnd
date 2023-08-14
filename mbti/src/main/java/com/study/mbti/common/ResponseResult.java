package com.study.mbti.common;

import lombok.Data;

@Data
public class ResponseResult {

	private int code;
	private String message;
	private Object data;
}
