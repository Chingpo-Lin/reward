package com.chuwa.reward.payload;

import com.chuwa.reward.utils.AppConstants;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class PurchaseRecordRequest {

    @NotEmpty(message = AppConstants.USER_NAME_EMPTY)
    @Size(min = AppConstants.MIN_USER_NAME_LEN, message = AppConstants.USER_NAME_SHORT)
    private String username;

    @NotNull(message = AppConstants.AMOUNT_NULL)
    private Double amount;
}
