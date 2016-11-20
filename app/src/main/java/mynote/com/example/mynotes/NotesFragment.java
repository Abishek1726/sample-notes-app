package mynote.com.example.mynotes;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class NotesFragment extends mAbstractFragment {

    NotesDatabaseHelper db;

    public NotesFragment() {
        // Required empty public constructor
    }

    @Override
    List<mCard> getmCardList() {
        List<mCard> list = new ArrayList<>();
        list.addAll(db.getAllNotes());
        return list;
    }

    @Override
    void deleteCard(mCard card) {
        Note note = (Note) card;
        db.deleteNote(note.getID());
    }

    @Override
    void openCard(mCard card) {

        Intent intent = new Intent(getActivity(), ContentActivity.class);
        intent.putExtra(MainActivity.CONTENT_KEY, (Note) card);
        startActivity(intent);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if (!NotesDatabaseHelper.isContextSet())
            NotesDatabaseHelper.setContext(getActivity());

        db = NotesDatabaseHelper.getInstance();

        View fragView = inflater.inflate(R.layout.fragment_notes, container, false);

        RecyclerView rv = (RecyclerView)fragView.findViewById(R.id.notes_recycler);
        setRecyclerView(rv);

        return fragView;
    }


}
