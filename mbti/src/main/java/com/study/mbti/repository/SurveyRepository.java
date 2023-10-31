package com.study.mbti.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.study.mbti.entity.SurveyEntity;

@Repository
public interface SurveyRepository extends JpaRepository<SurveyEntity, Long> {

	@Query(nativeQuery = true, value = "SELECT s_num, s_survey_id, s_survey_length, s_survey_questions, s_create_at FROM mbti_create_survey ORDER BY RAND() LIMIT 1")
	SurveyEntity findRandomEntity();
	
	@Query(nativeQuery = true, value = "SELECT s_num, s_survey_id, s_survey_length, s_survey_questions, s_create_at FROM mbti_create_survey WHERE s_survey_id = :id")
	SurveyEntity findByS_SURVEY_ID(@Param("id")String id);
	
}
