package com.yheng.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Entity
@Table(name = "Tasks")
@Data
public class Task extends ResourceSupport implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "TaskID")
	private int taskId;
	
	@Column(name = "ProjectID")
	private int projectId;
	
	@Column(name = "Description")
	private String description;
	
	@JsonManagedReference
	@OneToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name="PriorityID", nullable = true, insertable = true)
	private Priority priority;
	
	@JsonManagedReference
	@OneToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name="StatusID", nullable = true, insertable = true)
	private Status status;
	
	@Column(name = "DueDate")
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date dueDate;
	
	// @JsonManagedReference
	@ManyToOne
    @JoinColumn(name = "ModifiedBy", foreignKey = @ForeignKey(name = "FK_MODIFIED_BY"))
	private User modifiedBy;
	
	@Column(name = "ModifiedOn")
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date modifiedOn;

	@JsonManagedReference
	@ManyToOne
    @JoinColumn(name = "OwnedBy", foreignKey = @ForeignKey(name = "FK_OWNER"))
	private User owner;
	
	@Column(name = "Content", length = 65535)
	private String content;
	

}