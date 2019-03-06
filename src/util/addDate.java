package util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class addDate {

	/**
	 * 添加上日期字符串，如“1/SCJX12-010/day”转换成“1/SCJX12-010/day/20190301”
	 * 
	 * @param info
	 * @param k
	 *            表示第k天（年和月是相同的）
	 */
	public static String addDateString(String info, int k) {
		info = info + "/" + getCurrentDataString();
		if (k > 9) {
			info += k;
		} else {
			info += "0" + k;
		}
		return info;
	}

	private static String getCurrentDataString() {

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		String res = sdf.format(date);
		System.out.println();
		if (res == null) {
			res = "";
		}
		return res;
	}

}
