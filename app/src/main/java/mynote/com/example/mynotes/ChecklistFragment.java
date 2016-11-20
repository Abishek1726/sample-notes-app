package mynote.com.example.mynotes;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChecklistFragment extends mAbstractFragment {

    NotesDatabaseHelper db;

    public ChecklistFragment() {
        // Required empty public constructor
    }

    @Override
    List<mCard> getmCardList() {

        List<mCard> list = new ArrayList<>();
        list.addAll(db.getCheckNoteList());
        return list;

    }

    @Override
    void deleteCard(mCard card) {
        ///// TODO: 10/13/2016  
    }

    @Override
    void openCard(mCard card) {
        Intent intent = new Intent(getActivity(), ContentActivity.class);
        intent.putExtra(MainActivity.CONTENT_KEY, (CheckNote) card);
        startActivity(intent);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if (!NotesDatabaseHelper.isContextSet())
            NotesDatabaseHelper.setContext(getActivity());

        db = NotesDatabaseHelper.getInstance();


        View fragView = inflater.inflate(R.layout.fragment_checklist, container, false);

        RecyclerView rv = (RecyclerView)fragView.findViewById(R.id.check_notes_recycler);
        setRecyclerView(rv);

        return fragView;

    }

    
}
