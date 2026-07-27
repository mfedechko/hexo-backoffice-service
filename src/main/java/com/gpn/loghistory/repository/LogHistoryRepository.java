package com.gpn.loghistory.repository;

import com.gpn.loghistory.model.LogHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LogHistoryRepository extends JpaRepository<LogHistoryEntity, Long>,
        JpaSpecificationExecutor<LogHistoryEntity> {
}
