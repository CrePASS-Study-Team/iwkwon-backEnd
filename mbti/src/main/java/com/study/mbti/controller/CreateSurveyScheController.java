package com.study.mbti.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.study.mbti.common.ResponseResult;
import com.study.mbti.entity.SurveyEntity;
import com.study.mbti.repository.SurveyRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreateSurveyScheController {

	private final SurveyRepository surveyRepository;
	
	//매주 일,목 새벽4시에 설문 하나씩 생성함
	@Scheduled(cron = "0 0 4 * * 0,4")
	public void createSurveySche() {
		
		try {
			
			UUID uuid4 = UUID.randomUUID();
			String surveyId = uuid4.toString().substring(0,18);//uuid 18자리
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders header = new HttpHeaders();
			header.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<?> entity = new HttpEntity<>(header);

			
			ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://3.35.152.198:8000/survey/questions", entity, String.class);
			
			JSONObject responseSurvey = new JSONObject(responseEntity.getBody());
			JSONArray questions = responseSurvey.getJSONArray("questions");
			
			SurveyEntity survey = SurveyEntity.builder()
					.S_SURVEY_ID(surveyId)
					.S_SURVEY_LENGTH(questions.length())
					.S_SURVEY_QUESTIONS(questions.toString())
					.build();
			surveyRepository.save(survey);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
