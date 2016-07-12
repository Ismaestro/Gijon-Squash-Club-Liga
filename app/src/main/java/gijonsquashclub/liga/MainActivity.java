package gijonsquashclub.liga;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ShareActionProvider;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gijonsquashclub.liga.fragments.SearchGroupFragment;
import gijonsquashclub.liga.fragments.SearchPlayerFragment;
import gijonsquashclub.liga.utils.Design;

public class MainActivity extends Activity {

    public static final String GROUP = "group";
    public static final String PLAYER_NAME = "playerName";
    public static final String LOAD_PLAYER_FRAGMENT = "loadPlayerFragment";
    private static final String OPTION = "option";
    private static final String FLAG = "flag";
    private static final String PLAY_STORE_URL_OLD = "market://details?id=";
    private static final int POSITION_SEARCH_PLAYER_ITEM = 1;
    private static final String EMAIL = "litohevia@gmail.com";
    private static final String PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=";
    private static final int NUM_DRAWER_OPTIONS = 4;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;
    private CharSequence drawerTitle;
    private CharSequence title;
    private String[] options;
    private View drawerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT >= 21)
            Design.setStatusBarColor(this);

        title = drawerTitle = getTitle();
        options = getResources().getStringArray(R.array.options);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.drawerList);
        List<HashMap<String, String>> mList = new ArrayList<>();
        int[] mFlags = new int[]{R.drawable.ic_action_view_as_grid,
                R.drawable.ic_action_person, R.drawable.ic_action_email,
                R.drawable.ic_action_half_important};

        for (int i = 0; i < NUM_DRAWER_OPTIONS; i++) {
            HashMap<String, String> hm = new HashMap<>();
            hm.put(OPTION, options[i]);
            hm.put(FLAG, Integer.toString(mFlags[i]));
            mList.add(hm);
        }

        String[] from = {FLAG, OPTION};
        int[] to = {R.id.flag, R.id.option};

        SimpleAdapter mAdapter = new SimpleAdapter(this, mList, R.layout.drawer_list_item,
                from, to);

        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);
        drawerList.setAdapter(mAdapter);
        drawerList.setOnItemClickListener(new DrawerItemClickListener());

        getActionBar().setDisplayHomeAsUpEnabled(true);

        drawerToggle = createActionBarDrawer();
        drawerLayout.setDrawerListener(drawerToggle);

        if (savedInstanceState == null) {
            itemSelected(0);
        }

        checkIfLoadPlayerFragment();
    }

    private void checkIfLoadPlayerFragment() {
        Intent i = getIntent();
        if (i != null) {
            boolean loadPlayerFragment = i.getBooleanExtra(
                    LOAD_PLAYER_FRAGMENT, false);
            if (loadPlayerFragment)
                itemSelected(POSITION_SEARCH_PLAYER_ITEM);
        }
    }

    private ActionBarDrawerToggle createActionBarDrawer() {
        return new ActionBarDrawerToggle(this, drawerLayout,
                R.drawable.ic_drawer, R.string.drawerOpen) {
            @Override
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(title);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(drawerTitle);
                invalidateOptionsMenu();
            }
        };
    }

    private void itemSelected(int position) {
        Fragment fragment = new SearchGroupFragment();

        Bundle args = new Bundle();
        FragmentManager fragmentManager = getFragmentManager();

        switch (position) {
            case 0:
                fragment = new SearchGroupFragment();
                args.putInt(SearchGroupFragment.SEARCH_GROUP, position);
                break;
            case 1:
                fragment = new SearchPlayerFragment();
                args.putInt(SearchPlayerFragment.SEARCH_PLAYER, position);
                break;
            case 2:
                sendEmail();
                break;
            case 3:
                rateApp();
                break;
        }

        fragment.setArguments(args);
        fragmentManager.beginTransaction().replace(R.id.contentFrame, fragment)
                .commit();

        drawerList.setItemChecked(position, true);
        setTitle(options[position]);

        drawerView = findViewById(R.id.drawer);
        drawerLayout.closeDrawer(drawerView);
    }

    private void sendEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", EMAIL, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
        startActivity(Intent.createChooser(emailIntent, ""));
    }

    private void rateApp() {
        Uri uri = Uri.parse(PLAY_STORE_URL_OLD + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY
                | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (Exception e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(PLAY_STORE_URL + getPackageName())));
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        this.title = title;
        getActionBar().setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        createShareItem(menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void createShareItem(Menu menu) {
        MenuItem item = menu.findItem(R.id.share);
        ShareActionProvider mShareActionProvider = (ShareActionProvider) item.getActionProvider();
        String playStoreLink = PLAY_STORE_URL + getPackageName();
        String yourShareText = getString(R.string.shareApp) + " "
                + playStoreLink;
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain").setText(yourShareText).getIntent();
        mShareActionProvider.setShareIntent(shareIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("UnusedParameters")
    public void executeButtonSearchGroup(View view) {
        Spinner spinner = (Spinner) findViewById(R.id.spinner1);
        Intent intent = new Intent(this, GroupActivity.class);
        intent.putExtra(GROUP, spinner.getSelectedItemPosition() + 1);
        startActivity(intent);
    }

    @SuppressWarnings("UnusedParameters")
    public void executeButtonSearchPlayer(View view) {
        EditText editText = (EditText) findViewById(R.id.editText1);
        String playerName = editText.getText().toString();
        if (playerName.isEmpty()) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    R.string.noPlayerName, Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        Intent intent = new Intent(this, GroupActivity.class);
        intent.putExtra(PLAYER_NAME, playerName);
        startActivity(intent);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = drawerLayout.isDrawerOpen(drawerView);
        menu.findItem(R.id.share).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            itemSelected(position);
        }
    }
}
