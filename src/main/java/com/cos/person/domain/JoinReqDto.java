package com.cos.person.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class JoinReqDto {
	@NotNull(message = "키 값을 올바르게 입력해 주세요")
	@NotBlank(message = "값을 입력해 주세요")
	@Size(min = 1, max = 10, message = "최소 1자에서 10자 사이로 입력 해주세요" )
	private String username;
	
	@NotBlank(message = "값을 입력해 주세요")
	private String password;
	private String phone;
}
