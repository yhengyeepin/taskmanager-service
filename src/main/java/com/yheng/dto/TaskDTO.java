package com.yheng.dto;

import java.util.Date;

import org.springframework.hateoas.ResourceSupport;

import lombok.Data;

public @Data class TaskDTO extends ResourceSupport {
	
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	private Integer taskId;

	private Integer projectId;
	
	private String description;
	
	private String priority;
	
	private String status;
	
	private Date dueDate;

	private String content;

}
