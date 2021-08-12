package com.beha.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.beha.dao.model.Customer;
import com.beha.dao.model.Grade;
import com.beha.dao.model.IyzicoForm;
import com.beha.dao.model.IyzicoPayment;
import com.beha.dao.model.IyzicoPaymentItem;
import com.beha.dao.model.Order;
import com.beha.dao.model.OrderDetail;
import com.beha.dao.model.OrderHistory;
import com.beha.dao.model.OrderStatus;
import com.beha.dao.model.Product;
import com.beha.dao.model.User;
import com.beha.dao.repository.AddressRepository;
import com.beha.dao.repository.CustomerRepository;
import com.beha.dao.repository.GradeRepository;
import com.beha.dao.repository.IyzicoFormRepository;
import com.beha.dao.repository.IyzicoPaymentItemRepository;
import com.beha.dao.repository.IyzicoPaymentRepository;
import com.beha.dao.repository.OrderDetailRepository;
import com.beha.dao.repository.OrderHistoryRepository;
import com.beha.dao.repository.OrderRepository;
import com.beha.dao.repository.UserRepository;
import com.beha.dto.AddressDTO;
import com.beha.dto.GeneralResponseDTO;
import com.beha.dto.MailDTO;
import com.beha.dto.MessageDTO;
import com.beha.dto.MessageRequestDTO;
import com.beha.dto.MessageResponseDTO;
import com.beha.dto.OrderDTO;
import com.beha.dto.OrderDetailDTO;
import com.beha.dto.OrderHistoryDTO;
import com.beha.dto.OrderRequestDTO;
import com.beha.dto.OrderResponseDTO;
import com.beha.dto.ProductDTO;
import com.beha.dto.ShopListDTO;
import com.beha.mapper.DtoMapper;
import com.iyzipay.Options;
import com.iyzipay.model.Address;
import com.iyzipay.model.BasketItem;
import com.iyzipay.model.BasketItemType;
import com.iyzipay.model.Buyer;
import com.iyzipay.model.CheckoutForm;
import com.iyzipay.model.CheckoutFormInitialize;
import com.iyzipay.model.Currency;
import com.iyzipay.model.Locale;
import com.iyzipay.model.Payment;
import com.iyzipay.model.PaymentCard;
import com.iyzipay.model.PaymentChannel;
import com.iyzipay.model.PaymentGroup;
import com.iyzipay.model.PaymentItem;
import com.iyzipay.request.CreateCheckoutFormInitializeRequest;
import com.iyzipay.request.CreatePaymentRequest;
import com.iyzipay.request.RetrieveCheckoutFormRequest;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class OrderService {
	private GradeRepository gradeRepository;
	private IyzicoFormRepository iyzicoFormRepository;
	private CustomerRepository customerRepository;
	private OrderRepository orderRepository;
	private OrderDetailRepository orderDetailRepository;
	private OrderHistoryRepository orderHistoryRepository;
	private IyzicoPaymentRepository iyzicoPaymentRepository;
	private IyzicoPaymentItemRepository iyzicoPaymentItemRepository;
	private AddressRepository addressRepository;
	private UserRepository userRepository;
	private DtoMapper dtoMapper = DtoMapper.INSTANCE;
	private MailService mailService;


	public List<OrderResponseDTO> getOrders(Long userId) {
		User user = userRepository.findById(userId).orElse(null);
		Customer customer = customerRepository.findByUserId(userId);
		List<Order> orders = orderRepository.findByCustomerIdAndOrderStatus(customer.getId());
		List<OrderDTO> orderDTOs=dtoMapper.toOrderDTOList(orders);
		List<OrderResponseDTO> orderResponseDTOs = new ArrayList<OrderResponseDTO>();
		for (Order order : orders) {
			if(order.getOrderStatus()!=OrderStatus.STATUS_PAYMENT_ERROR) {
			List<OrderDetail> orderDetails = order.getOrderDetail();
			List<OrderDetailDTO> orderDetailDTOs = dtoMapper.toOrderDetailDTOList(orderDetails);
			OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
			orderResponseDTO.setOrderNumber(order.getId());			
			orderResponseDTO.setGradeName(order.getGrade().getName());
			orderResponseDTO.setSchoolName(order.getGrade().getSchool().getName());
			orderResponseDTO.setOrderStatus(order.getOrderStatus());
			orderResponseDTO.setOrderStatusDescription(order.getOrderStatus().getDescription());
			orderResponseDTO.setCustomer(order.getCustomer().getName()+" "+order.getCustomer().getSurname());
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			orderResponseDTO.setCreatedDate(formatter.format(order.getCreatedDate()));
			orderResponseDTO.setGradeId(order.getGrade().getId());
			orderResponseDTO.setStudentName(order.getStudentName());
			orderResponseDTO.setTotal(order.getTotal());
			orderResponseDTO.setOrderDetailList(orderDetailDTOs);
			
			List<OrderHistoryDTO> orderHistoryList = dtoMapper.toOrderHistoryDTOList(order.getOrderHistory());
			orderResponseDTO.setOrderHistoryList(orderHistoryList);
			
			AddressDTO addressDTO = dtoMapper.toAddressDTO(order.getAddress());
			orderResponseDTO.setAddressDTO(addressDTO);
			orderResponseDTOs.add(orderResponseDTO);
			
		}}
		
		
		
		return orderResponseDTOs;
	}
	
	public GeneralResponseDTO addOrder(OrderRequestDTO orderRequestDTO) {
		GeneralResponseDTO generalResponseDTO = new GeneralResponseDTO();

		// User user=userRepository.findById(orderRequestDTO.getUserId()).orElse(null);
		Customer customerCome = customerRepository.findByUserId(orderRequestDTO.getUserId());
		com.beha.dao.model.Address address = addressRepository.findById(orderRequestDTO.getAddressId()).orElse(null);
		Order order = prepareOrder(orderRequestDTO, customerCome);
		customerCome.setSchool(order.getGrade().getSchool());
		Customer customer = customerRepository.save(customerCome);
		order.setAddress(address);
		orderRepository.save(order);

		MailDTO mailDto = new MailDTO();
		mailDto.setId(order.getId());
		mailDto.setEmail(customer.getEmail());
		mailDto.setName(customer.getName());
		mailDto.setStudentName(order.getStudentName());

		GeneralResponseDTO payment = createPayment(customer, orderRequestDTO, order.getId());
		String paymentStatus = payment.getStatus();
		String paymentMessage = payment.getMessage();
		OrderHistory orderHistory = new OrderHistory();
		orderHistory.setOrder(order);
		if (paymentStatus.equalsIgnoreCase("failure")) {
			order.setOrderStatus(OrderStatus.STATUS_PAYMENT_ERROR);
			orderHistory.setOrderStatus(OrderStatus.STATUS_PAYMENT_ERROR);
			orderHistory.setComment("Ödeme Başarısız");
			orderHistoryRepository.save(orderHistory);
			orderRepository.save(order);
			generalResponseDTO.setMessage(paymentMessage);
			generalResponseDTO.setStatus("f");
		} else {
			orderHistory.setOrderStatus(OrderStatus.STATUS_PAYMENT_COMPLETE);
			order.setOrderStatus(OrderStatus.STATUS_PAYMENT_COMPLETE);
			orderHistory.setComment("Ödeme Tamamlandı.");
			orderHistoryRepository.save(orderHistory);
			orderRepository.save(order);
			List<ShopListDTO> shopLists = orderRequestDTO.getShopList();
			for (ShopListDTO shopList : shopLists) {
				OrderDetail orderDetail = prepareOrderDetail(shopList, order);
				orderDetailRepository.save(orderDetail);
			}
			try {
				mailService.sendEmail(mailDto);
			} catch (MailException mailException) {
				System.out.println(mailException);
			}
			String customerPhone = "9" + address.getTelephone(); // +customer.getAddress().getTelephone();
			if(customerPhone.length()==12 && customerPhone.startsWith("905") ) {
			MessageResponseDTO message = prepareMessage(customer.getName() + " " + customer.getSurname(),
					customerPhone);
			String a = message.getStatus();
			if (message.getError() != "")
				System.out.println("Mesaj gönderilemedi");
			else
				System.out.println("Mesaj gönderildi.");}
			generalResponseDTO.setMessage(paymentMessage + "Sipariş başarıyla oluşturuldu.");
			generalResponseDTO.setStatus("s");

		}
		return generalResponseDTO;

	}

	public GeneralResponseDTO addOrderWithCheckoutForm(OrderRequestDTO orderRequestDTO) {
		GeneralResponseDTO generalResponseDTO = new GeneralResponseDTO();

		// User user=userRepository.findById(orderRequestDTO.getUserId()).orElse(null);
		Customer customerCome = customerRepository.findByUserId(orderRequestDTO.getUserId());
		com.beha.dao.model.Address address = addressRepository.findById(orderRequestDTO.getAddressId()).orElse(null);
		Order order = prepareOrder(orderRequestDTO, customerCome);
		customerCome.setSchool(order.getGrade().getSchool());
		Customer customer = customerRepository.save(customerCome);
		order.setAddress(address);
		orderRepository.save(order);

		MailDTO mailDto = new MailDTO();
		mailDto.setId(order.getId());
		mailDto.setEmail(customer.getEmail());
		mailDto.setName(customer.getName());
		mailDto.setStudentName(order.getStudentName());

		GeneralResponseDTO payment = createPaymentCheckoutForm(customer, orderRequestDTO, order.getId());
		String paymentStatus = payment.getStatus();
		String paymentMessage = payment.getMessage();
		OrderHistory orderHistory = new OrderHistory();
		orderHistory.setOrder(order);
		if (paymentStatus.equalsIgnoreCase("failure")) {
			order.setOrderStatus(OrderStatus.STATUS_PAYMENT_ERROR);
			orderHistory.setOrderStatus(OrderStatus.STATUS_PAYMENT_ERROR);
			orderHistory.setComment("Ödeme Başarısız");
			orderHistoryRepository.save(orderHistory);
			orderRepository.save(order);
			generalResponseDTO.setMessage(paymentMessage);
			generalResponseDTO.setStatus("f");
		} else {
			orderHistory.setOrderStatus(OrderStatus.STATUS_PAYMENT_COMPLETE);
			order.setOrderStatus(OrderStatus.STATUS_PAYMENT_COMPLETE);
			orderHistory.setComment("Ödeme Tamamlandı.");
			orderHistoryRepository.save(orderHistory);
			orderRepository.save(order);
			List<ShopListDTO> shopLists = orderRequestDTO.getShopList();
			for (ShopListDTO shopList : shopLists) {
				OrderDetail orderDetail = prepareOrderDetail(shopList, order);
				orderDetailRepository.save(orderDetail);
			}
			try {
				mailService.sendEmail(mailDto);
			} catch (MailException mailException) {
				System.out.println(mailException);
			}
			String customerPhone = "9" + address.getTelephone(); // +customer.getAddress().getTelephone();
			if(customerPhone.length()==12 && customerPhone.startsWith("905") ) {
			MessageResponseDTO message = prepareMessage(customer.getName() + " " + customer.getSurname(),
					customerPhone);
			String a = message.getStatus();
			if (message.getError() != "")
				System.out.println("Mesaj gönderilemedi");
			else
				System.out.println("Mesaj gönderildi.");}
			generalResponseDTO.setMessage(paymentMessage + "Sipariş başarıyla oluşturuldu.");
			generalResponseDTO.setStatus("s");

		}
		return generalResponseDTO;

	}

	private Order prepareOrder(OrderRequestDTO orderRequestDTO, Customer customer) {
		Long gradeId = orderRequestDTO.getGradeId();
		Grade grade = gradeRepository.findById(gradeId).orElse(null);
		Double comissionRate = grade.getSchool().getCommissionRate();
		Order order = new Order();
		order.setStudentName(orderRequestDTO.getStudentName());
		order.setTotal(orderRequestDTO.getTotal());
		order.setCommissionAmount((orderRequestDTO.getTotal()*comissionRate)/100);
		order.setCustomer(customer);
		order.setGrade(grade);
		order.setOrderStatus(OrderStatus.STATUS_AWATING_PAYMENT);
		return order;
	}

	private OrderDetail prepareOrderDetail(ShopListDTO shopListDTO, Order order) {
		ProductDTO productDto = shopListDTO.getProduct();
		Product product = dtoMapper.toProduct(productDto);
		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setPrice(shopListDTO.getPrice());
		orderDetail.setProduct(product);
		orderDetail.setQuantity(shopListDTO.getQuantity());
		orderDetail.setOrder(order);
		return orderDetail;

	}

	private OrderDetail prepareOrderDetail(ShopListDTO shopListDTO) {
		ProductDTO productDto = shopListDTO.getProduct();
		Product product = dtoMapper.toProduct(productDto);
		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setPrice(shopListDTO.getPrice());
		orderDetail.setProduct(product);
		orderDetail.setQuantity(shopListDTO.getQuantity());
		// orderDetail.setOrder(order);
		return orderDetail;

	}

	private com.beha.dao.model.Address prepareAddress(AddressDTO addressDTO, Customer customer) {
		com.beha.dao.model.Address address = new com.beha.dao.model.Address();
		address.setAddressDescription(addressDTO.getAddressDescription());
		address.setAddressName(addressDTO.getAddressName());
		address.setProvince(addressDTO.getProvince());
		address.setCounty(addressDTO.getCounty());
		address.setZipCode(addressDTO.getZipCode());
		address.setTelephone(addressDTO.getTelephone());
		address.setCustomer(customer);
		return address;

	}

	private GeneralResponseDTO createPayment(Customer customer, OrderRequestDTO orderRequestDTO, Long orderId) {
		GeneralResponseDTO generalResponseDTO = new GeneralResponseDTO();
		com.beha.dao.model.Address address = addressRepository.findById(orderRequestDTO.getAddressId()).orElse(null);
		Options options = new Options();
		options.setApiKey("ZMmyer8AoB660B6UDr91J6VEUCYeKp3O");
		options.setSecretKey("NcjDIWDnqq0CgOth2p0RM8dmyuVTQ1pI");
		options.setBaseUrl("https://api.iyzipay.com");

		CreatePaymentRequest request = new CreatePaymentRequest();
		request.setLocale(Locale.TR.getValue());
		request.setConversationId(orderId.toString()); // sipariş no olabilir
		BigDecimal price = BigDecimal.valueOf(orderRequestDTO.getTotal()).setScale(2, RoundingMode.FLOOR);
		request.setPrice(price);
		request.setPaidPrice(price);
		request.setCurrency(Currency.TRY.name());
		request.setInstallment(1); // taksit
		// request.setBasketId("B67832"); //sipariiş numarası olabilir
		request.setPaymentChannel(PaymentChannel.WEB.name());
		request.setPaymentGroup(PaymentGroup.PRODUCT.name());

		PaymentCard paymentCard = new PaymentCard();
		paymentCard.setCardHolderName(orderRequestDTO.getCreditCardDTO().getCardHolderName());
		paymentCard.setCardNumber(orderRequestDTO.getCreditCardDTO().getCardNumber());
		paymentCard.setExpireMonth(orderRequestDTO.getCreditCardDTO().getCardExpireMonth());
		paymentCard.setExpireYear(orderRequestDTO.getCreditCardDTO().getCardExpireYear());
		paymentCard.setCvc(orderRequestDTO.getCreditCardDTO().getCvc());
		paymentCard.setRegisterCard(0); // kartın kaydedilip kaydedilmeyeceği
		request.setPaymentCard(paymentCard);

		Buyer buyer = new Buyer();
		buyer.setId(customer.getId().toString());
		buyer.setName(customer.getName());
		buyer.setSurname(customer.getSurname());
		buyer.setGsmNumber(address.getTelephone()); // + customer.getAddress().getTelephone());
		buyer.setEmail(customer.getEmail());
		buyer.setIdentityNumber("11392911478");// customer.getAddress().getTelephone());
		// buyer.setLastLoginDate("2015-10-05 12:43:35");//zorunlu değil
		// buyer.setRegistrationDate("2013-04-21 15:12:09");//zorunlu değil
		buyer.setRegistrationAddress(address.getAddressDescription());// customer.getAddress().getAddressDescription());
		try {
			buyer.setIp(getIp());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // ip?

		buyer.setCity(address.getProvince());
		buyer.setCountry("Turkey");
		buyer.setZipCode(address.getZipCode());
		request.setBuyer(buyer);

		Address shippingAddress = new Address();
		shippingAddress.setContactName(customer.getName() + customer.getSurname());
		shippingAddress.setCity(address.getProvince());
		shippingAddress.setCountry("Turkey");
		shippingAddress.setAddress(address.getAddressDescription());
		shippingAddress.setZipCode(address.getZipCode());
		request.setShippingAddress(shippingAddress);

		Address billingAddress = new Address();
		billingAddress.setContactName(customer.getName() + customer.getSurname());
		billingAddress.setCity(address.getProvince());
		billingAddress.setCountry("Turkey");
		billingAddress.setAddress(address.getAddressDescription());
		billingAddress.setZipCode(address.getZipCode());
		request.setBillingAddress(billingAddress);

		List<BasketItem> basketItems = new ArrayList<BasketItem>();
		List<ShopListDTO> shopLists = orderRequestDTO.getShopList();
		for (ShopListDTO shopList : shopLists) {
			BasketItem basketItem = new BasketItem();
			OrderDetail orderDetail = prepareOrderDetail(shopList);
			basketItem.setId(orderDetail.getProduct().getBarcode());
			basketItem.setName(orderDetail.getProduct().getName());
			basketItem.setCategory1("KIRTASİYE-KİTAP");
			basketItem.setItemType(BasketItemType.PHYSICAL.name());
			BigDecimal priceBasket = BigDecimal.valueOf(orderDetail.getPrice() * orderDetail.getQuantity()).setScale(2,
					RoundingMode.FLOOR);
			basketItem.setPrice(priceBasket);
			basketItems.add(basketItem);
		}
		request.setBasketItems(basketItems);

		Payment payment = Payment.create(request, options);
		String status = payment.getStatus();
		generalResponseDTO.setStatus(status);
		generalResponseDTO.setMessage(payment.getErrorMessage());

		if (status.equals("success")) {
			IyzicoPayment iyzicoPayment = new IyzicoPayment();
			iyzicoPayment.setFraudStatus(payment.getFraudStatus());
			iyzicoPayment.setPaymentId(payment.getPaymentId());
			iyzicoPayment.setStatus(payment.getStatus());
			iyzicoPayment.setSystemTime(payment.getSystemTime());
			iyzicoPayment.setConversationId(payment.getConversationId());
			iyzicoPayment.setErrorCode(payment.getErrorCode());
			iyzicoPayment.setErrorMessage(payment.getErrorMessage());
			iyzicoPayment.setIyziCommissionRateAmount(payment.getIyziCommissionRateAmount());
			IyzicoPayment iyzicoPaymentAdded = iyzicoPaymentRepository.save(iyzicoPayment);

			List<PaymentItem> paymentItems = payment.getPaymentItems();
			for (PaymentItem paymentItem : paymentItems) {
				IyzicoPaymentItem iyzicoPaymentItem = new IyzicoPaymentItem();
				iyzicoPaymentItem.setIyzicoPayment(iyzicoPaymentAdded);
				iyzicoPaymentItem.setPaidPrice(paymentItem.getPaidPrice());
				iyzicoPaymentItem.setPaidPriceConvertedPayout(paymentItem.getConvertedPayout().getPaidPrice());
				iyzicoPaymentItem.setPaymentTransactionId(paymentItem.getPaymentTransactionId());
				iyzicoPaymentItemRepository.save(iyzicoPaymentItem);
			}
			System.out.println(payment);
			return generalResponseDTO;
		} else {
			return generalResponseDTO;
		}
	}
	
	private GeneralResponseDTO createPaymentCheckoutForm(Customer customer, OrderRequestDTO orderRequestDTO, Long orderId) {
		GeneralResponseDTO generalResponseDTO = new GeneralResponseDTO();
		com.beha.dao.model.Address address = addressRepository.findById(orderRequestDTO.getAddressId()).orElse(null);
		Options options = new Options();
		options.setApiKey("sandbox-MsGo1gMqJuiEuLJ73ljZOPkSdD6JGMxH");
		options.setSecretKey("sandbox-i7gvGCcLxTxmEiX60sJrY3LFUq1m10Yt");
		options.setBaseUrl("https://sandbox-api.iyzipay.com");

		CreateCheckoutFormInitializeRequest request = new CreateCheckoutFormInitializeRequest();
		request.setLocale(Locale.TR.getValue());
		request.setConversationId(orderId.toString()); // sipariş no olabilir
		BigDecimal price = BigDecimal.valueOf(orderRequestDTO.getTotal()).setScale(2, RoundingMode.FLOOR);
		request.setPrice(price);
		request.setPaidPrice(price);
		request.setCurrency(Currency.TRY.name());
		request.setCallbackUrl("http://localhost:8080/customer/result-order-with-form");
		//request.setInstallment(1); // taksit
		// request.setBasketId("B67832"); //sipariiş numarası olabilir
		//request.setPaymentChannel(PaymentChannel.WEB.name());
		request.setPaymentGroup(PaymentGroup.PRODUCT.name());

		Buyer buyer = new Buyer();
		buyer.setId(customer.getId().toString());
		buyer.setName(customer.getName());
		buyer.setSurname(customer.getSurname());
		buyer.setGsmNumber(address.getTelephone()); // + customer.getAddress().getTelephone());
		buyer.setEmail(customer.getEmail());
		buyer.setIdentityNumber("11392911478");// customer.getAddress().getTelephone());
		// buyer.setLastLoginDate("2015-10-05 12:43:35");//zorunlu değil
		// buyer.setRegistrationDate("2013-04-21 15:12:09");//zorunlu değil
		buyer.setRegistrationAddress(address.getAddressDescription());// customer.getAddress().getAddressDescription());
		try {
			buyer.setIp(getIp());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // ip?

		buyer.setCity(address.getProvince());
		buyer.setCountry("Turkey");
		buyer.setZipCode(address.getZipCode());
		request.setBuyer(buyer);

		Address shippingAddress = new Address();
		shippingAddress.setContactName(customer.getName() + customer.getSurname());
		shippingAddress.setCity(address.getProvince());
		shippingAddress.setCountry("Turkey");
		shippingAddress.setAddress(address.getAddressDescription());
		shippingAddress.setZipCode(address.getZipCode());
		request.setShippingAddress(shippingAddress);

		Address billingAddress = new Address();
		billingAddress.setContactName(customer.getName() + customer.getSurname());
		billingAddress.setCity(address.getProvince());
		billingAddress.setCountry("Turkey");
		billingAddress.setAddress(address.getAddressDescription());
		billingAddress.setZipCode(address.getZipCode());
		request.setBillingAddress(billingAddress);

		List<BasketItem> basketItems = new ArrayList<BasketItem>();
		List<ShopListDTO> shopLists = orderRequestDTO.getShopList();
		for (ShopListDTO shopList : shopLists) {
			BasketItem basketItem = new BasketItem();
			OrderDetail orderDetail = prepareOrderDetail(shopList);
			basketItem.setId(orderDetail.getProduct().getBarcode());
			basketItem.setName(orderDetail.getProduct().getName());
			basketItem.setCategory1("KIRTASİYE-KİTAP");
			basketItem.setItemType(BasketItemType.PHYSICAL.name());
			BigDecimal priceBasket = BigDecimal.valueOf(orderDetail.getPrice() * orderDetail.getQuantity()).setScale(2,
					RoundingMode.FLOOR);
			basketItem.setPrice(priceBasket);
			basketItems.add(basketItem);
		}
		request.setBasketItems(basketItems);

		CheckoutFormInitialize checkoutFormInitialize = CheckoutFormInitialize.create(request, options);
		System.out.println(checkoutFormInitialize);
		System.out.println(checkoutFormInitialize.getPaymentPageUrl());
		System.out.println(checkoutFormInitialize.getCheckoutFormContent());
		
		String status = checkoutFormInitialize.getStatus();
		generalResponseDTO.setStatus(status);
		generalResponseDTO.setMessage(checkoutFormInitialize.getErrorMessage());

		if (status.equals("success")) {
			IyzicoForm iyzicoForm = new IyzicoForm();
			iyzicoForm.setToken(checkoutFormInitialize.getToken());
			iyzicoForm.setStatus(checkoutFormInitialize.getStatus());
			iyzicoForm.setSystemTime(checkoutFormInitialize.getSystemTime());
			iyzicoForm.setConversationId(checkoutFormInitialize.getConversationId());
			iyzicoForm.setErrorCode(checkoutFormInitialize.getErrorCode());
			iyzicoForm.setErrorMessage(checkoutFormInitialize.getErrorMessage());
			iyzicoFormRepository.save(iyzicoForm);
		} 
		
			return generalResponseDTO;
		
	}
	
	public GeneralResponseDTO resultPaymentCheckoutFrom(String token) {
		GeneralResponseDTO generalResponseDTO = new GeneralResponseDTO();
		RetrieveCheckoutFormRequest request = new RetrieveCheckoutFormRequest();
		request.setToken(token);
		request.setLocale(Locale.TR.getValue());
		
		Options options = new Options();
		options.setApiKey("sandbox-MsGo1gMqJuiEuLJ73ljZOPkSdD6JGMxH");
		options.setSecretKey("sandbox-i7gvGCcLxTxmEiX60sJrY3LFUq1m10Yt");
		options.setBaseUrl("https://sandbox-api.iyzipay.com");
		
		CheckoutForm checkoutForm = CheckoutForm.retrieve(request, options);
		
		IyzicoPayment iyzicoPayment = new IyzicoPayment();
		iyzicoPayment.setFraudStatus(checkoutForm.getFraudStatus());
		iyzicoPayment.setPaymentId(checkoutForm.getPaymentId());
		iyzicoPayment.setStatus(checkoutForm.getStatus());
		iyzicoPayment.setSystemTime(checkoutForm.getSystemTime());
		iyzicoPayment.setConversationId(checkoutForm.getConversationId());
		iyzicoPayment.setErrorCode(checkoutForm.getErrorCode());
		iyzicoPayment.setErrorMessage(checkoutForm.getErrorMessage());
		iyzicoPayment.setIyziCommissionRateAmount(checkoutForm.getIyziCommissionRateAmount());
		IyzicoPayment iyzicoPaymentAdded = iyzicoPaymentRepository.save(iyzicoPayment);
		generalResponseDTO.setStatus(checkoutForm.getStatus());
				
		List<PaymentItem> paymentItems = checkoutForm.getPaymentItems();
		for (PaymentItem paymentItem : paymentItems) {
			IyzicoPaymentItem iyzicoPaymentItem = new IyzicoPaymentItem();
			iyzicoPaymentItem.setIyzicoPayment(iyzicoPaymentAdded);
			iyzicoPaymentItem.setPaidPrice(paymentItem.getPaidPrice());
			iyzicoPaymentItem.setPaidPriceConvertedPayout(paymentItem.getConvertedPayout().getPaidPrice());
			iyzicoPaymentItem.setPaymentTransactionId(paymentItem.getPaymentTransactionId());
			iyzicoPaymentItemRepository.save(iyzicoPaymentItem);
		}
		System.out.println(checkoutForm);
		return generalResponseDTO;
		
	}
	
	public CreatePaymentRequest prepareSampleRequest() {
		CreatePaymentRequest request = new CreatePaymentRequest();
		request.setLocale(Locale.TR.getValue());
		request.setConversationId("123456789");
		request.setPrice(new BigDecimal("1"));
		request.setPaidPrice(new BigDecimal("1.2"));
		request.setCurrency(Currency.TRY.name());
		request.setInstallment(1);
		request.setBasketId("B67832");
		request.setPaymentChannel(PaymentChannel.WEB.name());
		request.setPaymentGroup(PaymentGroup.PRODUCT.name());

		PaymentCard paymentCard = new PaymentCard();
		paymentCard.setCardHolderName("John Doe");
		paymentCard.setCardNumber("5528790000000008");
		paymentCard.setExpireMonth("12");
		paymentCard.setExpireYear("2030");
		paymentCard.setCvc("123");
		paymentCard.setRegisterCard(0);
		request.setPaymentCard(paymentCard);

		Buyer buyer = new Buyer();
		buyer.setId("BY789");
		buyer.setName("John");
		buyer.setSurname("Doe");
		buyer.setGsmNumber("+905350000000");
		buyer.setEmail("email@email.com");
		buyer.setIdentityNumber("74300864791");
		buyer.setLastLoginDate("2015-10-05 12:43:35");
		buyer.setRegistrationDate("2013-04-21 15:12:09");
		buyer.setRegistrationAddress("Nidakule Göztepe, Merdivenköy Mah. Bora Sok. No:1");
		buyer.setIp("85.34.78.112");
		buyer.setCity("Istanbul");
		buyer.setCountry("Turkey");
		buyer.setZipCode("34732");
		request.setBuyer(buyer);

		Address shippingAddress = new Address();
		shippingAddress.setContactName("Jane Doe");
		shippingAddress.setCity("Istanbul");
		shippingAddress.setCountry("Turkey");
		shippingAddress.setAddress("Nidakule Göztepe, Merdivenköy Mah. Bora Sok. No:1");
		shippingAddress.setZipCode("34742");
		request.setShippingAddress(shippingAddress);

		Address billingAddress = new Address();
		billingAddress.setContactName("Jane Doe");
		billingAddress.setCity("Istanbul");
		billingAddress.setCountry("Turkey");
		billingAddress.setAddress("Nidakule Göztepe, Merdivenköy Mah. Bora Sok. No:1");
		billingAddress.setZipCode("34742");
		request.setBillingAddress(billingAddress);

		List<BasketItem> basketItems = new ArrayList<BasketItem>();
		BasketItem firstBasketItem = new BasketItem();
		firstBasketItem.setId("BI101");
		firstBasketItem.setName("Binocular");
		firstBasketItem.setCategory1("Collectibles");
		firstBasketItem.setCategory2("Accessories");
		firstBasketItem.setItemType(BasketItemType.PHYSICAL.name());
		firstBasketItem.setPrice(new BigDecimal("0.3"));
		basketItems.add(firstBasketItem);

		BasketItem secondBasketItem = new BasketItem();
		secondBasketItem.setId("BI102");
		secondBasketItem.setName("Game code");
		secondBasketItem.setCategory1("Game");
		secondBasketItem.setCategory2("Online Game Items");
		secondBasketItem.setItemType(BasketItemType.VIRTUAL.name());
		secondBasketItem.setPrice(new BigDecimal("0.5"));
		basketItems.add(secondBasketItem);

		BasketItem thirdBasketItem = new BasketItem();
		thirdBasketItem.setId("BI103");
		thirdBasketItem.setName("Usb");
		thirdBasketItem.setCategory1("Electronics");
		thirdBasketItem.setCategory2("Usb / Cable");
		thirdBasketItem.setItemType(BasketItemType.PHYSICAL.name());
		thirdBasketItem.setPrice(new BigDecimal("0.2"));
		basketItems.add(thirdBasketItem);
		request.setBasketItems(basketItems);
		return request;
	}

	public static String getIp() throws Exception {
		URL whatismyip = new URL("http://checkip.amazonaws.com");
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
			String ip = in.readLine();
			return ip;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	
	private MessageResponseDTO prepareMessage(String customerName, String customerPhone) {
		RestTemplate restTemplate = new RestTemplate();
		MessageRequestDTO request = new MessageRequestDTO();
		request.setGate(0L);
		request.setPassword("tr16kp262");
		request.setSDate("");
		request.setUserName("behastore");
		request.setVPeriod("48");
		MessageDTO message = new MessageDTO();
		message.setSender("BEHA A.S.");
		message.setText("Sayın " + customerName + ", siparişiniz başarıyla oluşturulmuştur.Sipariş durumunu web sitemizden Hesabım > Siparişlerim"
				+ "bölümünden takip edebilirsiniz.KazançlıOkul'u tercih ettiğiniz için teşekkürler.");
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
