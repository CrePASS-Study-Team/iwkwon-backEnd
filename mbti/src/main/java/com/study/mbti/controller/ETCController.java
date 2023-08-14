package com.study.mbti.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.study.mbti.common.ResponseResult;
import com.study.mbti.entity.NavigationEntity;
import com.study.mbti.repository.NavigationRepository;
import com.study.mbti.repository.SurveyRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api" , method = RequestMethod.POST)
public class ETCController {

	private final NavigationRepository navigationRepository;
	
	@RequestMapping(path = "/navigation")
	public ResponseEntity<?> selectNavigation(){
		
		ResponseResult response = new ResponseResult();
		try {
			response.setCode(200);
			response.setMessage("Success");
			long id = 1;
			NavigationEntity entity = navigationRepository.findById(id).get();
			
			response.setData( entity );
			
			
		} catch (Exception e) {
			response.setCode(999);
			response.setMessage("관리자문의");
			return new ResponseEntity<ResponseResult>(response, HttpStatus.OK);
		}
		
		return new ResponseEntity<ResponseResult>(response, HttpStatus.OK);
	}
}
