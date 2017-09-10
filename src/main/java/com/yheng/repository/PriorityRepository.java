package com.yheng.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.yheng.entity.Priority;


@RepositoryRestResource(collectionResourceRel = "priorities", path = "priorities")
public interface PriorityRepository extends CrudRepository<Priority, Integer> {
	Priority findByText(String label);
}
