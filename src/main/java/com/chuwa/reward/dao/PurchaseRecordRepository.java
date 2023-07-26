package com.chuwa.reward.dao;

import com.chuwa.reward.entity.PurchaseRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PurchaseRecordRepository extends JpaRepository<PurchaseRecord, Long> {

    @Query("SELECT p FROM PurchaseRecord p WHERE p.createDateTime BETWEEN :startDate AND :endDate")
    List<PurchaseRecord> findPurchaseRecordsBetweenDate(@Param("startDate") LocalDate startDate,
                                                        @Param("endDate") LocalDate endDate);
}
