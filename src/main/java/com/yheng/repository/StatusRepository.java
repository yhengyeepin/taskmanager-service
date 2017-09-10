package com.yheng.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.yheng.entity.Status;


@RepositoryRestResource(collectionResourceRel = "statuses", path = "statuses")
public interface StatusRepository extends CrudRepository<Status, Integer> {
	Status findByText(String label);
}
