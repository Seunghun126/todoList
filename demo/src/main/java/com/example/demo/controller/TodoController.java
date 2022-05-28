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
			
			//TodoEntity�� ��ȯ
			TodoEntity entity = TodoDTO.toEntity(dto);
			
			//id�� null�� �ʱ�ȭ. ���� ��ÿ��� id�� ����� �ϱ� ����
			entity.setId(null);
			
			//�ӽ� ����� ���̵� ����.
			entity.setUserId(temporaryUserId);
			
			//���񽺸� �̿��� Todo ��ƼƼ ����
			List<TodoEntity> entities = service.create(entity);
			
			//�ڹ� ��Ʈ���� �̿��� ���ϵ� ��ƼƼ ����Ʈ�� TodoDTO ����Ʈ�� ��ȯ
			List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
			
			//��ȯ�� TodoDTO ����Ʈ�� �̿��� ResponseDTO�� �ʱ�ȭ
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
			
			//ResponseDTO ����
			return ResponseEntity.ok().body(response);
			
		}catch(Exception e) {
			//���ܰ� �ִ� ��� dto ��� error�� �޼����� �־� ����
			String error = e.getMessage();
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
		}
	}
	@GetMapping
	public ResponseEntity<?> retrieveTodoList(){
		String temporaryUserId = "temporary-user";
		
		// ���� �޼����� retrieve() �޼��带 ����� Todo ����Ʈ�� ������
		List<TodoEntity> entites = service.retrieve(temporaryUserId);
		
		//�ڹ� ��Ʈ���� �̿��� ���ϵ� ��ƼƼ ����Ʈ�� TodoDTO ����Ʈ�� ��ȯ
		List<TodoDTO> dtos = entites.stream().map(TodoDTO::new).collect(Collectors.toList());
		
		//��ȯ�� TodoDTO ����Ʈ�� �̿��� ResponseDTO�� �ʱ�ȭ�Ѵ�.
		ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
		
		//ResponseDTO ����
		return ResponseEntity.ok().body(response);
	}
	
	@PutMapping
	public ResponseEntity<?> updateTodo(@RequestBody TodoDTO dto){
		String temporaryUserId = "temporary-user";
		
		// dto�� entity�� ��ȯ
		TodoEntity entity = TodoDTO.toEntity(dto);
		
		// id�� temporaryUserId�� �ʱ�ȭ. (���� ����)
		entity.setUserId(temporaryUserId);
		
		// ���񽺸� �̿��� entity�� ������Ʈ
		List<TodoEntity> entites = service.update(entity);
		
		// �ڹ� ��Ʈ���� �̿��� ���ϵ� ��ƼƼ ����Ʈ�� TodoDTO ����Ʈ�� ��ȯ
		List<TodoDTO> dtos = entites.stream().map(TodoDTO::new).collect(Collectors.toList());
		
		// ��ȯ�� TodoDTO ����Ʈ�� �̿��� ResponseDTO�� �ʱ�ȭ
		ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
		
		// ResponseDTO ����
		return ResponseEntity.ok().body(response);
		
	}
	
	@DeleteMapping
	public ResponseEntity<?> deleteTodo(@RequestBody TodoDTO dto){
		try {
			// temporary user id.
			String temporaryUserId = "temporary-user";
			
			// TodoEntity�� ��ȯ
			TodoEntity entity = TodoDTO.toEntity(dto);
			
			// �ӽû���� ���̵� ����(���� ����)
			entity.setUserId(temporaryUserId);
			
			// ���񽺸� �̿��� entity ����
			List<TodoEntity> entities = service.delete(entity);
			
			// �ڹ� ��Ʈ���� �̿��� ���ϵ��� ��ƼƼ ����Ʈ�� TodoDTO ����Ʈ�� ��ȯ
			List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
			
			// ��ȯ�� TodoDTO ����Ʈ�� �̿��� ResponseDTO�� �ʱ�ȭ
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
			
			// ResponseDTO ����
			return ResponseEntity.ok().body(response);
	
		} catch(Exception e) {
			// Ȥ�� ���ܰ� �ִ� ��� dto ��� error �޽����� �־� ����
			String error = e.getMessage();
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	
}
