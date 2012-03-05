package uk.co.droidinactu.ebooklauncher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * this class gets called when the user presses the nook button and selects 'Home'.
 * 
 * @author andy
 */
public class NookHome extends Activity {
	@Override
	public void onCreate(final Bundle paramBundle) {
		super.onCreate(paramBundle);
		this.startActivity(new Intent(this, EBookLauncherActivity.class));
		this.finish();
	}
}
