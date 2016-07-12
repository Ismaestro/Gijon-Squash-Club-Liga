package gijonsquashclub.liga;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import gijonsquashclub.liga.core.GetGroupsTask;
import gijonsquashclub.liga.core.GetGroupsTaskResponse;
import gijonsquashclub.liga.utils.Design;
import gijonsquashclub.liga.utils.LevenshteinDistance;
import gijonsquashclub.liga.utils.NormalizeText;
import gijonsquashclub.liga.utils.Preferences;

public class GroupActivity extends Activity implements GetGroupsTaskResponse {

    private String nameInserted;
    private int numberOfGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        if (android.os.Build.VERSION.SDK_INT >= 21)
            Design.setStatusBarColor(this);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        TextView textView = (TextView) findViewById(R.id.textViewLoading);
        final TableLayout tableLayout = (TableLayout) findViewById(R.id.tableLayout1);
        tableLayout.setVisibility(View.INVISIBLE);

        Bundle extras = getIntent().getExtras();
        nameInserted = extras.getString(MainActivity.PLAYER_NAME);
        numberOfGroup = extras.getInt(MainActivity.GROUP);

        GetGroupsTask asyncTask = new GetGroupsTask();
        asyncTask.response = this;
        asyncTask.setProgressBar(progressBar);
        asyncTask.setTableLayout(textView);
        asyncTask.setTableLayout(tableLayout);
        asyncTask.execute();

        Design.setFont(findViewById(R.id.relativeLayout));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.group, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.itemRotate) {
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            switch (rotation) {
                case Surface.ROTATION_0:
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    break;
                case Surface.ROTATION_90:
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    break;
                default:
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    break;
            }

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                }
            }, 3000);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Map<String, LinkedList<Integer>> getGroupFromString(
            Map<Integer, Map<String, LinkedList<Integer>>> groups) {
        String nameInsertedCopy = nameInserted;
        nameInsertedCopy = NormalizeText.removeAccents(nameInsertedCopy);
        String[] nameInsertedCopyArray = nameInsertedCopy.split(" ");

        for (Entry<Integer, Map<String, LinkedList<Integer>>> entry : groups
                .entrySet()) {
            for (Entry<String, LinkedList<Integer>> entry2 : entry.getValue()
                    .entrySet()) {
                String playerName = entry2.getKey();
                playerName = NormalizeText.removeAccents(playerName);
                System.out.println(playerName);
                String[] playerNameArray = playerName.split(" ");

                for (int i = playerNameArray.length - 1; i > 0; i--) {
                    for (int j = nameInsertedCopyArray.length - 1; j > 0; j--) {
                        if (LevenshteinDistance.computeLevenshteinDistance(
                                playerNameArray[i], nameInsertedCopyArray[j]) < 1) {
                            if (playerNameArray[i - 1] != null
                                    && nameInsertedCopyArray[j - 1] != null
                                    && playerNameArray[i - 1]
                                    .equals(nameInsertedCopyArray[j - 1])) {
                                numberOfGroup = entry.getKey();
                                return groups.get(entry.getKey());
                            }
                        }
                    }
                }
            }
        }

        for (Entry<Integer, Map<String, LinkedList<Integer>>> entry : groups
                .entrySet()) {
            for (Entry<String, LinkedList<Integer>> entry2 : entry.getValue()
                    .entrySet()) {
                String playerName = entry2.getKey().toLowerCase(
                        new Locale("ES"));
                if (playerName.contains(nameInsertedCopy)) {
                    numberOfGroup = entry.getKey();
                    return groups.get(entry.getKey());
                }
            }
        }

        System.out.println(nameInserted);
        return null;
    }

    @Override
    public void injectGroups(
            final Map<Integer, Map<String, LinkedList<Integer>>> groups) {
        Map<String, LinkedList<Integer>> groupPlayers = null;

        // Group number election
        if (numberOfGroup != 0) {
            groupPlayers = groups.get(numberOfGroup);
        } else if (nameInserted != null) {
            groupPlayers = getGroupFromString(groups);

        }

        Typeface face = Typeface.createFromAsset(getAssets(),
                Design.ROBOTO_REGULAR_FONT);
        Intent intent = new Intent(this, MainActivity.class);
        if (groupPlayers != null) {
            TextView textViewTitle = (TextView) findViewById(R.id.textViewTitle);
            textViewTitle.setText(getString(R.string.group) + " "
                    + numberOfGroup);
            textViewTitle.setTextSize(35);
            textViewTitle.setTypeface(face);
            textViewTitle.setGravity(Gravity.CENTER);
            textViewTitle.setTypeface(null, Typeface.BOLD);
            textViewTitle.setTextColor(getResources().getColor(R.color.red));

            TableLayout table = (TableLayout) findViewById(R.id.tableLayout1);

            // Header
            TableRow rowHeader = new TableRow(this);

            TextView textView = createSpecialTextView();
            textView.setText(getString(R.string.group) + " " + numberOfGroup);
            rowHeader.addView(textView);

            for (Entry<String, LinkedList<Integer>> entry : groupPlayers
                    .entrySet()) {
                String playerName = entry.getKey();
                textView = createSpecialTextView();
                textView.setText(playerName);
                rowHeader.addView(textView);
            }

            textView = createSpecialTextView();
            textView.setText(R.string.points);
            rowHeader.addView(textView);

            table.addView(rowHeader);

            // Players and points
            int playerIndex = 0;
            for (Entry<String, LinkedList<Integer>> entry : groupPlayers
                    .entrySet()) {
                rowHeader = new TableRow(this);
                String playerName = entry.getKey();
                LinkedList<Integer> points = entry.getValue();
                textView = createSpecialTextView();
                textView.setText(playerName);
                rowHeader.addView(textView);

                for (int i = 0; i < points.size(); i++) {
                    Integer point = points.get(i);
                    textView = createSpecialTextView();
                    if (playerIndex == i)
                        textView.setBackgroundResource(R.drawable.custom_text_view_empty);
                    if (point != -1)
                        textView.setText(String.valueOf(point));
                    else
                        textView.setText("");
                    rowHeader.addView(textView);
                }

                table.addView(rowHeader);
                playerIndex++;
            }
        } else if (nameInserted != null) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    R.string.notFound, Toast.LENGTH_LONG);
            toast.show();
            intent.putExtra(MainActivity.LOAD_PLAYER_FRAGMENT, true);
            startActivity(intent);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    R.string.errorConnection, Toast.LENGTH_LONG);
            toast.show();
            startActivity(intent);
        }

    }

    @Override
    protected void onResume() {
        super.onDestroy();
        Preferences.saveNumberOfGroupInPreferences(this, numberOfGroup);
    }

    private TextView createSpecialTextView() {
        Typeface face = Typeface.createFromAsset(getAssets(),
                Design.ROBOTO_REGULAR_FONT);
        TextView textView = new TextView(this);
        textView.setBackgroundResource(R.drawable.custom_text_view);
        textView.setLayoutParams(new TableRow.LayoutParams(0,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT, 1));
        textView.setHorizontallyScrolling(false);
        textView.setEllipsize(TruncateAt.END);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(10);
        textView.setTypeface(face);
        return textView;
    }
}
