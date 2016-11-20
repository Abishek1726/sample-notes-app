package mynote.com.example.mynotes;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;


public class EditFragment extends Fragment implements View.OnClickListener {

    EditText notesEditText, titleEditText;
    Toolbar options;
    ImageButton saveButton;
    NotesDatabaseHelper dbHelper;
    Note currentNote = null;
    public EditFragment() {
        // Required empty public constructor

    }

    public void setCurrentNote(Note currentNote) {
        this.currentNote = currentNote;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_edit, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        notesEditText = (EditText) getView().findViewById(R.id.notes_et);
        titleEditText = (EditText) getView().findViewById(R.id.title_et);
        options = (Toolbar) getView().findViewById(R.id.toolbar1);
        saveButton = (ImageButton) getView().findViewById(R.id.save_button);

            if(!NotesDatabaseHelper.isContextSet())
                NotesDatabaseHelper.setContext(getActivity());

            dbHelper = NotesDatabaseHelper.getInstance();

        //Log.d("EditFragment","OnActiity created");

        if(currentNote!=null)
        {
            titleEditText.setText(currentNote.getTitle());
            notesEditText.setText(currentNote.getText());
        }
        notesEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                options.setVisibility(View.VISIBLE);

            }
        });

        saveButton.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == saveButton.getId()) {
            String title = titleEditText.getText().toString();
            String str = notesEditText.getText().toString();

            if(currentNote==null) {
                currentNote = new Note(title, str);
                dbHelper.insertNote(currentNote);
            }
            else
            {
                currentNote.setTitle(title);
                currentNote.setText(str);
                dbHelper.updateNote(currentNote);
            }
            SavedTextFragment fragment = new SavedTextFragment();
            fragment.setCurrentNote(currentNote);

            getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();

        }
    }
}
