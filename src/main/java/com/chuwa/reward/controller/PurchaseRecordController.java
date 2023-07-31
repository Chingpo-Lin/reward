package com.chuwa.reward.controller;

import com.chuwa.reward.payload.*;
import com.chuwa.reward.service.PurchaseRecordService;
import com.chuwa.reward.utils.AppConstants;
import com.chuwa.reward.utils.CommonUtils;
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
        log.debug("save record with Post /api/v1/records with request: {}", purchaseRecordRequest);
        PurchaseRecordDto result = purchaseRecordService.createRecord(purchaseRecordRequest);

        httpServletResponse.setStatus(HttpStatus.CREATED.value());
        log.debug("Respond Post /api/v1/records with: {}", result);
        return JsonData.buildSuccess(CommonUtils.encode(result));
    }

    @GetMapping()
    public JsonData getRecord(HttpServletResponse httpServletResponse) {
        log.debug("get records with Get /api/v1/records");
        List<PurchaseRecordDto> purchaseRecordDtos = purchaseRecordService.getAllRecord();
        httpServletResponse.setStatus(HttpStatus.OK.value());
        log.debug("Respond Get /api/v1/records with: {}", purchaseRecordDtos);
        return JsonData.buildSuccess(CommonUtils.encode(purchaseRecordDtos));
    }

    @GetMapping("page")
    public JsonData getAllRecords(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.USER_DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.USER_DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.USER_DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.USER_DEFAULT_SORT_DIR, required = false) String sortDir,
            HttpServletResponse httpServletResponse
    ) {
        log.debug("page records with Get /api/v1/records/page with requests: {}, {}, {}, {}", pageNo, pageSize, sortBy, sortDir);
        PurchaseRecordResponse purchaseRecordResponse = purchaseRecordService.getAllRecord(pageNo, pageSize, sortBy, sortDir);
        httpServletResponse.setStatus(HttpStatus.OK.value());
        log.debug("Respond Get /api/v1/records/page with: {}", purchaseRecordResponse);
        return JsonData.buildSuccess(CommonUtils.encode(purchaseRecordResponse));
    }

    @GetMapping("/{id}")
    public JsonData getRecordById(@PathVariable(name="id") long id, HttpServletResponse httpServletResponse) {
        log.debug("get records by id with Get /api/v1/records/{id} with requests: {}", id);
        PurchaseRecordDto purchaseRecordDto = purchaseRecordService.getRecordById(id);
        httpServletResponse.setStatus(HttpStatus.OK.value());
        log.debug("Respond Get /api/v1/records/{id} with: {}", purchaseRecordDto);
        return JsonData.buildSuccess(CommonUtils.encode(purchaseRecordDto));
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
        log.debug("page records with Get /api/v1/records/search with requests: {}", dateRequest);
        List<UserPointVO> userPointVOS = purchaseRecordService
                .getUserRecordByDateRange(dateRequest.getStartDate(), dateRequest.getEndDate());
        httpServletResponse.setStatus(HttpStatus.OK.value());
        log.debug("Respond  Get /api/v1/records/search with: {}", userPointVOS);
        return JsonData.buildSuccess(CommonUtils.encode(userPointVOS));
    }
}
