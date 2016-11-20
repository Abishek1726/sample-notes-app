package mynote.com.example.mynotes;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.ActionBarOverlayLayout;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Abi on 10/7/2016.
 */
public class CheckNote implements Parcelable, mCard {

    private String title;
    private int ID;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    ///Should support duplicate keys
    private Map<String, Boolean> checkNoteMap;

    public CheckNote(String title, Map<String, Boolean> checkNoteMap) {
        this.title = title;
        this.checkNoteMap = checkNoteMap;
    }

    public String getTitle() {
        return title;
    }

    public Map<String, Boolean> getCheckNoteMap() {
        return checkNoteMap;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCheckNote(HashMap<String, Boolean> checkNote) {
        this.checkNoteMap = checkNote;
    }

    ///Parcelable implementation

    protected CheckNote(Parcel in) {
        checkNoteMap = new HashMap<>();
        title = in.readString();
        ID = in.readInt();

        String text[] = new String[in.readInt()];
        in.readStringArray(text);

        boolean checked[] = new boolean[in.readInt()];
        in.readBooleanArray(checked);


        for(int i = 0;i<text.length;++i)
        checkNoteMap.put(text[i],checked[i]);
    }

    public static final Creator<CheckNote> CREATOR = new Creator<CheckNote>() {
        @Override
        public CheckNote createFromParcel(Parcel in) {
            return new CheckNote(in);
        }

        @Override
        public CheckNote[] newArray(int size) {
            return new CheckNote[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeInt(ID);
        Bundle b = new Bundle();
        Set<Map.Entry<String, Boolean>> set = this.getCheckNoteMap().entrySet();

        String[] text = new String[set.size()];
        boolean[] checked = new boolean[set.size()];
        int i = 0;

        for (Map.Entry<String, Boolean> me : set) {

            text[i] = me.getKey();
            checked[i] = me.getValue();
        }

        dest.writeInt(text.length);
        dest.writeStringArray(text);
        dest.writeInt(checked.length);
        dest.writeBooleanArray(checked);
    }


    /////mCard implementation
    @Override
    public int getCardType() {
        return 1;
    }

    @Override
    public void onBindView(CardView cardView) {

        cardView.removeAllViews();

        cardView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        LinearLayout ll = new LinearLayout(cardView.getContext());
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        TextView tv = new TextView(cardView.getContext());
        tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        CheckBox checkBox = new CheckBox(cardView.getContext());
        checkBox.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        Set<Map.Entry<String, Boolean>> set = checkNoteMap.entrySet();
        Iterator<Map.Entry<String, Boolean>> it = set.iterator();
        Map.Entry<String, Boolean> e = it.next();

        tv.setText(this.title);

        checkBox.setText(e.getKey());
        checkBox.setChecked(e.getValue());

        ll.addView(tv);
        ll.addView(checkBox);

        cardView.addView(ll);
        ll.setFocusable(false);

    }
}
