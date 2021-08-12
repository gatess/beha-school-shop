package com.beha.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.beha.dao.model.Lesson;
import com.beha.dao.model.Product;
import com.beha.dao.model.ShopList;



public interface ShopListRepository extends JpaRepository<ShopList, Long>, JpaSpecificationExecutor<ShopList> {
	List<ShopList> findByLessonId(Long lessonId);
	ShopList findByProductAndLesson(Product product, Lesson lesson);
	long deleteByLessonId(Long lessonId);

}
