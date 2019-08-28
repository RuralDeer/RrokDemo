package com.cn.request.anno;

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
     * 网络地址
     *
     * @return default
     */
    String url() default "";

    /**
     * Json数据
     *
     * @return default
     */
    String json() default "";

    /**
     * assets 文件
     *
     * @return default
     */
    String assets() default "";
}
