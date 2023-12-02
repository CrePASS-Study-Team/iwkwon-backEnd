package com.study.mbti.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.study.mbti.common.ResponseResult;
import com.study.mbti.dto.ReqAnswerDto;
import com.study.mbti.entity.SurveyAnswerEntity;
import com.study.mbti.entity.SurveyEntity;
import com.study.mbti.repository.SurveyAnswersRepository;
import com.study.mbti.repository.SurveyRepository;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api")
public class SurveyController {

	private final SurveyRepository surveyRepository;
	
	private final SurveyAnswersRepository surveyAnswersRepository;
	
	@PostMapping(path = "/survey/create")
	public ResponseEntity<?> surveyCreate(){
		
		Map<String, Object> result = new HashMap<>();
		ResponseResult response = new ResponseResult();
		
		response.setCode(200);
		response.setMessage("Success");
		
		try {

			SurveyEntity survey = surveyRepository.findRandomEntity();
			result.put("unique_id", survey.getS_SURVEY_ID());
			result.put("length", survey.getS_SURVEY_LENGTH());
			
			JSONArray surveyJson = new JSONArray(survey.getS_SURVEY_QUESTIONS());
			List<Map<String, Object>> list = IntStream.range(0, surveyJson.length())
                    .mapToObj(i -> ((JSONObject) surveyJson.get(i)).toMap())
                    .collect(Collectors.toList());
			result.put("survey", list);
			response.setData(result);


		} catch (Exception e) {
			response.setCode(999);
			response.setMessage("관리자에게 문의 해주세요.");
			response.setData(e.getMessage());
		
			return ResponseEntity.ok(response);
		}
		
		return ResponseEntity.ok(response);
	}
	
	@PostMapping(path = "/survey/create/test")
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
	
	@PostMapping(path = "/survey/answers")
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
			
			ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://3.35.152.198:8000/survey/answers", entity, String.class);
			System.out.println("survey answers: " + responseEntity);
			
			JSONObject responseSurvey = new JSONObject(responseEntity.getBody());
			
			SurveyAnswerEntity surveyAnswer = SurveyAnswerEntity.builder()
					.id(responseSurvey.getString("id"))
					.type(responseSurvey.getString("type"))
					.percentage(responseSurvey.getJSONObject("percentage").toString())
					.result(responseSurvey.getString("result"))
					.build();
					
			surveyAnswersRepository.save(surveyAnswer);
			
			Map<String,Object> result = new HashMap<String,Object>();
			result.put("id", responseSurvey.getString("id"));
			
			response.setCode(200);
			response.setMessage("Success");
			response.setData(result);
			
		} catch (Exception e) {
			response.setCode(999);
			response.setMessage("관리자에게 문의 해주세요.");
			response.setData(e.getMessage());
			System.out.println(e.getMessage());
			return new ResponseEntity<ResponseResult>(response, HttpStatus.OK);
		}
		return new ResponseEntity<ResponseResult>(response, HttpStatus.OK);
	}
	
	@GetMapping(path = "/survey/result/{id}")
	public ResponseEntity<?> resultAnswer(@PathVariable String id){
		
		ResponseResult response = new ResponseResult();
		Map<String,Object> result = new HashMap<>();
		
		try {
			SurveyAnswerEntity surveyAnswer = surveyAnswersRepository.findById(id);
			JSONObject percentage = new JSONObject(surveyAnswer.getPercentage());
			
			result.put("id", surveyAnswer.getId());
			result.put("type", surveyAnswer.getType());
			result.put("percentage", percentage.toMap());
			result.put("result", surveyAnswer.getResult());
			
			return ResponseEntity.ok(Map.of(
					"code", 200,
					"message", "Success",
					"data", result
					));
			
		} catch (Exception e) {
			
			response.setCode(999);
			response.setMessage("관리자에게 문의 해주세요.");
			response.setData(e.getMessage());
			System.out.println(e.getMessage());
			return new ResponseEntity<ResponseResult>(response, HttpStatus.OK);
		}
		
	}
	
}

