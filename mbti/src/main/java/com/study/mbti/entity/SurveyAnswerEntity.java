package com.study.mbti.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name="MBTI_ANSWERS")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SurveyAnswerEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long NUM;

	@Column(nullable = true, unique = true, length = 100)
	private String id;
	
	@Column(length = 20)
	private String type;
	
	@Column(length = 255)
	private String percentage;
	
	@Column(columnDefinition = "TEXT")
	private String result;
}
