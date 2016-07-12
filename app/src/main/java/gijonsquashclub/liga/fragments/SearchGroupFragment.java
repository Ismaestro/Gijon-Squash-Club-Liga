package gijonsquashclub.liga.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import gijonsquashclub.liga.R;
import gijonsquashclub.liga.utils.Design;
import gijonsquashclub.liga.utils.Preferences;

public class SearchGroupFragment extends Fragment {
    public static final String SEARCH_GROUP = "searchGroup";

    public SearchGroupFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_group, container,
                false);
        int i = getArguments().getInt(SEARCH_GROUP);
        String option = getResources().getStringArray(R.array.options)[i];

        Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner1);
        // Create an ArrayAdapter using the string array and a default
        // spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                rootView.getContext(), R.array.groups,
                android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        int numGroupLoaded = Preferences.getNumberOfGroupFromPreferences(
                getActivity());
        if (numGroupLoaded != 0) {
            int spinnerPosition = adapter.getPosition(getString(R.string.group)
                    + " " + numGroupLoaded);
            spinner.setSelection(spinnerPosition);
        }

        getActivity().setTitle(option);
        Design.setFont(rootView);
        return rootView;
    }
}