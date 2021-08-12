package com.beha.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.beha.dto.AddLessonDTO;
import com.beha.dto.AddTeacherDTO;
import com.beha.dto.AddTeacherResponseDTO;
import com.beha.dto.AdminDashboardDTO;
import com.beha.dto.BaseResponseDTO;
import com.beha.dto.BaseResponseWithIdDTO;
import com.beha.dto.EmailPasswordRequestDTO;
import com.beha.dto.GradeDTO;
import com.beha.dto.LessonDTO;
import com.beha.dto.UpdateTeacherDTO;
import com.beha.dto.UpdateTeacherResponseDTO;
import com.beha.service.SchoolAdminService;

@RestController
@RequestMapping(path="/school-admin")
public class SchoolAdminController {
	
	@Autowired
	SchoolAdminService schoolAdminService;
	
	@RequestMapping(path="/login",method=RequestMethod.POST)
	public ResponseEntity<AdminDashboardDTO> login(@RequestBody EmailPasswordRequestDTO emailPasswordRequestDTO) {
		AdminDashboardDTO adminDashboardDTO = schoolAdminService.getDashboardDetail(emailPasswordRequestDTO);
	    return new ResponseEntity<AdminDashboardDTO>(adminDashboardDTO,  HttpStatus.OK);
	  }
	
	@RequestMapping(path="/add-lesson",method=RequestMethod.POST)
	public ResponseEntity<BaseResponseDTO> addLesson(@RequestBody AddLessonDTO addLessonDTO) {
		BaseResponseDTO baseResponseDTO = schoolAdminService.addLesson(addLessonDTO);
	    return new ResponseEntity<BaseResponseDTO>(baseResponseDTO,  HttpStatus.OK);
	  }
	
	@RequestMapping(path="/add-teacher",method=RequestMethod.POST)
	public ResponseEntity<AddTeacherResponseDTO> addTeacher(@RequestBody AddTeacherDTO addTeacherDTO) {
		AddTeacherResponseDTO addTeacherResponseDTO = schoolAdminService.addTeacher(addTeacherDTO);
	    return new ResponseEntity<AddTeacherResponseDTO>(addTeacherResponseDTO,  HttpStatus.OK);
	  }
	
	@RequestMapping(path="/update-teacher",method=RequestMethod.POST)
	public ResponseEntity<UpdateTeacherResponseDTO> updateTeacher(@RequestBody UpdateTeacherDTO updateTeacherDTO) {
		UpdateTeacherResponseDTO updateTeacherResponseDTO = schoolAdminService.updateTeacher(updateTeacherDTO);
	    return new ResponseEntity<UpdateTeacherResponseDTO>(updateTeacherResponseDTO,  HttpStatus.OK);
	  }

	@GetMapping("/getLessons/{schoolId}")
	public ResponseEntity<List<GradeDTO>> getGrades(@PathVariable(value = "schoolId") Long id){
		List<GradeDTO> gradeDTOs = schoolAdminService.getLessons(id);
		return new ResponseEntity<List<GradeDTO>>(gradeDTOs ,  HttpStatus.OK);
	}
	
	@GetMapping("/deleteTeacher/{teacherId}")
	public ResponseEntity<AddTeacherResponseDTO> deleteTeacher(@PathVariable(value = "teacherId") Long teacherId) {
		AddTeacherResponseDTO response = schoolAdminService.deleteTeacher(teacherId);
		return new ResponseEntity<AddTeacherResponseDTO>(response,  HttpStatus.OK);
	}
	
	@GetMapping("/deleteLesson/{lessonId}")
	public ResponseEntity<BaseResponseWithIdDTO> deleteLesson(@PathVariable(value = "lessonId") Long lessonId) {
		BaseResponseWithIdDTO response = schoolAdminService.deleteLesson(lessonId);
		return new ResponseEntity<BaseResponseWithIdDTO>(response,  HttpStatus.OK);
	}
}
