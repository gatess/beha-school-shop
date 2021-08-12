package com.beha.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beha.dao.model.Customer;
import com.beha.dao.model.Role;
import com.beha.dao.model.Role;
import com.beha.dao.model.User;
import com.beha.dao.repository.CustomerRepository;
import com.beha.dao.repository.UserRepository;
import com.beha.dto.EmailPasswordRequestDTO;
import com.beha.dto.GeneralResponseDTO;
import com.beha.dto.UserDTO;
import com.beha.dto.UserInformationDTO;
import com.beha.dto.UserResponseDTO;
import com.beha.mapper.DtoMapper;

@Service
public class UserService {
	private UserRepository userRepository;
	private CustomerRepository customerRepository;
	private DtoMapper dtoMapper = DtoMapper.INSTANCE;
	
	@Autowired
	public UserService(CustomerRepository customerRepository, DtoMapper dtoMapper,UserRepository userRepository ){
		super();
		this.userRepository = userRepository;
		this.customerRepository = customerRepository;
		this.dtoMapper = dtoMapper;
	}
	
	public UserResponseDTO addUser(UserDTO userDTO){
		UserResponseDTO userResponseDTO = new UserResponseDTO();
		User user = dtoMapper.toUser(userDTO);
		String email = user.getEmail();
		User user2 = userRepository.findByEmail(email);
		boolean count = userRepository.existsByEmail(user.getEmail());
		if(count==true) {
			//throw new UserAlreadyExistsException("User already exists. Email : "+customer.getEmail()); 
			userResponseDTO.setMessage("Kullanıcı kayıtlı. E-posta : "+user.getEmail());
			userResponseDTO.setStatus("f");
		} else {
			user.setRole(Role.ROLE_CUSTOMER);
			User addedUser = userRepository.save(user);
			Customer customer = new Customer();
			customer.setName(addedUser.getName());
			customer.setSurname(addedUser.getSurName());
			customer.setEmail(addedUser.getEmail());
			customer.setUser(addedUser);
			customerRepository.save(customer);
			userResponseDTO.setMessage("Kullanıcı başarıyla eklendi");
			userResponseDTO.setStatus("s");
			UserInformationDTO userInformationDTO = new UserInformationDTO();
			userInformationDTO.setUserId(addedUser.getId());
			userInformationDTO.setUserName(addedUser.getName());
			userInformationDTO.setUserSurName(addedUser.getSurName());
			userResponseDTO.setUserInformationDTO(userInformationDTO);
			
		}
		
		return userResponseDTO;
		
	}
	
	public UserResponseDTO signIn(EmailPasswordRequestDTO emailPasswordRequestDTO) {
		String email= emailPasswordRequestDTO.getEmail();
		String password = emailPasswordRequestDTO.getPassword();
		User user = userRepository.findByEmail(email);
		boolean sonuc = userRepository.existsByEmailAndPassword(email, password);
		UserResponseDTO userResponseDTO = new UserResponseDTO();
		if(sonuc==true && user.getRole().getValue()==0) {
			//User user = userRepository.findByEmail(email);
			UserInformationDTO userInformationDTO = new UserInformationDTO();
			userInformationDTO.setUserId(user.getId());
			userInformationDTO.setUserName(user.getName());
			userInformationDTO.setUserSurName(user.getSurName());
			userResponseDTO.setUserInformationDTO(userInformationDTO);
			userResponseDTO.setMessage("Giriş Başarılı");
			userResponseDTO.setStatus("s");}
		else {
			boolean sonuc2 = customerRepository.existsByEmail(email);
			if(sonuc2==true && user.getRole().getValue()==0) {
				userResponseDTO.setMessage("Şifre Hatalı");
				userResponseDTO.setStatus("f");
			} else {
				userResponseDTO.setMessage("E-posta Kayıtlı Değil");
				userResponseDTO.setStatus("f");
			}
			
		}
		return userResponseDTO;
	}

}
