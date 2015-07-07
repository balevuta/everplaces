package com.zulu.places.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.content.Context;

public class DateTimeUtils {

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String formatStandarDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
		return sdf.format(date);
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String formatStandardDate(long date) {
		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
		return sdf.format(date*1000);
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDateFullMonth(long date) {
		SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");
		return sdf.format(new Date(date));
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static long formatLongStandarDate(long date) {
		// UTC
		SimpleDateFormat inputFormat = new SimpleDateFormat(
				"EEE MMM dd HH:mm:ss 'GMT' yyyy", Locale.US);
		inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		// Local
		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
		// Set Time Zone: GMT
		sdf.setTimeZone(TimeZone.getDefault());

		// Adjust locale and zone appropriately
		Date outputDate = null;
		try {
			outputDate = inputFormat.parse(inputFormat.format(new Date(date)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Date(sdf.format(outputDate)).getTime();
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String formatMonthYearDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("MMM, yyyy");
		return sdf.format(date);
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String formatFullDateTime(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy KK:mm:ss a");
		return sdf.format(date);
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String formatFullTime(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		return sdf.format(date);
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String getTimeHourAndMinute(long date) {
		// UTC
		SimpleDateFormat inputFormat = new SimpleDateFormat(
				"EEE MMM dd HH:mm:ss 'GMT' yyyy", Locale.US);
		inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		// Local
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		// Set Time Zone: GMT
		sdf.setTimeZone(TimeZone.getDefault());

		// Adjust locale and zone appropriately
		Date outputDate = null;
		try {
			outputDate = inputFormat.parse(inputFormat.format(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return sdf.format(outputDate);
	}

	/**
	 * Get Hour and Minute with AM and PM format
	 * 
	 * @param date
	 * @return
	 */
	public static String getTimeHourAndMinuteAmPm(long date) {
		// UTC
		SimpleDateFormat inputFormat = new SimpleDateFormat(
				"EEE MMM dd HH:mm:ss 'GMT' yyyy", Locale.US);
		inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		// Local
		SimpleDateFormat sdf = new SimpleDateFormat("K:mm a");
		// Set Time Zone: GMT
		sdf.setTimeZone(TimeZone.getDefault());

		// Adjust locale and zone appropriately
		Date outputDate = null;
		try {
			outputDate = inputFormat.parse(inputFormat.format(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return sdf.format(outputDate);
	}

	/**
	 * 
	 * @param date
	 * @param time
	 * @return
	 */
	public static long addTimeToDate(long date, String time) {
		String sDate = formatStandarDate(new Date(date)) + " " + time;
		Date newDate = new Date(sDate);
		return newDate.getTime();
	}

	/**
	 * 
	 * @param sDate
	 * @return
	 */
	public static Date getSimpleFormatDate(Date sDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
		String nDate = sdf.format(sDate.getTime());
		return new Date(nDate);
	}


	/**
	 * Convert Local Time to UTC Time to Submit
	 * 
	 * @param date
	 * @return
	 */
	public static long convertLocalTimeToUTC(Date date) {
		// UTC
		SimpleDateFormat outFormat = new SimpleDateFormat(
				"EEE MMM dd HH:mm:ss 'GMT' yyyy", Locale.US);
		outFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		return new Date(outFormat.format(date)).getTime();
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static long convertLocalTimeToUTC(long date) {
		// UTC
		SimpleDateFormat outFormat = new SimpleDateFormat(
				"EEE MMM dd HH:mm:ss 'GMT' yyyy", Locale.US);
		outFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		return new Date(outFormat.format(new Date(date))).getTime();
	}

	/**
	 * Get tomorrow date
	 */
	public static Date getTomorrowDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, 1);
		return calendar.getTime();
	}
	
	/////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////
	/**
	 * Convert UTC Time to Local Time - First, set format with Locale.US -
	 * Second, set UTC TimeZone
	 * 
	 * @param context
	 * @param date
	 * @return
	 */
	public static String formatDateShowNewUI(Context context, Date date) {
		// UTC
		SimpleDateFormat inputFormat = new SimpleDateFormat(
				"EEE MMM dd HH:mm:ss 'GMT' yyyy", Locale.US);
		inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		// Local
		String dateFormat = "MM/dd/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		sdf.setTimeZone(TimeZone.getDefault());

		// Adjust locale and zone appropriately
		Date outputDate = null;
		try {
			outputDate = inputFormat.parse(inputFormat.format(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sdf.format(outputDate);
	}
	
	
	/**
	 * Convert UTC Time to Local Time - First, set format with Locale.US -
	 * Second, set UTC TimeZone
	 * 
	 * @param context
	 * @param date
	 * @return
	 */
	public static String formatDateShowUI(Context context, Date date) {
		// UTC
		SimpleDateFormat inputFormat = new SimpleDateFormat(
				"EEE MMM dd HH:mm:ss 'GMT' yyyy", Locale.US);
		inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		// Local
		String dateFormat = "MMM dd, yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		sdf.setTimeZone(TimeZone.getDefault());

		// Adjust locale and zone appropriately
		Date outputDate = null;
		try {
			outputDate = inputFormat.parse(inputFormat.format(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sdf.format(outputDate);
	}
	
	
	/**
	 * 
	 * @param context
	 * @param date
	 * @return
	 */
	public static String formatDateSubmitToServer(Context context, Date date) {
		// UTC
		SimpleDateFormat inputFormat = new SimpleDateFormat(
				"EEE MMM dd HH:mm:ss 'GMT' yyyy", Locale.US);
		inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		// Local
		String dateFormat = "yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		sdf.setTimeZone(TimeZone.getDefault());

		// Adjust locale and zone appropriately
		Date outputDate = null;
		try {
			outputDate = inputFormat.parse(inputFormat.format(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sdf.format(outputDate);
	}
	
	/**
	 * 
	 * @param mContext
	 * @param dateWithTime
	 * @return
	 */
	public static String convertDateWithTime(Context mContext, String dateWithTime) {
		String startDate = dateWithTime.
				substring(0, dateWithTime.lastIndexOf(" "));
		String[] dateStartArr = startDate.split("-");

		Calendar cStart = Calendar.getInstance();
		cStart.set(Integer.valueOf(dateStartArr[0].trim()),
				Integer.valueOf(dateStartArr[1].trim()) - 1,
				Integer.valueOf(dateStartArr[2].trim()), 0, 0);
		
		String formatedStartDate = DateTimeUtils.formatDateShowUI(mContext, cStart.getTime());
		return formatedStartDate;
	}
	
	/**
	 * Convert UTC Time to Local Time - First, set format with Locale.US -
	 * Second, set UTC TimeZone
	 * 
	 * @param context
	 * @param date
	 * @return
	 */
	public static String formatDateShowUIFull(Context context, Date date) {
		// UTC
		SimpleDateFormat inputFormat = new SimpleDateFormat(
				"EEE MMM dd HH:mm:ss 'GMT' yyyy", Locale.US);
		inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		// Local
		String dateFormat = "MMM dd, yyyy KK:mm:ss a";
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		sdf.setTimeZone(TimeZone.getDefault());

		// Adjust locale and zone appropriately
		Date outputDate = null;
		try {
			outputDate = inputFormat.parse(inputFormat.format(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sdf.format(outputDate);
	}
}
