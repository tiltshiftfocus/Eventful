package com.jerry.eventful;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;

public class EventsAdapter2 extends BaseAdapter {
	
	private static ArrayList<Eventful> eventsArrayList;
	private LayoutInflater mInflater;

	public EventsAdapter2(Context context, ArrayList<Eventful> data) {
		eventsArrayList = data;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return eventsArrayList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return eventsArrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_events, null);
			holder = new ViewHolder();
			holder.txtEvent = (TextView) convertView.findViewById(R.id.events);
			holder.txtTime = (TextView) convertView.findViewById(R.id.time_from_event);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.txtEvent.setText(eventsArrayList.get(position).getEvent());
		holder.txtTime.setText(eventsArrayList.get(position).getTime().toString());

		return convertView;
	}

	static class ViewHolder {
		TextView txtEvent;
		TextView txtTime;

	}
	
	public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                eventsArrayList = (ArrayList<Eventful>) results.values;
                EventsAdapter2.this.notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<Eventful> filteredResults = getFilteredResults(constraint);

                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }

			private ArrayList<Eventful> getFilteredResults(CharSequence constraint) {
				// TODO Auto-generated method stub
				return null;
			}
        };
    }
}
	
