package com.dbs.loyalty.service;

import static com.dbs.loyalty.config.constant.EntityConstant.CUSTOMER;
import static com.dbs.loyalty.config.constant.MessageConstant.DATA_IS_ALREADY_EXIST;
import static com.dbs.loyalty.config.constant.MessageConstant.DATA_IS_NOT_FOUND;
import static com.dbs.loyalty.config.constant.MessageConstant.NO_LIMIT_iS_AVAILABLE;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbs.loyalty.config.constant.MessageConstant;
import com.dbs.loyalty.domain.Appointment;
import com.dbs.loyalty.domain.Customer;
import com.dbs.loyalty.domain.Wellness;
import com.dbs.loyalty.exception.BadRequestException;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.repository.AppointmentRepository;
import com.dbs.loyalty.repository.CustomerRepository;
import com.dbs.loyalty.repository.WellnessRepository;
import com.dbs.loyalty.util.SecurityUtil;
import com.dbs.loyalty.web.response.Response;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AppointmentService {

	private final AppointmentRepository appointmentRepository;
	
	private final CustomerRepository customerRespository;
	
	private final WellnessRepository wellnessRepository;
	
	public Optional<Appointment> findById(String id){
		return appointmentRepository.findById(id);
	}
	
	public Page<Appointment> findAll(Pageable pageable){
		return appointmentRepository.findAll(pageable);
	}

	@Transactional
	public Response save(Appointment appointment) throws BadRequestException, NotFoundException{
		Optional<Customer> customer = customerRespository.findById(SecurityUtil.getId());
		
		if(customer.isPresent()) {
			Optional<Appointment> current = appointmentRepository.findByCustomerAndMedicalProviderAndDate(
					SecurityUtil.getId(), 
					appointment.getMedicalProvider().getId(), 
					appointment.getDate());
			
			if(current.isPresent()) {
				return new Response(DATA_IS_ALREADY_EXIST);
			}else {
				Optional<Wellness> wellness = wellnessRepository.findById(SecurityUtil.getId());
				
				if(wellness.isPresent() && wellness.get().getTotal() > 0) {
					wellness.get().setTotal(wellness.get().getTotal() - 1);
					wellnessRepository.save(wellness.get());
					
					appointment.setCustomer(customer.get());
					appointment.setCreatedBy(SecurityUtil.getLogged());
					appointment.setCreatedDate(Instant.now());
					appointment = appointmentRepository.save(appointment);
					return new Response(MessageConstant.DATA_IS_SAVED);
				}else {
					throw new BadRequestException(NO_LIMIT_iS_AVAILABLE);
				}
			}
		}else {
			throw new NotFoundException(String.format(DATA_IS_NOT_FOUND, CUSTOMER, SecurityUtil.getId()));
		}
	}
	
}
