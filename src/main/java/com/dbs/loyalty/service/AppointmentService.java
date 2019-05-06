package com.dbs.loyalty.service;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbs.loyalty.domain.Appointment;
import com.dbs.loyalty.domain.Customer;
import com.dbs.loyalty.domain.Wellness;
import com.dbs.loyalty.exception.BadRequestException;
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
	public Response save(Appointment appointment) throws BadRequestException{
		Optional<Customer> customer = customerRespository.findById(SecurityUtil.getId());
		
		if(customer.isPresent()) {
			Optional<Appointment> current = appointmentRepository.findByCustomerAndHealthPartnerAndDate(
					SecurityUtil.getId(), 
					appointment.getHealthPartner().getId(), 
					appointment.getArrivalDate());
			
			if(current.isPresent()) {
				return new Response("Data is already exist");
			}else {
				Optional<Wellness> wellness = wellnessRepository.findById(SecurityUtil.getId());
				
				if(wellness.isPresent() && wellness.get().getTotal() > 0) {
					wellness.get().setTotal(wellness.get().getTotal() - 1);
					wellnessRepository.save(wellness.get());
					
					appointment.setCustomer(customer.get());
					appointment.setCreatedBy(SecurityUtil.getLogged());
					appointment.setCreatedDate(Instant.now());
					appointment = appointmentRepository.save(appointment);
					return new Response("Data is saved");
				}else {
					throw new BadRequestException("No limit is available");
				}
			}
		}

		throw new BadRequestException("Failed to save data");
	}
	
}
