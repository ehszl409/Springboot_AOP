package com.cos.person.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.person.domain.CommonDto;
import com.cos.person.domain.JoinReqDto;
import com.cos.person.domain.UpdateReqDto;
import com.cos.person.domain.User;
import com.cos.person.domain.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class UserController {
	
	// 의존성 주입
	final UserRepository userRepository;
	
//	public UserController(UserRepository userRepository) {
//		this.userRepository = userRepository;
//	}

	@GetMapping("/user")
	public CommonDto<List<User>> findAll() {
		System.out.println("findAll()");
		return new CommonDto<>(HttpStatus.OK.value(), userRepository.findAll());
	}
	
	@GetMapping("/user/{id}")
	public CommonDto<User> findById(@PathVariable int id) {
		System.out.println("findById()");
		System.out.println("id: " + id);
		return new CommonDto<>(HttpStatus.OK.value(), userRepository.findById(id));
	}
	
	@PostMapping("/user")
	public CommonDto<?> save(@RequestBody @Valid JoinReqDto dto, BindingResult bindingResult) {
		System.out.println("save()");
		System.out.println(dto);
		
		return new CommonDto<>(HttpStatus.ACCEPTED.value(), "OK");
	}
	
	@DeleteMapping("/user/{id}")
	public CommonDto<String> delete(@PathVariable int id) {
		System.out.println("delete()");
		userRepository.delete(id);
		return new CommonDto<>(HttpStatus.OK.value(), "OK");
		
	}
	
	@PutMapping("/user/{id}")
	public CommonDto<?> update(@PathVariable int id, @Valid @RequestBody UpdateReqDto dto, BindingResult bindingResult) {
		
		
		
		System.out.println("update()");
		System.out.println(dto);
	    userRepository.update(id, dto);
		return new CommonDto<>(HttpStatus.OK.value(), "OK");
	}
	
}
