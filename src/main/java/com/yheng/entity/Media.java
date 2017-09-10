package com.yheng.entity;

import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "TaskMedia")
public class Media implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "MediaID")
	private int mediaId;
	
	@ManyToOne
	@JoinColumn(name = "TaskID", foreignKey = @ForeignKey(name = "FK_TASK"))
	private Task task;
	
	@Column(name = "MediaType")
	private String mediaType;
	
	@Column(name = "MediaName")
	private String mediaName;
	
	@Column(name = "UploadedDate")
	private Date uploadedDate;
	
	@Column(name = "Media")
	private Blob media;
	
}