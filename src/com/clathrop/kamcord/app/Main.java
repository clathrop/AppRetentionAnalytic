package com.clathrop.kamcord.app;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
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
    private static DateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);


    /*
    One of the metrics we use to measure the performance of our SDK is
    Day­7 UI retention​.  We define Day­7 UI retention as the percent of users
    who re­open the Kamcord UI ​exactly ​7 days after the date of their first
    Kamcord UI open.  Note that whether a user re­opens the Kamcord UI on
    Day 6 or Day 8 is irrelevant to this metric.

    For example, say we were looking to measure Day­7 UI retention over the
    first two days of October 2014.  Twenty users opened the Kamcord UI for
    the first time on 10/1.  On 10/8, four of these twenty users re­opened the
    UI.  On 10/2, five users opened the Kamcord UI for the first time and two
    of these users re­opened the UI on 10/9.  The SDK’s Day­7 UI retention
    from 10/1 to 10/2 would then be (4+2) / (20+5) = 24%.
     */

    public static void main(String[] args) {

        double sevenDayRetentionPercentage = calculateDay7Retention("2014-09-01",
                "2014-09-07");

        System.out.println("Seven Day Retention Percentage for " + "2014-09-01"
                + " through " + "2014-09-07" + ": " + sevenDayRetentionPercentage
                + "%");
    }

    /*
     * Given a start and end date, this function will calculate the 7 day ui retention rate
     * of users who opened the app on day1 that also opened the app on day7 for the given range.
     */
    public static double calculateDay7Retention(String startDate, String endDate) {

        EventModelDao eventDao = new EventModelDaoImpl();

        //get dates for end range

        MyDate sDate = MyDate.parseDate(startDate);
        MyDate eDate = MyDate.parseDate(endDate);
        String day7StartDate = sDate.add7Days().toString();
        String day7EndDate = eDate.add7Days().toString();

        List<EventModel> eventListStartRange = eventDao.findEventsByDateRange(
                startDate, endDate);

        List<EventModel> eventListEndRange = eventDao.findEventsByDateRange(
                day7StartDate, day7EndDate);

        // contains <DATE, Set<UIDS> >
        HashMap<String, HashSet<String>> uidDateMap1 = generateDateUidMap(eventListStartRange);
        HashMap<String, HashSet<String>> uidDateMap2 = generateDateUidMap(eventListEndRange);

        double uiOpenCount;
        double top = 0;
        double bottom = 0;
        // for each date
        for (Entry<String, HashSet<String>> entry : uidDateMap1.entrySet()) {
            uiOpenCount = entry.getValue().size();
            Entry<String, HashSet<String>> sevenDayLaterEntry = null;
            // get matching 7 day later entry
            for (Entry<String, HashSet<String>> sevenDayEntry : uidDateMap2.entrySet()) {

                String matchDate = MyDate.parseDate(entry.getKey()).add7Days().toString();

                if (matchDate.equals(sevenDayEntry.getKey())) {
                    sevenDayLaterEntry = sevenDayEntry;
                    break;
                }
            }
            double sevenDayUidOpenCount = 0;
            HashSet<String> sevenDayUids;
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
            bottom = bottom + uiOpenCount;
            top = top + sevenDayUidOpenCount;
        }

        double sevenDayRetention = (top / bottom) * ONE_HUNDRED_PERCENT;

        return sevenDayRetention;

    }


    /*
    *Generates and returns a HashMap with a key of date and a list of uids that triggered
    *an OPEN_UI_EVENT on that date
    *
    *eg: {2014-09-01=[9019382, 2934028, 2093829], 2014-09-02=[9019382, 2934028]}
    */
    private static HashMap<String, HashSet<String>> generateDateUidMap(List<EventModel> eventList) {
        HashMap<String, HashSet<String>> uidDateMap = new HashMap<String, HashSet<String>>();
        HashSet<String> uidSet = new HashSet<String>();

        for (EventModel event : eventList) {
            // if date not already added, set date and new set with single uid in it
            if (!uidDateMap.containsKey(event.getEventTime())) {
                uidSet = new HashSet<String>();
                uidSet.add(event.getUserId());
                uidDateMap.put(event.getEventTime(), uidSet);
            } else {
                // if date exists, find entry in hashmap, get list of uids and
                // add to it and set back into hashmap
                for (Entry<String, HashSet<String>> entry : uidDateMap.entrySet()) {
                    if (entry.getKey().equals(event.getEventTime())) {
                        uidSet = entry.getValue();
                        break;
                    }
                }
                if (!uidSet.contains(event.getUserId())) {
                    uidSet.add(event.getUserId());
                    uidDateMap.put(event.getEventTime(), uidSet);
                }

            }
        }
        return uidDateMap;
    }

}
