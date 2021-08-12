package com.beha.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beha.dao.model.Customer;
import com.beha.dao.model.Grade;
import com.beha.dao.model.Lesson;
import com.beha.dao.model.Order;
import com.beha.dao.model.School;
import com.beha.dao.model.SchoolUser;
import com.beha.dao.model.Teacher;
import com.beha.dao.repository.CustomerRepository;
import com.beha.dao.repository.GradeRepository;
import com.beha.dao.repository.LessonRepository;
import com.beha.dao.repository.OrderRepository;
import com.beha.dao.repository.ProductRepository;
import com.beha.dao.repository.SchoolRepository;
import com.beha.dao.repository.SchoolUserRepository;
import com.beha.dao.repository.TeacherRepository;
import com.beha.dto.AddLessonDTO;
import com.beha.dto.AddLessonResponseDTO;
import com.beha.dto.AddTeacherDTO;
import com.beha.dto.AddTeacherResponseDTO;
import com.beha.dto.BaseResponseWithIdDTO;
import com.beha.dto.EmailPasswordRequestDTO;
import com.beha.dto.GradeDTO;
import com.beha.dto.GradeLessonDTO;
import com.beha.dto.OrderDashboardDetail;
import com.beha.dto.SchoolAdminDashboardDTO;
import com.beha.dto.TeacherDTO;
import com.beha.dto.UpdateTeacherDTO;
import com.beha.dto.UpdateTeacherResponseDTO;
import com.beha.exceptions.UserNotFoundException;
import com.beha.mapper.DtoMapper;
import com.beha.util.DateUtil;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class SchoolAdminService {
	private CustomerRepository customerRepository;
	private ProductRepository productRepository;
	private SchoolUserRepository schoolUserRepository;
	private GradeRepository gradeRepository;
	private SchoolRepository schoolRepository;
	private OrderRepository orderRepository;
	private LessonRepository lessonRepository;
	private DtoMapper dtoMapper = DtoMapper.INSTANCE;
	private TeacherRepository teacherRepository;
	
	public List<GradeDTO> getLessons(long schoolId) {
		
		School school = schoolRepository.findById(schoolId).orElse(null);
		List<GradeDTO> gradeList = new ArrayList<GradeDTO>();
		gradeList=dtoMapper.toGradeDTOList(school.getGrades());
		return gradeList;
	}
	
	public UpdateTeacherResponseDTO updateTeacher(UpdateTeacherDTO changeTeacherDTO) {
		UpdateTeacherResponseDTO updateTeacherResponseDTO = new UpdateTeacherResponseDTO();
		Teacher teacher = teacherRepository.findById(changeTeacherDTO.getTeacherId()).orElse(null);
		Lesson lesson = lessonRepository.findById(changeTeacherDTO.getLessonId()).orElse(null);
		lesson.setTeacher(teacher);
		lessonRepository.save(lesson);
		updateTeacherResponseDTO.setLessonId(lesson.getId());
		updateTeacherResponseDTO.setLessonName(lesson.getName());
		updateTeacherResponseDTO.setMessage("Ders/Öğretmen Başarıyla Güncellendi.");
		updateTeacherResponseDTO.setStatus("s");
		updateTeacherResponseDTO.setTeacherId(teacher.getId());
		updateTeacherResponseDTO.setTeacherName(teacher.getName());
		
		return updateTeacherResponseDTO;
	}
	
	public AddLessonResponseDTO addLesson(AddLessonDTO addLessonDTO) {
		AddLessonResponseDTO addLessonResponseDTO = new AddLessonResponseDTO();
		Teacher teacher = teacherRepository.findById(addLessonDTO.getTeacherId()).orElse(null);
		School school = schoolRepository.findById(addLessonDTO.getSchoolId()).orElse(null);
		Grade grade = gradeRepository.findByNameAndSchool(addLessonDTO.getGradeName(),school);
		Lesson newLesson = new Lesson();
		newLesson.setName(addLessonDTO.getLessonName());
		List<Lesson> lessonList = new ArrayList<Lesson>();
		lessonList.add(newLesson);
		GradeLessonDTO gradeLessonDTO = new GradeLessonDTO();
		gradeLessonDTO.setTeacherId(teacher.getId());
		gradeLessonDTO.setTeacherName(teacher.getName());
		if(grade==null) { 
			Grade newGrade = new Grade();
			newGrade.setName(addLessonDTO.getGradeName());
			newGrade.setSchool(school);
			newGrade.setLessons(lessonList);
			newLesson.setGrade(newGrade);
			newLesson.setTeacher(teacher);
			Grade addedGrade = gradeRepository.save(newGrade);//lesson altında olduğu için burada ekliyor.
			Lesson addedLesson = lessonRepository.save(newLesson);
			
			gradeLessonDTO.setGradeId(addedGrade.getId());
			gradeLessonDTO.setGradeName(addedGrade.getName());
			gradeLessonDTO.setLessonId(addedLesson.getId());
			gradeLessonDTO.setLessonName(addedLesson.getName());
			addLessonResponseDTO.setMessage("Ders ve Sınıf Eklendi.");
			addLessonResponseDTO.setStatus("s");

		} else {
			Lesson lesson = lessonRepository.findByNameAndGrade(addLessonDTO.getLessonName(), grade);
			if(lesson==null) {
				newLesson.setGrade(grade);
				newLesson.setTeacher(teacher);
				lessonRepository.save(newLesson);
				Lesson addedLesson = lessonRepository.save(newLesson);
				gradeLessonDTO.setGradeId(grade.getId());
				gradeLessonDTO.setGradeName(grade.getName());
				gradeLessonDTO.setLessonId(addedLesson.getId());
				gradeLessonDTO.setLessonName(addedLesson.getName());
				addLessonResponseDTO.setMessage("Sınıf sistemde mevcut! Ders eklendi.");
				addLessonResponseDTO.setStatus("s");
			} else {
				addLessonResponseDTO.setMessage("Ders sistemde mevcut!");
				addLessonResponseDTO.setStatus("f");
			}

		}
		addLessonResponseDTO.setGradeLessonDTO(gradeLessonDTO);
		return addLessonResponseDTO;
	}
	
	public AddTeacherResponseDTO deleteTeacher(long teacherId) {
		AddTeacherResponseDTO addTeacherResponseDTO = new AddTeacherResponseDTO();
		Teacher teacherCheck = teacherRepository.findById(teacherId).orElse(null);
		if(teacherCheck==null) {
			addTeacherResponseDTO.setStatus("f");
			addTeacherResponseDTO.setMessage("Öğretmen bulunamadı.");
		}
		else {
			if(teacherCheck.getLessons().size()==0) {
				teacherRepository.delete(teacherCheck);
				addTeacherResponseDTO.setStatus("s");
				addTeacherResponseDTO.setMessage("Başarıyla silindi.");
				addTeacherResponseDTO.setTeacherDTO(dtoMapper.toTeacherDTO(teacherCheck));	}
			else {
				addTeacherResponseDTO.setStatus("f");
				addTeacherResponseDTO.setMessage("Öğretmene kayıtlı ders olduğu için silinemez.");
			}
		}
		
		return addTeacherResponseDTO;
	}
	
	public BaseResponseWithIdDTO deleteLesson(long lessonId) {
		BaseResponseWithIdDTO response = new BaseResponseWithIdDTO();
		Lesson lessonCheck = lessonRepository.findById(lessonId).orElse(null);
		if(lessonCheck==null) {
			response.setStatus("f");
			response.setMessage("Ders bulunamadı.");
		}
		else {
			lessonRepository.delete(lessonCheck);
			response.setStatus("s");
			response.setMessage("Başarıyla silindi.");
			response.setId(lessonId);
		}
		
		return response;
	}
	
	public AddTeacherResponseDTO addTeacher(AddTeacherDTO addTeacherDTO) {
		AddTeacherResponseDTO addTeacherResponseDTO = new AddTeacherResponseDTO();
		Teacher teacherCheck = teacherRepository.findByEmail(addTeacherDTO.getEmail());
		School school = schoolRepository.findById(addTeacherDTO.getSchoolId()).orElse(null);
		if(teacherCheck!=null) {
			addTeacherResponseDTO.setStatus("f");
			addTeacherResponseDTO.setMessage("kullanıcı kayıtlı");
		}
		else {
		Teacher teacher = new Teacher();
		teacher.setEmail(addTeacherDTO.getEmail());
		teacher.setName(addTeacherDTO.getName());
		teacher.setPassword(addTeacherDTO.getPassword());
		teacher.setSchool(school);
		teacherRepository.save(teacher);
		addTeacherResponseDTO.setMessage("başarılı");
		addTeacherResponseDTO.setStatus("s");
		TeacherDTO teacherDTO = dtoMapper.toTeacherDTO(teacher);
		addTeacherResponseDTO.setTeacherDTO(teacherDTO); }
		return addTeacherResponseDTO;
	}
	
	public SchoolAdminDashboardDTO getDashboardDetail(EmailPasswordRequestDTO emailPasswordRequestDTO) {
		SchoolAdminDashboardDTO adminDashboardDTO = new SchoolAdminDashboardDTO();
		String passwordUser = emailPasswordRequestDTO.getPassword();

		SchoolUser schoolUser = schoolUserRepository.findByEmail(emailPasswordRequestDTO.getEmail());
		if (schoolUser == null) {
			adminDashboardDTO.setErrorMessage("Kullanıcı bulunamadı.");
			adminDashboardDTO.setStatus("f");
			throw new UserNotFoundException("Kullanıcı bulunamadı.");
		}
		else {
		if (passwordUser.equals(schoolUser.getPassword())) {
			adminDashboardDTO.setAdminId(schoolUser.getId());
			
			String userName = schoolUser.getName() != null ? schoolUser.getName() : "";
			String userSurname = schoolUser.getSurname() != null ? schoolUser.getName() : "";
			adminDashboardDTO.setAdminName(userName+" "+userSurname);
			School school = schoolUser.getSchool();
			adminDashboardDTO.setSchoolName(school.getName());
			
			List<Teacher> teacher = teacherRepository.findBySchool(school);
			adminDashboardDTO.setTeacherDto(dtoMapper.toTeacherDTOList(teacher));
			
			
			List<GradeDTO> gradeList = dtoMapper.toGradeDTOList(school.getGrades());
			List<GradeLessonDTO> gradeLessonDTOList = new ArrayList<GradeLessonDTO>();
			for(GradeDTO grade:gradeList) {
				int size=grade.getLessons().size();
				for(int i=0;i<size;i++) {
					
					GradeLessonDTO gradeLessonDTO = new GradeLessonDTO();
					gradeLessonDTO.setGradeId(grade.getId());
					gradeLessonDTO.setGradeName(grade.getName());
					gradeLessonDTO.setLessonId(grade.getLessons().get(i).getId());
					gradeLessonDTO.setTeacherId(grade.getLessons().get(i).getTeacher().getId());
					gradeLessonDTO.setTeacherName(grade.getLessons().get(i).getTeacher().getName());
					gradeLessonDTO.setLessonName(grade.getLessons().get(i).getName());
					gradeLessonDTOList.add(gradeLessonDTO);
				}
			}
			adminDashboardDTO.setGradeLessonDto(gradeLessonDTOList);
			
			long schoolId = school.getId();
			adminDashboardDTO.setSchoolId(schoolId);

			long newOrderQuantity;
			
			newOrderQuantity = schoolUserRepository.getNewOrdersCount(schoolId);
			adminDashboardDTO.setNewOrderQuantity(newOrderQuantity);
			
			long allOrderQuantity;
			allOrderQuantity = schoolUserRepository.getAllOrdersCount(schoolId);
			adminDashboardDTO.setAllOrderQuantity(allOrderQuantity);

			long allCustomerQuantity;
			allCustomerQuantity = customerRepository.getAllCustomerCountForSchoolUser(schoolId);
			adminDashboardDTO.setAllCustomerQuantity(allCustomerQuantity);

			Double ordersSum;
			ordersSum = schoolUserRepository.getCompletedOrdersSum(schoolId);
			adminDashboardDTO.setOrdersSum(ordersSum);

			Double comissionSum;
			comissionSum = schoolUserRepository.getCompletedOrdersComissionAmount(school.getId());
			adminDashboardDTO.setComissionSum(comissionSum);

			// LocalDate date = LocalDate.now();
			List<OrderDashboardDetail> orderDashboardDetailList = new ArrayList<OrderDashboardDetail>();
			List<Integer> customerQuantityList = new ArrayList<Integer>();

			List<Customer> lastWeekCustomers = customerRepository.getLastWeekCustomers();
			List<Order> lastWeekOrders = schoolUserRepository.getLastWeekOrders(schoolId);
			for (int i = 0; i < 7; i++) {
				OrderDashboardDetail orderDashboardDetail = new OrderDashboardDetail();
				String date = LocalDate.now().minusDays(i).toString();
				Long customerQuantity = lastWeekCustomers.stream().filter(
						c -> DateUtil.convertToLocalDateViaInstant(c.getCreatedDate()).toString().startsWith(date))
						.count();
				orderDashboardDetail.setDate(date);
				Double dailyTotal = (double) 0;
				Double dailyComission = (double) 0;
				long orderQuantity = 0;
				List<Order> orders = lastWeekOrders.stream().filter(
						o -> DateUtil.convertToLocalDateViaInstant(o.getCreatedDate()).toString().startsWith(date))
						.collect(Collectors.toList());
				for (Order order : orders) {
					dailyComission = dailyComission + order.getCommissionAmount();
					dailyTotal = dailyTotal + order.getTotal();
					orderQuantity = orderQuantity + 1;
				}
				orderDashboardDetail.setComissionTotal(dailyComission);
				orderDashboardDetail.setOrderQuantity(orderQuantity);
				orderDashboardDetail.setOrderTotal(dailyTotal);
				orderDashboardDetailList.add(orderDashboardDetail);
				customerQuantityList.add(customerQuantity.intValue());
			}

			adminDashboardDTO.setOrderDashboardDetail(orderDashboardDetailList);
			adminDashboardDTO.setCustomerQuantity(customerQuantityList);
			System.out.println("new order:" + newOrderQuantity + " all order:" + allOrderQuantity + "all customer:"
					+ allCustomerQuantity  + "ordersSum : " + ordersSum + "date:");

			adminDashboardDTO.setStatus("s");
		} else {
			adminDashboardDTO.setStatus("f");
			adminDashboardDTO.setErrorMessage("Şifre hatalı");
		}
		}
		return adminDashboardDTO;
	}

}
