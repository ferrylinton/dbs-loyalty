package com.dbs.loyalty.service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbs.loyalty.config.constant.DomainConstant;
import com.dbs.loyalty.config.constant.MessageConstant;
import com.dbs.loyalty.domain.Appointment;
import com.dbs.loyalty.domain.Customer;
import com.dbs.loyalty.domain.Wellness;
import com.dbs.loyalty.exception.BadRequestException;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.repository.AppointmentRepository;
import com.dbs.loyalty.repository.CustomerRepository;
import com.dbs.loyalty.repository.WellnessRepository;
import com.dbs.loyalty.service.specification.AppointmentSpec;
import com.dbs.loyalty.util.SecurityUtil;

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
	
	public Page<Appointment> findAll(Map<String, String> params, Pageable pageable){
		return appointmentRepository.findAll(new AppointmentSpec(params), pageable);
	}

	@Transactional
	public Appointment save(Appointment appointment) throws BadRequestException, NotFoundException{
		Optional<Customer> customer = customerRespository.findById(SecurityUtil.getId());

		if(customer.isPresent()) {
				Optional<Wellness> wellness = wellnessRepository.findById(SecurityUtil.getId());
				
				if(wellness.isPresent() && wellness.get().getTotal() > 0) {
					wellness.get().setTotal(wellness.get().getTotal() - 1);
					wellnessRepository.save(wellness.get());
					
					appointment.setCustomer(customer.get());
					appointment.setCreatedBy(SecurityUtil.getLogged());
					appointment.setCreatedDate(Instant.now());
					return appointmentRepository.save(appointment);
				}else {
					throw new BadRequestException(MessageConstant.NO_LIMIT_iS_AVAILABLE);
				}
		}else {
			throw new NotFoundException(String.format(MessageConstant.DATA_IS_NOT_FOUND, DomainConstant.CUSTOMER, SecurityUtil.getId()));
		}
	}
	
	public boolean isExist(String medicalProviderId, LocalDate date) {
		List<Appointment> current = appointmentRepository.findByCustomerAndMedicalProviderAndDate(SecurityUtil.getId(), medicalProviderId, date);
		return current.size() > 0;
	}
	
}
