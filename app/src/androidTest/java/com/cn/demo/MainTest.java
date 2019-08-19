package com.cn.demo;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.chad.library.adapter.base.BaseViewHolder;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Date: 2019/7/24
 * <p>
 * Time: 10:09 AM
 * <p>
 * author: 鹿文龙
 */
@RunWith(AndroidJUnit4.class)
public class MainTest {

	@Rule
	public ActivityTestRule mActivityRule = new ActivityTestRule(MainActivity.class);


	@Test
	public void onclickBtn1() throws Exception {

		Espresso.onView(ViewMatchers.withId(R.id.recyleView)).perform(RecyclerViewActions.<BaseViewHolder>actionOnItemAtPosition(0, ViewActions.click()));


		Thread.sleep(30000);
	}
}
