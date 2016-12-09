package com.kosratdahmad.downloadfiles;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String MESSAGE_PROGRESS = "message_progress";
    private static final int PERMISSION_REQUEST_CODE = 1;


    MaterialDialog dialog;
    ButtonItemAdapter adapter;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(MESSAGE_PROGRESS)) {

                Download download = intent.getParcelableExtra("download");
//                mProgressBar.setProgress(download.getProgress());

                RecyclerView list = dialog.getRecyclerView();
                ButtonItemAdapter.ButtonVH holder = (ButtonItemAdapter.ButtonVH) list.findViewHolderForAdapterPosition(1);

                if (holder != null) {
                    holder.button.setVisibility(View.GONE);
                    holder.progress.setVisibility(View.VISIBLE);
//                    holder.progress.setProgress(download.getProgress());
//                    adapter.notifyItemChanged(1);
                }

                if (download.getProgress() == 100) {

//                    mProgressText.setText("File Download Complete");

                } else {

//                    mProgressText.setText(String.format("Downloaded (%d/%d) MB", download.getCurrentFileSize(), download.getTotalFileSize()));

                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        adapter = new ButtonItemAdapter(this, R.array.muainzList);
        adapter.setCallback(new ButtonItemAdapter.Callback() {
            @Override
            public void onItemClicked(int index) {
                Toast.makeText(MainActivity.this, "Item clicked: " + index, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onButtonClicked(int index) {
                downloadFile();
                Toast.makeText(MainActivity.this, "Button clicked: " + index, Toast.LENGTH_SHORT).show();
            }
        });

        dialog = new MaterialDialog.Builder(this)
                .title("Select Muazins")
                .adapter(adapter, null).build();

        registerReceiver();
    }

    public void customList(View unused) {

        dialog.show();
    }

    public void downloadFile() {

        if (checkPermission()) {
            startDownload();
        } else {
            requestPermission();
        }
    }

    private void startDownload() {

        Intent intent = new Intent(this, DownloadService.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startService(intent);

    }

    private void registerReceiver() {

        LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MESSAGE_PROGRESS);
        bManager.registerReceiver(broadcastReceiver, intentFilter);

    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {

            return true;

        } else {

            return false;
        }
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    startDownload();
                } else {

                    Toast.makeText(MainActivity.this, "Permission Denied, Please allow to proceed !", Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }

}