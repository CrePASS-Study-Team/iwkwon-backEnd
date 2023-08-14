package com.study.mbti.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.study.mbti.entity.SurveyEntity;

@Repository
public interface SurveyRepository extends JpaRepository<SurveyEntity, Long> {

	@Query(nativeQuery = true, value = "SELECT s_survey_id, s_survey_length, s_survey_questions FROM mbti_create_survey ORDER BY RAND() LIMIT 1")
	SurveyEntity findRandomEntity();
}
