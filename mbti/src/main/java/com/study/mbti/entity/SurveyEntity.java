package com.study.mbti.entity;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name="MBTI_CREATE_SURVEY")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class SurveyEntity {
	

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long S_NUM;
	
	@Column(nullable = true, unique = true, length = 100)
	private String S_SURVEY_ID;
	
	@Column(nullable = true)
	private int S_SURVEY_LENGTH;
	
	//제한없는 길이
	@Column(nullable = true, columnDefinition = "TEXT")
	private String S_SURVEY_QUESTIONS;
	
	@CreatedDate
	private LocalDateTime S_CREATE_AT;
//	
//	@Column(nullable = true)
//	private Timestamp S_CREATED_DT;
	
	public SurveyEntity(String surveyId, int length, String body) {
		this.S_SURVEY_ID = surveyId;
		this.S_SURVEY_LENGTH = length;
		this.S_SURVEY_QUESTIONS = body;
	}
	
}
