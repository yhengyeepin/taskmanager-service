package com.yheng.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.yheng.entity.Task;
import com.yheng.entity.User;


@RepositoryRestResource(collectionResourceRel = "tasks", path = "tasks")
public interface TaskRepository extends CrudRepository<Task, Integer> {

	List<Task> findByOwner(@Param("owner") User user);
	
	@Query("select t from Task t where t.owner.userId = :ownerId")
	List<Task> findByOwnerId(@Param("ownerId") Integer ownerId);
}
