package com.example.kartishe.test2;

import android.app.Activity;
import android.view.Menu;
import android.os.Bundle;
import android.app.TabActivity;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.content.Intent;
/**
 * Created by kartishe on 10/14/15.
 */
public class MainActivity2 extends TabActivity{

    SampleAlarmReceiver2 alarm = new SampleAlarmReceiver2();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

        TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);
        TabSpec tab1 = tabHost.newTabSpec("First Tab");
        TabSpec tab2 = tabHost.newTabSpec("Second Tab");
        TabSpec tab3 = tabHost.newTabSpec("Third tab");

        tab1.setIndicator("Tab1");
        tab1.setContent(new Intent(this, Tab1Activity.class));

        tab2.setIndicator("Tab2");
        tab2.setContent(new Intent(this, Tab2Activity.class));

        tab3.setIndicator("Tab3");
        tab3.setContent(new Intent(this, Tab3Activity.class));

        tabHost.addTab(tab1);

        tabHost.addTab(tab2);
        tabHost.addTab(tab3);

        alarm  .setAlarm(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
