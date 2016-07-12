package gijonsquashclub.liga.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import gijonsquashclub.liga.R;
import gijonsquashclub.liga.utils.Design;

public class SearchPlayerFragment extends Fragment {
    public static final String SEARCH_PLAYER = "searchPlayer";

    public SearchPlayerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_player, container,
                false);
        int i = getArguments().getInt(SEARCH_PLAYER);
        String option = getResources().getStringArray(R.array.options)[i];

        EditText editText = (EditText) rootView.findViewById(R.id.editText1);
        editText.setGravity(Gravity.CENTER);

        getActivity().setTitle(option);
        Design.setFont(rootView);
        return rootView;
    }
}
