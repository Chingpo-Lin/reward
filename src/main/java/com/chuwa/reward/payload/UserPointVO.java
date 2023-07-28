package com.chuwa.reward.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPointVO {

    private String username;

    private Map<String, Integer> monthlyPoints;

    private Integer total;

}
