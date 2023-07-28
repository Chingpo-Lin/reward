package com.chuwa.reward.enums;

import lombok.Getter;

public enum TransactionEnum {

    MIN_POINT_AMOUNT(50),

    MIN_POINT(1),

    MAX_POINT(2),

    MAX_POINT_AMOUNT(100),

    RECORD_PERIOD(3);

    @Getter
    private int value;

    TransactionEnum(int value) {
        this.value = value;
    }
}
