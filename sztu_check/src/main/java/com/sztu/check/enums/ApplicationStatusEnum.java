package com.sztu.check.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Data;

/**
 * ApplicationStatus
 * @author elysiaEgo
 */
public enum ApplicationStatusEnum {

    /**
     * notStart
     */
    notStart(0),
    /**
     * processing
     */
    processing(1),
    /**
     * exception
     */
    exception(2),
    /**
     * finished
     */
    finished(3),

    passed(4),

    ejected(5);

    ApplicationStatusEnum(int status) {
        this.status = status;
    }

    public int getStatus(){
        return status;
    }
    @EnumValue
    private final int status;
}