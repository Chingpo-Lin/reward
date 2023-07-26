package com.chuwa.reward.controller;

import com.chuwa.reward.payload.*;
import com.chuwa.reward.service.PurchaseRecordService;
import com.chuwa.reward.utils.AppConstants;
import com.chuwa.reward.utils.JsonData;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/records")
@Slf4j
public class PurchaseRecordController {

    @Autowired
    private PurchaseRecordService purchaseRecordService;

    @PostMapping
    public JsonData createRecord(@Valid @RequestBody PurchaseRecordRequest purchaseRecordRequest,
                               HttpServletResponse httpServletResponse) {
        PurchaseRecordDto result = purchaseRecordService.createRecord(purchaseRecordRequest);

        httpServletResponse.setStatus(HttpStatus.CREATED.value());
        return JsonData.buildSuccess(result);
    }

    @GetMapping()
    public JsonData getRecord(HttpServletResponse httpServletResponse) {
        List<PurchaseRecordDto> purchaseRecordDtos = purchaseRecordService.getAllRecord();
        httpServletResponse.setStatus(HttpStatus.OK.value());
        return JsonData.buildSuccess(purchaseRecordDtos);
    }

    @GetMapping("page")
    public JsonData getAllRecords(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.USER_DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.USER_DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.USER_DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.USER_DEFAULT_SORT_DIR, required = false) String sortDir,
            HttpServletResponse httpServletResponse
    ) {
        PurchaseRecordResponse purchaseRecordResponse = purchaseRecordService.getAllRecord(pageNo, pageSize, sortBy, sortDir);
        httpServletResponse.setStatus(HttpStatus.OK.value());
        return JsonData.buildSuccess(purchaseRecordResponse);
    }

    @GetMapping("/{id}")
    public JsonData getRecordById(@PathVariable(name="id") long id, HttpServletResponse httpServletResponse) {
        PurchaseRecordDto purchaseRecordDto = purchaseRecordService.getRecordById(id);
        httpServletResponse.setStatus(HttpStatus.OK.value());
        return JsonData.buildSuccess(purchaseRecordDto);
    }

    /**
     * if no variable was passed, auto show the last 3 months data
     * @param
     * @param httpServletResponse
     * @return
     */
    @GetMapping("search")
    public JsonData calculatePointsInPeriod(
            @ModelAttribute DateRequest dateRequest,
            HttpServletResponse httpServletResponse
    ) {
        log.info("DataRequest", dateRequest);
        List<UserPointVO> userPointVOS = purchaseRecordService
                .getUserRecordByDateRange(dateRequest.getStartDate(), dateRequest.getEndDate());
        httpServletResponse.setStatus(HttpStatus.OK.value());
        return JsonData.buildSuccess(userPointVOS);
    }
}
