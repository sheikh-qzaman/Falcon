package com.example.kartishe.test2;
import android.app.Activity;
import android.os.Bundle;
import android.widget.*;
import android.view.Gravity;
/**
 * Created by kartishe on 10/14/15.
 */
public class Tab1Activity  extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        TextView  tv=new TextView(this);
        tv.setTextSize(25);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        tv.setText("This Is Tab1 Activity");

        setContentView(tv);
    }
}

