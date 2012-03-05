package uk.co.droidinactu.common.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import uk.co.droidinactu.ebooklauncher.EBookLauncherApplication;
import uk.co.droidinactu.ebooklauncher.R;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.util.Linkify;
import android.widget.TextView;

public class AboutDialog extends Dialog {

    private static Context mContext = null;
    private static EBookLauncherApplication myApp = null;

    public static String readRawTextFile(int id) {
        InputStream inputStream = AboutDialog.mContext.getResources().openRawResource(id);
        InputStreamReader in = new InputStreamReader(inputStream);
        BufferedReader buf = new BufferedReader(in);
        String line;
        StringBuilder text = new StringBuilder();
        try {
            while ((line = buf.readLine()) != null) {
                text.append(line);
            }
        } catch (IOException e) {
            return null;
        }
        return text.toString();
    }

    public AboutDialog(Context context, EBookLauncherApplication app) {
        super(context);
        AboutDialog.myApp = app;
        AboutDialog.mContext = context;
    }

    /**
     * This is the standard Android on create method that gets called when the activity initialized.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.setContentView(R.layout.about);

        Resources r = AboutDialog.mContext.getResources();

        TextView tv = (TextView) this.findViewById(R.id.about_author);
        tv.setText(AboutDialog.myApp.getApplicationAuthor());
        tv.setLinkTextColor(Color.BLUE);
        Linkify.addLinks(tv, Linkify.ALL);

        tv = (TextView) this.findViewById(R.id.about_author_email);
        tv.setText(AboutDialog.myApp.getApplicationAuthorEmail());
        tv.setLinkTextColor(Color.BLUE);
        Linkify.addLinks(tv, Linkify.ALL);

        tv = (TextView) this.findViewById(R.id.about_summary);
        tv.setText(AboutDialog.myApp.getApplicationDescription());
        tv.setLinkTextColor(Color.BLUE);
        // Linkify.addLinks(tv, Linkify.ALL);

        tv = (TextView) this.findViewById(R.id.about_version);
        tv.setText("Ver: " + AboutDialog.myApp.getAppVersion());
        tv.setLinkTextColor(Color.BLUE);
        // Linkify.addLinks(tv, Linkify.ALL);

        tv = (TextView) this.findViewById(R.id.about_website);
        tv.setText(AboutDialog.myApp.getApplicationAuthorWebsite());
        tv.setLinkTextColor(Color.BLUE);
        Linkify.addLinks(tv, Linkify.ALL);

        tv = (TextView) this.findViewById(R.id.about_legal_text);
        tv.setText(Html.fromHtml(AboutDialog.readRawTextFile(R.raw.license_short)));
        tv.setLinkTextColor(Color.BLUE);
        Linkify.addLinks(tv, Linkify.ALL);

    }
}
