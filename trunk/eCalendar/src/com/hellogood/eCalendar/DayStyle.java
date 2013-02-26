package com.hellogood.eCalendar;

import java.util.Calendar;

/**
 * ����ؼ���ʽ������
 * @Description: ����ؼ���ʽ������

 * @FileName: DayStyle.java 

 * @Package com.calendar.demo 

 * @Author Hanyonglu

 * @Date 2012-3-18 ����03:33:42 

 * @Version V1.0
 */
public class DayStyle {
	private final static String[] vecStrWeekDayNames = getWeekDayNames();

	private static String[] getWeekDayNames() {
		String[] vec = new String[10];

		vec[Calendar.SUNDAY] = "星期日";
		vec[Calendar.MONDAY] = "星期一";
		vec[Calendar.TUESDAY] = "星期二";
		vec[Calendar.WEDNESDAY] = "星期三";
		vec[Calendar.THURSDAY] = "星期四";
		vec[Calendar.FRIDAY] = "星期五";
		vec[Calendar.SATURDAY] = "星期六";
		
		return vec;
	}

	public static String getWeekDayName(int iDay) {
		return vecStrWeekDayNames[iDay];
	}
	
	public static int getWeekDay(int index, int iFirstDayOfWeek) {
		int iWeekDay = -1;

		if (iFirstDayOfWeek == Calendar.MONDAY) {
			iWeekDay = index + Calendar.MONDAY;
			
			if (iWeekDay > Calendar.SATURDAY)
				iWeekDay = Calendar.SUNDAY;
		}

		if (iFirstDayOfWeek == Calendar.SUNDAY) {
			iWeekDay = index + Calendar.SUNDAY;
		}

		return iWeekDay;
	}
}
