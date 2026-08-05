package com.gpn.loghistory.repository;

import com.gpn.loghistory.model.LogHistoryEntity;
import com.gpn.loghistory.model.dto.LogHistoryProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogHistoryRepository extends JpaRepository<LogHistoryEntity, Long>,
        LogHistoryRepositoryCustom {

    // Другий крок: по id, знайдених через findIds(...) (Specification-фільтр),
    // одразу джойнимо users - звичайний LEFT JOIN у запиті, без Hibernate-звязку
    // на LogHistoryEntity. Проекція мапиться прямо в DTO в мапері.
    @Query("""
            SELECT new com.gpn.loghistory.model.dto.LogHistoryProjection(
                lh.id, u.username, lh.module, lh.objectId, lh.action, lh.details, lh.createdAt)
            FROM LogHistoryEntity lh
            LEFT JOIN UserEntity u ON u.id = lh.userId
            WHERE lh.id IN :ids
            ORDER BY lh.createdAt DESC
            """)
    List<LogHistoryProjection> findProjectionsByIds(@Param("ids") List<Long> ids);
}
