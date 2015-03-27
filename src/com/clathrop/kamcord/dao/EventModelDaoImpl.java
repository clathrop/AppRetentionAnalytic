package com.clathrop.kamcord.dao;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kamcordjava.MyDatabase;
import model.EventModel;

public class EventModelDaoImpl implements EventModelDao {

	private static final String KAMCORD_DB_URL = "jdbc:sqlite:kamcorddata.db";
	private static final String SQLITE_JDBC_DRIVER = "org.sqlite.JDBC";
	private static final String USER_ID = "user_id";
	private static final String EVENT_COUNT = "event_count";
	private static final String EVENT_NAME = "event_name";
	private static final String EVENT_TIME = "event_time";
	private static final String OS_NAME = "os_name";
	private static final String SDK_VERSION = "sdk_version";

	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	MyDatabase mydb = null;

	public EventModelDaoImpl() {
		mydb = new MyDatabase(SQLITE_JDBC_DRIVER, KAMCORD_DB_URL);
	}

	@Override
	public List<EventModel> getAllEvents() {
		String selectQuery = "SELECT * FROM sdkdata LIMIT 10";
		List<EventModel> eventList = new ArrayList<EventModel>();

		try {
			conn = mydb.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(selectQuery);

			while (rs.next()) {
				String userId = rs.getString(USER_ID);
				String eventCount = rs.getString(EVENT_COUNT);
				String eventName = rs.getString(EVENT_NAME);
				String eventTime = rs.getString(EVENT_TIME);
				String osName = rs.getString(OS_NAME);
				String sdkVersion = rs.getString(SDK_VERSION);

				EventModel event = new EventModel(userId,
						Integer.parseInt(eventCount), eventName, eventTime,
						osName, sdkVersion);
				eventList.add(event);
			}

			return eventList;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				rs.close();
			} catch (SQLException e1) {
			}
			try {
				stmt.close();
			} catch (SQLException e2) {
			}
			try {
				conn.close();
			} catch (SQLException e3) {
			}
		}
	}

	@Override
	public List<EventModel> findEventsByDateRange(String d1, String d2) {

		String selectQuery = "SELECT * FROM sdkdata WHERE event_name = 'UI_OPEN_COUNT' AND event_time BETWEEN '"
				+ d1 + "' AND '" + d2 + "';";
		List<EventModel> eventList = new ArrayList<EventModel>();

		try {
			conn = mydb.getConnection();
			stmt = conn.createStatement();

			rs = stmt.executeQuery(selectQuery);

			while (rs.next()) {
				String userId = rs.getString(USER_ID);
				String eventCount = rs.getString(EVENT_COUNT);
				String eventName = rs.getString(EVENT_NAME);
				String eventTime = rs.getString(EVENT_TIME);
				String osName = rs.getString(OS_NAME);
				String sdkVersion = rs.getString(SDK_VERSION);

				EventModel event = new EventModel(userId,
						Integer.parseInt(eventCount), eventName, eventTime,
						osName, sdkVersion);
				eventList.add(event);
			}

			return eventList;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				rs.close();
			} catch (SQLException e1) {
			}
			try {
				stmt.close();
			} catch (SQLException e2) {
			}
			try {
				conn.close();
			} catch (SQLException e3) {
			}
		}
	}

	@Override
	public List<EventModel> findEventsByOS(String os) {

		String selectQuery = "SELECT * FROM sdkdata WHERE os_name = '" + os
				+ "';";
		List<EventModel> eventList = new ArrayList<EventModel>();

		try {
			conn = mydb.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(selectQuery);

			while (rs.next()) {
				String userId = rs.getString(USER_ID);
				String eventCount = rs.getString(EVENT_COUNT);
				String eventName = rs.getString(EVENT_NAME);
				String eventTime = rs.getString(EVENT_TIME);
				String osName = rs.getString(OS_NAME);
				String sdkVersion = rs.getString(SDK_VERSION);

				EventModel event = new EventModel(userId,
						Integer.parseInt(eventCount), eventName, eventTime,
						osName, sdkVersion);
				eventList.add(event);
			}

			return eventList;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				rs.close();
			} catch (SQLException e1) {
			}
			try {
				stmt.close();
			} catch (SQLException e2) {
			}
			try {
				conn.close();
			} catch (SQLException e3) {
			}
		}
	}

	@Override
	public List<EventModel> findEventsBySdkVer(String sdkVer) {
		String selectQuery = "SELECT * FROM sdkdata WHERE sdk_version = "
				+ sdkVer + ";";
		List<EventModel> eventList = new ArrayList<EventModel>();

		try {
			conn = mydb.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(selectQuery);

			while (rs.next()) {
				String userId = rs.getString(USER_ID);
				String eventCount = rs.getString(EVENT_COUNT);
				String eventName = rs.getString(EVENT_NAME);
				String eventTime = rs.getString(EVENT_TIME);
				String osName = rs.getString(OS_NAME);
				String sdkVersion = rs.getString(SDK_VERSION);

				EventModel event = new EventModel(userId,
						Integer.parseInt(eventCount), eventName, eventTime,
						osName, sdkVersion);
				eventList.add(event);
			}

			return eventList;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				rs.close();
			} catch (SQLException e1) {
			}
			try {
				stmt.close();
			} catch (SQLException e2) {
			}
			try {
				conn.close();
			} catch (SQLException e3) {
			}
		}
	}

}
