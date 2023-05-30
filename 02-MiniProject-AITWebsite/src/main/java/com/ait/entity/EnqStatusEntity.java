package com.ait.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name="AIT_ENQ_STATUS")
public class EnqStatusEntity {
	 
	@Id
	@GeneratedValue
	private Integer statusId;
	
	private String statusName;

}
