package com.beha.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beha.dao.model.School;
import com.beha.dao.repository.SchoolRepository;
import com.beha.dto.SchoolDTO;
import com.beha.mapper.DtoMapper;

@Service
public class SchoolService {
	private SchoolRepository schoolRepository;
	private DtoMapper dtoMapper = DtoMapper.INSTANCE;
	
	@Autowired
	public SchoolService(SchoolRepository schoolRepository,DtoMapper dtoMapper) {
		super();
		this.schoolRepository = schoolRepository;
		this.dtoMapper = dtoMapper;
	}

	public List<SchoolDTO> getAllSchools(){
		//List<School> schoolList = schoolRepository.findAll();
		List<School> schoolList = schoolRepository.findByStatus(true);
		return dtoMapper.toSchoolDTOList(schoolList);
	}
	
	public List<SchoolDTO> getCampusesBySchool(String school) {
		List<School> schoolList = schoolRepository.findBySubdomainAndStatus(school, true);
		return dtoMapper.toSchoolDTOList(schoolList);
		
	}
}
