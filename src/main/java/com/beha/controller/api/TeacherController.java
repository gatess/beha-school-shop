package com.beha.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.beha.dto.EmailPasswordRequestDTO;
import com.beha.dto.GeneralResponseDTO;
import com.beha.dto.GetProductsResponse;
import com.beha.dto.LessonDTO;
import com.beha.dto.LoginResponseDTO;
import com.beha.dto.ProductSearchDTO;
import com.beha.dto.ShopListAddDTO;
import com.beha.service.TeacherService;

@RestController
@RequestMapping(path="/teacher")
public class TeacherController {

	@Autowired
	TeacherService teacherService;
	
	@RequestMapping(path="/login",method=RequestMethod.POST)
	public ResponseEntity<LoginResponseDTO> login(@RequestBody EmailPasswordRequestDTO emailPasswordRequestDTO) {
		LoginResponseDTO loginResponseDTO = teacherService.teacherLogin(emailPasswordRequestDTO);
	    return new ResponseEntity<LoginResponseDTO>(loginResponseDTO,  HttpStatus.OK);
	     
	  }
	
	@RequestMapping(path="/addProduct",method=RequestMethod.POST)
	public ResponseEntity<GeneralResponseDTO> addProductToLesson(@RequestBody ShopListAddDTO shopListAddDTO) {
		GeneralResponseDTO generalResponseDTO =teacherService.addProductToLesson(shopListAddDTO);
		return new ResponseEntity<GeneralResponseDTO>(generalResponseDTO,  HttpStatus.OK);
	     
	  }
	
	@RequestMapping(path="/addShopList",method=RequestMethod.POST)
	public ResponseEntity<GeneralResponseDTO> addShopList(@RequestBody LessonDTO lessonDTO) {
		GeneralResponseDTO generalResponseDTO =teacherService.addShopList(lessonDTO);
		return new ResponseEntity<GeneralResponseDTO>(generalResponseDTO,  HttpStatus.OK);
	     
	  }
	
	@RequestMapping(path="/findProducts",method=RequestMethod.POST)
	public ResponseEntity<ProductSearchDTO> findProducts(@RequestBody String name) {
		ProductSearchDTO product = teacherService.findProducts(name);
		return new ResponseEntity<ProductSearchDTO>(product,  HttpStatus.OK);
	     
	  }
	
	@GetMapping("/getProducts/{lessonId}")
	public ResponseEntity<LessonDTO> getProducts(@PathVariable(value = "lessonId") Long lessonId) {
		LessonDTO response = teacherService.getProductsToLesson(lessonId);
		return new ResponseEntity<LessonDTO>(response ,  HttpStatus.OK);
	}
	
	
}
