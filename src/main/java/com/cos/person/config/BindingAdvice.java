package com.cos.person.config;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.cos.person.domain.CommonDto;

// AOP는 @Controller가 메모리에 뜬 이후 처리를 한다.
// 그래서 @Configuration말고 @Component를 해주는 것이 바람직하다.
@Component
@Aspect
public class BindingAdvice {

	// @Around 함수의 앞 뒤 제어
	// 여기서 Around의 개념을 제대로 잡아야지 왜 @Before와 @After에는 ProceedingJoinPoint가 필요 없는지 정확하게 이해할 수 있다.
	// @Around 함수의 쉽게 설명하면 함수의 앞뒤를 제어하는 것이지만, 사실 함수가 실행되고 함수 속 스택이 실행될때 
	// 가장 먼저 실행되는 위치에 있다. 함수 @Around로 맵핑되어 있는 함수는 JoinPoint의 함수 스택안에 
	// 보이진 않지만 제일 위에 적혀있는 상태라고 말할 수 있다.
	// 반면에 @Before와 @After는 정말로 함수가 실행되기 전에 위치하고 있어서
	// 함수가 받아오는 매개변수 값을 알아낼수 없는게 당연하다. (함수 매개변수는 함수 스택이 끝나면 소멸되기 때문이다.)
	// 그래서 @Before와 @After는 함수가 실행되었다 또는 끝이 났다는 로그 값만 남길때 사용하거나
	// 함수가 실행된 시간을 나타낼때 사용되곤 한다. vali체크를 위해서는 @Around를 사용하는 것이 정답이다.
	// @Before 함수의 앞만 제어 
	// @After 함수의 뒤만 제어(응답만 관리)

	// Vail체크를 위해서는 함수에 들어오는 값들을 가지고 와야한다.
	// 그래서 ProceedingJoinPoint(내가 보는 관점 대상) 타입을 사용하면 받아오는 값을 낚아챌 수 있다.
	// com.cos.person.web안의 모든 controller로 끝나는 모든 매개인수 상관없이 모든 함수들
	@Around("execution(* com.cos.person.web..*Controller.*(..))")
	public Object vaildCheck(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		// com.cos.person.web.UserController
		String type = proceedingJoinPoint.getSignature().getDeclaringTypeName();
		// 컨트롤러에서 호출된 함수의 이름 (save, update, delete...)
		String method = proceedingJoinPoint.getSignature().getName();

		// 후처리를 해줘야 다음 로직이 실행된다.
		// 함수의 매개변수를 받아서 배열에 넣어준다.
		Object[] args = proceedingJoinPoint.getArgs();
		for (Object arg : args) {
			// arg가 BindingResult 값이 라면
			if (arg instanceof BindingResult) {
				BindingResult bindingResult = (BindingResult) arg;

				if (bindingResult.hasErrors()) {
					Map<String, String> errorMap = new HashMap<>();

					// FieldError는 리스트 타입이다. 그래서 바인딩 결과 값을 getFieldErrors()로 해준것이다.
					for (FieldError error : bindingResult.getFieldErrors()) {
						// error.getField() 에는 키 값이 담기고
						// error.getDefaultMessage() 에는 Message가 담긴다.
						errorMap.put(error.getField(), error.getDefaultMessage());
					}

					// 언제는 String을 리턴 해주고 언제는 리스트를 리턴해줘야 하니까
					// 제네릭 타입을 <?>라고 해주면 된다.
					return new CommonDto<>(HttpStatus.BAD_REQUEST.value(), errorMap);
				}

			}
		} // end of foreach
		// vali체크를 했는데 오류가 없다면 joinpoint을 그대로 실행 하면된다 라고 알려주는 메세지.
		return proceedingJoinPoint.proceed();
	}

}
