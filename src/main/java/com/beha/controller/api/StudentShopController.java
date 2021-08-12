package com.beha.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.beha.dto.GradeDTO;
import com.beha.dto.LessonDTO;
import com.beha.dto.SchoolDTO;
import com.beha.dto.ShopListDTO;
import com.beha.service.GradeService;
import com.beha.service.SchoolService;
import com.beha.service.ShopListService;

@RestController
@RequestMapping(path="/student")
public class StudentShopController {
	
	@Autowired
	SchoolService schoolService;
	
	@Autowired
	GradeService gradeService;
	
	@Autowired
	ShopListService shopListService;
	
	@RequestMapping(path="/getSchools", method = RequestMethod.GET)
	public ResponseEntity<List<SchoolDTO>> getSchools(){
		List<SchoolDTO> schoolDTOs = schoolService.getAllSchools();
		return new ResponseEntity<List<SchoolDTO>>(schoolDTOs ,  HttpStatus.OK);
	}
	
	@GetMapping("/school/{schoolName}")
	public ResponseEntity<List<SchoolDTO>> getCampuses(@PathVariable(value = "schoolName") String name){
		List<SchoolDTO> schoolDTOs = schoolService.getCampusesBySchool(name);
		return new ResponseEntity<List<SchoolDTO>>(schoolDTOs ,  HttpStatus.OK);
	}
	
	
	@GetMapping("/grade/{schoolId}")
	public ResponseEntity<List<GradeDTO>> getGrades(@PathVariable(value = "schoolId") Long id){
		List<GradeDTO> gradeDTOs = gradeService.getGradesBySchool(id);
		return new ResponseEntity<List<GradeDTO>>(gradeDTOs ,  HttpStatus.OK);
	}
	
	@GetMapping("/shoplist/{gradeId}")
	public ResponseEntity<List<LessonDTO>> getShopLists(@PathVariable(value = "gradeId") Long id) throws Exception{
		List<LessonDTO> lessonDTOs = gradeService.getShopListsByGradeId(id);
		return new ResponseEntity<List<LessonDTO>>(lessonDTOs ,  HttpStatus.OK);
	}
	
}
