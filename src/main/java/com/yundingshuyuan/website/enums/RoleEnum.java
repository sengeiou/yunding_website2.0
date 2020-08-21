package com.yundingshuyuan.website.enums;

/**
 * 角色枚举，跟数据库内对应
 * @author leeyf
 */
public enum RoleEnum {
    //ROLE_ENUM0(0,"游客"),
    ROLE_ENUM1(1,"学员"),
    ROLE_ENUM2(2,"极客"),
    ROLE_ENUM3(3,"创客"),
    ROLE_ENUM4(4,"秘书处"),
    ROLE_ENUM5(5,"云顶院"),
    ROLE_ENUM6(6,"管理员"),
    ;
    Integer id;
    String roleName;

    RoleEnum(Integer id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }

    public Integer getId() {
        return id;
    }


    public String getRoleName() {
        return roleName;
    }

}
