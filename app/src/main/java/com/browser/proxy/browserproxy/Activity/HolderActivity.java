package com.browser.proxy.browserproxy.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import com.browser.proxy.browserproxy.Database.Record;
import com.browser.proxy.browserproxy.R;
import com.browser.proxy.browserproxy.Unit.BrowserUnit;
import com.browser.proxy.browserproxy.Unit.IntentUnit;
import com.browser.proxy.browserproxy.View.DialogAdapter;
import com.browser.proxy.browserproxy.View.NinjaContextWrapper;
import com.browser.proxy.browserproxy.View.NinjaToast;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import java.util.*;

public class HolderActivity extends Activity {
    private static final int TIMER_SCHEDULE_DEFAULT = 512;

    private Record first = null;
    private Record second = null;
    private Timer timer = null;
    private boolean background = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() == null || getIntent().getData() == null) {
            finish();
            return;
        }

        first = new Record();
        first.setTitle(getString(R.string.album_untitled));
        first.setURL(getIntent().getData().toString());
        first.setTime(System.currentTimeMillis());

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (HolderActivity.this.first != null && HolderActivity.this.second == null) {
                    Intent intent = new Intent(HolderActivity.this, BrowserActivity.class);
                    intent.putExtra(IntentUnit.OPEN, HolderActivity.this.first.getURL());
                    HolderActivity.this.startActivity(intent);
                    boolean unused = HolderActivity.this.background = true;
                }
//                if (first != null && second == null) {
//                    Intent toService = new Intent(HolderActivity.this, HolderService.class);
//                    RecordUnit.setHolder(first);
//                    startService(toService);
//                    background = true;
//                }
                HolderActivity.this.finish();
            }
        };
        timer = new Timer();
        timer.schedule(task, TIMER_SCHEDULE_DEFAULT);
    }

    @Override
    public void onNewIntent(Intent intent) {
        if (intent == null || intent.getData() == null || first == null) {
            finish();
            return;
        }

        if (timer != null) {
            timer.cancel();
        }

        second = new Record();
        second.setTitle(getString(R.string.album_untitled));
        second.setURL(intent.getData().toString());
        second.setTime(System.currentTimeMillis());

//        if (first.getURL().equals(second.getURL())) {
//            showHolderDialog();
//            return;
//        } else {
//            Intent toService = new Intent(HolderActivity.this, HolderService.class);
//            RecordUnit.setHolder(second);
//            startService(toService);
//            background = true;
//            finish();
//        }
        if (this.first.getURL().equals(this.second.getURL())) {
            showHolderDialog();
            return;
        }
        Intent intent2 = new Intent(this, BrowserActivity.class);
        intent2.putExtra(IntentUnit.OPEN, this.second.getURL());
        startActivity(intent2);
        this.background = true;
        finish();
    }

    @Override
    public void onDestroy() {
        if (timer != null) {
            timer.cancel();
        }

        if (background) {
            NinjaToast.show(this, R.string.toast_load_in_background);
        }

        first = null;
        second = null;
        timer = null;
        background = false;
        super.onDestroy();
    }

    private void showHolderDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new NinjaContextWrapper(this));
        builder.setCancelable(true);

        FrameLayout linearLayout = (FrameLayout) getLayoutInflater().inflate(R.layout.ninja_dialog_list, null, false);
        builder.setView(linearLayout);

        String[] strings = getResources().getStringArray(R.array.holder_menu);
        List<String> list = new ArrayList<>();
        list.addAll(Arrays.asList(strings));

        ListView listView = (ListView) linearLayout.findViewById(R.id.dialog_list);
        DialogAdapter adapter = new DialogAdapter(this, R.layout.dialog_text_item, list);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        final AlertDialog dialog = builder.create();
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                HolderActivity.this.finish();
            }
        });
        dialog.show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent toActivity = new Intent(HolderActivity.this, BrowserActivity.class);
                        toActivity.putExtra(IntentUnit.OPEN, first.getURL());
                        startActivity(toActivity);
                        break;
                    case 1:
                        BrowserUnit.copyURL(HolderActivity.this, first.getURL());
                        break;
                    case 2:
                        IntentUnit.share(HolderActivity.this, first.getTitle(), first.getURL());
                        break;
                    default:
                        break;
                }
                dialog.hide();
                dialog.dismiss();
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Uri data = getIntent().getData();
        if (data==null) {
            return;
        }
        Intent intent2 = new Intent(this, BrowserActivity.class);
        intent2.putExtra(IntentUnit.OPEN, data);
        startActivity(intent2);
        this.background = true;
        finish();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
