package com.beha.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.beha.dto.AdminDashboardDTO;
import com.beha.dto.AdminGetProductDTO;
import com.beha.dto.BaseResponseDTO;
import com.beha.dto.CustomerDTO;
import com.beha.dto.EmailPasswordRequestDTO;
import com.beha.dto.GetCustomerDTO;
import com.beha.dto.OrderHistoryAddDTO;
import com.beha.dto.OrderResponseDTO;
import com.beha.exceptions.DataNotFoundException;
import com.beha.service.AdminService;

@RestController
@RequestMapping(path="/admin")
public class AdminController {
	
	@Autowired
	AdminService adminService;
	
	@RequestMapping(path="/login",method=RequestMethod.POST)
	public ResponseEntity<AdminDashboardDTO> login(@RequestBody EmailPasswordRequestDTO emailPasswordRequestDTO) {
		AdminDashboardDTO adminDashboardDTO = adminService.getDashboardDetail(emailPasswordRequestDTO);
	    return new ResponseEntity<AdminDashboardDTO>(adminDashboardDTO,  HttpStatus.OK);
	     
	  }
	
	@RequestMapping(path="/getProducts", method = RequestMethod.GET)
	public ResponseEntity<List<AdminGetProductDTO>> getProducts(){
		List<AdminGetProductDTO> response = adminService.getProducts();
		return new ResponseEntity<List<AdminGetProductDTO>>(response ,  HttpStatus.OK);
	}
	
	@RequestMapping(path="/getOrders", method = RequestMethod.GET)
	public ResponseEntity<List<OrderResponseDTO>> getOrders(){
		List<OrderResponseDTO> orderResponseDTOs = adminService.getOrders();
		return new ResponseEntity<List<OrderResponseDTO>>(orderResponseDTOs ,  HttpStatus.OK);
	}
	
	@RequestMapping(path="/getCustomers", method = RequestMethod.GET)
	public ResponseEntity<List<GetCustomerDTO>> getCustomers(){
		List<GetCustomerDTO> getCustomerDTOs = adminService.getCustomers();
		return new ResponseEntity<List<GetCustomerDTO>>(getCustomerDTOs ,  HttpStatus.OK);
	}

	@RequestMapping(path="/add-order-history",method=RequestMethod.POST)
	public ResponseEntity<BaseResponseDTO> addOrderHistory(@RequestBody OrderHistoryAddDTO orderHistoryAddDTO) throws DataNotFoundException {
		BaseResponseDTO baseResponseDTO = adminService.addOrderHistory(orderHistoryAddDTO);
	    return new ResponseEntity<BaseResponseDTO>(baseResponseDTO,  HttpStatus.OK);
	     
	  }
}
