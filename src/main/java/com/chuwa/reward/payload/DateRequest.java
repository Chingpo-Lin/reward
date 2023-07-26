package com.chuwa.reward.payload;

import com.chuwa.reward.enums.TransactionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@AllArgsConstructor
public class DateRequest {

    @Getter
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Getter
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    public void setStartDate(LocalDate startDate) {
        if (startDate != null) {
            this.startDate = startDate;
        } else {
            if (endDate == null) {
                this.endDate = LocalDate.now();
            }
            this.startDate = endDate.minusMonths(TransactionEnum.RECORD_PERIOD.getValue());
        }
    }

    public void setEndDate(LocalDate endDate) {
        if (endDate != null) {

        } else {
            if (startDate == null) {
                this.endDate = LocalDate.now();
                this.startDate = endDate.minusMonths(TransactionEnum.RECORD_PERIOD.getValue());
            } else {
                this.endDate = startDate.plusMonths(TransactionEnum.RECORD_PERIOD.getValue());
            }
        }
    }

}
