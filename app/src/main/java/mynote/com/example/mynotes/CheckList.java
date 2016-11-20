package mynote.com.example.mynotes;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Abi on 10/7/2016.
 */
public class CheckList {
    List<CheckView> checkViews;
    Context context;
    ViewGroup container;
    boolean isEditDisabled = false;


    public CheckList(Context context, ViewGroup container) {
        this.context  = context;
        this.container = container;
        checkViews = new ArrayList<>();
    }

    public void addCheckViews(List<CheckViewData> checkViewData)
    {
        for (CheckViewData cvd:checkViewData) {
            CheckView checkView = new CheckView(context);

            if(isEditDisabled)
            checkView.disableEdit();

            checkView.setText(cvd.getText());
            checkView.setChecked(cvd.isChecked());

            container.addView(checkView);

            checkViews.add(checkView);
        }
    }

    public void newCheckView()
    {
        CheckView checkView = new CheckView(context);

        if(isEditDisabled)
        checkView.disableEdit();

        container.addView(checkView);
        checkViews.add(checkView);
    }

    public List<CheckViewData> getCheckViewDataList() {

        List<CheckViewData>  checkViewDataList = new ArrayList<>();
        for (CheckView cv:checkViews) {
            checkViewDataList.add(new CheckViewData(cv.getText(),cv.isChecked()));
        }

        return checkViewDataList;
    }

    public static class CheckViewData
    {
        private String text;
        private boolean isChecked;

        public CheckViewData(String text, boolean isChecked) {
            this.text = text;
            this.isChecked = isChecked;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }
    }


    public void setEditDisabled(boolean editDisabled) {
        isEditDisabled = editDisabled;
    }
}
