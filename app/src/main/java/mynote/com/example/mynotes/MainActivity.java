package mynote.com.example.mynotes;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    ActionBarDrawerToggle drawerToggle;
    DrawerLayout drawerLayout;
    ListView drawerList;
    FloatingActionButton fabMain, fabNote, fabChecklist;
    Toolbar appbar;
    boolean isFABclicked = false;
    public static String CONTENT_KEY = "notes", LAYOUT_KEY = "current_layout_type",FAB_TYPE = "fab_type";
    String currentLayoutType = "list";
    int selectedFragPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListeners();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout.closeDrawer(drawerList);

        selectedFragPosition = 0;

        replaceFragment(selectedFragPosition);
    }

    private void initView() {
        appbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(appbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.nav_drawer);
        drawerList = (ListView) findViewById(R.id.nav_list);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name);

        fabMain = (FloatingActionButton) findViewById(R.id.fab_main);
        fabNote = (FloatingActionButton) findViewById(R.id.fab_new_text);
        fabChecklist = (FloatingActionButton) findViewById(R.id.fab_new_checklist);
    }

    private void initListeners() {
        drawerLayout.addDrawerListener(drawerToggle);
        drawerList.setOnItemClickListener(this);

        fabMain.setOnClickListener(this);
        fabNote.setOnClickListener(this);
        fabChecklist.setOnClickListener(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.appbar_menu, menu);

        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        currentLayoutType = preferences.getString(LAYOUT_KEY, currentLayoutType);

        if (currentLayoutType.equals("list"))
            menu.findItem(R.id.appbar_menu_layout_options).setIcon(R.mipmap.ic_grid_option).setTitle("grid");
        else
            menu.findItem(R.id.appbar_menu_layout_options).setIcon(R.mipmap.ic_list_option).setTitle("list");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item))
            return true;

        if (item.getItemId() == R.id.appbar_menu_layout_options) {

            if (item.getTitle().toString().equals("list")) {
                item.setIcon(R.mipmap.ic_grid_option);
                item.setTitle("grid");
                currentLayoutType = "list";

            } else {
                item.setIcon(R.mipmap.ic_list_option);
                item.setTitle("list");
                currentLayoutType = "grid";

            }
            replaceFragment(selectedFragPosition);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        replaceFragment(position);
        drawerLayout.closeDrawer(drawerList);
    }

    private void replaceFragment(int pos) {
        selectedFragPosition = pos;
        int fragLayoutType;

        ///Refactor later
        if (currentLayoutType.equals("list"))
            fragLayoutType = mAbstractFragment.LAYOUT_LIST;
        else
            fragLayoutType = mAbstractFragment.LAYOUT_STAG_GRID;

        Fragment fragment = null;
        switch (selectedFragPosition) {
            case 0: {
                fragment = new AllNotesFragment();
            }
            break;
            case 1: {
                fragment = new NotesFragment();
                NotesFragment fragment1 = (NotesFragment) fragment;
                fragment1.setLayoutType(fragLayoutType);
            }
            break;
            case 2: {
                fragment = new ChecklistFragment();
                ChecklistFragment fragment1 = (ChecklistFragment)fragment;
                fragment1.setLayoutType(fragLayoutType);
            }
            break;
            case 3: {
                fragment = new FavouritesFragment();
            }
            break;
        }

        if (fragment != null)
            getFragmentManager().beginTransaction().replace(R.id.nav_frame, fragment).commit();


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fab_main: {
                if (!isFABclicked) {
                    fabNote.show();
                    fabChecklist.show();
                    isFABclicked = true;
                } else {
                    fabNote.hide();
                    fabChecklist.hide();
                    isFABclicked = false;
                }
            }
            break;
            case R.id.fab_new_text: {
                isFABclicked = false;
                fabNote.hide();
                fabChecklist.hide();

                Intent intent = new Intent(this, ContentActivity.class);
                intent.putExtra(FAB_TYPE,"new_note");
                startActivity(intent);
            }
            break;
            case R.id.fab_new_checklist:
            {
                isFABclicked = false;
                fabNote.hide();
                fabChecklist.hide();

                Intent intent = new Intent(this, ContentActivity.class);
                intent.putExtra(FAB_TYPE,"new_cl");
                startActivity(intent);

            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        preferences.edit().putString(LAYOUT_KEY, currentLayoutType).commit();
    }
}
