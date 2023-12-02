package com.study.mbti.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.mbti.entity.SurveyAnswerEntity;

public interface SurveyAnswersRepository extends JpaRepository<SurveyAnswerEntity, Long>{

	SurveyAnswerEntity findById(String string);

}
