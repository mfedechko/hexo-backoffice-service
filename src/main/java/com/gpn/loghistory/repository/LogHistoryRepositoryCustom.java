package com.gpn.loghistory.repository;

import com.gpn.loghistory.model.LogHistoryEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface LogHistoryRepositoryCustom {

    List<Long> findIds(Specification<LogHistoryEntity> spec, Sort sort);
}
