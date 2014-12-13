package com.jerry.eventful.helper;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.jerry.eventful.database.Eventful;
import com.jerry.eventful.R;

public class EventsAdapter extends ArrayAdapter<Eventful> implements Filterable {
	
	private List<Eventful> eventsList;
	private List<Eventful> origEventsList;
	private Context context;
	
	private Filter eventFilter;
	
	public EventsAdapter(List<Eventful> eventsList, Context context){
		super(context, R.layout.list_events, eventsList);
		this.eventsList = eventsList;
		this.context = context;
		this.origEventsList = eventsList;
	}
	
	public void setList(List<Eventful> eventsList){
		this.eventsList = eventsList;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return eventsList.size();
	}

	@Override
	public Eventful getItem(int position) {
		// TODO Auto-generated method stub
		return eventsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return eventsList.get(position).hashCode();
	}
	
	public View getView(int position, View convertView, ViewGroup parent){
		View v = convertView;
		EventsHolder holder = new EventsHolder();

		if(convertView==null){
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.list_events, null);

			TextView tv = (TextView)v.findViewById(R.id.events);
			TextView dateOfEvent = (TextView)v.findViewById(R.id.date_of_event);
			TextView timeFromEvent = (TextView)v.findViewById(R.id.time_from_event);

			holder.eventNameView = tv;
			holder.dateOfEventView = dateOfEvent;
			holder.timeView = timeFromEvent;

			v.setTag(holder);
		}else
			holder = (EventsHolder) v.getTag();
		
		Eventful e = eventsList.get(position);
		holder.eventNameView.setText(e.getEvent());
		holder.dateOfEventView.setText(e.getReadableTime());
		holder.timeView.setText(Html.fromHtml(e.getAgoString()));
		
		
		return v;
	}
	
	public void resetData(){
		eventsList = origEventsList;
	}
	
	private static class EventsHolder {
		public TextView eventNameView;
		public TextView dateOfEventView;
		public TextView timeView;
	}
	
	@Override
	public Filter getFilter() {
		if (eventFilter == null)
			eventFilter = new EventFilter();

		return eventFilter;
	}

	private class EventFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results = new FilterResults();
			// We implement here the filter logic
			if (constraint == null || constraint.length() == 0) {
				// No filter implemented we return all the list
				results.values = origEventsList;
				results.count = origEventsList.size();
			}
			else {
				// We perform filtering operation
				List<Eventful> nEventList = new ArrayList<Eventful>();

				for (Eventful e : eventsList) {
					if (e.getEvent().toUpperCase().contains(constraint.toString().toUpperCase()))
						nEventList.add(e);
				}

				results.values = nEventList;
				results.count = nEventList.size();

			}
			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {

			// Now we have to inform the adapter about the new list filtered
			if (results.count == 0)
				notifyDataSetInvalidated();
			else {
				eventsList = (List<Eventful>) results.values;
				notifyDataSetChanged();
			}

		}

}
	

}
