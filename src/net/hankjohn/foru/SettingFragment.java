package net.hankjohn.foru;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SettingFragment extends ForUFragment {

	public final static String URL_REGISTER = "http://hankjohn.net/";
	@Override
	public int getTitle(){
		return R.string.title_sectionSetting;
	}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setting, container, false);
        //Register
        Button btnRegister = (Button)rootView.findViewById(R.id.btn_setting_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setData(Uri.parse(URL_REGISTER));
				intent.setAction(Intent.ACTION_VIEW);
				SettingFragment.this.startActivity(intent);
			}
		});
        return rootView;
    }
}
