package com.red_folder.phonegap.plugin.backgroundservice.sample;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.TrafficStats;
import android.content.pm.PackageManager;
import android.util.Log;

import com.red_folder.phonegap.plugin.backgroundservice.BackgroundService;

public class MyService extends BackgroundService {
	
	private final static String TAG = MyService.class.getSimpleName();
	
	private String mHelloTo = "World";

	@Override
	protected JSONArray doWork() {
		JSONArray jsons = new JSONArray();
		
        List<ApplicationInfo> packages = getActivity().getPackageManager().getInstalledPackages(0);

        for (ApplicationInfo packageInfo : packages) {
            // get the UID for the selected app
			
            int UID = packageInfo.uid;
            String package_name = packageInfo.packageName;
            ApplicationInfo app = null;
            try {
                app = pm.getApplicationInfo(package_name, 0);
            } catch (PackageManager.NameNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            String name = (String) pm.getApplicationLabel(app);
            double received = (double) TrafficStats.getUidRxBytes(UID) / 1024 / 1024;
            double send = (double) TrafficStats.getUidTxBytes(UID) / 1024 / 1024;
            double total = received + send;
			
			if (total > 0) {
				try {
					JSONObject json = new JSONObject();
					json.put("uid", UID);
					json.put("name", name);
					json.put("received", received);
					json.put("send", send);
					json.put("total", total);
					jsons.put((Object)json);
				} catch ( Exception e ) { 
					e.printStackTrace(); 
				}
			}
        }
		
        return jsons;
	}

	@Override
	protected JSONObject getConfig() {
		JSONObject result = new JSONObject();
		
		try {
			result.put("HelloTo", this.mHelloTo);
		} catch (JSONException e) {
		}
		
		return result;
	}

	@Override
	protected void setConfig(JSONObject config) {
		try {
			if (config.has("HelloTo"))
				this.mHelloTo = config.getString("HelloTo");
		} catch (JSONException e) {
		}
		
	}     

	@Override
	protected JSONObject initialiseLatestResult() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onTimerEnabled() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onTimerDisabled() {
		// TODO Auto-generated method stub
		
	}


}
