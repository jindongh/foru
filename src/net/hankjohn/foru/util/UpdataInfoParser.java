package net.hankjohn.foru.util;

import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

public class UpdataInfoParser {

	/*
	 * ”√pullΩ‚Œˆ∆˜Ω‚Œˆ∑˛ŒÒ∆˜∑µªÿµƒxmlŒƒº˛ (xml∑‚◊∞¡À∞Ê±æ∫≈)
	 */
	public static UpdateInfo getUpdataInfo(InputStream is) throws Exception{
		XmlPullParser  parser = Xml.newPullParser();  
		parser.setInput(is, "utf-8");//…Ë÷√Ω‚Œˆµƒ ˝æ›‘¥ 
		int type = parser.getEventType();
		UpdateInfo info = new UpdateInfo();// µÃÂ
		while(type != XmlPullParser.END_DOCUMENT ){
			switch (type) {
			case XmlPullParser.START_TAG:
				if("version".equals(parser.getName())){
					info.setVersion(parser.nextText());	//ªÒ»°∞Ê±æ∫≈
				}else if ("url".equals(parser.getName())){
					info.setUrl(parser.nextText());	//ªÒ»°“™…˝º∂µƒAPKŒƒº˛
				}else if ("description".equals(parser.getName())){
					info.setDescription(parser.nextText());	//ªÒ»°∏√Œƒº˛µƒ–≈œ¢
				}
				break;
			}
			type = parser.next();
		}
		return info;
	}
}
