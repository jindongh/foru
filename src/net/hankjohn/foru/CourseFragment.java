package net.hankjohn.foru;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.hankjohn.foru.data.Course;
import net.hankjohn.foru.data.ForUCalendar;
import net.hankjohn.foru.util.CalendarSelect;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class CourseFragment extends ForUFragment {

	 final List<View> views = new ArrayList<View>();
	
	@Override
	public int getTitle() {
		return R.string.title_sectionCourse;
	}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_course, container, false);
        final List<String> titles = new ArrayList<String>();
        views.clear();
        for(int i=0; i<Course.array.length; i++){
        	View dayOfWeek = inflater.inflate(R.layout.course_day,  container, false);
        	views.add(dayOfWeek);
        	titles.add(Course.array[i]);
        	initCourse(dayOfWeek);
        }
        PagerAdapter mPageAdapter = new PagerAdapter(){
			@Override
			public int getCount() {
				return Course.array.length;
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}
			@Override
            public void destroyItem(View container, int position, Object object) {
                ((ViewPager)container).removeView(views.get(position));
            }
 
            @Override
            public CharSequence getPageTitle(int position) {
                return titles.get(position);
            }
 
            @Override
            public Object instantiateItem(View container, int position) {
                ((ViewPager)container).addView(views.get(position));
                return views.get(position);
            }
        	
        };
        ViewPager mViewPager = (ViewPager)rootView.findViewById(R.id.viewpager);
        mViewPager.setAdapter(mPageAdapter);
        return rootView;
    }
    private void initCourse(View cview){
    	String week = getWeek(cview);
    	//action
    	Button btnAdd = (Button)cview.findViewById(R.id.btn_course_add);
    	AddActionListener listener = new AddActionListener(week, cview, this);
    	btnAdd.setOnClickListener(listener);
    	
    	//data
    	ListView lvCourse = (ListView)cview.findViewById(R.id.lv_course);
        lvCourse.setAdapter(listener.getAdapter());
    }
    private String getWeek(View cview){
    	for(int i = 0; i < views.size(); i ++){
    		if(views.get(i) == cview){
    			return Course.array[i];
    		}
    	}
    	return Course.array[0];
    }
    public class AddActionListener implements View.OnClickListener{
    	private String week;
    	private View view;
    	CourseAdapter adapter;
    	ListView lvCourse;
    	private CalendarSelect clSelect = new CalendarSelect();
    	public AddActionListener(String week, View view, Fragment fragment){
    		this.week = week;
    		this.view = view;

        	lvCourse = (ListView)view.findViewById(R.id.lv_course);
    		adapter = new CourseAdapter(week, fragment.getActivity(), lvCourse);

            clSelect.initView(fragment,
            		null,
            		(EditText)view.findViewById(R.id.et_course_time));
    	}
		@Override
		public void onClick(View arg0) {
			EditText etCourse = (EditText)this.view.findViewById(R.id.et_course_name);
			EditText etTime = (EditText)this.view.findViewById(R.id.et_course_time);
			Course c = new Course(etCourse.getText().toString(),
					this.week,
					etTime.getText().toString());
			adapter.addCourse(c);
			lvCourse.setAdapter(adapter);
		}
		public BaseAdapter getAdapter(){
			return adapter;
		}
	};
	final class ViewHolder {  
        public TextView title;  
        public TextView info;  
        public Button viewBtn;  
    } 
	public class CourseAdapter extends BaseAdapter {  
        private LayoutInflater mInflater;  
        ListView courses;
        private Context context;
        private String week;
        private List<Map<String, Object>> mData = new ArrayList<Map<String,Object>>();
  
        public CourseAdapter(String week, Context context, ListView courses) {  
            this.mInflater = LayoutInflater.from(context);  
            this.courses = courses;
            this.context = context;
            this.week = week;
            dataChanged();
        }  
        public void dataChanged(){
        	mData.clear();
        	List<Course> courses = ForUCalendar.getInstance(context).getCourses(week);
        	for(Course course : courses){
            	Map<String,Object> map = new HashMap<String, Object>();
            	map.put("id",  String.valueOf(course.getId()));
            	map.put("title", course.getDescription());
            	map.put("info", course.getTime());
            	mData.add(map);
        	}
        }
        public void addCourse(Course c){
			ForUCalendar.getInstance(getActivity()).addCourse(c);
			dataChanged();
        }
        public void rmCourse(int position){
        	long id = Long.parseLong((String)mData.get(position).get("id"));
        	ForUCalendar.getInstance(getActivity()).rmEvent(id);
        	dataChanged();
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
                	CourseAdapter.this.rmCourse(position);
                	//rm
                	courses.setAdapter(CourseAdapter.this);
                }  
            });  
            return convertView;  
        }  
    }  
}
