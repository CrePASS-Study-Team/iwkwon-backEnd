package com.study.mbti.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name="MBTI_NAVIGATION")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NavigationEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long N_NUM;
	
	@Column(nullable = true)
	private String N_type_test;
	
	@Column(nullable = true)
	private String N_personality_type;
	
	@Column(nullable = true)
	private String N_teams;
	
	@Column(nullable = true)
	private String N_language;
	
}
