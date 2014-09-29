package net.hankjohn.foru.util;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import net.hankjohn.foru.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;


public class UpgradeUtil{
	private Activity activity;
	private UpdateInfo info;
	

	private final String TAG = this.getClass().getName();

	private final int UPDATA_NONEED = 0;
	private final int UPDATA_CLIENT = 1;
	private final int GET_UNDATAINFO_ERROR = 2;
	private final int SDCARD_NOMOUNTED = 3;
	private final int DOWN_ERROR = 4;
	
	private String packageName;
	private String localVersion;
	public void checkVersion(Activity activity){
		this.activity = activity;
		PackageManager packageManager = activity.getPackageManager();
		try{
			PackageInfo packInfo = packageManager.getPackageInfo(activity.getPackageName(),0);
			packageName = packInfo.packageName;
			localVersion = packInfo.versionName;
		}
		catch(Exception e){
			
		}
		CheckVersionTask cv = new CheckVersionTask();
		new Thread(cv).start();
	}
	/*
	 * ¥”∑˛ŒÒ∆˜ªÒ»°xmlΩ‚Œˆ≤¢Ω¯––±»∂‘∞Ê±æ∫≈
	 */
	public class CheckVersionTask implements Runnable {
		public void run() {
			try {
				// ¥”◊ ‘¥Œƒº˛ªÒ»°∑˛ŒÒ∆˜ µÿ÷∑
				String path = activity.getResources().getString(R.string.url_server) + packageName;
				// ∞¸◊∞≥…urlµƒ∂‘œÛ
				URL url = new URL(path);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(5000);
				InputStream is = conn.getInputStream();
				info = UpdataInfoParser.getUpdataInfo(is);
				System.out.println("VersionActivity            ----------->          info = "
								+ info);
				if (info.getVersion().equals(localVersion)) {
					Log.i(TAG, "版本一致"+info.getVersion());
					Message msg = new Message();
					msg.what = UPDATA_NONEED;
					handler.sendMessage(msg);
				} else {
					Log.i(TAG, info.getVersion() + info.getDescription());
					Message msg = new Message();
					msg.what = UPDATA_CLIENT;
					handler.sendMessage(msg);
				}
			} catch (Exception e) {
				Message msg = new Message();
				msg.what = GET_UNDATAINFO_ERROR;
				handler.sendMessage(msg);
				e.printStackTrace();
			}
		}
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case UPDATA_NONEED:
				Toast.makeText(activity.getApplicationContext(), "版本号相同无需升级",
						Toast.LENGTH_SHORT).show();
				break;
			case UPDATA_CLIENT:
				showUpdataDialog();
				break;
			case GET_UNDATAINFO_ERROR:
				// ∑˛ŒÒ∆˜≥¨ ±
				Toast.makeText(activity.getApplicationContext(), "获取服务器更新信息失败", 1)
						.show();
				// LoginMain();
				break;
			case SDCARD_NOMOUNTED:
				// sdcard≤ªø…”√
				Toast.makeText(activity.getApplicationContext(), "SD卡不可用",1).show();
				break;
			case DOWN_ERROR:
				// œ¬‘ÿapk ß∞‹
				Toast.makeText(activity.getApplicationContext(), "下载新版本失败", 1).show();
				// LoginMain();
				break;
			}
		}
	};

	/*
	 * 
	 * µØ≥ˆ∂‘ª∞øÚÕ®÷™”√ªß∏¸–¬≥Ã–Ú
	 * 
	 * µØ≥ˆ∂‘ª∞øÚµƒ≤Ω÷Ë£∫ 1.¥¥Ω®alertDialogµƒbuilder. 2.“™∏¯builder…Ë÷√ Ù–‘, ∂‘ª∞øÚµƒƒ⁄»›,—˘ Ω,∞¥≈•
	 * 3.Õ®π˝builder ¥¥Ω®“ª∏ˆ∂‘ª∞øÚ 4.∂‘ª∞øÚshow()≥ˆ¿¥
	 */
	protected void showUpdataDialog() {
		AlertDialog.Builder builer = new Builder(activity);
		builer.setTitle("版本升级");
		builer.setMessage(info.getDescription());
		builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Log.i(TAG, "开始升级");
				downLoadApk();
			}
		});
		// µ±µ„»°œ˚∞¥≈• ±Ω¯––µ«¬º
		builer.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				// LoginMain();
			}
		});
		AlertDialog dialog = builer.create();
		dialog.show();
	}

	/*
	 * ¥”∑˛ŒÒ∆˜÷–œ¬‘ÿAPK
	 */
	protected void downLoadApk() {
		final ProgressDialog pd; // Ω¯∂»Ãı∂‘ª∞øÚ
		pd = new ProgressDialog(activity);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("正在下载更新");
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			Message msg = new Message();
			msg.what = SDCARD_NOMOUNTED;
			handler.sendMessage(msg);
		} else {
			pd.show();
			new Thread() {
				@Override
				public void run() {
					try {
						File file = DownloadManager.getFileFromServer(
								info.getUrl(), pd);
						sleep(1000);
						installApk(file);
						pd.dismiss(); // Ω· ¯µÙΩ¯∂»Ãı∂‘ª∞øÚ

					} catch (Exception e) {
						Message msg = new Message();
						msg.what = DOWN_ERROR;
						handler.sendMessage(msg);
						e.printStackTrace();
					}
				}
			}.start();
		}
	}

	// ∞≤◊∞apk
	protected void installApk(File file) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		this.activity.startActivity(intent);
	}

}
