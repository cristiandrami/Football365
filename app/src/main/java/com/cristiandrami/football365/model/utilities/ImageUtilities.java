package com.cristiandrami.football365.model.utilities;

import android.widget.ImageView;

import androidx.fragment.app.FragmentActivity;

import com.ahmadrosid.svgloader.SvgLoader;
import com.cristiandrami.football365.R;

public class ImageUtilities {

    private ImageUtilities(){}

    public static void loadSVGInImageView(FragmentActivity activity, String imageUrl, ImageView competitionIcon){
        try{
            SvgLoader.pluck()
                    .with(activity).setPlaceHolder(R.drawable.trophy_icon, R.drawable.trophy_icon)
                    .load(imageUrl, competitionIcon);
        }catch (Exception e){
            e.printStackTrace();

        }

    }


}
