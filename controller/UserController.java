package com.devdoc.backend.controller;


import com.devdoc.backend.dto.ResponseDTO;
import com.devdoc.backend.dto.UserDTO;
import com.devdoc.backend.model.UserEntity;
import com.devdoc.backend.security.TokenProvider;
import com.devdoc.backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// UserController: 사용자 인증 및 회원가입 처리

@Slf4j
@RestController
@RequestMapping("/auth")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private TokenProvider tokenProvider; // 토큰 제공자

	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); // 비밀번호 인코더

	// 회원가입 처리
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
		try {
			// 사용자 엔티티 빌드
			UserEntity user = UserEntity.builder()
					.email(userDTO.getEmail()) // 이메일 설정
					.username(userDTO.getUsername()) // 사용자 이름 설정
					.password(passwordEncoder.encode(userDTO.getPassword())) // 비밀번호 인코딩 후 설정
					.build();

			// 사용자 생성
			UserEntity registeredUser = userService.create(user);

			// 응답 DTO 빌드
			UserDTO responseUserDTO = UserDTO.builder()
					.email(registeredUser.getEmail()) // 응답 DTO에 이메일 설정
					.id(registeredUser.getId()) // 응답 DTO에 ID 설정
					.username(registeredUser.getUsername()) // 응답 DTO에 사용자 이름 설정
					.createdAt(registeredUser.getCreatedAt()) // CreatedAt 추가
					.build();

			// 성공 응답 반환
			return ResponseEntity.ok(responseUserDTO);
		} catch (Exception e) {
			// 오류 응답 생성
			ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();

			// 400 Bad Request 응답 반환
			return ResponseEntity
					.badRequest() // 400 Bad Request 응답
					.body(responseDTO); // 오류 메시지 포함하여 응답
		}
	}

	// 로그인 처리
	@PostMapping("/signin")
	public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO) {
		// 사용자 인증
		UserEntity user = userService.getByCredentials(
				userDTO.getEmail(), // 이메일로 사용자 조회
				userDTO.getPassword(), // 비밀번호 확인
				passwordEncoder); // 비밀번호 인코더 사용

		if(user != null) {
			// JWT 토큰 생성
			final String token = tokenProvider.create(user);

			// 응답 DTO 빌드
			final UserDTO responseUserDTO = UserDTO.builder()
					.email(user.getUsername()) // 응답 DTO에 이메일 설정
					.id(user.getId()) // 응답 DTO에 ID 설정
					.token(token) // 응답 DTO에 토큰 설정
					.createdAt(user.getCreatedAt()) // CreatedAt 추가
					.build();

			// 성공 응답 반환
			return ResponseEntity.ok().body(responseUserDTO);
		} else {
			// 로그인 실패 오류 메시지 설정
			ResponseDTO responseDTO = ResponseDTO.builder()
					.error("Login failed.") // 오류 메시지 설정
					.build();

			// 400 Bad Request 응답 반환
			return ResponseEntity
					.badRequest() // 400 Bad Request 응답
					.body(responseDTO); // 오류 메시지 포함하여 응답
		}
	}
}
