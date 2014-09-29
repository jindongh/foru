package net.hankjohn.foru.util;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.util.Log;
import junit.framework.TestCase;

public class Test  {
	public void testDate(){
		String date = "2014-08-27";
		String time = "08:04";
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm");
		Calendar due = Calendar.getInstance();
		try{
			due.setTime(sdf.parse(date + " " + time));
		}
		catch(Exception pe){
			Log.w("parseTime", Log.getStackTraceString(pe));
		}
	}
	public static void main(String args[]){
		System.out.println("Hello");
		String arguments = "/Users/hankjohn/Documents/apks/bin.xml";
		try {
			AXmlResourceParser parser = new AXmlResourceParser();
			parser.open(new FileInputStream(arguments));
			StringBuilder indent = new StringBuilder(10);
			final String indentStep = " ";
			while (true) {
				int type = parser.next();
				System.out.println("Get Type=========="+type);
				if (type == AXmlResourceParser.END_DOCUMENT) {
					break;
				}
				if(type == AXmlResourceParser.START_TAG){
					for (int i = 0; i != parser.getAttributeCount(); ++i) {
						if ("versionName".equals(parser.getAttributeName(i))) {
							System.out.println(parser.getAttributeValue(i));
						}
						if("package".equals(parser.getAttributeName(i))){
							System.out.println(parser.getAttributeValue(i));					
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
