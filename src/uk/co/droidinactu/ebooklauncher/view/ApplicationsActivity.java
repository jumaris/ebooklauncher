/*
 * Copyright 2012 Andy Aspell-Clark
 *
 * This file is part of eBookLauncher.
 * 
 * eBookLauncher is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * eBookLauncher is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with eBookLauncher. If not, see http://www.gnu.org/licenses/.
 *
 */
package uk.co.droidinactu.ebooklauncher.view;

import java.util.ArrayList;

import uk.co.droidinactu.common.view.EInkGridView;
import uk.co.droidinactu.ebooklauncher.EBookLauncherApplication;
import uk.co.droidinactu.ebooklauncher.R;
import uk.co.droidinactu.ebooklauncher.data.Application;
import uk.co.droidinactu.ebooklauncher.data.DeviceFactory;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public final class ApplicationsActivity extends AbstractListActivity implements OnItemClickListener, OnClickListener {

	/**
	 * GridView adapter to show the list of all installed applications.
	 */
	public class ApplicationsAdapter extends ArrayAdapter<Application> {
		private final Rect mOldBounds = new Rect();

		public ApplicationsAdapter(final Context context, final ArrayList<Application> apps) {
			super(context, 0, apps);
		}

		@Override
		public View getView(final int position, View convertView, final ViewGroup parent) {
			final Application info = mApplications.get(position);

			if (convertView == null) {
				final LayoutInflater inflater = getLayoutInflater();
				convertView = inflater.inflate(DeviceFactory.getAppsGridItemLayout(), parent, false);
			}

			Drawable icon = info.icon;

			if (!info.filtered) {
				int width = 90;
				int height = 90;

				final int iconWidth = icon.getIntrinsicWidth();
				final int iconHeight = icon.getIntrinsicHeight();

				if (icon instanceof PaintDrawable) {
					final PaintDrawable painter = (PaintDrawable) icon;
					painter.setIntrinsicWidth(width);
					painter.setIntrinsicHeight(height);
				}

				// if ((width > 0) && (height > 0) && ((width < iconWidth) ||
				// (height <
				// iconHeight))) {
				final float ratio = (float) iconWidth / iconHeight;

				if (iconWidth > iconHeight) {
					height = (int) (width / ratio);
				} else if (iconHeight > iconWidth) {
					width = (int) (height * ratio);
				}

				final Bitmap.Config c = icon.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
						: Bitmap.Config.RGB_565;
				final Bitmap thumb = Bitmap.createBitmap(width, height, c);
				final Canvas canvas = new Canvas(thumb);
				canvas.setDrawFilter(new PaintFlagsDrawFilter(Paint.DITHER_FLAG, 0));

				// Copy the old bounds to restore them later If we were to
				// do oldBounds =
				// icon.getBounds(), the call to setBounds() that follows
				// would change the same
				// instance and we would lose the old bounds
				mOldBounds.set(icon.getBounds());
				icon.setBounds(0, 0, width, height);
				icon.draw(canvas);
				icon.setBounds(mOldBounds);
				icon = info.icon = new BitmapDrawable(thumb);
				info.filtered = true;
				// }
			}

			final TextView textView = (TextView) convertView.findViewById(R.id.label);
			textView.setCompoundDrawablesWithIntrinsicBounds(null, icon, null, null);
			textView.setText(info.title);

			return convertView;
		}
	}

	private String filterChar = "";

	private EInkGridView mAppGrid;

	private ArrayList<Application> mApplications;
	private EBookLauncherApplication myApp;

	/**
	 * Loads the list of installed applications in mApplications.
	 */
	private void loadApplications() {
		if (mApplications != null) {
			return;
		}

		mApplications = myApp.dataMdl.getApplications(this, null);

		mAppGrid.setAdapter(new ApplicationsAdapter(this, mApplications));
		mAppGrid.setSelection(0);
	}

	@Override
	public void onClick(final View v) {

		if (v.getTag() == null) {
			final String btnPressed = ((Button) v).getText().toString();
			filterChar = btnPressed;

		} else if (v.getTag().equals("app_grid_btn_grid")) {
			showNotImplementedDialog();

		} else if (v.getTag().equals("app_grid_btn_close")) {
			finish();

		}
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(DeviceFactory.getAppsGridLayout());

		myApp = (EBookLauncherApplication) getApplication();

		if (DeviceFactory.isNook()) {
			final ImageButton btnClose = (ImageButton) findViewById(R.id.app_grid_btn_close);
			btnClose.setOnClickListener(this);
		}

		mAppGrid = (EInkGridView) findViewById(R.id.apps_grid);
		mAppGrid.setVisibility(View.VISIBLE);
		mAppGrid.setOnItemClickListener(this);

		loadApplications();
	}

	@Override
	public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
		final Application app = (Application) parent.getItemAtPosition(position);
		startActivity(app.intent);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}
