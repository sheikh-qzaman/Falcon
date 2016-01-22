package com.example.kartishe.test2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageButton;
import android.view.View.OnClickListener;
import android.app.Dialog;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.Button;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by kartishe on 10/14/15.
 */
public class Tab1Fragment extends Fragment implements RestApiResultReceiver.Receiver {
    public static final String ARG_OBJECT = "object";
    ImageButton imageButton;
    Button cancelButton;
    private RestApiResultReceiver mReceiver;
    View rootView;
    final String[] url = {"https://172.25.223.182:55443/api/v1/global/host-name", "https://172.25.223.182:55443/api/v1/global/host-name"};
    static String hname = "";

    private  static boolean isRestExecuted = false;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        rootView = inflater.inflate(
                R.layout.csr_information, container, false);
        ((TextView) rootView.findViewById(R.id.title)).setText(
                "CSR1000V");

        int csr_id = args.getInt(ARG_OBJECT);
        if(csr_id == 1){
            //if (!isRestExecuted) {
                mReceiver = new RestApiResultReceiver(new Handler());
                mReceiver.setReceiver(this);
                Intent intent = new Intent(Intent.ACTION_SYNC, null, getActivity(), RestApiService.class);

        /* Send optional extras to Download IntentService */
                intent.putExtra("url", url);
                intent.putExtra("receiver", mReceiver);
                intent.putExtra("requestId", 101);

                getActivity().startService(intent);

                String lines[] = "CSR-ipbase uptime is 2 days, 20 hours, 17 minutes".split(" uptime is ");
            //}
        }
        else if (csr_id == 2)
        {
            ((TextView) rootView.findViewById(R.id.hostName2)).setText("ultra-315");
            ((TextView) rootView.findViewById(R.id.ipAddress2)).setText("172.25.223.121");
            ((TextView) rootView.findViewById(R.id.imgVersion2)).setText("3.15");
            ((TextView) rootView.findViewById(R.id.upTime2)).setText("2 d, 2 h, 1 m");
            ((TextView) rootView.findViewById(R.id.license2)).setText("10 Mb/s");


        }
        else
        {
            ((TextView) rootView.findViewById(R.id.hostName2)).setText("ultra-test");
            ((TextView) rootView.findViewById(R.id.ipAddress2)).setText("172.19.112.121");
            ((TextView) rootView.findViewById(R.id.imgVersion2)).setText("3.16");
            ((TextView) rootView.findViewById(R.id.upTime2)).setText("1 d, 11 h, 1 m");
            ((TextView) rootView.findViewById(R.id.license2)).setText("100 Mb/s");
        }
        addListenerOnButton(rootView);

        return rootView;
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case RestApiService.STATUS_RUNNING:

                getActivity().setProgressBarIndeterminateVisibility(true);
                break;
            case RestApiService.STATUS_FINISHED:
                /* Hide progress & extract result from bundle */
                getActivity().setProgressBarIndeterminateVisibility(false);

                String results[] = resultData.getStringArray("result");
                ObjectMapper mapper = new ObjectMapper();
                try {

                    JsonNode node = mapper.readTree(results[0].getBytes());
                    hname = node.get("host-name").asText();

                    ((TextView) rootView.findViewById(R.id.hostName2)).setText(
                            hname);
                    ((TextView) rootView.findViewById(R.id.ipAddress2)).setText("172.25.223.182");
                    ((TextView) rootView.findViewById(R.id.imgVersion2)).setText("3.17");
                    ((TextView) rootView.findViewById(R.id.upTime2)).setText("2 d, 2 h, 1 m");
                    ((TextView) rootView.findViewById(R.id.license2)).setText("100 kb/s");
                    isRestExecuted =true;

                }
                catch(Exception e)
                {}
                /* Update ListView with result */
                //arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_2, results);
                //listView.setAdapter(arrayAdapter);

                break;
            case RestApiService.STATUS_ERROR:
                /* Handle the error */
                String error = resultData.getString(Intent.EXTRA_TEXT);
                Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
                break;
        }
    }

    public void addListenerOnButton(View rootView) {

        imageButton = (ImageButton) rootView.findViewById(R.id.moreImageButton);
        final Dialog dialog = new Dialog(getActivity());
        imageButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                //Toast.makeText(getActivity(),
                //        "ImageButton is clicked!", Toast.LENGTH_SHORT).show();



                dialog.setContentView(R.layout.dialog_layout);
                dialog.setTitle("Custom Alert Dialog");

                final EditText editText=(EditText)dialog.findViewById(R.id.ipaddressText);
                Button save=(Button)dialog.findViewById(R.id.save);
                cancelButton=(Button)dialog.findViewById(R.id.cancel);
                cancelButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }

        });

    }
}