package com.beha.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beha.dao.model.Address;
import com.beha.dao.model.Customer;
import com.beha.dao.model.User;
import com.beha.dao.repository.AddressRepository;
import com.beha.dao.repository.CustomerRepository;
import com.beha.dao.repository.UserRepository;
import com.beha.dto.AddressDTO;
import com.beha.dto.AddressRequestDTO;
import com.beha.dto.AddressUpdateRequestDTO;
import com.beha.dto.BaseResponseWithIdDTO;
import com.beha.dto.CustomerDTO;
import com.beha.dto.CustomerInformationDTO;
import com.beha.dto.CustomerResponseDTO;
import com.beha.dto.CustomerUpdateRequestDTO;
import com.beha.dto.GeneralResponseDTO;
import com.beha.exceptions.DataNotFoundException;
import com.beha.mapper.DtoMapper;

@Service
public class CustomerService {
	private CustomerRepository customerRepository;
	private AddressRepository addressRepository;
	private UserRepository userRepository;
	private DtoMapper dtoMapper = DtoMapper.INSTANCE;

	@Autowired
	public CustomerService(UserRepository userRepository ,CustomerRepository customerRepository, 
			DtoMapper dtoMapper,AddressRepository addressRepository ){
		super();
		this.customerRepository = customerRepository;
		this.userRepository = userRepository;
		this.addressRepository = addressRepository;
		this.dtoMapper = dtoMapper;
	}
	
//	public GeneralResponseDTO signIn(CustomerSignInDTO customerSignInDTO) {
//		String email= customerSignInDTO.getEmail();
//		String password = customerSignInDTO.getPassword();
//		boolean sonuc = customerRepository.existsByEmailAndPassword(email, password);
//		GeneralResponseDTO generalResponseDTO = new GeneralResponseDTO();
//		if(sonuc==true) {
//		generalResponseDTO.setMessage("Giriş Başarılı");
//		generalResponseDTO.setStatus("s");}
//		else {
//			boolean sonuc2 = customerRepository.existsByEmail(email);
//			if(sonuc2==true) {
//				generalResponseDTO.setMessage("Şifre Hatalı");
//				generalResponseDTO.setStatus("f");
//			} else {
//				generalResponseDTO.setMessage("E-posta Kayıtlı Değil");
//				generalResponseDTO.setStatus("f");
//			}
//			
//		}
//		return generalResponseDTO;
//	}
	
	public List<CustomerDTO> getAllCustomers(){
		List<Customer> customerList = customerRepository.findAll();
		return dtoMapper.toCustomerDTOList(customerList);
	}
	
	public GeneralResponseDTO addCustomer(CustomerDTO customerDTO){
		GeneralResponseDTO generalResponseDTO = new GeneralResponseDTO();
		Customer customer = dtoMapper.toCustomer(customerDTO);
		boolean count = customerRepository.existsByEmail(customer.getEmail());
		if(count==true) {
			//throw new UserAlreadyExistsException("User already exists. Email : "+customer.getEmail()); 
			generalResponseDTO.setMessage("Kullanıcı kayıtlı. E-posta : "+customer.getEmail());
			generalResponseDTO.setStatus("f");
		} else {
			generalResponseDTO.setMessage("Kullanıcı başarıyla eklendi");
			generalResponseDTO.setStatus("s");
			customerRepository.save(customer);
		}
		
		return generalResponseDTO;
		
	}
	
	public BaseResponseWithIdDTO addAddress(AddressRequestDTO addressRequestDTO) {
		BaseResponseWithIdDTO response = new BaseResponseWithIdDTO();
		User user = userRepository.findById(addressRequestDTO.getUserId()).orElse(null);
		//Customer customer = customerRepository.findById(addressRequestDTO.getUserId()).orElse(null);
		if(user==null) {
			response.setStatus("f");
			response.setMessage("Müşteri bulunamadı");
		} else {
		Customer customer = customerRepository.findByUserId(addressRequestDTO.getUserId());
		Address address = new Address();
		address.setAddressName(addressRequestDTO.getAddressName());
		address.setAddressDescription(addressRequestDTO.getAddressDescription());
		address.setProvince(addressRequestDTO.getProvince());
		address.setTelephone(addressRequestDTO.getTelephone());
		address.setCounty(addressRequestDTO.getCounty());
		address.setZipCode(addressRequestDTO.getZipCode());
		address.setCustomer(customer);
		Address addressAdded = addressRepository.save(address);
		response.setId(addressAdded.getId());
		response.setStatus("s");
		response.setMessage("Adres başarıyla kaydedildi");
		}
		return response;
	}
	
	public List<AddressDTO> getAddress(Long userId) throws DataNotFoundException {
		Customer customer = customerRepository.findByUserId(userId);
		if(customer == null) {
			throw new DataNotFoundException(userId+" ile müşteri bulunamadı");
		}		
		//List<Address> addressList = addressRepository.findByCustomerId(customer.getId());
		return dtoMapper.toAddressDTOList(customer.getAddress());
	}
	
	public GeneralResponseDTO updateAddress(AddressUpdateRequestDTO addressUpdateRequestDTO) {
		GeneralResponseDTO generalResponseDTO = new GeneralResponseDTO();
		User user = userRepository.findById(addressUpdateRequestDTO.getUserId()).orElse(null);
		//Customer customer = customerRepository.findById(addressRequestDTO.getUserId()).orElse(null);
		if(user==null) {
			generalResponseDTO.setStatus("f");
			generalResponseDTO.setMessage("Müşteri bulunamadı");
		} else {
		//Customer customer = customerRepository.findByUserId(addressUpdateRequestDTO.getUserId());
		Address address = addressRepository.findById(addressUpdateRequestDTO.getId()).orElse(null);
		address.setAddressName(addressUpdateRequestDTO.getAddressName());
		address.setAddressDescription(addressUpdateRequestDTO.getAddressDescription());
		address.setProvince(addressUpdateRequestDTO.getProvince());
		address.setTelephone(addressUpdateRequestDTO.getTelephone());
		address.setCounty(addressUpdateRequestDTO.getCounty());
		address.setZipCode(addressUpdateRequestDTO.getZipCode());
		address.setId(addressUpdateRequestDTO.getId());
		//address.setCustomer(customer);
		addressRepository.save(address);
		generalResponseDTO.setStatus("s");
		generalResponseDTO.setMessage("Adres başarıyla kaydedildi");
		}
		return generalResponseDTO;
	}
	
	public GeneralResponseDTO deleteAddress(Long addressId) {
		GeneralResponseDTO generalResponseDTO = new GeneralResponseDTO();
		addressRepository.deleteById(addressId);
		generalResponseDTO.setMessage("Adres başarıyla silindi.");
		generalResponseDTO.setStatus("s");
		return generalResponseDTO;
		
	}
	public CustomerResponseDTO getCustomer(Long userId) {
		CustomerResponseDTO customerResponseDTO = new CustomerResponseDTO();
		Customer customer = customerRepository.findByUserId(userId);
		if(customer==null) {
			customerResponseDTO.setStatus("f");
			customerResponseDTO.setMessage("Kullanıcı bulunamadı.");
		} else {
			customerResponseDTO.setStatus("s");
			customerResponseDTO.setMessage("Başarıyla getirildi.");
			CustomerInformationDTO customerInformationDTO = new CustomerInformationDTO();
			customerInformationDTO.setEmail(customer.getEmail());
			customerInformationDTO.setName(customer.getName());
			customerInformationDTO.setSurname(customer.getSurname());
			customerResponseDTO.setCustomerInformationDTO(customerInformationDTO);
		}
		
		return customerResponseDTO;
	}
	
	public GeneralResponseDTO updateCustomer(CustomerUpdateRequestDTO customerUpdateRequestDTO) {
		GeneralResponseDTO generalResponseDTO = new GeneralResponseDTO();
		User user = userRepository.findById(customerUpdateRequestDTO.getUserId()).orElse(null);
		Customer customer = customerRepository.findByUserId(customerUpdateRequestDTO.getUserId());
		if(user==null) {
			generalResponseDTO.setStatus("f");
			generalResponseDTO.setMessage("Müşteri bulunamadı");
		} else {
		
		User userUpdated = new User();
		userUpdated.setEmail(customerUpdateRequestDTO.getEmail());
		userUpdated.setName(customerUpdateRequestDTO.getName());
		userUpdated.setSurName(customerUpdateRequestDTO.getSurname());
		userUpdated.setId(user.getId());
		userUpdated.setCustomer(customer);
		userUpdated.setPassword(user.getPassword());
		userUpdated.setRole(user.getRole());
		
		Customer customerUpdated = new Customer();
		customerUpdated.setEmail(customerUpdateRequestDTO.getEmail());
		customerUpdated.setName(customerUpdateRequestDTO.getName());
		customerUpdated.setSurname(customerUpdateRequestDTO.getSurname());
		customerUpdated.setId(customer.getId());
		customerUpdated.setUser(userUpdated);
		customerRepository.save(customerUpdated);
		userRepository.save(userUpdated);
		generalResponseDTO.setStatus("s");
		generalResponseDTO.setMessage("Başarıyla güncellendi.");
		}
		return generalResponseDTO;
	}
}
