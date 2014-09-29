package net.hankjohn.foru;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.hankjohn.foru.data.ForUCalendar;
import net.hankjohn.foru.data.ForUEvent;
import net.hankjohn.foru.data.Tip;
import net.hankjohn.foru.util.CalendarSelect;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class TipFragment extends ForUFragment {

	//main view
	private Button btnAddTip;
	private View rootView;
	private View addView;
	private ListView lvTips;
	private CalendarSelect clSelect = new CalendarSelect();
	
    private List<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
    private TipAdapter adapter = null;
	
	@Override
	public int getTitle(){
		return R.string.title_sectionTip;
	}
	public void getData(){
		mData.clear();
        List<Tip> tips = ForUCalendar.getInstance(getActivity().getApplicationContext()).getTips();
        for(Tip tip : tips){
        	Map<String, Object> map = new HashMap<String, Object>();  
        	map.put("id", String.valueOf(tip.getId()));
            map.put("title", tip.getDescription());  
            map.put("info", tip.getDate()+" " + tip.getTime() + " ");  
            mData.add(map);  
        }
	}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_tip, container, false);

        //main: list
        addView = rootView.findViewById(R.id.ll_add_tip);
        lvTips = (ListView)rootView.findViewById(R.id.lvTip);
        
        //actions:
        btnAddTip = (Button)rootView.findViewById(R.id.btnAddTip);        
        btnAddTip.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				/*
				if(addView.getVisibility() != View.VISIBLE){
					addView.setVisibility(View.VISIBLE);
				}
				else{
					addView.setVisibility(View.GONE);
					*/
					//TODO: 持久化数据
					EditText et_tip_content = (EditText)addView.findViewById(R.id.et_tip_content);
					EditText et_tip_day = (EditText)addView.findViewById(R.id.et_tip_day);
					EditText et_tip_time = (EditText)addView.findViewById(R.id.et_tip_time);
					Spinner sp_tip_repeat = (Spinner)addView.findViewById(R.id.sp_tip_repeat);
					Tip tip = new Tip(et_tip_content.getText().toString(),
							et_tip_day.getText().toString(),
							et_tip_time.getText().toString(),
							sp_tip_repeat.getSelectedItem().toString());
		        	ForUCalendar.getInstance(TipFragment.this.getActivity().getApplicationContext()).addEvent(tip);
		        	refreshData();
				//}
			}     	
        });
        clSelect.initView(this,
        		(EditText)addView.findViewById(R.id.et_tip_day),
        		(EditText)addView.findViewById(R.id.et_tip_time));
        List<String> repeatList = new ArrayList<String>();
        repeatList.add(ForUEvent.REPEAT_NONE);
        repeatList.add(ForUEvent.REPEAT_DAILY);
        repeatList.add(ForUEvent.REPEAT_MONTH);
        repeatList.add(ForUEvent.REPEAT_WEEK);
        repeatList.add(ForUEvent.REPEAT_YEAR);
        ArrayAdapter<String> repeatAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, repeatList);
        Spinner spRepeat = (Spinner)addView.findViewById(R.id.sp_tip_repeat);
        spRepeat.setAdapter(repeatAdapter);
        
        //data
        adapter = new TipAdapter(this.getActivity());
        refreshData();
        return rootView;
    }
    public void refreshData(){
    	getData();
    	lvTips.setAdapter(adapter);
    }
    // listview中点击按键弹出对话框  
    public void showInfo(final int position) {  
        new AlertDialog.Builder(this.getActivity()).setTitle("我的提示").setMessage("确定要删除吗？")                  
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {  
                    @Override  
                    public void onClick(DialogInterface dialog, int which) {  
                    	long id = Long.parseLong((String)mData.get(position).get("id"));
                    	ForUCalendar.getInstance(getActivity().getApplicationContext()).rmEvent(id);
                        refreshData();
                    }  
                }).show();  
    }  
    final class ViewHolder {  
        public TextView title;  
        public TextView info;  
        public Button viewBtn;  
    } 
    public class TipAdapter extends BaseAdapter {  
        private LayoutInflater mInflater;  
  
        public TipAdapter(Context context) {  
            this.mInflater = LayoutInflater.from(context);  
        }  
  
        @Override  
        public int getCount() {  
            return mData.size();  
        }  
  
        @Override  
        public Object getItem(int arg0) {  
            return null;  
        }  
  
        @Override  
        public long getItemId(int arg0) {  
            return 0;  
        }  
        @Override  
        public View getView(final int position, View convertView,  
                ViewGroup parent) {  
            ViewHolder holder = null;  
            if (convertView == null) {  
                holder = new ViewHolder();  
                convertView = mInflater.inflate(R.layout.list_tip, null);  
                holder.title = (TextView) convertView  
                        .findViewById(R.id.list_tip_title);  
                holder.info = (TextView) convertView  
                        .findViewById(R.id.list_tip_info);  
                holder.viewBtn = (Button) convertView  
                        .findViewById(R.id.list_tip_button);  
                convertView.setTag(holder);  
            } else {  
                holder = (ViewHolder) convertView.getTag();  
            }  
            holder.title.setText((String) mData.get(position).get("title"));  
            holder.info.setText((String) mData.get(position).get("info"));  
            holder.viewBtn.setOnClickListener(new OnClickListener() {  
                @Override  
                public void onClick(View v) {  
                    showInfo(position);  
                }  
            });  
            return convertView;  
        }  
    }  
}
