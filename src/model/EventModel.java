package model;

public class EventModel {
	
	private String userId;
	private Integer eventCount;
	private String eventName;
	private String eventTime;
	private String osName;
	private String sdkVersion;
	
	
	public EventModel(String userId, Integer eventCount, String eventName,
			String eventTime, String osName, String sdkVersion) {
		setUserId(userId);
		setEventCount(eventCount);
		setEventName(eventName);
		setEventTime(eventTime);
		setOsName(osName);
		setSdkVersion(sdkVersion);
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public int getEventCount() {
		return eventCount;
	}
	public void setEventCount(int eventCount) {
		this.eventCount = eventCount;
	}
	public String getEventTime() {
		return eventTime;
	}
	public void setEventTime(String eventTime) {
		this.eventTime = eventTime;
	}
	public String getOsName() {
		return osName;
	}
	public void setOsName(String osName) {
		this.osName = osName;
	}
	public String getSdkVersion() {
		return sdkVersion;
	}
	public void setSdkVersion(String sdkVersion) {
		this.sdkVersion = sdkVersion;
	}
	
	
	

}
