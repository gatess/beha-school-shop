package com.beha.controller.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.beha.dto.AddressDTO;
import com.beha.dto.AddressRequestDTO;
import com.beha.dto.AddressUpdateRequestDTO;
import com.beha.dto.BaseResponseWithIdDTO;
import com.beha.dto.CustomerDTO;
import com.beha.dto.CustomerResponseDTO;
import com.beha.dto.CustomerUpdateRequestDTO;
import com.beha.dto.GeneralResponseDTO;
import com.beha.dto.MessageDTO;
import com.beha.dto.MessageRequestDTO;
import com.beha.dto.MessageResponseDTO;
import com.beha.dto.OrderRequestDTO;
import com.beha.dto.OrderResponseDTO;
import com.beha.exceptions.DataNotFoundException;
import com.beha.service.CustomerService;
import com.beha.service.MailService;
import com.beha.service.OrderService;

@RestController
@RequestMapping(path="/customer")
public class CustomerController {
	

	@Autowired
	CustomerService customerService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	MailService mailService;
	
//	@RequestMapping(path="/signIn",method=RequestMethod.POST)
//	public ResponseEntity<GeneralResponseDTO> signIn(@RequestBody CustomerSignInDTO customerSignInDTO) {
//		GeneralResponseDTO generalResponseDTO = customerService.signIn(customerSignInDTO);
//		return new ResponseEntity<GeneralResponseDTO>(generalResponseDTO ,  HttpStatus.OK);
//	  }
	
	@RequestMapping(path="/getCustomers", method = RequestMethod.GET)
	public ResponseEntity<List<CustomerDTO>> getCustomers(){
		List<CustomerDTO> customerDTOs = customerService.getAllCustomers();
		return new ResponseEntity<List<CustomerDTO>>(customerDTOs ,  HttpStatus.OK);
	}
	
	
	
	@RequestMapping(path="/add",method=RequestMethod.POST)
	public ResponseEntity<GeneralResponseDTO> addCustomer(@RequestBody CustomerDTO customerDTO) {
		GeneralResponseDTO generalResponseDTO = customerService.addCustomer(customerDTO);
		return new ResponseEntity<GeneralResponseDTO>(generalResponseDTO ,  HttpStatus.OK);	     
	  }
	
	@RequestMapping(path="/add-order",method=RequestMethod.POST)
	public ResponseEntity<GeneralResponseDTO> addOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
		GeneralResponseDTO generalResponseDTO = orderService.addOrder(orderRequestDTO);
	    return new ResponseEntity<GeneralResponseDTO>(generalResponseDTO,  HttpStatus.OK);
	     
	  }
	
	@RequestMapping(path="/add-order-with-form",method=RequestMethod.POST)
	public ResponseEntity<GeneralResponseDTO> addOrderWithForm(@RequestBody OrderRequestDTO orderRequestDTO) {
		GeneralResponseDTO generalResponseDTO = orderService.addOrderWithCheckoutForm(orderRequestDTO);
	    return new ResponseEntity<GeneralResponseDTO>(generalResponseDTO,  HttpStatus.OK);
	     
	  }
	
	@RequestMapping(path="/result-order-with-form",method=RequestMethod.POST)
	public ResponseEntity<GeneralResponseDTO> resultOrderWithForm(@RequestBody String token) {
		GeneralResponseDTO generalResponseDTO = orderService.resultPaymentCheckoutFrom(token);
	    return new ResponseEntity<GeneralResponseDTO>(generalResponseDTO,  HttpStatus.OK);
	     
	  }
	
	@GetMapping("/getCustomer/{userId}")
	public ResponseEntity<CustomerResponseDTO> getCustomer(@PathVariable(value = "userId") Long userId) {
		CustomerResponseDTO customerResponseDTO = customerService.getCustomer(userId);
		return new ResponseEntity<CustomerResponseDTO>(customerResponseDTO,  HttpStatus.OK);
	}
	
	
	

	@GetMapping("/deleteAddress/{addressId}")
	public ResponseEntity<GeneralResponseDTO> deleteAddress(@PathVariable(value = "addressId") Long addressId) throws DataNotFoundException{
		GeneralResponseDTO generalResponseDTO = customerService.deleteAddress(addressId);
		return new ResponseEntity<GeneralResponseDTO>(generalResponseDTO,  HttpStatus.OK);
	}
	
	@GetMapping("/getAddresses/{userId}")
	public ResponseEntity<List<AddressDTO>> getAddresses(@PathVariable(value = "userId") Long userId) throws DataNotFoundException{
		List<AddressDTO> addressDTOs = customerService.getAddress(userId);
		return new ResponseEntity<List<AddressDTO>>(addressDTOs ,  HttpStatus.OK);
	}
	
	@GetMapping("/getOrders/{userId}")
	public ResponseEntity<List<OrderResponseDTO>> getOrders(@PathVariable(value = "userId") Long userId) {
		List<OrderResponseDTO> orderResponseDTOs = orderService.getOrders(userId);
		return new ResponseEntity<List<OrderResponseDTO>>(orderResponseDTOs ,  HttpStatus.OK);
	}
	
	@RequestMapping(path="/add-address",method=RequestMethod.POST)
	public ResponseEntity<BaseResponseWithIdDTO> addAddress(@RequestBody AddressRequestDTO addressRequestDTO) {
		BaseResponseWithIdDTO response = customerService.addAddress(addressRequestDTO);
	    return new ResponseEntity<BaseResponseWithIdDTO>(response,  HttpStatus.OK);
	     
	  }
	
	@RequestMapping(path="/update-address",method=RequestMethod.POST)
	public ResponseEntity<GeneralResponseDTO> updateAddress(@RequestBody AddressUpdateRequestDTO addressUpdateRequestDTO) {
		GeneralResponseDTO generalResponseDTO = customerService.updateAddress(addressUpdateRequestDTO);
	    return new ResponseEntity<GeneralResponseDTO>(generalResponseDTO,  HttpStatus.OK);
	     
	  }
	
	@RequestMapping(path="/update-customer",method=RequestMethod.POST)
	public ResponseEntity<GeneralResponseDTO> updateCustomer(@RequestBody CustomerUpdateRequestDTO customerUpdateRequestDTO) {
		GeneralResponseDTO generalResponseDTO = customerService.updateCustomer(customerUpdateRequestDTO);
	    return new ResponseEntity<GeneralResponseDTO>(generalResponseDTO,  HttpStatus.OK);
	     
	  }
	
	@RequestMapping(path="/sendmessage", method = RequestMethod.GET)
	public ResponseEntity<MessageResponseDTO> sendMessage() {
		RestTemplate restTemplate = new RestTemplate();
		MessageRequestDTO request = new MessageRequestDTO();
		request.setGate(0L);
		request.setPassword("tr16kp262");
		request.setSDate("");
		request.setUserName("behastore");
		request.setVPeriod("48");
		MessageDTO message = new MessageDTO();
		message.setSender("BEHA A.S.");
		message.setText("beha deneme mesaj java");
		message.setUtf8("1");
		List<String> gsm = new ArrayList<>();
		gsm.add("905422460019");
		message.setGsm(gsm);
		request.setMessage(message);
		
		MessageResponseDTO response = restTemplate.postForObject("http://s.innomobil.com/api/v1/sendsms", request, MessageResponseDTO.class);
		return new ResponseEntity<MessageResponseDTO>(response ,  HttpStatus.OK);
		
	}

}
