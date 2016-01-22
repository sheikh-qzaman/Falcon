package com.example.kartishe.test2;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.github.mikephil.charting.charts.LineChart;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

/**
 * Created by kartishe on 10/14/15.
 */
public class Tab3Fragment extends Fragment {

    public static final int[] JOYFUL_COLORS = {
            Color.rgb(67,115,192), Color.rgb(67,192,100),
            Color.rgb(217, 80, 138), Color.rgb(254, 149, 7), Color.rgb(254, 247, 120),
            Color.rgb(106, 167, 134), Color.rgb(53, 194, 209)
    };
   // Color.rgb(195,192,182),Color.rgb(67,115,192),
    private static int[] COLORS = new int[] { JOYFUL_COLORS[0], JOYFUL_COLORS[1],JOYFUL_COLORS[2],JOYFUL_COLORS[3],};
    private static double[] VALUES = new double[] { 30,70 };

    private static String[] NAME_LIST = new String[] { "Used", "Free"};

    private CategorySeries mSeries= new CategorySeries("");

    private DefaultRenderer mRenderer = new DefaultRenderer();

    private GraphicalView mChartView;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //LineChart chart = (LineChart) getView().findViewById(R.id.lineChart);
       // Log.d("sheikh", chart.toString());
        View rootView = inflater.inflate(
                R.layout.memory_layout, container, false);
        //((TextView) rootView.findViewById(android.R.id.text1)).setText(
        //        "Memory Stats");


        mRenderer.setApplyBackgroundColor(true);
        //mRenderer.setBackgroundColor(Color.parseColor("#ffffff"));

        mRenderer.setLabelsTextSize(50);
        mRenderer.setLegendTextSize(50);
        mRenderer.setMargins(new int[]{20, 30, 15, 0});
        mRenderer.setZoomButtonsVisible(false);
        mRenderer.setStartAngle(90);

        //mrenderer.setChartTitle("Efforts");
        //mrenderer.setChartTitleTextSize((float) 30);
        mRenderer.setShowLabels(false);
        //renderer.setLabelsTextSize(20);
        //renderer.setLegendTextSize(25);
        mRenderer.setDisplayValues(true);

        if (mSeries.getItemCount() !=VALUES.length) {
            for (int i = 0; i < VALUES.length; i++) {
                mSeries.add(NAME_LIST[i] + " ", VALUES[i]);
                SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
                renderer.setColor(COLORS[i]);
                renderer.setDisplayBoundingPoints(false);
                mRenderer.addSeriesRenderer(renderer);
            }
        }

        LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.image1);
        mChartView = ChartFactory.getPieChartView(getActivity().getBaseContext(), mSeries, mRenderer);
        mRenderer.setClickEnabled(true);
        mRenderer.setSelectableBuffer(10);
        layout.addView(mChartView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        if (mChartView != null) {
            mChartView.repaint();
        }

        return rootView;
    }


}
