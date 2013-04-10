package com.hellogood.eCalendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Main extends Activity {
	private Calendar cal = Calendar.getInstance();
	private TextView textDate = null;
	private ArrayList<DateWidgetDayCell> days = new ArrayList<DateWidgetDayCell>();
	private LinearLayout dateLayout = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		textDate = (TextView) findViewById(R.id.text_date);
		updateMonth(cal, textDate);

		Button preBtn = (Button) findViewById(R.id.btn_pre_month);
		preBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				cal.add(Calendar.MONTH, -1);
				updateMonth(cal, textDate);
				updateCalendar();
			}
		});
		Button nextBtn = (Button) findViewById(R.id.btn_next_month);
		nextBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				cal.add(Calendar.MONTH, 1);
				updateMonth(cal, textDate);
				updateCalendar();
			}
		});

		Button todayBtn = (Button) findViewById(R.id.btn_today);
		todayBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				cal = Calendar.getInstance();
				updateMonth(cal, textDate);
				updateCalendar();
			}
		});

		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int topHeight = findViewById(R.id.layout_top).getLayoutParams().height;
		int bottomHeight = findViewById(R.id.layout_bottom).getLayoutParams().height;		
		int calendarHeight = display.getHeight() - topHeight - bottomHeight;		
		int statusBarHeight = getStatusBarHeight();
		calendarHeight = calendarHeight - statusBarHeight;
		generateCalendarMain(display.getWidth(), calendarHeight);
		updateCalendar();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	private void updateMonth(Calendar cal, TextView textDate) {
		String date = cal.get(Calendar.YEAR) + "年" + (cal.get(Calendar.MONTH) + 1) + "月";
		Lunar lunar = new Lunar(cal);
		date += " " + lunar.getChineseMonth();
		textDate.setText(date);
	}

	private View generateCalendarMain(int width, int height) {
		RelativeLayout layContent = (RelativeLayout) findViewById(R.id.layout_calendar);
		// layContent.setPadding(1, 0, 1, 0);
		layContent.setBackgroundColor(Color.argb(255, 105, 105, 103));

		LinearLayout layRow = (LinearLayout) findViewById(R.id.layout_calendar_header);
		// layRow.setBackgroundColor(Color.argb(255, 207, 207, 205));
		int headerHeight = 50;
		for (int iDay = 0; iDay < 7; iDay++) {
			DateWidgetDayHeader day = new DateWidgetDayHeader(this, width / 7, headerHeight);

			final int iWeekDay = DayStyle.getWeekDay(iDay, Calendar.SUNDAY);
			day.setData(iWeekDay);
			layRow.addView(day, iDay);
		}
		Calendar cal4Calc = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
		cal4Calc.set(Calendar.DATE, 1);
		int b = cal4Calc.get(Calendar.WEEK_OF_MONTH);
		cal4Calc.add(Calendar.MONTH, 1);
		cal4Calc.add(Calendar.DATE, -1);
		int e = cal4Calc.get(Calendar.WEEK_OF_MONTH);
		int weeks = e - b + 1;

		int cellHeight = (height - headerHeight) / weeks;

		dateLayout = (LinearLayout) findViewById(R.id.layout_calendar_date);
		dateLayout.setBackgroundColor(Color.WHITE);
		for (int i = 0; i < weeks; i++) {

			LinearLayout dateRow = createLayout(LinearLayout.HORIZONTAL);
			for (int iDay = 0; iDay < 7; iDay++) {
				DateWidgetDayCell dayCell = new DateWidgetDayCell(this, width / 7, cellHeight);
				days.add(dayCell);
				dateRow.addView(dayCell);
			}
			dateLayout.addView(dateRow, i);
		}

		return layContent;
	}

	private LinearLayout createLayout(int iOrientation) {
		LinearLayout lay = new LinearLayout(this);
		lay.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		lay.setOrientation(iOrientation);

		return lay;
	}

	private void updateCalendar() {
		dateLayout.invalidate();
		Calendar cal4Calc = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1);
		int currentMonth = cal.get(Calendar.MONTH);
		int start = cal4Calc.get(Calendar.DAY_OF_WEEK) - 1;
		cal4Calc.add(Calendar.DAY_OF_MONTH, -start);
		for (int i = 0; i < days.size(); i++) {
			days.get(i).invalidate();
			days.get(i).setData(cal4Calc.get(Calendar.YEAR), cal4Calc.get(Calendar.MONTH), cal4Calc.get(Calendar.DATE), false, false, 1, false);
			cal4Calc.add(Calendar.DATE, 1);
			if (cal4Calc.get(Calendar.MONTH) > currentMonth) {
				break;
			}
		}
		for (int i = 0; i < start; i++) {
			days.get(i).invalidate();
		}		 
	}
	
	 public int getStatusBarHeight()  
	    {  
	        Class<?> c = null;  
	        Object obj = null;  
	        java.lang.reflect.Field field = null;  
	        int x = 0;  
	        int statusBarHeight = 0;  
	        try  
	        {  
	            c = Class.forName("com.android.internal.R$dimen");  
	            obj = c.newInstance();  
	            field = c.getField("status_bar_height");  
	            x = Integer.parseInt(field.get(obj).toString());  
	            statusBarHeight = getResources().getDimensionPixelSize(x);  
	            return statusBarHeight;  
	        }  
	        catch (Exception e)  
	        {  
	            e.printStackTrace();  
	        }  
	        return statusBarHeight;  
	    }  
}
