package mynote.com.example.mynotes;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class SavedCheckFragment extends Fragment implements View.OnClickListener {

    CheckList checkList;
    ImageButton editButton;
    TextView titleText;
    CheckNote currentCheckNote = null;

    public SavedCheckFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved_check, container, false);
    }


    public void setCurrentCheckNote(CheckNote currentCheckNote) {
        this.currentCheckNote = currentCheckNote;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        editButton = (ImageButton) getView().findViewById(R.id.edit_cl);
        titleText = (TextView) getView().findViewById(R.id.cl_title_tv);

        LinearLayout ll = (LinearLayout) getView().findViewById(R.id.saved_check_fragment_ll);
        checkList = new CheckList(getActivity(), ll);
        checkList.setEditDisabled(true);

        editButton.setOnClickListener(this);

        if(currentCheckNote!=null)
        {
            titleText.setText(currentCheckNote.getTitle());

            Set<Map.Entry<String,Boolean>> set = currentCheckNote.getCheckNoteMap().entrySet();

            List<CheckList.CheckViewData> cvdList = new ArrayList<>();

            for (Map.Entry<String,Boolean> m:set) {
                cvdList.add(new CheckList.CheckViewData(m.getKey(),m.getValue()));
            }
            checkList.addCheckViews(cvdList);
        }

    }


    @Override
    public void onClick(View v) {

       if (v.getId() == editButton.getId()) {

           CheckListEditFragment checkListEditFragment = new CheckListEditFragment();
           checkListEditFragment.setCurrentCheckNote(currentCheckNote);

           getFragmentManager().beginTransaction().replace(R.id.content_frame,checkListEditFragment).commit();

        }
    }



}
