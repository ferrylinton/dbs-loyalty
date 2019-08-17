package com.dbs.loyalty.component;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.repository.TadaOrderRepository;
import com.dbs.loyalty.util.SequenceUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component
public class StartupApplicationListener {

	private final TadaOrderRepository tadaOrderRepository;
	
	@EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
		log.info("onApplicationEvent");
        String maxOrderReference = tadaOrderRepository.findMaxOrderReference(SequenceUtil.getPrefix());
        
        if(maxOrderReference == null) {
        	SequenceUtil.reset(1);
        }else {
        	int maxOrder = Integer.parseInt(maxOrderReference.replace(SequenceUtil.getPrefix(), Constant.EMPTY));
        	SequenceUtil.reset(maxOrder + 1);
        }
    }
	
}
