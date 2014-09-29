package net.hankjohn.foru.data;

import java.util.List;

import android.content.Context;

public interface ForUWatcher {
	void onUpdate(List<ForUItem> items, Context context);
}
