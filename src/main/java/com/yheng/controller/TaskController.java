package com.yheng.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryLinksResource;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.hateoas.Resources;
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

import com.yheng.entity.Priority;
import com.yheng.entity.Status;
import com.yheng.entity.Task;
import com.yheng.entity.TaskDTO;
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

	/*
	 * Gets all tasks owned by a user
	 */
	@RequestMapping(value = "/users/{userId}/tasks", method = RequestMethod.GET, produces = "application/hal+json")
	public @ResponseBody ResponseEntity<Resources<Task>> getTasks( @PathVariable("userId") Integer userId, UriComponentsBuilder builder) {

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
			task.add(entityLinks.linkForSingleResource(task.getOwner()).slash(task.getOwner().getUserId()).withRel("ownedBy"));
			task.add(entityLinks.linkForSingleResource(task.getModifiedBy()).slash(task.getModifiedBy().getUserId()).withRel("modifiedBy"));
			Priority priority = task.getPriority();
			if (priority != null) {
				task.add(entityLinks.linkForSingleResource(priority).slash(priority.getPriorityId()).withRel("priority"));
			}
			Status status = task.getStatus();
			if (status != null) {
				task.add(entityLinks.linkForSingleResource(status).slash(status.getStatusId()).withRel("status"));
			}
		}
		
		HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/users/{userId}/tasks").buildAndExpand(userId).toUri());

        Resources<Task> resources = new Resources<Task>(tasks, entityLinks.linkToCollectionResource(Task.class));

        return new ResponseEntity<Resources<Task>>(resources, HttpStatus.ACCEPTED);
	}
	
	/*
	 * Creates a new task for a user
	 */
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
		
		// Add rels
		taskToBe.add(entityLinks.linkForSingleResource(taskToBe).slash(taskToBe.getTaskId()).withSelfRel());
		taskToBe.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(TaskController.class).deleteTask(ownerId, taskToBe.getTaskId())).withRel("delete task"));
		taskToBe.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(TaskController.class).updateTask(ownerId, taskToBe.getTaskId(), null, null)).withRel("update task"));
		taskToBe.add(entityLinks.linkToCollectionResource(Task.class));
		
		HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/tasks/{id}").buildAndExpand(taskToBe.getTaskId()).toUri());
        return new ResponseEntity<Task>(taskToBe, headers, HttpStatus.CREATED);
	}
	
	/*
	 * Updates the task of a user
	 */
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

		// Add rels
		taskToBeUpdated.add(entityLinks.linkForSingleResource(taskToBeUpdated).slash(taskToBeUpdated.getTaskId()).withSelfRel());
		taskToBeUpdated.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(TaskController.class).deleteTask(ownerId, taskToBeUpdated.getTaskId())).withRel("delete task"));
		taskToBeUpdated.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(TaskController.class).updateTask(ownerId, taskToBeUpdated.getTaskId(), null, null)).withRel("update task"));
		taskToBeUpdated.add(entityLinks.linkToCollectionResource(Task.class));

		
		HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/tasks/{id}").buildAndExpand(taskToBeUpdated.getTaskId()).toUri());
        return new ResponseEntity<Task>(taskToBeUpdated, headers, HttpStatus.ACCEPTED);
	}
	
	/*
	 * Deletes a task from a user
	 */
	@RequestMapping(value = "/users/{userId}/tasks/{taskId}", method = RequestMethod.DELETE)
	public ResponseEntity<Task> deleteTask( @PathVariable("userId") Integer userId, @PathVariable("taskId") Integer taskId) {

		if (userId == null || taskId == null) {
			return ResponseEntity.notFound().build();
		}
		
		if (!taskRepository.exists(taskId) || !userRepository.exists(userId)) {
			return ResponseEntity.notFound().build();
		}
		taskRepository.delete(taskId);
		
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}

	/*
	 * Gets a specific task of a user
	 */
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
 
        // add rels
        task.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(TaskController.class).deleteTask(ownerId, task.getTaskId())).withRel("delete task"));
        task.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(TaskController.class).updateTask(ownerId, task.getTaskId(), null, null)).withRel("update task"));
        task.add(entityLinks.linkToCollectionResource(Task.class));

        return new ResponseEntity<Task>(task, headers, HttpStatus.ACCEPTED);
	}
	
	@Override
	public RepositoryLinksResource process(RepositoryLinksResource resource) {
		resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(TaskController.class).getTasks(null, null)).withRel("get all tasks of a user"));
		resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(TaskController.class).addTask(null, null, null)).withRel("add a task to a user"));
		resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(TaskController.class).getTask(null, null, null)).withRel("get a task of a user"));
		resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(TaskController.class).updateTask(null, null, null, null)).withRel("update a task of a user"));
		resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(TaskController.class).deleteTask(null, null)).withRel("delete a task from a user"));
        return resource;
	}
} 