package com.beha.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.beha.dao.model.Address;
import com.beha.dao.model.Customer;
import com.beha.dao.model.Grade;
import com.beha.dao.model.Lesson;
import com.beha.dao.model.Order;
import com.beha.dao.model.OrderDetail;
import com.beha.dao.model.OrderHistory;
import com.beha.dao.model.Product;
import com.beha.dao.model.School;
import com.beha.dao.model.ShopList;
import com.beha.dao.model.Teacher;
import com.beha.dao.model.User;
import com.beha.dto.AddressDTO;
import com.beha.dto.CustomerDTO;
import com.beha.dto.EmailPasswordRequestDTO;
import com.beha.dto.GradeDTO;
import com.beha.dto.LessonDTO;
import com.beha.dto.OrderDTO;
import com.beha.dto.OrderDetailDTO;
import com.beha.dto.OrderHistoryDTO;
import com.beha.dto.OrderProductDetailDTO;
import com.beha.dto.ProductDTO;
import com.beha.dto.SchoolDTO;
import com.beha.dto.ShopListDTO;
import com.beha.dto.TeacherDTO;
import com.beha.dto.TeacherLessonDTO;
import com.beha.dto.UserDTO;

@Mapper(componentModel = "spring")
public interface DtoMapper {
	DtoMapper INSTANCE = Mappers.getMapper(DtoMapper.class);

	List<SchoolDTO> toSchoolDTOList(List<School> schoolList);
	
	List<OrderHistoryDTO> toOrderHistoryDTOList(List<OrderHistory> orderHistoryList);
	
	List<CustomerDTO> toCustomerDTOList(List<Customer> customerList);

	List<GradeDTO> toGradeDTOList(List<Grade> gradeList);

	List<ShopListDTO> toShopListDTOList(List<ShopList> shopList);
	
	List<ShopList> toShopListList(List<ShopListDTO> shopListDTO);

	ShopListDTO toShopListDTO(ShopList shopList);

	List<TeacherLessonDTO> toTeacherLessonDTOList(List<Lesson> lessons);
	
	
	List<LessonDTO> toLessonDTOList(List<Lesson> lessonList);
	
	List<TeacherDTO> toTeacherDTOList(List<Teacher> teacher);

	List<ProductDTO> toProductDTOList(List<Product> productList);
	
	List<AddressDTO> toAddressDTOList(List<Address> addressList);
	
	List<OrderDTO> toOrderDTOList(List<Order> orderList);
	
	List<OrderDetailDTO> toOrderDetailDTOList(List<OrderDetail> orderDetailList);
	
	List<OrderProductDetailDTO> toOrderProductDetailDTOList(List<OrderDetail> orderProductDetailList);
	
	CustomerDTO toCustomerDTO(Customer customer);
	
	Customer toCustomer(CustomerDTO customerDTO);
	
	ProductDTO toProductDTO(Product product);
	
	Product toProduct(ProductDTO productDTO);
	
	OrderDTO toOrderDTO(Order order);
	
	Order toOrder(OrderDTO OrderDTO);
	
	LessonDTO toLessonDTO(Lesson lesson);
	
	Lesson toLesson(LessonDTO LessonDTO);
	
	Address toAddress(AddressDTO addressDTO);
	
	AddressDTO toAddressDTO(Address address);

	Teacher toTeacher(EmailPasswordRequestDTO emailPasswordRequestDTO);
	
	Teacher toTeacher(TeacherDTO teacherDTO);
	
	TeacherDTO toTeacherDTO(Teacher teacher);
	
	UserDTO toUserDTO(User user);
	
	User toUser(UserDTO userDTO);
	
	Grade toGrade(GradeDTO gradeDTO);
	
	GradeDTO toGradeDTO(Grade grade);
	
	
}
