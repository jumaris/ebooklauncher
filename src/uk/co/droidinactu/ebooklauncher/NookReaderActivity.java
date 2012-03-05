package uk.co.droidinactu.ebooklauncher;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;

/**
 * class written by JoshMiers@xda
 * @see http://forum.xda-developers.com/showthread.php?p=22490176
 * @author JoshMiers
 */
public class NookReaderActivity extends Activity {
	private Intent m_lastIntent = null;

	@Override
	protected void onStart() {
		super.onStart();
		try {
			final Intent i = this.getIntent();
			if (this.m_lastIntent == i) {
				return;
			}
			this.m_lastIntent = i;
			final Intent newIntent = new Intent(i);
			newIntent.setComponent(new ComponentName("com.bn.nook.reader.activities",
			        "com.bn.nook.reader.activities.ReaderActivity"));
			this.startActivity(newIntent);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		this.finish();
	}
}
