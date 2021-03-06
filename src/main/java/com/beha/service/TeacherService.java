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
			generalResponseDTO.setMessage("Ba??ar??l??");
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
			productSearchDTO.setMessage("??r??n bulunamad??");
		} else {
			List<ProductDTO> productDtoList =dtoMapper.toProductDTOList(productList);
			productSearchDTO.setProductList(productDtoList);
			productSearchDTO.setSuccess(true);
			productSearchDTO.setMessage("??r??nler ba??ar??yla getirildi");
		}
			return productSearchDTO;
	}
	
	public LoginResponseDTO teacherLogin(EmailPasswordRequestDTO emailPasswordRequestDTO) {
		LoginResponseDTO loginResponseDto = new LoginResponseDTO();
		String passwordUser = emailPasswordRequestDTO.getPassword();

		Teacher user = teacherRepository.findByEmail(emailPasswordRequestDTO.getEmail());
		
		if (user == null) {
			loginResponseDto.setErrorMessage("Kullan??c?? bulunamad??.");
			loginResponseDto.setStatus("f");
			throw new UserNotFoundException("Kullan??c?? bulunamad??.");
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
			//loginResponseDto.setErrorMessage("??ifre hatal??");
		}
		return loginResponseDto;
	}
	
	//ShopList'e ??r??n Ekleme
	public GeneralResponseDTO addProductToLesson(ShopListAddDTO shopListAddDto) {
		GeneralResponseDTO generalResponseDTO = new GeneralResponseDTO();
			ShopList shopList = new ShopList();
			//Gelen lessonId al??n??r.
			Long lessonId = shopListAddDto.getLessonId();
			//Lesson lar??n i??inde lesson bulunur.
			Lesson lesson = lessonRepository.findById(lessonId).orElse(null);
			//Lesson'a g??re okul ??a????r??l??r.
			School school = lesson.getGrade().getSchool();		
			//??r??n ve okula g??re ??zel fiyat olup-olmad?????? kontrol edilir.
			Product product = productRepository.findByBarcode(shopListAddDto.getBarcode());
			if(product!=null) {
			SpecialPrice specialPrice = specialPriceRepository.findByProductAndSchoolAndStatus(product, school,true);
			//??r??n??n normal fiyat?? ??a????r??l??r.
			Double price;
			//E??er ??zel fiyat yoksa normal fiyat yazd??r??l??r.
			if(specialPrice==null) {
				price = product.getPrice();
			} else {
				price = specialPrice.getPrice();
			}
			shopList.setLesson(lesson);
			shopList.setProduct(product);
			shopList.setPrice(price);
			//Derse ait ShopList'te ??r??n??n olup-olmad?????? kontrol edilir.
			ShopList shopList2 = shopListRepository.findByProductAndLesson(product, lesson);
			//??r??n yoksa yeni kay??t eklenir.
			if(shopList2==null) {
			shopList.setQuantity(shopListAddDto.getQuantity());
			shopListRepository.save(shopList);
			} else {
				
				shopList.setQuantity(shopListAddDto.getQuantity()+shopList2.getQuantity());
				shopList.setId(shopList2.getId());
				shopListRepository.save(shopList);
				
			}
			generalResponseDTO.setMessage("??r??n ba??ar??yla eklendi.");
			generalResponseDTO.setStatus("s");
			
			
			}else {
				generalResponseDTO.setMessage("??r??n bulunamad??.");
				generalResponseDTO.setStatus("f");
			}
			
		
		return generalResponseDTO;
	}
}
