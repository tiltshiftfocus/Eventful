package com.jerry.eventful;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

public class MainActivity extends Activity {

	private int EDIT_EVENT_REQUEST = 0;
	private String PACKAGE_NAME;

	
	private String filterText = "";

	private EditText searchBox;
	private ListView eventsLV;

	private EventsAdapter adapEvents;
	private List<Eventful> eventsList = null;
	private EventfulDB eventDB;

	private Runnable viewEvents;
	private Timer timer;
	final Handler mHandler = new Handler();

	final Runnable mRebuildView = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			MainActivity.this.initList();
			adapEvents.setList(eventsList);
			adapEvents.getFilter().filter(filterText.toString());
		}
	};

	final Runnable mUpdateView = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			MainActivity.this.adapEvents.notifyDataSetChanged();
		}
	};
	
	private ProgressDialog m_ProgressDialog = null;
	private Runnable returnRes = new Runnable()
	{
		public void run()
		{
			Iterator localIterator = null;
			if (MainActivity.this.eventsList != null){
				MainActivity.this.adapEvents.notifyDataSetChanged();
				MainActivity.this.adapEvents.clear();
				localIterator = MainActivity.this.eventsList.iterator();
			}
			while(true){
				if (!localIterator.hasNext()){
					MainActivity.this.m_ProgressDialog.dismiss();
					MainActivity.this.adapEvents.notifyDataSetChanged();
					return;
				}
				Eventful localEvent = (Eventful)localIterator.next();
				MainActivity.this.adapEvents.add(localEvent);
			}
		}
	};

	@Override
	public void onContentChanged() {
	    super.onContentChanged();

	    View empty = findViewById(R.id.emptyList);
	    ListView list = (ListView) findViewById(R.id.eventsList);
	    list.setEmptyView(empty);

	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		PACKAGE_NAME = this.getPackageName();
		
		SharedPreferences pref = this.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);
		if(pref.getBoolean("darktheme", false)==true){
			setTheme(R.style.AppDarkTheme);
		}else
			setTheme(R.style.AppTheme);
		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		

		eventDB = new EventfulDB(this);
		eventDB.open();
		eventsList = new ArrayList<Eventful>();

		eventsLV = (ListView)findViewById(R.id.eventsList);
		eventsLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				return MainActivity.this.onLongListItemClick(view, position, id);
			}
		});

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.attachToListView(eventsLV);
        fab.setOnClickListener(new FloatingActionButton.OnClickListener(){

            @Override
            public void onClick(View v) {
                newEvent();
            }
        });

		
		adapEvents = new EventsAdapter(eventsList,this);
		eventsLV.setAdapter(adapEvents);

		this.viewEvents = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				MainActivity.this.initList();

			}
		};
		new Thread(null, this.viewEvents, "MagentoBackground").start();
		this.m_ProgressDialog = ProgressDialog.show(this, "Please wait...", "Populating data ...", true);




		/*eventsLV.setTextFilterEnabled(true);
		searchBox = (EditText)findViewById(R.id.eventSearch);	
		searchBox.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(count < before){
					adapEvents.resetData();
					filteredList = eventsList;
				}

				adapEvents.getFilter().filter(s.toString());
				filteredList = adapEvents.getFilteredList();
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				filteredList = adapEvents.getFilteredList();
				
			}
		});*/

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_activity_actions, menu);
		
		SharedPreferences pref = this.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);
		boolean themePref = pref.getBoolean("darktheme", false);
		
		menu.findItem(R.id.darkTheme).setChecked(themePref);

		// Associate searchable configuration with the SearchView
		SearchManager searchManager =
				(SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView =
				(SearchView) menu.findItem(R.id.search).getActionView();
		searchView.setSearchableInfo(
				searchManager.getSearchableInfo(getComponentName()));

		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub
				adapEvents.getFilter().filter(newText.toString());
				filterText = newText;
				return false;

				//filterText = newText;
				
			}
		});


		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		SharedPreferences pref = this.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);
		Editor edit = pref.edit();
		
		
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		/*case R.id.add_event:
			//openAddDialog();
			newEvent();
			return true;*/
		/*case R.id.settings:
			Intent i = new Intent(this,SettingsActivity.class);
			startActivity(i);*/
		case R.id.darkTheme:
			if(item.isChecked()){
				item.setChecked(false);
				edit.putBoolean("darktheme", false);
				edit.commit();
				setTheme(R.style.AppDarkTheme);
				this.recreate();
			}else{
				item.setChecked(true);
				edit.putBoolean("darktheme", true);
				edit.commit();
				setTheme(R.style.AppTheme);
				this.recreate();
			}
			return true;
        case R.id.importDB:
            importDB();
            return true;

        case R.id.exportDB:
            exportDB();
            return true;



		case R.id.about:
			Intent i = new Intent(this, AboutActivity.class);
			startActivity(i);
		default:
			return super.onOptionsItemSelected(item);
		}
	}


	/*private List<Events> getEvents(){
		ArrayList<Events> data = new ArrayList<Events>();
		Events ev1 = new Events();
		ev1.setEvent("Completed this game");
		ev1.setTimeFromEvent("1 hour ago");
		data.add(ev1);

		ev1 = new Events();
		ev1.setEvent("Watched this movie");
		ev1.setTimeFromEvent("10 hours ago");
		data.add(ev1);

		return data;

	}*/

	@Override
	protected void onStart(){
		super.onStart();
		startScreenUpdates();
	}

	@Override
	protected void onStop(){
		super.onStop();
		stopScreenUpdates();
	}

	private void startScreenUpdates(){
		timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				MainActivity.this.mHandler.post(MainActivity.this.mUpdateView);
			}
		},0L,1000);
	}

	private void stopScreenUpdates() {
		if (timer != null) {
			timer.cancel();
		}
	}

	private void initList(){

		eventsList = new ArrayList<Eventful>();
		Cursor localCursor = eventDB.all();
		if(localCursor.getCount()>=0){
			localCursor.moveToFirst();
			for(;;){
				if(localCursor.isAfterLast()){
					localCursor.close();
					runOnUiThread(returnRes);
					return;
				}
				int i = localCursor.getInt(0);
				String str = localCursor.getString(1);
				long l = localCursor.getLong(2);
				Time localTime = new Time();
				localTime.set(l);
				eventsList.add(new Eventful(this, i,str,localTime));
				localCursor.moveToNext();
			}//while(localCursor.moveToNext());
		}
	}

	/* Test dialog add event code */
	/* ******** UNUSED ******** 
	public void openAddDialog(){
		final Dialog d = new Dialog(this);
		d.setContentView(R.layout.newevent_activity);
		d.setTitle("New event");
		d.setCancelable(true);

		final EditText edit = (EditText)d.findViewById(R.id.editTextEvent);
		Button b = (Button)d.findViewById(R.id.button1);
		b.setOnClickListener(new View.OnClickListener() {


			public void onClick(View v) {
				// TODO Auto-generated method stub
				String eventName = edit.getText().toString();
				//MainActivity.this.eventsList.add(new Eventful(eventName, "dummy"));
				MainActivity.this.adapEvents.notifyDataSetChanged();
				d.dismiss();
			}
		});


		d.show();
	}
	 ************************ */

	public void newEvent(){
		Intent i = new Intent(this,EditEventActivity.class);
		startActivityForResult(i, EDIT_EVENT_REQUEST);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		/*String str;
		long l;
		int i;

		if((requestCode == this.EDIT_EVENT_REQUEST) && (resultCode == RESULT_OK)){
			str = data.getExtras().getString("eventname");
			l = data.getExtras().getLong("when");
			i = data.getExtras().getInt("id");
			if(!str.isEmpty() && str.length()>0){
				//MainActivity.this.eventsList.add(new Eventful(str, "dummy"));
				ContentValues localContentValues2 = new ContentValues();
		        localContentValues2.put("name", str);
		        localContentValues2.put("when_db", Long.valueOf(l));
		        eventDB.addItem(localContentValues2);
				this.adapEvents.notifyDataSetChanged();
			}else
				this.adapEvents.notifyDataSetChanged();
		}else if((requestCode == this.EDIT_EVENT_REQUEST) && (resultCode == RESULT_CANCELED)){
			Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
		}*/
		
		String str;
		long l;
		int i;
		
		if((requestCode == this.EDIT_EVENT_REQUEST) && (resultCode == RESULT_OK)){
			str = data.getStringExtra("eventname");
			l = data.getLongExtra("when", 0L);
			i = data.getIntExtra("id", -1);
			
			if(!data.getBooleanExtra("delete",false)){
				while(true){
					mHandler.post(mRebuildView);
					if(i>=0){
						if (data.getBooleanExtra("delete",false)) {
							this.eventDB.deleteItem(i);
						}
						ContentValues localContentValues = new ContentValues();
						localContentValues.put("name", str);
						localContentValues.put("when_db", Long.valueOf(l));
						this.eventDB.updateItem(localContentValues, i);
					}else if(!str.isEmpty() && str.length()>0){
						ContentValues localContentValues2 = new ContentValues();
						localContentValues2.put("name", str);
						localContentValues2.put("when_db", Long.valueOf(l));
						this.eventDB.addItem(localContentValues2);
					}
					
					return;
				}
				
			}else{
				this.eventDB.deleteItem(i);
				mHandler.post(mRebuildView);

			}

			

		}else if((requestCode == this.EDIT_EVENT_REQUEST) && (resultCode == RESULT_CANCELED)){
			Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
		}
		
		
	}

	protected boolean onLongListItemClick(View view, int position, long id){
		//Eventful localEvent2 = (Eventful)this.filteredList.get(position);
		Eventful localEvent = (Eventful)this.adapEvents.getItem(position);
		Intent localIntent = new Intent(this, EditEventActivity.class);
		localIntent.putExtra("id", localEvent.getId());
		localIntent.putExtra("eventname", localEvent.getEvent());
		localIntent.putExtra("when", localEvent.getWhenMillis());
		
		localIntent.putExtra("editTitle",true);
		
		startActivityForResult(localIntent, this.EDIT_EVENT_REQUEST);
		return true;
	}

    public void exportDB() {
        final EditText input = new EditText(this);
        Calendar today = Calendar.getInstance();

        int year = today.get(Calendar.YEAR);
        int month = today.get(Calendar.MONTH) + 1;
        int day = today.get(Calendar.DAY_OF_MONTH);

        input.setText(String.format("eventful-backup-%04d%02d%02d.db", year, month, day));
        new AlertDialog.Builder(this)
                .setTitle(R.string.export_db)
                .setView(input)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Editable value = input.getText();
                        String name = value.toString();
                        try {
                            DatabaseHelper.getInstance(MainActivity.this).exportDatabase(name);
                            Toast.makeText(MainActivity.this, R.string.export_db_success, Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Do nothing.
            }
        }).show();
    }

    public void importDB() {
        final String[] items = DatabaseHelper.getInstance(this).getExternalDatabaseNames();
        if (items.length == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.import_db_none_found)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.import_db);

            builder.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    final int that = which;
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage(R.string.import_db_really)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        DatabaseHelper.getInstance(MainActivity.this).importDatabase(items[that]);
                                        Toast.makeText(MainActivity.this, R.string.import_db_success, Toast.LENGTH_LONG).show();
                                        mHandler.post(mRebuildView);
                                    } catch (IOException e) {
                                        Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                }
            });
            builder.show();
        }
    }
}
