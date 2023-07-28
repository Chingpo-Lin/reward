package com.chuwa.reward.service;

import com.chuwa.reward.payload.PurchaseRecordDto;
import com.chuwa.reward.payload.PurchaseRecordRequest;
import com.chuwa.reward.payload.PurchaseRecordResponse;
import com.chuwa.reward.payload.UserPointVO;

import java.time.LocalDate;
import java.util.List;

public interface PurchaseRecordService {

    PurchaseRecordDto createRecord(PurchaseRecordRequest purchaseRecordRequest);

    List<PurchaseRecordDto> getAllRecord();

    PurchaseRecordResponse getAllRecord(int pageNo, int pageSize, String sortBy, String sortDir);

    PurchaseRecordDto getRecordById(long id);

    List<UserPointVO> getUserRecordByDateRange(LocalDate startDate, LocalDate endDate);

}
