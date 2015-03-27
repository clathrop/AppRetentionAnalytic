package com.clathrop.kamcord.dao;

import java.util.List;

import model.EventModel;

public interface EventModelDao {
	
	public List<EventModel> getAllEvents();
	public List<EventModel> findEventsByDateRange(String d1, String d2);
	public List<EventModel> findEventsByOS(String os);
	public List<EventModel> findEventsBySdkVer(String sdkVer);

}
