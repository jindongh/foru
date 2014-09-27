package net.hankjohn.foru.data;

import java.util.Calendar;

public class ForUItem {
	ForUEvent event;
	boolean isDone;
	public ForUItem(){
		isDone = false;
	}
	public ForUItem(ForUEvent event){
		this.event = event;
		isDone = false;
	}
	public String toString(){
		return event.getTime() + " " + event.getDescription();
	}
}
