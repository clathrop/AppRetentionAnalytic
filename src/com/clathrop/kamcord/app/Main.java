package com.clathrop.kamcord.app;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import com.clathrop.kamcord.dao.EventModelDao;
import com.clathrop.kamcord.dao.EventModelDaoImpl;
import com.clathrop.kamcord.model.EventModel;

public class Main {
	
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private static final int ONE_HUNDRED_PERCENT = 100;

	public static void main(String[] args) {

//		double sevenDayRetentionPercentage = calculateDay7Retention("2014-09-01",
//				"2014-09-02");
		double sevenDayRetentionPercentage = calculateDay7Retention(args[0],
				args[1]);

		System.out.println("Seven Day Retention Percentage for " + args[0]
				+ " through " + args[1] + ": " + sevenDayRetentionPercentage
				+ "%");
	}

	/*
	 * Given a start and end date, this function will calculate the 7 day ui retention rate
	 * of users who opened the app on day1 that also opened the app on day7 for the given range.
	 */
	public static double calculateDay7Retention(String startDate, String endDate) {
		EventModelDao eventDao = new EventModelDaoImpl();
		List<EventModel> eventListStartRange = eventDao.findEventsByDateRange(
				startDate, endDate);

		DateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);

		Date sDate = null;
		Date eDate = null;

		try {
			sDate = format.parse(startDate);
			eDate = format.parse(endDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Calendar c = Calendar.getInstance();
		c.setTime(sDate);
		c.add(Calendar.DATE, 7);

		String day7StartDate = format.format(c.getTime());

		c.setTime(eDate);
		c.add(Calendar.DATE, 7);
		String day7EndDate = format.format(c.getTime());

		List<EventModel> eventListEndRange = eventDao.findEventsByDateRange(
				day7StartDate, day7EndDate);

		// contains <DATE, List<UIDS> >
		HashMap<String, ArrayList<String>> uidDateMap1 = generateDateUidMap(eventListStartRange);
		HashMap<String, ArrayList<String>> uidDateMap2 = generateDateUidMap(eventListEndRange);

		double uiOpenCount = 0;

		double top = 0;
		double bottom = 0;
		// for each date
		for (Entry<String, ArrayList<String>> entry : uidDateMap1.entrySet()) {
			uiOpenCount = entry.getValue().size();
			Entry<String, ArrayList<String>> sevenDayLaterEntry = null;
			// get matching 7 day later entry
			for (Entry<String, ArrayList<String>> sevenDayEntry : uidDateMap2
					.entrySet()) {
				try {
					c.setTime(format.parse(entry.getKey()));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				c.add(Calendar.DATE, 7);

				String matchDate = format.format(c.getTime());
				if (matchDate.equals(sevenDayEntry.getKey())) {
					sevenDayLaterEntry = sevenDayEntry;
					break;
				}
			}
			double sevenDayUidOpenCount = 0;
			ArrayList<String> sevenDayUids = null;
			try {
				sevenDayUids = sevenDayLaterEntry.getValue();
			} catch (NullPointerException e) {
				//if we get a null pointer then we know we're looking at a date for which there 
				//is no data, so continue to further dates
				continue;
			}
			// compare uids in seven day later entry and count which are the
			// same
			for (String sevenDayUid : sevenDayUids) {
				if (entry.getValue().contains(sevenDayUid)) {
					sevenDayUidOpenCount++;
				}
			}
			// System.out.println("Total UIDs with at least one UI OPEN EVENTS: "
			// + uiOpenCount);
			// System.out.println("Number of those that opened again 7 days later : "
			// + sevenDayUidOpenCount);
			bottom = bottom + uiOpenCount;
			top = top + sevenDayUidOpenCount;
		}

		double sevenDayRetention = (top / bottom) * ONE_HUNDRED_PERCENT;
		// System.out.println("SevenDayRetention: " + sevenDayRetention + "%");
		
		return sevenDayRetention;
		
//		for (Entry<String, ArrayList<String>> entry : uidDateMap1.entrySet()) {
//		System.out.println("\n" + entry.getKey() + ": ");
//
//		for (int i = 0; i < 20; i++) {
//			System.out.print(entry.getValue().get(i) + ", ");
//		}
//	}
	}


	/*
	*Generates and returns a HashMap with a key of date and a list of uids that triggered
	*an OPEN_UI_EVENT on that date 
	*
	*eg: {2014-09-01=[9019382, 2934028, 2093829], 2014-09-02=[9019382, 2934028]}
	*/
	private static HashMap<String, ArrayList<String>> generateDateUidMap(
			List<EventModel> eventList) {
		HashMap<String, ArrayList<String>> uidDateMap = new HashMap<String, ArrayList<String>>();
		ArrayList<String> uidList = new ArrayList<String>();

		for (EventModel event : eventList) {
			// if date not already added, set date and new list with single uid in it
			if (!uidDateMap.containsKey(event.getEventTime())) {
				uidList = new ArrayList<String>();
				uidList.add(event.getUserId());
				uidDateMap.put(event.getEventTime(), uidList);
			} else {
				// if date exists, find entry in hashmap, get list of uids and
				// add to it and set back into hashmap
				for (Entry<String, ArrayList<String>> entry : uidDateMap.entrySet()) {
					if (entry.getKey().equals(event.getEventTime())) {
						uidList = entry.getValue();
						break;
					}
				}
				if (!uidList.contains(event.getUserId())) {
					uidList.add(event.getUserId());
					uidDateMap.put(event.getEventTime(), uidList);
				}

			}
		}
		return uidDateMap;
	}

}
