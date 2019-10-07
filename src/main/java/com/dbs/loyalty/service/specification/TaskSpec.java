package com.dbs.loyalty.service.specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.domain.Task;
import com.dbs.loyalty.enumeration.TaskOperation;
import com.dbs.loyalty.enumeration.TaskStatus;
import com.dbs.loyalty.util.TaskUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TaskSpec implements Specification<Task>{
	
	private static final long serialVersionUID = 1L;

	private final String taskDataType;
	
	private final Map<String, String> params;
	
	@Override
	public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
		List<Predicate> predicates = new ArrayList<>();
		
		predicates.add(cb.equal(cb.lower(root.get(Constant.TASK_DATA_TYPE)), taskDataType.toLowerCase()));
		
		if(TaskUtil.getTaskStatus(params) != TaskStatus.ALL) {
			predicates.add(cb.equal(root.get(Constant.TASK_STATUS), TaskUtil.getTaskStatus(params)));
		}
		
		if(TaskUtil.getTaskOperation(params) != TaskOperation.ALL) {
			predicates.add(cb.equal(root.get(Constant.TASK_OPERATION), TaskUtil.getTaskOperation(params)));
		}
		
		if(params.containsKey(Constant.KY_PARAM) && !Constant.EMPTY.equals(params.get(Constant.KY_PARAM))) {
			String keyword = String.format(Constant.LIKE_FORMAT, params.get(Constant.KY_PARAM).trim().toLowerCase());
			predicates.add(cb.or(
					cb.like(cb.lower(root.get(Constant.MAKER)), keyword),
					cb.like(cb.lower(root.get(Constant.CHECKER)), keyword)
				));
		}
		
		return cb.and(predicates.toArray(new Predicate[predicates.size()]));
	}

}