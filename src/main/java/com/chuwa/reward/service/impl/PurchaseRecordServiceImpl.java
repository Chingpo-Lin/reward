package com.chuwa.reward.service.impl;

import com.chuwa.reward.dao.PurchaseRecordRepository;
import com.chuwa.reward.dao.UserRepository;
import com.chuwa.reward.entity.PurchaseRecord;
import com.chuwa.reward.entity.User;
import com.chuwa.reward.enums.BizCodeEnum;
import com.chuwa.reward.enums.TransactionEnum;
import com.chuwa.reward.exception.BizException;
import com.chuwa.reward.payload.*;
import com.chuwa.reward.service.PurchaseRecordService;
import com.chuwa.reward.service.UserService;
import com.chuwa.reward.utils.AppConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PurchaseRecordServiceImpl implements PurchaseRecordService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PurchaseRecordRepository purchaseRecordRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PurchaseRecordDto createRecord(PurchaseRecordRequest purchaseRecordRequest) {

        PurchaseRecordDto purchaseRecordDto = convertRequestToDto(purchaseRecordRequest);

        PurchaseRecord purchaseRecord = convertToRecord(purchaseRecordDto);
        PurchaseRecord newRecord = purchaseRecordRepository.save(purchaseRecord);

        userService.updateUserPoints(newRecord.getUser().getId(), newRecord.getPoints());

        return convertToRecordDto(newRecord);
    }

    @Override
    public List<PurchaseRecordDto> getAllRecord() {
        List<PurchaseRecord> purchaseRecords = purchaseRecordRepository.findAll();
        return purchaseRecords.stream().map((record) -> convertToRecordDto(record)).collect(Collectors.toList());
    }

    @Override
    public PurchaseRecordResponse getAllRecord(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, sort);

        Page<PurchaseRecord> pageUsers = purchaseRecordRepository.findAll(pageRequest);

        List<PurchaseRecord> purchaseRecords = pageUsers.getContent();
        List<PurchaseRecordDto> purchaseRecordDtos = purchaseRecords.stream().map(record -> convertToRecordDto(record)).collect(Collectors.toList());

        return new PurchaseRecordResponse(purchaseRecordDtos, pageUsers.getNumber(),
                pageUsers.getSize(), pageUsers.getTotalElements(),
                pageUsers.getTotalPages(), pageUsers.isLast());
    }

    @Override
    public PurchaseRecordDto getRecordById(long id) {
        PurchaseRecord purchaseRecord = purchaseRecordRepository.findById(id).orElseThrow(() ->
                new BizException(BizCodeEnum.PURCHASE_RECORD_NOT_FOUND));
        return convertToRecordDto(purchaseRecord);
    }

    @Override
    public List<UserPointVO> getUserRecordByDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            endDate = LocalDate.now();
            startDate = endDate.minusMonths(TransactionEnum.RECORD_PERIOD.getValue());
        }

        List<PurchaseRecord> purchaseRecords = getPurchaseRecordsByDateRange(startDate, endDate);


        Map<User, List<PurchaseRecord>> purchaseRecordsMap = purchaseRecords.stream()
                .collect(Collectors.groupingBy(PurchaseRecord::getUser));

        List<UserPointVO> userPointVOS = new ArrayList<>();

        for (User user: purchaseRecordsMap.keySet()) {
            Map<String, Integer> monthlyPoints = new TreeMap<>(Comparator.reverseOrder());
            Integer totalPoint = 0;
            UserPointVO userPointVO = new UserPointVO();
            userPointVO.setUsername(user.getUsername());
            for (PurchaseRecord records: purchaseRecordsMap.get(user)) {
                LocalDate localDate = records.getCreateDateTime();
                int monthDifference = (int) ChronoUnit.MONTHS.between(localDate, endDate);
                String key = String.format(AppConstants.TIME_KEY, monthDifference);
                Integer points = records.getPoints();
                totalPoint += points;
                monthlyPoints.put(key, monthlyPoints.getOrDefault(key, 0) + points);
            }
            userPointVO.setMonthlyPoints(monthlyPoints);
            userPointVO.setTotal(totalPoint);

            userPointVOS.add(userPointVO);
        }

        return userPointVOS;
    }

    public List<PurchaseRecord> getPurchaseRecordsByDateRange(LocalDate startDate, LocalDate endDate) {

        log.info("start date:", startDate);
        log.info("end date:", endDate);
        List<PurchaseRecord> purchaseRecords = purchaseRecordRepository
                .findPurchaseRecordsBetweenDate(startDate, endDate);


        if (purchaseRecords == null || purchaseRecords.size() == 0) {
            throw new BizException(BizCodeEnum.PERIOD_RECORD_NOT_FOUND);
        }
        return purchaseRecords;
    }

    private PurchaseRecord convertToRecord(PurchaseRecordDto purchaseRecordDto) {
        PurchaseRecord purchaseRecord = new PurchaseRecord();
        BeanUtils.copyProperties(purchaseRecordDto, purchaseRecord);
        User user = Optional.ofNullable(userRepository.findByUsername(purchaseRecordDto.getUsername()))
                .orElseThrow(() -> new BizException(BizCodeEnum.USER_NOT_FOUND));
        purchaseRecord.setUser(user);
        return purchaseRecord;
    }

    private PurchaseRecordDto convertToRecordDto(PurchaseRecord purchaseRecord) {
        PurchaseRecordDto purchaseRecordDto = new PurchaseRecordDto();
        BeanUtils.copyProperties(purchaseRecord, purchaseRecordDto);
        purchaseRecordDto.setUsername(purchaseRecord.getUser().getUsername());
        return purchaseRecordDto;
    }

    private PurchaseRecordDto convertRequestToDto(PurchaseRecordRequest purchaseRecordRequest) {
        PurchaseRecordDto purchaseRecordDto = new PurchaseRecordDto();
        BeanUtils.copyProperties(purchaseRecordRequest, purchaseRecordDto);
        purchaseRecordDto.setPoints(calculatePoint(purchaseRecordRequest.getAmount()));
        return purchaseRecordDto;
    }

    private Integer calculatePoint(Double amount) {
        int points = 0;
        if (amount >= TransactionEnum.MIN_POINT_AMOUNT.getValue()) {
            if (amount > TransactionEnum.MAX_POINT_AMOUNT.getValue()) {
                points += TransactionEnum.MAX_POINT.getValue()
                        * (amount - TransactionEnum.MAX_POINT_AMOUNT.getValue())
                        + TransactionEnum.MIN_POINT.getValue()
                        * (TransactionEnum.MAX_POINT_AMOUNT.getValue()
                        - TransactionEnum.MIN_POINT_AMOUNT.getValue());
            } else {
                points += (amount - TransactionEnum.MIN_POINT_AMOUNT.getValue())
                        * TransactionEnum.MIN_POINT.getValue();
            }
        }
        return points;
    }
}
