package com.example.rsp;




import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.ActivityRecognitionClient;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
@SuppressLint("NewApi")
public class OnsiteReviewActivity extends FragmentActivity implements ActionBar.TabListener,GooglePlayServicesClient.ConnectionCallbacks,GooglePlayServicesClient.OnConnectionFailedListener{

	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionbar;
	private String[] tabs={"Rate","Comment","Map"};
	private ActivityRecognitionClient arclient;
	private PendingIntent pIntent;
	private BroadcastReceiver receiver;
	TextView tvActivity;

	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.onsite_review);
		tvActivity = (TextView) findViewById(R.id.tVDisplayActivity);

		viewPager=(ViewPager) findViewById(R.id.pager);
		actionbar=getActionBar();
		mAdapter=new TabsPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(mAdapter);
		
		
		actionbar.setHomeButtonEnabled(false);
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		int resp =GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		  if(resp == ConnectionResult.SUCCESS){
		   arclient = new ActivityRecognitionClient(this, this, this);
		   arclient.connect();
		  }

		  else{
			     Toast.makeText(this, "Please install Google Play Service.", Toast.LENGTH_SHORT).show();
			    }
		  receiver = new BroadcastReceiver() {
			        @Override
			        public void onReceive(Context context, Intent intent) {
			         String v =  "Activity :" + intent.getStringExtra("Activity") + " " + "Confidence : " + intent.getExtras().getInt("Confidence") + "n";
			         v += tvActivity.getText();
			         tvActivity.setText(v);
			        }
			      };
          
			      IntentFilter filter = new IntentFilter();
			      filter.addAction("com.example.rsp.ACTIVITY_RECOGNITION_DATA");
			      registerReceiver(receiver, filter);

		
		for(String tab_names:tabs)
		{
			actionbar.addTab(actionbar.newTab().setText(tab_names).setTabListener(this));
		}
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				actionbar.setSelectedNavigationItem(arg0);
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	 

	  

	 

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		viewPager.setCurrentItem(tab.getPosition());
		
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	 protected void onDestroy() {
	  super.onDestroy();
	  if(arclient!=null){
	   arclient.removeActivityUpdates(pIntent);
	   arclient.disconnect();
	  }
	  unregisterReceiver(receiver);
	 }

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "Connection Failed", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, ActivityRecognitionService.class);
		  pIntent = PendingIntent.getService(this, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
		  arclient.requestActivityUpdates(1000, pIntent);  

	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}
	 
}
