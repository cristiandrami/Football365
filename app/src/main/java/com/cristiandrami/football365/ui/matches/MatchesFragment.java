package com.cristiandrami.football365.ui.matches;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ahmadrosid.svgloader.SvgLoader;
import com.cristiandrami.football365.R;
import com.cristiandrami.football365.databinding.FragmentMatchesBinding;
import com.cristiandrami.football365.model.utilities.matchesUtilities.Competition;
import com.cristiandrami.football365.model.utilities.matchesUtilities.CompetitionsUtilities;
import com.squareup.picasso.Picasso;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class MatchesFragment extends Fragment {

    private FragmentMatchesBinding binding;
    private LinearLayout linearLayout;
    private HashMap<String, Competition> competitionHashMap;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MatchesViewModel matchesViewModel =
                new ViewModelProvider(this).get(MatchesViewModel.class);

        binding = FragmentMatchesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        linearLayout= binding.matchesFragmentLinearLayout;



        //matchesViewModel.executeAPICall();

        //final TextView textView = binding.textDashboard;
        //matchesViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        competitionHashMap= CompetitionsUtilities.getInstance().getCompetitions();
        Collection competitionsValues =competitionHashMap.values();
        Iterator i = competitionsValues.iterator();
        while (i.hasNext()) {
            View viewCompetition= getLayoutInflater().inflate(R.layout.competitions_matches_layout,null);

            TextView competitionInfo=viewCompetition.findViewById(R.id.competition_matches_info_text);
            ImageView competitionIcon= viewCompetition.findViewById(R.id.competition_matches_icon);


            Competition competitionObject= (Competition) i.next();
            competitionInfo.setText(competitionObject.toString());
            Log.e("image", competitionObject.getImageUrl());
            SvgLoader.pluck()
                    .with(getActivity()).setPlaceHolder(R.drawable.ic_baseline_sports_soccer_24, R.drawable.ic_baseline_sports_soccer_24)
                    .load(competitionObject.getImageUrl(), competitionIcon);


            linearLayout.addView(viewCompetition);
        }



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}