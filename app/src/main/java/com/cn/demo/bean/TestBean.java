package com.cn.demo.bean;

import java.io.Serializable;

/**
 * Date: 2019/7/11
 * <p>
 * Time: 6:31 PM
 * <p>
 * author: 鹿文龙
 */
public class TestBean implements Serializable {

	public String name;

	public String mobile;

	public TestBean(String name, String mobile) {
		this.name = name;
		this.mobile = mobile;
	}
}
