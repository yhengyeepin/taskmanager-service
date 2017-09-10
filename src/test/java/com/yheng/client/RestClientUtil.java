package com.yheng.client;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestTemplate;

import com.yheng.entity.Priority;
import com.yheng.entity.Status;
import com.yheng.entity.Task;
import com.yheng.entity.User;
import com.yheng.repository.PriorityRepository;
import com.yheng.repository.StatusRepository;
import com.yheng.repository.TaskRepository;
import com.yheng.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RestClientUtil {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired StatusRepository statusRepository;
	
	@Autowired PriorityRepository priorityRepository;
	
	@Autowired UserRepository userRepository;
	
	@Autowired TaskRepository taskRepository;
	
	private User currentUser = null;
	
    public void getTaskByIdDemo() {
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
	    String url = "http://localhost:8080/task/{id}";
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
        ResponseEntity<Task> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Task.class, 1);
        Task task = responseEntity.getBody();
        // System.out.println("Id:"+task.getTaskId()+", Title:"+task.getTitle()
        //         +", Category:"+task.getCategory());      
    }
	public void getAllTasksDemo() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
	    String url = "http://localhost:8080/tasks";
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
        ResponseEntity<Task[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Task[].class);
        Task[] tasks = responseEntity.getBody();
        for(Task task : tasks) {
//              System.out.println("Id:"+task.getTaskId()+", Title:"+task.getTitle()
//                      +", Category: "+task.getCategory());
        }
    }
    public void addTaskDemo() {
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
	    String url = "http://localhost:8080/task";
	    Task objTask = new Task();
//	    objTask.setTitle("Spring REST Security using Hibernate");
//	    objTask.setCategory("Spring");
        HttpEntity<Task> requestEntity = new HttpEntity<Task>(objTask, headers);
        URI uri = restTemplate.postForLocation(url, requestEntity);
        System.out.println(uri.getPath());    	
    }
    public void updateTaskDemo() {
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
	    String url = "http://localhost:8080/task";
	    Task objTask = new Task();
	    objTask.setTaskId(1);
//	    objTask.setTitle("Update:Java Concurrency");
//	    objTask.setCategory("Java");
        HttpEntity<Task> requestEntity = new HttpEntity<Task>(objTask, headers);
        restTemplate.put(url, requestEntity);
    }
    public void deleteTaskDemo() {
	    	HttpHeaders headers = new HttpHeaders();
	    	headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
	    String url = "http://localhost:8080/task/{id}";
        HttpEntity<Task> requestEntity = new HttpEntity<Task>(headers);
        restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, Void.class, 4);        
    }
    
    public void addStatuses() {

	    /*	HttpHeaders headers = new HttpHeaders();
	    	headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
	    String url = "http://localhost:8080/status";*/
	    Status status = new Status();
	    status.setText("In progress");
		statusRepository.save(status);
        /*HttpEntity<Status> requestEntity = new HttpEntity<Status>(status, headers);
        URI uri = restTemplate.postForLocation(url, requestEntity);
        System.out.println(uri.getPath());  */
		System.out.println(status.getStatusId() + status.getText());
    }
  
	public void deleteAllUsers() {
		HttpHeaders headers = new HttpHeaders();
	    	headers.setContentType(MediaType.APPLICATION_JSON);
	    RestTemplate restTemplate = new RestTemplate();
	    String url = "http://localhost:8080/users";
	    HttpEntity<User> requestEntity = new HttpEntity<User>(headers);
	    restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, Void.class, 4);    
	}
    
	public static HttpHeaders getJSONHeader() {
	    	HttpHeaders headers = new HttpHeaders();
	    	headers.setContentType(MediaType.APPLICATION_JSON);
	    	return headers;
	}
    public void addUser() {
	    RestTemplate restTemplate = new RestTemplate();
	    String url = "http://localhost:8080/user";
	    User user = new User();
	    user.setAdmin(false);
	    user.setFirstName("Ryan");
	    user.setLastName("Yheng");
	    user.setEmail("yeepin@gmail.com");
	    HttpEntity<User> requestEntity = new HttpEntity<User>(user, getJSONHeader());
	    URI uri = restTemplate.postForLocation(url, requestEntity);
	    System.out.println(uri.getPath());
	    System.out.println("user id" + user.getUserId());
	    this.currentUser = user;
	}
    
	public void deleteAllTasks() {
		RestTemplate restTemplate = new RestTemplate();
		String url = "http://localhost:8080/tasks";
		HttpEntity<Task> requestEntity = new HttpEntity<Task>(getJSONHeader());
		restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, Void.class, 4);    
	} 
	
	public void addTask() {
	    RestTemplate restTemplate = new RestTemplate();
	    String url = "http://localhost:8080/task";
	    Task task = new Task();
	    task.setDescription("task description");
	    task.setDueDate(new Date());
	    task.setModifiedBy(this.currentUser);
	    task.setModifiedOn(new Date());
	    task.setOwner(this.currentUser);
	    
	    HttpEntity<Task> requestEntity = new HttpEntity<Task>(task, getJSONHeader());
	    URI uri = restTemplate.postForLocation(url, requestEntity);
	    System.out.println(uri.getPath());  
		
	} 

	@Before
	public void deleteAllBeforeTests() throws Exception {
		taskRepository.deleteAll();
		statusRepository.deleteAll();
		priorityRepository.deleteAll();
		userRepository.deleteAll();
		
		// add some statuses and priorities
		Status status = new Status();
	    status.setText("New");
		statusRepository.save(status);
		status = new Status();
	    status.setText("In progress");
		statusRepository.save(status);
		status = new Status();
	    status.setText("Completed");
		statusRepository.save(status);
		
		Priority priority = new Priority();
		priority.setText("High");
		priorityRepository.save(priority);
		priority = new Priority();
		priority.setText("Normal");
		priorityRepository.save(priority);
		priority = new Priority();
		priority.setText("Low");
		priorityRepository.save(priority);
	}
	
    public void deleteStatuses() {
    		statusRepository.deleteAll();
    		
    		Status status = new Status();
    	    status.setText("In progress");
    		statusRepository.save(status);
	    /*	HttpHeaders headers = new HttpHeaders();
	    	headers.setContentType(MediaType.APPLICATION_JSON);
	    RestTemplate restTemplate = new RestTemplate();
	    String url = "http://localhost:8080/statuses";
	    HttpEntity<Status> requestEntity = new HttpEntity<Status>(headers);
	    restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, Void.class, 4);   */ 
    }

	/*@Test
	public void shouldCreateTaskWithRepo() throws Exception {
		MvcResult mvcResult = mockMvc.perform(post("/users").content(
				"{" + 	
					String.join(",", 
								"\"firstName\": \"Peter\"",
								"\"lastName\":\"Reynold\"",
								"\"email\": \"peter@gmail.com\"") +
				"}")).andExpect(status().isCreated()).andReturn();

		String userlocation = mvcResult.getResponse().getHeader("Location");
		String id = userlocation.substring(userlocation.lastIndexOf("/")+1);
		
		Task task = new Task();
		User user = userRepository.findOne(new Integer(id));
		System.out.println("user found"+user+ id);
		task.setOwner(user);
		task.setModifiedBy(user);
		task.setDescription("hello");
		task.setDueDate(new Date());
		task.setModifiedOn(new Date());
		Priority priority = priorityRepository.findByText("Low");
		System.out.println("priorityx"+priority);
		task.setPriority(priority);
		Status status = statusRepository.findByText("Completed");
		task.setStatus(status);
		System.out.println("before task saved" + task);
		taskRepository.save(task);
	}*/
    
	/*@Test
	public void shouldCreateStatuses() {
		statusRepository.deleteAll();
		Status status = new Status();
	    status.setText("New");
		statusRepository.save(status);
		status = new Status();
	    status.setText("In progress");
		statusRepository.save(status);
		status = new Status();
	    status.setText("Completed");
		statusRepository.save(status);
		
		assertEquals("New", statusRepository.findByText("New").getText()); 
	}*/

	@Test
	public void shouldCreateTask() throws Exception {
		MvcResult mvcResult = mockMvc.perform(post("/users").content(
				"{" + 	
					String.join(",", 
								"\"firstName\": \"Peter\"",
								"\"lastName\":\"Reynold\"",
								"\"email\": \"peter@gmail.com\"") +
				"}")).andExpect(status().isCreated()).andReturn();

		String userlocation = mvcResult.getResponse().getHeader("Location");
		System.out.println("userlocation.lastIndexOf(\"/\")"+userlocation.lastIndexOf("/"));
		String id = userlocation.substring(userlocation.lastIndexOf("/")+1);
		System.out.println("created user id"+id);
		mockMvc.perform(post("/usertasks")
			.content(
			"{" + 	
				String.join(",", 
							"\"description\": \"new task\"",
							"\"ownerId\":\""+id+"\"",
							"\"modifiedBy\":\""+id+"\"",
							"\"dueDate\":\"2017-12-12\"",
							"\"priority\":\"Normal\"",
							"\"status\":\"New\"") +
			"}")).andExpect(status().isCreated())
				.andExpect(header().string("Location", containsString("tasks/")));
	}
	
	@Test
	public void shouldCreateUser() throws Exception {
		mockMvc.perform(post("/users").content(
			"{" + 	
				String.join(",", 
							"\"firstName\": \"Steven\"",
							"\"lastName\":\"Johnson\"",
							"\"email\": \"steve@gmail.com\"") +
			"}")).andExpect(status().isCreated())
				.andExpect(header().string("Location", containsString("users/")));
	}
	
	@Test
	public void shouldRetrieveUser() throws Exception {

		MvcResult mvcResult = mockMvc.perform(post("/users").content(
				"{" + 	
					String.join(",", 
								"\"firstName\": \"Henry\"",
								"\"lastName\":\"Jackson\"",
								"\"email\": \"steve@gmail.com\"") +
				"}")).andExpect(status().isCreated()).andReturn();

		String location = mvcResult.getResponse().getHeader("Location");
		mockMvc.perform(get(location)).andExpect(status().isOk()).andExpect(
				jsonPath("$.firstName").value("Henry")).andExpect(
						jsonPath("$.lastName").value("Jackson"));
	}
	
	@Test
	public void shouldQueryUser() throws Exception {

		mockMvc.perform(post("/users").content(
				"{" + 	
					String.join(",", 
								"\"firstName\": \"Susan\"",
								"\"lastName\":\"Lee\"",
								"\"email\": \"susan@gmail.com\"") +
				"}")).andExpect(status().isCreated()).andReturn();

		mockMvc.perform(
				get("/users/search/findByLastName?lastName={lastName}", "Lee")).andExpect(
						status().isOk()).andExpect(
								jsonPath("$._embedded.users[0].firstName").value(
										"Susan"));
	}
	
	@Test
	public void shouldUpdateUser() throws Exception {

		MvcResult mvcResult = mockMvc.perform(post("/users").content(
				"{" + 	
					String.join(",", 
								"\"firstName\": \"Kayce\"",
								"\"lastName\":\"Everett\"",
								"\"email\": \"kayce@gmail.com\"") +
				"}")).andExpect(status().isCreated()).andReturn();

		String location = mvcResult.getResponse().getHeader("Location");

		mockMvc.perform(put(location).content(
				"{\"firstName\": \"Bing\", \"lastName\":\"Everette\"}")).andExpect(
						status().isNoContent());

		mockMvc.perform(get(location)).andExpect(status().isOk()).andExpect(
				jsonPath("$.firstName").value("Bing")).andExpect(
						jsonPath("$.lastName").value("Everette"));
	}
	
    public static void main(String args[]) {
    		//SpringApplication.run(RestClientUtil.class, args);
    		// RestClientUtil util = new RestClientUtil();
    		// util.deleteStatuses();
    		// util.addStatuses();
    		
    		// util.deleteAllUsers();
    		// util.addUser();
    		
    		// util.deleteAllTasks();
    		// util.addTask();
        //util.getTaskByIdDemo();
    //	util.getAllTasksDemo();
    	//util.addTaskDemo();
    	//util.updateTaskDemo();
    	//util.deleteTaskDemo();
    }
	  
}
