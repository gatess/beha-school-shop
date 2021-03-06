package com.beha.service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.beha.dao.model.Customer;
import com.beha.dao.model.Order;
import com.beha.dao.model.OrderDetail;
import com.beha.dao.model.OrderHistory;
import com.beha.dao.model.OrderStatus;
import com.beha.dao.model.Product;
import com.beha.dao.model.ProductImage;
import com.beha.dao.model.User;
import com.beha.dao.repository.CustomerRepository;
import com.beha.dao.repository.OrderDetailRepository;
import com.beha.dao.repository.OrderHistoryRepository;
import com.beha.dao.repository.OrderRepository;
import com.beha.dao.repository.ProductRepository;
import com.beha.dao.repository.UserRepository;
import com.beha.dto.AddressDTO;
import com.beha.dto.AdminDashboardDTO;
import com.beha.dto.AdminGetProductDTO;
import com.beha.dto.BaseResponseDTO;
import com.beha.dto.EmailPasswordRequestDTO;
import com.beha.dto.GetCustomerDTO;
import com.beha.dto.MailOrderHistoryDTO;
import com.beha.dto.MessageDTO;
import com.beha.dto.MessageRequestDTO;
import com.beha.dto.MessageResponseDTO;
import com.beha.dto.OrderDashboardDetail;
import com.beha.dto.OrderDetailDTO;
import com.beha.dto.OrderHistoryAddDTO;
import com.beha.dto.OrderHistoryDTO;
import com.beha.dto.OrderResponseDTO;
import com.beha.exceptions.DataNotFoundException;
import com.beha.exceptions.UserNotFoundException;
import com.beha.mapper.DtoMapper;
import com.beha.util.DateUtil;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AdminService {
	private CustomerRepository customerRepository;
	private ProductRepository productRepository;
	private OrderRepository orderRepository;
	private OrderHistoryRepository orderHistoryRepository;
	private OrderDetailRepository orderDetailRepository;
	private DtoMapper dtoMapper = DtoMapper.INSTANCE;
	private UserRepository userRepository;
	private MailService mailService;
	
	
	public BaseResponseDTO addOrderHistory(OrderHistoryAddDTO orderHistoryAddDTO) throws DataNotFoundException {
		String orderStatusMessage="";
		String mailStatus = "Bildirim kapal??";
		String messageStatus = "Mesaj kapal??";
		BaseResponseDTO baseResponseDTO = new BaseResponseDTO();
		Order order = orderRepository.findById(orderHistoryAddDTO.getOrderId()).orElseThrow(
				() -> new DataNotFoundException(orderHistoryAddDTO.getOrderId() + " id li order bulunamadi"));
		OrderHistory orderHistory = new OrderHistory();
		orderHistory.setOrder(order);
		orderHistory.setComment(orderHistoryAddDTO.getComment());
		orderHistory.setCreatedDate(new Date());
		orderHistory.setNotice(orderHistoryAddDTO.isNotice());
		orderHistory.setOrderStatus(OrderStatus.getByValue(Integer.valueOf(orderHistoryAddDTO.getStatus())));
		orderHistoryRepository.save(orderHistory);

		if (orderHistoryAddDTO.isNotice() == true) {
			long orderId = orderHistoryAddDTO.getOrderId();
			String orderStatus = OrderStatus.getByValue(Integer.valueOf(orderHistoryAddDTO.getStatus())).getDescription();
			
			switch (orderStatus) {
			case "Sipari?? Teslim Edildi":
				orderStatusMessage=orderId+" nolu sipari??iniz teslim edildi.";
				break;
			case "??deme Tamamland??":
				orderStatusMessage=orderId+" nolu sipari??inizin ??demesi tamamland??.";
				break;
			case "Geri ??dendi":
				orderStatusMessage=orderId+" nolu sipari??inizin tutar?? ??deme yapt??????n??z karta iade edildi.";
				break;
			case "??ptal edildi":
				orderStatusMessage=orderId+" nolu sipari??iniz iptal edildi.";
				break;
			case "Paket Haz??rlan??yor":
				orderStatusMessage=orderId+" nolu sipari??iniz haz??rlan??yor.";
				break;
			case "Ge??ersiz":
				orderStatusMessage=orderId+" nolu sipari??iniz ge??ersiz.";
				break;
			case "??deme Bekleniyor":
				orderStatusMessage=orderId+" nolu sipari??iniz i??in ??deme bekleniyor.";
				break;
			case "Kargoya Teslim Edildi":
				orderStatusMessage=orderId+" nolu sipari??iniz kargoya verildi.";
				break;
			case "Paket Haz??r/Ma??azada Bekliyor":
				orderStatusMessage=orderId+" nolu sipari??iniz haz??r, ma??azada bekliyor. ";
				break;
			case "??deme Al??namad??.":
				orderStatusMessage=orderId+" nolu sipari??iniz i??in ??deme al??namad??.";
				break;

			}
			
			MailOrderHistoryDTO mailOrderHistoryDTO = new MailOrderHistoryDTO();
			mailOrderHistoryDTO.setId(order.getId());
			mailOrderHistoryDTO.setOrderStatus(orderStatusMessage);
			mailOrderHistoryDTO.setComment(orderHistoryAddDTO.getComment());
			mailOrderHistoryDTO.setEmail(order.getCustomer().getEmail());
			mailOrderHistoryDTO.setName(order.getCustomer().getName());
			mailOrderHistoryDTO.setStudentName(order.getStudentName());
			try {
				mailService.sendEmailForOrderHistory(mailOrderHistoryDTO);
				mailStatus = "Mail g??nderildi";
			} catch (MailException mailException) {
				System.out.println(mailException);
				mailStatus = "Mail g??nderilemedi";
			}
			String statusAndComment = orderStatusMessage +"Sipari?? notu : " + orderHistoryAddDTO.getComment();
			String customerPhone = "9" + order.getAddress().getTelephone();
			MessageResponseDTO message = prepareMessage(order.getCustomer().getName() + " " + order.getCustomer().getSurname(),
					customerPhone ,statusAndComment);
			if (message.getError() != "")
				messageStatus="Mesaj g??nderilemedi";
			else
				messageStatus="Mesaj g??nderildi.";
		}

		baseResponseDTO.setStatus("s");
		baseResponseDTO.setMessage("Ba??ar??yla eklendi " + mailStatus + messageStatus);
		
		return baseResponseDTO;
	}

	public List<AdminGetProductDTO> getProducts() {
		List<AdminGetProductDTO> productDTOs = new ArrayList<AdminGetProductDTO>();
		List<Product> products = productRepository.getAllProduct();
		for(Product product: products) {
			AdminGetProductDTO getProduct  = new AdminGetProductDTO();
			getProduct.setBarcode(product.getBarcode());
			getProduct.setName(product.getName());
			getProduct.setPrice(product.getPrice());
			if(product.getQuantity()!=null)
			getProduct.setQuantity(product.getQuantity());
			List<ProductImage> images = product.getProductImages();
			if(!images.isEmpty())
			getProduct.setImage(images.get(0).getFilePath());
			if(!product.getStatus().equals(null))
			getProduct.setStatus(product.getStatus());
			if(product.getTax()!=null)
			getProduct.setTax(product.getTax().getValue());
			productDTOs.add(getProduct);
		}
		return productDTOs;
	}
	
	public List<GetCustomerDTO> getCustomers() {
		List<Customer> customers = customerRepository.getAllCustomer();
		List<GetCustomerDTO> customerDTOs = new ArrayList<GetCustomerDTO>();
		for(Customer customer : customers) {
			GetCustomerDTO getCustomerDTO = new GetCustomerDTO();
			getCustomerDTO.setName(customer.getName());
			getCustomerDTO.setId(customer.getId());
			getCustomerDTO.setSurname(customer.getSurname());
			getCustomerDTO.setEmail(customer.getEmail());
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			getCustomerDTO.setCreatedDate(formatter.format(customer.getCreatedDate()));
			getCustomerDTO.setAddress(dtoMapper.toAddressDTOList(customer.getAddress()));
			customerDTOs.add(getCustomerDTO);
		}
		return customerDTOs;
	}
	public List<OrderResponseDTO> getOrders() {
		List<Order> orders = orderRepository.getAllOrders();
		List<OrderResponseDTO> orderResponseDTOs = new ArrayList<OrderResponseDTO>();
		for (Order order : orders) {
			List<OrderDetail> orderDetails = orderDetailRepository.findByOrder(order);
			List<OrderDetailDTO> orderDetailDTOs = dtoMapper.toOrderDetailDTOList(orderDetails);
			OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
			orderResponseDTO.setOrderNumber(order.getId());
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			orderResponseDTO.setCustomer(order.getCustomer().getName() + " " + order.getCustomer().getSurname());
			orderResponseDTO.setCreatedDate(formatter.format(order.getCreatedDate()));
			orderResponseDTO.setGradeName(order.getGrade().getName());
			orderResponseDTO.setSchoolName(order.getGrade().getSchool().getName());
			orderResponseDTO.setOrderStatus(order.getOrderStatus());
			orderResponseDTO.setOrderStatusDescription(order.getOrderStatus().getDescription());
			orderResponseDTO.setGradeId(order.getGrade().getId());
			orderResponseDTO.setStudentName(order.getStudentName());
			orderResponseDTO.setTotal(order.getTotal());
			orderResponseDTO.setOrderDetailList(orderDetailDTOs);

			List<OrderHistoryDTO> orderHistoryList = dtoMapper.toOrderHistoryDTOList(order.getOrderHistory());

			orderResponseDTO.setOrderHistoryList(orderHistoryList);

			AddressDTO addressDTO = dtoMapper.toAddressDTO(order.getAddress());
			orderResponseDTO.setAddressDTO(addressDTO);
			orderResponseDTOs.add(orderResponseDTO);
		}
		return orderResponseDTOs;
	}

	public AdminDashboardDTO getDashboardDetail(EmailPasswordRequestDTO emailPasswordRequestDTO) {
		AdminDashboardDTO adminDashboardDTO = new AdminDashboardDTO();
		String passwordUser = emailPasswordRequestDTO.getPassword();

		User user = userRepository.findByEmail(emailPasswordRequestDTO.getEmail());

		if (user == null) {
			adminDashboardDTO.setErrorMessage("Kullan??c?? bulunamad??.");
			adminDashboardDTO.setStatus("f");
			throw new UserNotFoundException("Kullan??c?? bulunamad??.");
		}

		if (passwordUser.equals(user.getPassword()) && user.getRole().getValue()==1 ) {
			adminDashboardDTO.setAdminId(user.getId());
			adminDashboardDTO.setAdminName(user.getName()+" "+user.getSurName());
			long newOrderQuantity;
			newOrderQuantity = orderRepository.getNewOrdersCount();
			adminDashboardDTO.setNewOrderQuantity(newOrderQuantity);

			long allOrderQuantity;
			allOrderQuantity = orderRepository.getAllOrdersCount();
			adminDashboardDTO.setAllOrderQuantity(allOrderQuantity);

			long allCustomerQuantity;
			allCustomerQuantity = customerRepository.getAllCustomerCount();
			adminDashboardDTO.setAllCustomerQuantity(allCustomerQuantity);

			long allProductQuantity;
			allProductQuantity = productRepository.getAllProductCount();
			adminDashboardDTO.setAllProductQuantity(allProductQuantity);

			Double ordersSum;
			ordersSum = orderRepository.getCompletedOrdersSum();
			adminDashboardDTO.setOrdersSum(ordersSum);

			Double comissionSum;
			comissionSum = orderRepository.getCompletedOrdersComissionAmount();
			adminDashboardDTO.setComissionSum(comissionSum);

			// LocalDate date = LocalDate.now();
			List<OrderDashboardDetail> orderDashboardDetailList = new ArrayList<OrderDashboardDetail>();
			List<Integer> customerQuantityList = new ArrayList<Integer>();

			List<Customer> lastWeekCustomers = customerRepository.getLastWeekCustomers();
			List<Order> lastWeekOrders = orderRepository.getLastWeekOrders();
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
					+ allCustomerQuantity + "all product:" + allProductQuantity + "ordersSum : " + ordersSum + "date:");

			adminDashboardDTO.setStatus("s");
		} else {
			if(user.getRole().getValue()==1) {
				adminDashboardDTO.setStatus("f");
				adminDashboardDTO.setErrorMessage("??ifre hatal??");
			} else {
			adminDashboardDTO.setStatus("f");
			adminDashboardDTO.setErrorMessage("E-posta kay??tl?? De??il"); }
		}
		return adminDashboardDTO;
	}

	private MessageResponseDTO prepareMessage(String customerName, String customerPhone,
			String statusAndComment) {
		RestTemplate restTemplate = new RestTemplate();
		MessageRequestDTO request = new MessageRequestDTO();
		request.setGate(0L);
		request.setPassword("tr16kp262");
		request.setSDate("");
		request.setUserName("behastore");
		request.setVPeriod("48");
		MessageDTO message = new MessageDTO();
		message.setSender("BEHA A.S.");

		message.setText("Say??n " + customerName + ", " + statusAndComment);
		message.setUtf8("1");
		List<String> gsm = new ArrayList<>();
		gsm.add(customerPhone);
		message.setGsm(gsm);
		request.setMessage(message);
		MessageResponseDTO response = restTemplate.postForObject("http://s.innomobil.com/api/v1/sendsms", request,
				MessageResponseDTO.class);
		return response;
	}
}
