package com.cn.demo.bean;

import java.io.Serializable;

/**
 * Date: 2019/8/5
 * <p>
 * Time: 5:42 PM
 * <p>
 * author: 鹿文龙
 */
public class UploadBean implements Serializable {

	/**
	 * name : 张三
	 * file : http://192.168.1.100:8080/Users/luwenlong/Documents/demo/Service/upload/file_.40efba74e48739cf222f2ce3d22ccae5.png
	 */

	public String name;


	public String file;


	@Override
	public String toString() {
		return "UploadBean{" +
			"name='" + name + '\'' +
			", file='" + file + '\'' +
			'}';
	}
}
