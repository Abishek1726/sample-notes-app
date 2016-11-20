package mynote.com.example.mynotes;

import android.support.v7.view.ActionMode;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Abi on 9/16/2016.
 */
public class NoteCardAdapter extends RecyclerView.Adapter<NoteCardAdapter.ViewHolder> {

    List<mCard> mCardList;
    onCardClickListener cardClickedListener;
    MultiSelector multiSelector;

    public List<mCard> getmCardList() {
        return mCardList;
    }

    public void setmCardList(List<mCard> mCardList) {
        this.mCardList = mCardList;
    }

    public void setCardClickedListener(onCardClickListener cardClickedListener) {
        this.cardClickedListener = cardClickedListener;
    }

    public NoteCardAdapter(List<mCard> list, MultiSelector multiSelector) {
        mCardList = list;
        this.multiSelector = multiSelector;
    }

    @Override
    public NoteCardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = null;
        if(viewType == R.layout.note_card_layout)
         cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.note_card_layout, parent, false);
        else if(viewType == 1)
            cv = new CardView(parent.getContext());

        return new ViewHolder(cv);
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if(!multiSelector.isSelectable())
            holder.cardView.setActivated(false);

    }

    @Override
    public int getItemViewType(int position) {
        return mCardList.get(position).getCardType();
    }

    @Override
    public void onBindViewHolder(NoteCardAdapter.ViewHolder holder, final int position) {

        CardView cv = holder.cardView;

        mCardList.get(position).onBindView(cv);

        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (multiSelector.isSelectable()) {
                    v.setActivated(!v.isActivated());
                    multiSelector.selectAt(position, v.isActivated());

                    if (multiSelector.selectedSet.size() == 0) {
                        cardClickedListener.onDeselect();
                        multiSelector.isCardSelectable = false;

                    }

                } else
                    cardClickedListener.onCardClick(mCardList.get(position));
            }
        });
        cv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!multiSelector.isSelectable()) {
                    v.setActivated(!v.isActivated());
                    cardClickedListener.onCardLongClick();
                    multiSelector.isCardSelectable = true;
                    multiSelector.selectAt(position, true);
                    return true;
                }
                return false;
            }
        });

        Log.d("bindview",String.valueOf(position));
    }

    @Override
    public int getItemCount() {
        return mCardList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;

        public ViewHolder(CardView cardView) {
            super(cardView);
            this.cardView = cardView;
            Log.d("bindview","new holder");
        }
    }

    public interface onCardClickListener {

        public void onCardClick(mCard card);

        public void onCardLongClick();

        public void onDeselect();
    }

    public interface onActionModeClickListener {
        public void onDeletePressed();
        public void onUpButtonPressed();
    }

    static class mActionModeCallback implements ActionMode.Callback {

        onActionModeClickListener listener;
        MultiSelector multiSelector;

        public mActionModeCallback(MultiSelector multiSelector) {
            this.multiSelector = multiSelector;
        }

        public void setListener(onActionModeClickListener listener) {
            this.listener = listener;
        }


        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {

            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.appbar_context_menu, menu);


            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.delete_context_menu: {

                    multiSelector.setCardSelectable(false);
                    listener.onDeletePressed();
                    mode.finish();
                    return true;
                }

            }

            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if (multiSelector.isSelectable()) {
                listener.onUpButtonPressed();
                multiSelector.setCardSelectable(false);
            }
            multiSelector.selectedSet.clear();
        }

    }

    static class MultiSelector {
        HashSet<Integer> selectedSet;
        boolean isCardSelectable;

        public MultiSelector() {
            this.selectedSet = new HashSet<Integer>();
            this.isCardSelectable = false;
        }

        public boolean isSelectable() {
            return isCardSelectable;
        }

        public void setCardSelectable(boolean cardSelectable) {
            isCardSelectable = cardSelectable;
        }

        public void selectAt(int x, boolean b) {
            if (!b)
                selectedSet.remove(x);
            else
                selectedSet.add(x);
        }

        public boolean isSelected(int pos) {
            return selectedSet.contains(pos);
        }
    }
}
