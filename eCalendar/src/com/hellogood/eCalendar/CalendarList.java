package com.hellogood.eCalendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class CalendarList extends Activity {
	private Calendar cal = Calendar.getInstance();
	private GridView gridview = null;
	private GridView titleview = null;
	private TextView textDate = null;
	private ChineseCalendarGB chineseCalendar = new ChineseCalendarGB();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		titleview = (GridView) findViewById(R.id.titleview);
		gridview = (GridView) findViewById(R.id.gridview);
		// 添加消息处理
		gridview.setOnItemClickListener(new ItemClickListener());
		textDate = (TextView) findViewById(R.id.text_date);
		// updateMonth(cal, textDate);

		ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
		// 生成适配器的ImageItem <====> 动态数组的元素，两者一一对应
		SimpleAdapter saImageItems = new SimpleAdapter(this, // 没什么解释
				lstImageItem,// 数据来源
				R.layout.title_item,// night_item的XML实现

				// 动态数组与ImageItem对应的子项
				new String[] { "WeekText" },

				// ImageItem的XML文件里面的一个ImageView,两个TextView ID
				new int[] { R.id.TitleText });
		String[] weekDays = { "日", "一", "二", "三", "四", "五", "六" };
		for (int i = 0; i < 7; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("WeekText", weekDays[i]);// 添加图像资源的ID
			lstImageItem.add(map);
		}
		titleview.setAdapter(saImageItems);

		updateCalendar();

		Button preBtn = (Button) findViewById(R.id.btn_pre_month);
		preBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				cal.add(Calendar.MONTH, -1);
				// updateMonth(cal, textDate);
				updateCalendar();
			}
		});

		Button nextBtn = (Button) findViewById(R.id.btn_next_month);
		nextBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				cal.add(Calendar.MONTH, 1);
				// updateMonth(cal, textDate);
				updateCalendar();
			}
		});
	}

	private Calendar getFristDayOfMonth(Calendar cal) {
		cal.set(Calendar.DAY_OF_MONTH, 1);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		int firstDay = dayOfWeek - cal.getFirstDayOfWeek();// monday = 2 sunday
															// = 1
		cal.add(Calendar.DAY_OF_MONTH, -firstDay);
		return cal;
	}

	private void updateCalendar() {
		chineseCalendar.setGregorian(cal.get(Calendar.YEAR), (cal.get(Calendar.MONTH) + 1), 1);
		chineseCalendar.computeChineseFields();
		chineseCalendar.computeSolarTerms();
		String date = cal.get(Calendar.YEAR) + "年" + (cal.get(Calendar.MONTH) + 1) + "月";
		date += " " + chineseCalendar.getYearString() + chineseCalendar.getMonthString();
		textDate.setText(date);
		// 生成动态数组，并且转入数据
		ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();

		int month = cal.get(Calendar.MONTH);
		cal = getFristDayOfMonth(cal);
		if (month == 8) {
			System.out.println(cal.get(Calendar.MONTH));
		}
		for (int i = 0; i < 42; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			if (cal.get(Calendar.MONTH) == month) {
				map.put("ItemText", cal.get(Calendar.DAY_OF_MONTH));// 添加图像资源的ID
				chineseCalendar.setGregorian(cal.get(Calendar.YEAR), (cal.get(Calendar.MONTH) + 1),
						cal.get(Calendar.DAY_OF_MONTH));
				chineseCalendar.computeChineseFields();
				chineseCalendar.computeSolarTerms();
				map.put("ItemText2", chineseCalendar.getDateString());// 按序号做ItemText
			} else {
				map.put("ItemText", "");// 添加图像资源的ID
				map.put("ItemText2", "");// 按序号做ItemText
			}
			lstImageItem.add(map);
			cal.add(Calendar.DAY_OF_MONTH, 1);
			Log.e("---------------",
					cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.DAY_OF_MONTH));
			if (cal.get(Calendar.MONTH) > month) {
				break;
			}
		}

		if (cal.get(Calendar.MONTH) > month || cal.get(Calendar.MONTH) == 0) {
			cal.add(Calendar.MONTH, -1);
		}

		// 生成适配器的ImageItem <====> 动态数组的元素，两者一一对应
		SimpleAdapter saImageItems = new SimpleAdapter(this, // 没什么解释
				lstImageItem,// 数据来源
				R.layout.night_item,// night_item的XML实现

				// 动态数组与ImageItem对应的子项
				new String[] { "ItemText", "ItemText2" },

				// ImageItem的XML文件里面的一个ImageView,两个TextView ID
				new int[] { R.id.ItemText, R.id.ItemText2 });
		// 添加并且显示
		gridview.setAdapter(saImageItems);
	}

	// private void updateMonth(Calendar cal, TextView textDate) {
	// String date = cal.get(Calendar.YEAR) + "年" + (cal.get(Calendar.MONTH) +
	// 1) + "月";
	// Lunar lunar = new Lunar(cal);
	// date += " " + lunar.getChineseMonth();
	// textDate.setText(date);
	// }

	// 当AdapterView被单击(触摸屏或者键盘)，则返回的Item单击事件
	class ItemClickListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> arg0,// The AdapterView where the
													// click happened
				View arg1,// The view within the AdapterView that was clicked
				int arg2,// The position of the view in the adapter
				long arg3// The row id of the item that was clicked
		) {
			// 在本例中arg2=arg3
			HashMap<String, Object> item = (HashMap<String, Object>) arg0.getItemAtPosition(arg2);
			// 显示所选Item的ItemText
			setTitle((String) item.get("ItemText"));
		}

	}
}
