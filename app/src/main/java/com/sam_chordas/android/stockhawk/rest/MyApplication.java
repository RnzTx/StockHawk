package com.sam_chordas.android.stockhawk.rest;

import android.app.Application;
import android.os.Build;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by rnztx on 21/6/16.
 */
public class MyApplication extends Application {
	private static final long SCHEMA_VERSION = 0;
	@Override
	public void onCreate() {
		super.onCreate();
		RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
				.name(Realm.DEFAULT_REALM_NAME)
				.schemaVersion(SCHEMA_VERSION)
				.deleteRealmIfMigrationNeeded()
				.build();
		Realm.setDefaultConfiguration(realmConfiguration);
	}
}
