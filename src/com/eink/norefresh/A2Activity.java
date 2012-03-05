package com.eink.norefresh;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Nook Touch NoRefresh.
 *
 * @author DairyKnight <dairyknight@gmail.com>
 * http://forum.xda-developers.com/showthread.php?t=1183173
 */
public class A2Activity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent svc = new Intent(this, A2Service.class);
        this.startService(svc);
        this.finish();
    }
}
