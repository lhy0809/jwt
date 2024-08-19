package com.hyong.jwt.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

@Entity
@Data
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String username;
	private String password;
	private String roles;
	
	@CreationTimestamp
	private Timestamp createDate;
	
	public List<String> getRoleList() {
		if(this.roles.length() > 0) {
			return Arrays.asList(roles.split(","));
		}
		return new ArrayList<>();
	}
}
