package com.sztu.check.enums;

/**
 * @author lujiawei
 * @version 1.0
 * @date 2022/01/04/ 20:30
 */
public enum FileStatusEnum {

    SUCCESS("通过", "1"),

    DOING("审核中", "0"),

    FAIL("不通过", "2");

    private String name;

    private String code;

    FileStatusEnum(String name, String code) {
        this.code = code;
        this.name = name;
    }


    public String getName() {
        return name;
    }

    /**
     * 名称
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    /**
     * 编码
     * @param code
     */
    public void setCode(String code) {
        this.code = code;
    }


}
