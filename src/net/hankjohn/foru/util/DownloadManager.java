package net.hankjohn.foru.util;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.ProgressDialog;
import android.os.Environment;
import android.util.Log;

public class DownloadManager {

	/**
	 * ¥”∑˛ŒÒ∆˜œ¬‘ÿapk
	 * @param path
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public static File getFileFromServer(String path, ProgressDialog pd) throws Exception{
		//»Áπ˚œ‡µ»µƒª∞±Ì æµ±«∞µƒsdcardπ“‘ÿ‘⁄ ÷ª˙…œ≤¢«“ «ø…”√µƒ
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			URL url = new URL(path);
			HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			//ªÒ»°µΩŒƒº˛µƒ¥Û–° 
			pd.setMax(conn.getContentLength());
			Log.d("DownloadManager", "Max="+conn.getContentLength());
			InputStream is = conn.getInputStream();
			File file = new File(Environment.getExternalStorageDirectory(), "updata.apk");
			FileOutputStream fos = new FileOutputStream(file);
			BufferedInputStream bis = new BufferedInputStream(is);
			byte[] buffer = new byte[10240];
			int len ;
			int total=0;
			while((len =bis.read(buffer))!=-1){
				fos.write(buffer, 0, len);
				total+= len;
				pd.setProgress(total);
			}
			fos.close();
			bis.close();
			is.close();
			return file;
		}
		else{
			return null;
		}
	}

}
