package mynote.com.example.mynotes;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Created by Abi on 10/7/2016.
 */
public class CheckView extends LinearLayout {

    private CheckBox checkBox;
    private EditText editText;
    Context context;

    public CheckView(Context context) {
        super(context);
        this.context = context;
        initViews();
    }

    private void initViews() {
        checkBox = new CheckBox(context);
        editText = new EditText(context);

        editText.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        editText.setBackgroundColor(Color.TRANSPARENT);
        setBackgroundColor(Color.GRAY);
        setOrientation(LinearLayout.HORIZONTAL);
        addView(checkBox);
        addView(editText);
    }

    public boolean isChecked()
    {
        return checkBox.isChecked();
    }
    public String getText()
    {
       return editText.getText().toString();
    }
    public void setText(String s) { editText.setText(s);}
    public void setChecked(boolean b){ checkBox.setChecked(b); }
    public void disableEdit()
    {
        editText.setFocusable(false);
    }
}
