package mynote.com.example.mynotes;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Abi on 9/16/2016.
 */
public class Note implements Parcelable,mCard {

    private String title,text;
    private int ID;

    public Note(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(title);
        dest.writeString(text);
        dest.writeInt(ID);
    }

    public static Parcelable.Creator<Note> CREATOR = new Parcelable.Creator<Note>()
    {

        @Override
        public Note createFromParcel(Parcel source) {
            return new Note(source);
        }

        @Override
        public Note[] newArray(int size) {
            return null;
        }
    };

    private Note(Parcel src)
    {
        title = src.readString();
        text = src.readString();
        ID = src.readInt();
    }


    @Override
    public int getCardType() {
        return R.layout.note_card_layout;
    }

    @Override
    public void onBindView(CardView cardView) {

        TextView tv1 = (TextView) cardView.findViewById(R.id.note_card_title);
        tv1.setText(this.title);

        TextView tv2 = (TextView) cardView.findViewById(R.id.note_card_text);
        tv2.setText(this.text);

    }
}
