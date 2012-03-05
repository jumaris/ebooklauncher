package uk.co.droidinactu.ebooklauncher;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

/**
 * class written by JoshMiers@xda
 * @see http://forum.xda-developers.com/showthread.php?p=22490176
 * @author JoshMiers
 */
public class NookReaderReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(final Context context, final Intent intent) {
		try {
			final Intent newIntent = new Intent(intent);
			newIntent.setComponent(new ComponentName("com.bn.nook.reader.activities", "ReaderActivity"));
			context.startActivity(newIntent);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
