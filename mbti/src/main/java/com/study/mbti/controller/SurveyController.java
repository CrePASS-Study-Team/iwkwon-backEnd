package com.study.mbti.controller;

import java.util.HashMap;
import java.util.List;

import java.util.Map;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.study.mbti.common.ResponseResult;
import com.study.mbti.entity.SurveyEntity;
import com.study.mbti.repository.SurveyRepository;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api" , method = RequestMethod.POST)
public class SurveyController {

	private final SurveyRepository surveyRepository;
	
	@RequestMapping(path = "/survey/create")
	public ResponseEntity<?> surveyCreate(){
		
		UUID uuid4 = UUID.randomUUID();
		String surveyId = uuid4.toString().substring(0,18);//uuid 18자리
		
		Map<String, Object> result = new HashMap<>();
		ResponseResult response = new ResponseResult();
		
		response.setCode(200);
		response.setMessage("Success");
		
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders header = new HttpHeaders();
			header.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<?> entity = new HttpEntity<>(header);

			ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://3.35.152.198:8000/survey/questions", entity, String.class);
			if(responseEntity.getStatusCode().isError()) {
				response.setCode(201);
				response.setMessage("Success");
				response.setData(surveyRepository.findRandomEntity());
				return new ResponseEntity<ResponseResult>(response, HttpStatus.OK);
			}
			
			JSONObject responseSurvey = new JSONObject(responseEntity.getBody());
			JSONArray questions = responseSurvey.getJSONArray("questions");
			result.put("unique_id", surveyId);
			result.put("length", questions.length());
			result.put("survey", questions.toList());
			response.setData(result);
		
//			SurveyEntity survey = new SurveyEntity(surveyId, questions.length(), questions.toString());
			SurveyEntity survey = SurveyEntity.builder()
					.S_SURVEY_ID(surveyId)
					.S_SURVEY_LENGTH(questions.length())
					.S_SURVEY_QUESTIONS(questions.toString())
					.build();
			SurveyEntity save = surveyRepository.save(survey);
			System.out.println(save.getS_CREATE_AT());
		} catch (Exception e) {
			response.setCode(999);
			response.setMessage("관리자문의");
		
			return new ResponseEntity<ResponseResult>(response, HttpStatus.OK);
		}
		
		return new ResponseEntity<ResponseResult>(response, HttpStatus.OK);
	}
}

