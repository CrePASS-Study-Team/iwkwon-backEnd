package com.study.mbti.controller;

import java.util.Collections;
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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.study.mbti.common.ResponseResult;
import com.study.mbti.dto.ReqAnswerDto;
import com.study.mbti.entity.SurveyEntity;
import com.study.mbti.repository.SurveyRepository;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api" , method = RequestMethod.POST)
@CrossOrigin(origins = "http://localhost:3000")
public class SurveyController {

	private final SurveyRepository surveyRepository;
	
	@RequestMapping(path = "/survey/create")
	public ResponseEntity<?> surveyCreate(){
		
		Map<String, Object> result = new HashMap<>();
		ResponseResult response = new ResponseResult();
		
		response.setCode(200);
		response.setMessage("Success");
		
		try {

			SurveyEntity survey = surveyRepository.findRandomEntity();
			result.put("unique_id", survey.getS_SURVEY_ID());
			result.put("length", survey.getS_SURVEY_LENGTH());
			result.put("survey", survey.getS_SURVEY_QUESTIONS());
			
			response.setData(result);


		} catch (Exception e) {
			response.setCode(999);
			response.setMessage("관리자에게 문의 해주세요.");
		
			return new ResponseEntity<ResponseResult>(response, HttpStatus.OK);
		}
		
		return new ResponseEntity<ResponseResult>(response, HttpStatus.OK);
	}
	
	@RequestMapping(path = "/survey/create/test")
	public ResponseEntity<?> surveyCreateTest(){
		
		ResponseResult response = new ResponseResult();
		
		
		try {
			
			response.setCode(200);
			response.setMessage("Success");
			UUID uuid4 = UUID.randomUUID();
			String surveyId = uuid4.toString().substring(0,18);//uuid 18자리
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders header = new HttpHeaders();
			header.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<?> entity = new HttpEntity<>(header);

			
			ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://3.35.152.198:8000/survey/questions", entity, String.class);
			System.out.println("created survey : " + responseEntity.getBody());
			
			JSONObject responseSurvey = new JSONObject(responseEntity.getBody());
			JSONArray questions = responseSurvey.getJSONArray("questions");
			
			SurveyEntity survey = SurveyEntity.builder()
					.S_SURVEY_ID(surveyId)
					.S_SURVEY_LENGTH(questions.length())
					.S_SURVEY_QUESTIONS(questions.toString())
					.build();
			surveyRepository.save(survey);
			
		} catch (Exception e) {
			response.setCode(999);
			response.setMessage("fail");
			response.setData(e.getMessage());
			e.printStackTrace();
		}
		
		return new ResponseEntity<ResponseResult>(response, HttpStatus.OK);
	}
	
	@RequestMapping(path = "/survey/answers")
	public ResponseEntity<?> surveyAnswers(@RequestBody String reqStr){
		
		JSONObject jsonStr = new JSONObject(reqStr);
		System.out.println(jsonStr.toString());
		
		ResponseResult response = new ResponseResult();
		JSONObject reqJSON = new JSONObject();
		try {
			
			SurveyEntity survey = surveyRepository.findByS_SURVEY_ID(jsonStr.getString("unique_id"));
			String questions = survey.getS_SURVEY_QUESTIONS();
			
			reqJSON.put("questions", new JSONArray(questions));
			reqJSON.put("answers", jsonStr.getJSONArray("answers"));
			
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders header = new HttpHeaders();
			
			header.setContentType(MediaType.APPLICATION_JSON);
			
			HttpEntity<String> entity = new HttpEntity<>(reqJSON.toString(),header);
			
			System.out.println(entity.getBody());
			ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://3.35.152.198:8000/survey/answers", entity, String.class);
			System.out.println("survey answers: " + responseEntity);
			
			JSONObject responseSurvey = new JSONObject(responseEntity.getBody());
			
			response.setCode(200);
			response.setMessage("Success");
			response.setData(responseSurvey.toString());
			
		} catch (Exception e) {
			response.setCode(999);
			response.setMessage("관리자에게 문의 해주세요.");
			response.setData(e.getMessage());
			System.out.println(e.getMessage());
			return new ResponseEntity<ResponseResult>(response, HttpStatus.OK);
		}
		return new ResponseEntity<ResponseResult>(response, HttpStatus.OK);
	}
	
	
}

