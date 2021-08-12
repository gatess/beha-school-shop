package com.beha.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beha.dao.model.ShopList;
import com.beha.dao.repository.ShopListRepository;
import com.beha.dto.ShopListDTO;
import com.beha.mapper.DtoMapper;

@Service
public class ShopListService {
	private ShopListRepository shopListRepository;
	private DtoMapper dtoMapper = DtoMapper.INSTANCE;
	
	@Autowired
	public ShopListService(ShopListRepository shopListRepository,DtoMapper dtoMapper) {
		super();
		this.dtoMapper=dtoMapper;
		this.shopListRepository=shopListRepository;		
	}
	
	public List<ShopListDTO> getShopListByLesson(Long id) {
		List<ShopList> shopList = shopListRepository.findByLessonId(id);
		return dtoMapper.toShopListDTOList(shopList);
		}
 
}
