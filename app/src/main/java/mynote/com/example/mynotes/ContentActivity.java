package mynote.com.example.mynotes;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class ContentActivity extends AppCompatActivity {

    Fragment currentFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        String fab_type = getIntent().getExtras().getString(MainActivity.FAB_TYPE);

        if (fab_type != null) {
            if (fab_type.equals("new_note"))
                currentFragment = new EditFragment();
            else
                currentFragment = new CheckListEditFragment();
        } else {
            Bundle b = getIntent().getExtras();
            Object o = b.getParcelable(MainActivity.CONTENT_KEY);
            if (o instanceof Note)
            {
                currentFragment = new SavedTextFragment();
                SavedTextFragment frag = (SavedTextFragment) currentFragment;
                frag.setCurrentNote((Note)o);
            }
            else if(o instanceof CheckNote)
            {
                currentFragment = new SavedCheckFragment();
                SavedCheckFragment frag = (SavedCheckFragment)currentFragment;
                frag.setCurrentCheckNote((CheckNote)o);
            }
        }
        getFragmentManager().beginTransaction().replace(R.id.content_frame, currentFragment).commit();
    }
}
