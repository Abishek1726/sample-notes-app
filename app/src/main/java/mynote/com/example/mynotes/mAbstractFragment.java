package mynote.com.example.mynotes;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class mAbstractFragment extends Fragment implements NoteCardAdapter.onCardClickListener, NoteCardAdapter.onActionModeClickListener {

    RecyclerView rv = null;
    NoteCardAdapter.MultiSelector multiSelector;
    ActionMode actionMode = null;
    public static final int LAYOUT_LIST = 0, LAYOUT_STAG_GRID = 1;
    private int LAYOUT_TYPE = 0;

    public mAbstractFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCardLongClick() {
        MainActivity activity = (MainActivity) getActivity();
        NoteCardAdapter.mActionModeCallback actionModeCallback = new NoteCardAdapter.mActionModeCallback(multiSelector);
        actionModeCallback.setListener(this);
        actionMode = activity.startSupportActionMode(actionModeCallback);
    }

    @Override
    public void onDeselect() {
        if (actionMode != null)
            actionMode.finish();
    }

    @Override
    public void onUpButtonPressed() {

        for (int x : multiSelector.selectedSet) {
            NoteCardAdapter.ViewHolder v = (NoteCardAdapter.ViewHolder) rv.findViewHolderForAdapterPosition(x);

            if (v != null)
                v.cardView.setActivated(false);
        }

    }

    private void createLayout() {
        if (LAYOUT_TYPE == LAYOUT_LIST) {
            rv.setLayoutManager(new LinearLayoutManager(getActivity()));
            return;
        }
        if (LAYOUT_TYPE == LAYOUT_STAG_GRID) {
            rv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            return;
        }
    }

    public void setLayoutType(int ID) {
        LAYOUT_TYPE = ID;
    }

    public void setRecyclerView(RecyclerView rv) { this.rv = rv; }

    @Override
    public final void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        multiSelector = new NoteCardAdapter.MultiSelector();


        NoteCardAdapter adapter = new NoteCardAdapter(getmCardList(), multiSelector);
        adapter.setCardClickedListener(this);

        if(rv == null)
        throw new NullPointerException("Call mAbstractFragment.setRecyclerView(RecyclerView) to initialize recycler view ");

        createLayout();
        rv.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (rv != null) {
            NoteCardAdapter adapter = (NoteCardAdapter) rv.getAdapter();

            adapter.setmCardList(getmCardList());
            ///should optimize
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDeletePressed() {

        NoteCardAdapter adapter = (NoteCardAdapter) rv.getAdapter();

        for (int x : multiSelector.selectedSet) {
            deleteCard(adapter.getmCardList().get(x));

        }
        List<mCard> list = new ArrayList<>();
        list.addAll(getmCardList());
        adapter.setmCardList(list);

        ////should optimize
        adapter.notifyDataSetChanged();


    }

    @Override
    public void onCardClick(mCard card) {
        openCard(card);
    }


    abstract List<mCard> getmCardList();

    abstract void deleteCard(mCard card);

    abstract void openCard(mCard card);

}
