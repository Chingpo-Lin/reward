package com.chuwa.reward.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPointVO {

    @JsonProperty("username")
    private String username;

    @JsonProperty("monthly_points")
    private Map<String, Integer> monthlyPoints;

    @JsonProperty("total")
    private Integer total;

}
