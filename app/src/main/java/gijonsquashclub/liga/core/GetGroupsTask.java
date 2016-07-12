package gijonsquashclub.liga.core;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.Map;

public class GetGroupsTask extends
        AsyncTask<Void, Void, Map<Integer, Map<String, LinkedList<Integer>>>> {

    public GetGroupsTaskResponse response = null;
    private ProgressBar progressBar;
    private TableLayout tableLayout;
    private TextView textView;

    @Override
    protected Map<Integer, Map<String, LinkedList<Integer>>> doInBackground(
            Void... params) {
        return LeagueParser.getGroups();
    }

    @Override
    protected void onPostExecute(
            Map<Integer, Map<String, LinkedList<Integer>>> groups) {
        response.injectGroups(groups);

        textView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        tableLayout.setVisibility(View.VISIBLE);
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public void setTableLayout(TableLayout tableLayout) {
        this.tableLayout = tableLayout;
    }

    public void setTableLayout(TextView textView) {
        this.textView = textView;
    }
}
