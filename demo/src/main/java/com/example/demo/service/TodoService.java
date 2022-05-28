package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.model.TodoEntity;
import com.example.demo.persistance.TodoRepository;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class TodoService {
	@Autowired
	private TodoRepository repository;
	
	public String testService() {
		TodoEntity entity = TodoEntity.builder().title("My first todo item").build();
		repository.save(entity);
		TodoEntity savedEntity = repository.findById(entity.getId()).get();
		return savedEntity.getTitle();
	}
	
	public List<TodoEntity> create(final TodoEntity entity){
		validate(entity);
		
		repository.save(entity);
		
		log.info("Entity Id : {} is saved.",entity.getId());
		
		return repository.findByUserId(entity.getUserId());
	}
	//Validations
	private void validate(final TodoEntity entity) {
			if(entity == null) {
				log.warn("Entity cannot be null.");
				throw new RuntimeException("Entity cannot be null.");
			}
			if(entity.getUserId() == null) {
				log.warn("Unknown user.");
				throw new RuntimeException("Unknown user.");
			}
	}
	
	public List<TodoEntity> retrieve(final String userId){
		return repository.findByUserId(userId);
	}
	
	public List<TodoEntity> update(final TodoEntity entity){
		// 저장할 엔티티가 유효한지 확인(Create Todo에서 구현)
		validate(entity);
		
		//넘겨받은 엔티티 Id를 이용해 TodoEntity를 가져옴. 존재하지 않는 엔티티는 업데이트 할 수없기 때문
		final Optional<TodoEntity> original = repository.findById(entity.getId());
		
		original.ifPresent(todo -> {
			//반환된 TodoEntity가 존재하면 새 entity 값으로 덮어 씌움
			todo.setTitle(entity.getTitle());
			todo.setDone(entity.isDone());
			
			repository.save(todo);
		});
		
		return retrieve(entity.getUserId());
	}
	
	public List<TodoEntity> delete(final TodoEntity entity){
		// 저장할 엔티티가 유효한지 확인(Create Todo에서 구현)
		validate(entity);
		
		try {
			// 엔티티 삭제
			repository.delete(entity);
		}catch(Exception e) {
			// exception 발생 시 id와 exception을 로깅
			log.error("error deleting entity", entity.getId(),e);
		
		// 컨트롤러로 exception을 보냄. 데이터베이스 내부 로직을 캡슐화하려면 e를 리턴하지 않고 새 exception 오브젝트 리턴
		throw new RuntimeException("error deleting entity" + entity.getId());
		}
		
		// 새 Todo 리스트르 가져와 리턴
		return retrieve(entity.getUserId());
	}
	
}
