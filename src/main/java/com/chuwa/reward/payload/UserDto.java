package com.chuwa.reward.payload;


import com.chuwa.reward.utils.AppConstants;
import jakarta.validation.constraints.NotEmpty;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;

    @NotEmpty(message = AppConstants.USER_NAME_EMPTY)
    @Size(min = AppConstants.MIN_USER_NAME_LEN, message = AppConstants.USER_NAME_SHORT)
    private String username;

    private Integer points;
}
