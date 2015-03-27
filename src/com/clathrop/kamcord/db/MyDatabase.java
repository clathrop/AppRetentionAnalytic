package com.clathrop.kamcord.db;

public class MyDatabase extends Database {

	public MyDatabase(String sDriverKey, String sUrlKey) {

		try {
			init(sDriverKey, sUrlKey);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (conn != null) {
//			System.out.println("Connected OK using " + sDriverKey + " to "
//					+ sUrlKey);
		} else {
			System.out.println("Connection failed");
		}
	}

}