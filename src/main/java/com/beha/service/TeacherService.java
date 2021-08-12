package com.beha.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beha.dao.model.Lesson;
import com.beha.dao.model.Product;
import com.beha.dao.model.School;
import com.beha.dao.model.ShopList;
import com.beha.dao.model.SpecialPrice;
import com.beha.dao.model.Teacher;
import com.beha.dao.repository.LessonRepository;
import com.beha.dao.repository.ProductRepository;
import com.beha.dao.repository.ShopListRepository;
import com.beha.dao.repository.SpecialPriceRepository;
import com.beha.dao.repository.TeacherRepository;
import com.beha.dao.repository.UserRepository;
import com.beha.dto.EmailPasswordRequestDTO;
import com.beha.dto.GeneralResponseDTO;
import com.beha.dto.LessonDTO;
import com.beha.dto.LoginResponseDTO;
import com.beha.dto.ProductDTO;
import com.beha.dto.ProductSearchDTO;
import com.beha.dto.ShopListAddDTO;
import com.beha.dto.ShopListDTO;
import com.beha.exceptions.UserNotFoundException;
import com.beha.mapper.DtoMapper;

@Service
public class TeacherService {
	private TeacherRepository teacherRepository;
	private UserRepository userRepository;
	private ShopListRepository shopListRepository;
	private LessonRepository lessonRepository;
	private ProductRepository productRepository;
	private SpecialPriceRepository specialPriceRepository;
	private DtoMapper dtoMapper = DtoMapper.INSTANCE;

	@Autowired
	public TeacherService(TeacherRepository teacherRepository, ShopListRepository shopListRepository, 
			LessonRepository lessonRepository, ProductRepository productRepository, SpecialPriceRepository specialPriceRepository,
			DtoMapper dtoMapper) {
		super();
		this.dtoMapper = dtoMapper;
		this.teacherRepository = teacherRepository;
		this.shopListRepository = shopListRepository;
		this.lessonRepository = lessonRepository;
		this.productRepository = productRepository;
		this.specialPriceRepository = specialPriceRepository;
		this.userRepository = userRepository;
	}

	@Transactional
	public GeneralResponseDTO addShopList(LessonDTO lessonDTO) {
		GeneralResponseDTO generalResponseDTO = new GeneralResponseDTO();
		List<ShopList> shopList = dtoMapper.toShopListList(lessonDTO.getShopLists());
		Lesson lesson = lessonRepository.findById(lessonDTO.getId()).orElse(null);
		shopListRepository.deleteByLessonId(lesson.getId());
		if(shopList!=null) {
			lesson.setShopLists(shopList);
			lessonRepository.save(lesson);
			generalResponseDTO.setMessage("Başarılı");
			generalResponseDTO.setStatus("s");
		} else {
			generalResponseDTO.setStatus("f");
			generalResponseDTO.setMessage("Eklenemedi");
		}
		return generalResponseDTO;
	}
	
	public LessonDTO getProductsToLesson(Long lessonId) {
		LessonDTO lessonDto = new LessonDTO();
		Lesson lesson = lessonRepository.findById(lessonId).orElse(null);
		
		List<ShopList> shopLists = shopListRepository.findByLessonId(lessonId);
		List<ShopListDTO> shopListsDto = dtoMapper.toShopListDTOList(shopLists);
		if(shopLists!=null) {
			lessonDto.setShopLists(shopListsDto);
			lessonDto.setId(lesson.getId());
			lessonDto.setName(lesson.getName());
		}
		else {

		}
		
		return lessonDto;
		
	}
	
	public ProductSearchDTO findProducts(String name) {
		ProductSearchDTO productSearchDTO = new ProductSearchDTO();
		List<Product> productList =productRepository.searchByNameLike(name);
		//List<Product> productList2 =productRepository.findByNameContainingIgnoreCase(name);
		//List<Product> productList3 =productRepository.findByNameLike(name);
		//List<Product> productList4 =productRepository.findByNameContaining(name);
		if(productList.isEmpty()) {
			productSearchDTO.setSuccess(false);
			productSearchDTO.setMessage("Ürün bulunamadı");
		} else {
			List<ProductDTO> productDtoList =dtoMapper.toProductDTOList(productList);
			productSearchDTO.setProductList(productDtoList);
			productSearchDTO.setSuccess(true);
			productSearchDTO.setMessage("Ürünler başarıyla getirildi");
		}
			return productSearchDTO;
	}
	
	public LoginResponseDTO teacherLogin(EmailPasswordRequestDTO emailPasswordRequestDTO) {
		LoginResponseDTO loginResponseDto = new LoginResponseDTO();
		String passwordUser = emailPasswordRequestDTO.getPassword();

		Teacher user = teacherRepository.findByEmail(emailPasswordRequestDTO.getEmail());
		
		if (user == null) {
			loginResponseDto.setErrorMessage("Kullanıcı bulunamadı.");
			loginResponseDto.setStatus("f");
			throw new UserNotFoundException("Kullanıcı bulunamadı.");
		}

		if (passwordUser.equals(user.getPassword())) {
			List<Lesson> lessons = user.getLessons();
			List<LessonDTO> lessonDtoList = new ArrayList<LessonDTO>();
			for(Lesson lesson:lessons) {
				LessonDTO newLessonDto= new LessonDTO();
				newLessonDto.setGradeName(lesson.getGrade().getName());
				newLessonDto.setId(lesson.getId());
				newLessonDto.setName(lesson.getName());
				newLessonDto.setShopLists(dtoMapper.toShopListDTOList(lesson.getShopLists()));
				newLessonDto.setTeacher(dtoMapper.toTeacherDTO(lesson.getTeacher()));
				lessonDtoList.add(newLessonDto);
			}
			
			
			loginResponseDto.setLesson(lessonDtoList);
			loginResponseDto.setStatus("s");
		} else {
			loginResponseDto.setStatus("f");
			//loginResponseDto.setErrorMessage("Şifre hatalı");
		}
		return loginResponseDto;
	}
	
	//ShopList'e Ürün Ekleme
	public GeneralResponseDTO addProductToLesson(ShopListAddDTO shopListAddDto) {
		GeneralResponseDTO generalResponseDTO = new GeneralResponseDTO();
			ShopList shopList = new ShopList();
			//Gelen lessonId alınır.
			Long lessonId = shopListAddDto.getLessonId();
			//Lesson ların içinde lesson bulunur.
			Lesson lesson = lessonRepository.findById(lessonId).orElse(null);
			//Lesson'a göre okul çağırılır.
			School school = lesson.getGrade().getSchool();		
			//Ürün ve okula göre özel fiyat olup-olmadığı kontrol edilir.
			Product product = productRepository.findByBarcode(shopListAddDto.getBarcode());
			if(product!=null) {
			SpecialPrice specialPrice = specialPriceRepository.findByProductAndSchoolAndStatus(product, school,true);
			//Ürünün normal fiyatı çağırılır.
			Double price;
			//Eğer özel fiyat yoksa normal fiyat yazdırılır.
			if(specialPrice==null) {
				price = product.getPrice();
			} else {
				price = specialPrice.getPrice();
			}
			shopList.setLesson(lesson);
			shopList.setProduct(product);
			shopList.setPrice(price);
			//Derse ait ShopList'te ürünün olup-olmadığı kontrol edilir.
			ShopList shopList2 = shopListRepository.findByProductAndLesson(product, lesson);
			//Ürün yoksa yeni kayıt eklenir.
			if(shopList2==null) {
			shopList.setQuantity(shopListAddDto.getQuantity());
			shopListRepository.save(shopList);
			} else {
				
				shopList.setQuantity(shopListAddDto.getQuantity()+shopList2.getQuantity());
				shopList.setId(shopList2.getId());
				shopListRepository.save(shopList);
				
			}
			generalResponseDTO.setMessage("Ürün başarıyla eklendi.");
			generalResponseDTO.setStatus("s");
			
			
			}else {
				generalResponseDTO.setMessage("Ürün bulunamadı.");
				generalResponseDTO.setStatus("f");
			}
			
		
		return generalResponseDTO;
	}
}
