package com.chuwa.reward.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class PurchaseRecordDto {

    private Long id;

    private String username;

    private Double amount;

    private Integer points;
}
