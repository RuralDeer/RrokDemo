package com.cn.request.mock.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Date: 2019-08-28
 * <p>
 * Time: 14:53
 * <p>
 * author: 鹿文龙
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Mock {
    /**
     * Mock 参数
     *
     * @return default
     */
    String value() default "";
    
    /**
     * 是否需要
     *
     * @return default
     */
    boolean enable() default true;
}