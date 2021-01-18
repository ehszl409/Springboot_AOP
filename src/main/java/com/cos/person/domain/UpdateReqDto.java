package com.cos.person.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UpdateReqDto {
	@NotBlank(message = "메세지를 입력해주세요")
	@NotNull(message = "키 값을 입력해주세요")
	private String password;
	private String phone;
}
