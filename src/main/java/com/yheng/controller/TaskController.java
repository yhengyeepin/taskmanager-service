package com.yheng.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryLinksResource;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.yheng.dto.TaskDTO;
import com.yheng.entity.Priority;
import com.yheng.entity.Status;
import com.yheng.entity.Task;
import com.yheng.entity.User;
import com.yheng.repository.PriorityRepository;
import com.yheng.repository.StatusRepository;
import com.yheng.repository.TaskRepository;
import com.yheng.repository.UserRepository;

@RestController
@ExposesResourceFor(TaskDTO.class)
@RequestMapping(value = "/")
public class TaskController implements
ResourceProcessor<RepositoryLinksResource> {

	@Autowired TaskRepository taskRepository;
	
	@Autowired UserRepository userRepository;
	
	@Autowired PriorityRepository priorityRepository;
	
	@Autowired StatusRepository statusRepository;
	
	@Autowired EntityLinks entityLinks;

	@RequestMapping(value = "/users/{userId}/tasks", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<?> getTasks( @PathVariable("userId") Integer userId, UriComponentsBuilder builder) {

		Integer ownerId = userId;
		if (userId == null) {
			return ResponseEntity.badRequest().build();
		}

		User user = userRepository.findOne(ownerId);
		
		if (user == null) {
			return ResponseEntity.notFound().build();
		}
		
		List<Task> tasks = taskRepository.findByOwnerId(userId);
		for (Task task : tasks) {
			task.add(entityLinks.linkForSingleResource(task).slash(task.getTaskId()).withSelfRel());
		}
		
		HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/users/{userId}/tasks").buildAndExpand(userId).toUri());
        return new ResponseEntity<>(tasks, headers, HttpStatus.ACCEPTED);
	}
	
	@RequestMapping(value = "/users/{userId}/tasks", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Task> addTask( @PathVariable("userId") Integer userId, @RequestBody TaskDTO taskDto, UriComponentsBuilder builder) {

		if (taskDto == null) {
			return ResponseEntity.notFound().build();
		}
		Integer ownerId = userId;
		if (userId == null) {
			return ResponseEntity.badRequest().build();
		}
		Task taskToBe = new Task();
		User user = userRepository.findOne(ownerId);
		taskToBe.setOwner(user);
		
		taskToBe.setModifiedBy(user);
		taskToBe.setDescription(taskDto.getDescription());
		taskToBe.setDueDate(taskDto.getDueDate());
		taskToBe.setModifiedOn(new Date());
		taskToBe.setContent(taskDto.getContent());
		Priority priority = priorityRepository.findByText(taskDto.getPriority());
		taskToBe.setPriority(priority);
		Status status = statusRepository.findByText(taskDto.getStatus());
		taskToBe.setStatus(status);
		taskRepository.save(taskToBe);
		//taskToBe.add(ControllerLinkBuilder.linkTo(TaskRepository.class).slash(taskToBe.getTaskId()).withSelfRel());
		taskToBe.add(entityLinks.linkForSingleResource(taskToBe).slash(taskToBe.getTaskId()).withSelfRel());
		HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/tasks/{id}").buildAndExpand(taskToBe.getTaskId()).toUri());
        return new ResponseEntity<Task>(taskToBe, headers, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/users/{userId}/tasks/{taskId}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Task> updateTask( @PathVariable("userId") Integer userId, @PathVariable("taskId") Integer taskId, @RequestBody TaskDTO taskDto, UriComponentsBuilder builder) {

		if (taskDto == null) {
			return ResponseEntity.notFound().build();
		}
		Integer ownerId = userId;
		if (userId == null || taskId == null) {
			return ResponseEntity.notFound().build();
		}
		
		User user = userRepository.findOne(ownerId);
		Task taskToBeUpdated = taskRepository.findOne(taskId);
		if (taskToBeUpdated == null) {
			return ResponseEntity.notFound().build();
		}
		taskToBeUpdated.setOwner(user);
		
		taskToBeUpdated.setModifiedBy(user);
		taskToBeUpdated.setDescription(taskDto.getDescription());
		taskToBeUpdated.setDueDate(taskDto.getDueDate());
		taskToBeUpdated.setModifiedOn(new Date());
		taskToBeUpdated.setContent(taskDto.getContent());
		Priority priority = priorityRepository.findByText(taskDto.getPriority());
		taskToBeUpdated.setPriority(priority);
		Status status = statusRepository.findByText(taskDto.getStatus());
		taskToBeUpdated.setStatus(status);
		taskRepository.save(taskToBeUpdated);
		taskToBeUpdated.add(entityLinks.linkForSingleResource(taskToBeUpdated).slash(taskToBeUpdated.getTaskId()).withSelfRel());
		HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/tasks/{id}").buildAndExpand(taskToBeUpdated.getTaskId()).toUri());
        return new ResponseEntity<Task>(taskToBeUpdated, headers, HttpStatus.ACCEPTED);
	}
	
	@RequestMapping(value = "/users/{userId}/tasks/{taskId}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Task> deleteTask( @PathVariable("userId") Integer userId, @PathVariable("taskId") Integer taskId) {

		if (userId == null || taskId == null) {
			return ResponseEntity.notFound().build();
		}
		
		if (!taskRepository.exists(taskId) || userRepository.exists(userId)) {
			return ResponseEntity.notFound().build();
		}
		taskRepository.delete(taskId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}

	@RequestMapping(value = "/users/{userId}/tasks/{taskId}", method = RequestMethod.GET)
	public ResponseEntity<Task> getTask( @PathVariable("userId") Integer userId, @PathVariable("taskId") Integer taskId, UriComponentsBuilder builder) {

		Integer ownerId = userId;
		if (userId == null || taskId == null) {
			return ResponseEntity.notFound().build();
		}
		
		if (!userRepository.exists(ownerId)) {
			return ResponseEntity.notFound().build();
		}
		Task task = taskRepository.findOne(taskId);
		if (task == null) {
			return ResponseEntity.notFound().build();
		}
		// if the user is not a collaborator or owner return not found
		if (task.getOwner().getUserId() != userId) {
			return ResponseEntity.notFound().build();
		}
		task.add(entityLinks.linkForSingleResource(task).slash(task.getTaskId()).withSelfRel());
		HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/tasks/{id}").buildAndExpand(task.getTaskId()).toUri());
        return new ResponseEntity<Task>(task, headers, HttpStatus.ACCEPTED);
	}
	
	@Override
	public RepositoryLinksResource process(RepositoryLinksResource resource) {
		// taskToBeUpdated.add(entityLinks.linkForSingleResource(taskToBeUpdated).slash(taskToBeUpdated.getTaskId()).withSelfRel());
		// entityLinks.
		resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(TaskController.class).getTasks(null, null)).withRel("get all tasks of a user"));
		resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(TaskController.class).addTask(null, null, null)).withRel("add a task to a user"));
		resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(TaskController.class).getTask(null, null, null)).withRel("get a task of a user"));
		resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(TaskController.class).updateTask(null, null, null, null)).withRel("update a task of a user"));
		resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(TaskController.class).deleteTask(null, null)).withRel("delete a task from a user"));
        return resource;
	}
} 