package com.beha.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beha.dao.model.Customer;
import com.beha.dao.model.Grade;
import com.beha.dao.model.Lesson;
import com.beha.dao.repository.GradeRepository;
import com.beha.dto.GradeDTO;
import com.beha.dto.LessonDTO;
import com.beha.mapper.DtoMapper;

@Service
public class GradeService {
	private GradeRepository gradeRepository;
	private DtoMapper dtoMapper = DtoMapper.INSTANCE;

	@Autowired
	public GradeService(GradeRepository gradeRepository, DtoMapper dtoMapper) {
		super();
		this.gradeRepository = gradeRepository;
		this.dtoMapper = dtoMapper;
	}

	public List<GradeDTO> getGradesBySchool(Long id) {

		List<Grade> gradeList = gradeRepository.findBySchoolId(id);
		return dtoMapper.toGradeDTOList(gradeList);
	}

	@Transactional
	public List<LessonDTO> getShopListsByGradeId(Long id) throws Exception {
		Grade grade = gradeRepository.findById(id).orElse(null);
		if (grade == null) {
			throw new Exception("Grade id ye bağlı kayıt bulunamadı.");
		}
		List<Lesson> lessonList = grade.getLessons();
		List<LessonDTO> lessonDTOs = dtoMapper.toLessonDTOList(lessonList);
		for(Lesson lesson : lessonList) {
			for(LessonDTO lessonDto : lessonDTOs) {
				lessonDto.setGradeName(lesson.getGrade().getName());
	}}
		return lessonDTOs;
}
}