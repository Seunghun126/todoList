package com.example.demo.controller;


import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.TodoDTO;
import com.example.demo.model.TodoEntity;
import com.example.demo.service.TodoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@RestController
@RequestMapping("todo")

public class TodoController {
	
	@Autowired
	private TodoService service;
	
	
	public ResponseEntity<?> testtodo(){
		String str = service.testService();
		List <String> list = new ArrayList<>();
		list.add(str);
		ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
		return ResponseEntity.ok().body(response);
	}
	@PostMapping
	public ResponseEntity<?> createTodo(@RequestBody TodoDTO dto){
		try {
			// temporary user Id.
			String temporaryUserId = "temporary-user";
			
			//TodoEntity로 변환
			TodoEntity entity = TodoDTO.toEntity(dto);
			
			//id를 null로 초기화. 생성 당시에는 id가 없어야 하기 때문
			entity.setId(null);
			
			//임시 사용자 아이디 설정.
			entity.setUserId(temporaryUserId);
			
			//서비스를 이용해 Todo 엔티티 생성
			List<TodoEntity> entities = service.create(entity);
			
			//자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환
			List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
			
			//변환된 TodoDTO 리스트를 이용해 ResponseDTO를 초기화
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
			
			//ResponseDTO 리턴
			return ResponseEntity.ok().body(response);
			
		}catch(Exception e) {
			//예외가 있는 경우 dto 대신 error에 메세지를 넣어 리턴
			String error = e.getMessage();
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
		}
	}
	@GetMapping
	public ResponseEntity<?> retrieveTodoList(){
		String temporaryUserId = "temporary-user";
		
		// 서비스 메서드의 retrieve() 메서드를 사용해 Todo 리스트를 가져옴
		List<TodoEntity> entites = service.retrieve(temporaryUserId);
		
		//자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환
		List<TodoDTO> dtos = entites.stream().map(TodoDTO::new).collect(Collectors.toList());
		
		//변환된 TodoDTO 리스트를 이용해 ResponseDTO를 초기화한다.
		ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
		
		//ResponseDTO 리턴
		return ResponseEntity.ok().body(response);
	}
	
	@PutMapping
	public ResponseEntity<?> updateTodo(@RequestBody TodoDTO dto){
		String temporaryUserId = "temporary-user";
		
		// dto를 entity로 변환
		TodoEntity entity = TodoDTO.toEntity(dto);
		
		// id를 temporaryUserId로 초기화. (수정 예정)
		entity.setUserId(temporaryUserId);
		
		// 서비스를 이용해 entity를 업데이트
		List<TodoEntity> entites = service.update(entity);
		
		// 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환
		List<TodoDTO> dtos = entites.stream().map(TodoDTO::new).collect(Collectors.toList());
		
		// 변환된 TodoDTO 리스트를 이용해 ResponseDTO를 초기화
		ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
		
		// ResponseDTO 리턴
		return ResponseEntity.ok().body(response);
		
	}
	
	@DeleteMapping
	public ResponseEntity<?> deleteTodo(@RequestBody TodoDTO dto){
		try {
			// temporary user id.
			String temporaryUserId = "temporary-user";
			
			// TodoEntity로 변환
			TodoEntity entity = TodoDTO.toEntity(dto);
			
			// 임시사용자 아이디 설정(수정 예정)
			entity.setUserId(temporaryUserId);
			
			// 서비스를 이용해 entity 삭제
			List<TodoEntity> entities = service.delete(entity);
			
			// 자바 스트림을 이용해 리턴도니 엔티티 리스트를 TodoDTO 리스트로 변환
			List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
			
			// 변환된 TodoDTO 리스트를 이용해 ResponseDTO를 초기화
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
			
			// ResponseDTO 리턴
			return ResponseEntity.ok().body(response);
	
		} catch(Exception e) {
			// 혹시 예외가 있는 경우 dto 대신 error 메시지를 넣어 리턴
			String error = e.getMessage();
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	
}
