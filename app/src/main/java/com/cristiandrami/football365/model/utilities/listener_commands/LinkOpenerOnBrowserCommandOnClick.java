package com.cristiandrami.football365.model.utilities.listener_commands;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

/**
 * This class is used as a Command class to make flexible the setting of onClickListener when we need
 * to set it on a graphical object, in this case this command is used to allow users to open a link on default
 * device browser
 * @see com.cristiandrami.football365.ui.news.NewsFragment
 * @author Cristian D. Dramisino
 *
 */

public class LinkOpenerOnBrowserCommandOnClick implements View.OnClickListener {

    private String link;
    private Context context;
    public LinkOpenerOnBrowserCommandOnClick(String link, Context context){
        this.context=context;
        this.link=link;

    }

    @Override
    public void onClick(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        context.startActivity(browserIntent);

    }
}
