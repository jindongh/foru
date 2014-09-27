package net.hankjohn.foru;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.hankjohn.foru.data.ForUCalendar;
import net.hankjohn.foru.data.ForUItem;
import net.hankjohn.foru.util.CalendarSelect;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class TodayFragment extends ForUFragment{

	private ListView lvToday;
	private EditText etDay;
	private List<Map<String,Object>> list;
	private TodoAdapter aa;;
	private CalendarSelect clSelect = new CalendarSelect();
	@Override
	public int getTitle(){
		return R.string.title_sectionToday;
	}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_today, container, false);
        //ListView of todos
        list = new ArrayList<Map<String,Object>>();
        aa = new TodoAdapter(getActivity());
        lvToday = (ListView)rootView.findViewById(R.id.lvToday);
        //etDay
        etDay = (EditText)rootView.findViewById(R.id.et_todo_day);
        clSelect.initView(this, etDay, null);
        etDay.addTextChangedListener(new TextWatcher(){
			@Override
			public void afterTextChanged(Editable arg0) {
				dataChanged();
			}
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}
        });
        
        //btnToday
        Button btnToday = (Button)rootView.findViewById(R.id.btn_today);
        btnToday.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				clSelect.setToday();
			}
		});
        
        dataChanged();
        return rootView;
    }
    public void dataChanged(){
    	list.clear();
    	List<ForUItem> items = ForUCalendar.getInstance(getActivity()).getTodo(clSelect.getCalendar());
    	for(ForUItem item : items){
    		Map<String,Object> map = new HashMap<String,Object>();
    		map.put("info", item.toString());
    		list.add(map);
    	}
    	lvToday.setAdapter(aa);
    }
    final class ViewHolder {  
        public TextView info;  
        public CheckBox cbDone;  
    } 
    public class TodoAdapter extends BaseAdapter {  
        private LayoutInflater mInflater;  
  
        public TodoAdapter(Context context) {  
            this.mInflater = LayoutInflater.from(context);  
        }  
  
        @Override  
        public int getCount() {  
            return list.size();  
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
                convertView = mInflater.inflate(R.layout.list_today, null); 
                holder.info = (TextView) convertView  
                        .findViewById(R.id.tv_today_info);  
                holder.cbDone = (CheckBox) convertView  
                        .findViewById(R.id.cb_today);  
                convertView.setTag(holder);  
            } else {  
                holder = (ViewHolder) convertView.getTag();  
            }  
            holder.info.setText((String) list.get(position).get("info"));  
            holder.cbDone.setOnClickListener(new OnClickListener() {  
                @Override  
                public void onClick(View v) {
                	Log.w("Today", "Checked"+position);
                }  
            });  
            return convertView;  
        }  
    }
}
