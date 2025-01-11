package com.lhhu.lhhupictureserver.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 实现自定义权限校验注解
 * 注解作用在方法上
 * 生命周期为运行时生效
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthCheck {
    /**
     * 访问时，用户需要具有的角色
     * @return
     */
    String mustRole() default "";
}
