package mynote.com.example.mynotes;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class SavedTextFragment extends Fragment implements View.OnClickListener{

     TextView titleTextView,notesTextView;
     Note currentNote;
     ImageButton editButton;

    public void setCurrentNote(Note currentNote) {
        this.currentNote = currentNote;
    }

    public Note getCurrentNote() {
        return currentNote;
    }

    public SavedTextFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved_text, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        titleTextView = (TextView) getView().findViewById(R.id.notes_title_text);
        notesTextView = (TextView)getView().findViewById(R.id.notes_text);
        editButton = (ImageButton) getView().findViewById(R.id.edit_button);

        titleTextView.setText(currentNote.getTitle());
        notesTextView.setText(currentNote.getText());
        editButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == editButton.getId()) {
            EditFragment fragment = new EditFragment();
            fragment.setCurrentNote(currentNote);
            getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
        }

    }
}
