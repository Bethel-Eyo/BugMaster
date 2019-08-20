package com.google.developer.bugmaster;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.developer.bugmaster.data.BugsDbHelper;
import com.google.developer.bugmaster.data.DatabaseManager;
import com.google.developer.bugmaster.data.Insect;
import com.google.developer.bugmaster.data.InsectRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.google.developer.bugmaster.QuizActivity.EXTRA_ANSWER;
import static com.google.developer.bugmaster.QuizActivity.EXTRA_INSECTS;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener {

    private InsectRecyclerAdapter mAdapter;
    RecyclerView recycler;
    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recycler = (RecyclerView) findViewById(R.id.recycler_view);
        recycler.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        assert recycler != null;
        recycler.setLayoutManager(layoutManager);

        BugsDbHelper dbHelper = new BugsDbHelper(this);
        mDb = dbHelper.getWritableDatabase();

        mAdapter = new InsectRecyclerAdapter(getBugData(), this);
        recycler.setAdapter(mAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort:
                //TODO: Implement the sort action
                DatabaseManager databaseManager = new DatabaseManager(this);
                mAdapter = new InsectRecyclerAdapter(databaseManager
                        .queryAllInsects(DatabaseManager.Contract.Columns
                                .FRIENDLY_NAME), this);
                recycler.setAdapter(mAdapter);
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public Cursor getBugData(){
        return mDb.query(
                DatabaseManager.Contract.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    /* Click events in Floating Action Button */
    @Override
    public void onClick(View v) {
        //TODO: Launch the quiz activity
        Intent intent = new Intent(this, QuizActivity.class);
        Cursor cursor = getBugData();
        ArrayList<Insect> insects = new ArrayList<>();
        for(int i=0; i< cursor.getCount(); i++){
            if (i < 0 || i >= cursor.getCount()) {
                throw new IllegalArgumentException("Item position is out of adapter's range");
            } else if (cursor.moveToPosition(i)) {
                insects.add(new Insect(cursor));
            }
        }

        intent.putParcelableArrayListExtra(EXTRA_INSECTS, insects);
        intent.putExtra(EXTRA_ANSWER, insects.get(0));
        startActivity(intent);
    }

    public interface ItemClickListener{
        void onItemClicked(View v,Insect insect, int position);
    }
}
