package com.red_folder.phonegap.plugin.backgroundservice.sample;

import java.util.Date;
import java.util.List;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Fragment;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.PhoneNumberUtils;
import android.util.Log;

import com.red_folder.phonegap.plugin.backgroundservice.BackgroundService;

public class MyService extends BackgroundService {

  private final static String TAG = MyService.class.getSimpleName();

  private String mHelloTo = "World";

  @Override
  protected JSONObject doWork() {
    JSONObject json_output = new JSONObject();
    Context context = getBaseContext();
    PackageManager pm = context.getPackageManager();

    // INTERNET USAGE
    JSONArray json_internet = new JSONArray();

    List < ApplicationInfo > packages = pm.getInstalledApplications(0);
    for (ApplicationInfo packageInfo: packages) {

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
          json_internet.put((Object) json);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }

    try {
      json_output.put("traffic", json_internet);
    }
    catch (Exception e){
      e.printStackTrace();
    }

    return json_output;
  }

  @Override
  protected JSONObject getConfig() {
    JSONObject result = new JSONObject();

    try {
      result.put("HelloTo", this.mHelloTo);
    } catch (JSONException e) {}

    return result;
  }

  @Override
  protected void setConfig(JSONObject config) {
    try {
      if (config.has("HelloTo"))
        this.mHelloTo = config.getString("HelloTo");
    } catch (JSONException e) {}

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
