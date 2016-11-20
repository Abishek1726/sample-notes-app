package mynote.com.example.mynotes;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class CheckListEditFragment extends Fragment implements View.OnClickListener {

    Button newItemButton;
    CheckList checkList;
    ImageButton saveButton;
    EditText editText;
    CheckNote currentCheckNote = null;

    public CheckListEditFragment() {
        // Required empty public constructor
    }

    public void setCurrentCheckNote(CheckNote currentCheckNote) {
        this.currentCheckNote = currentCheckNote;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_check_list_edit, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        newItemButton = (Button) getView().findViewById(R.id.new_checkview_button);
        saveButton = (ImageButton) getView().findViewById(R.id.save_cl);
        editText = (EditText)getView().findViewById(R.id.cl_title_et);

        LinearLayout ll = (LinearLayout) getView().findViewById(R.id.ll1);
        checkList = new CheckList(getActivity(), ll);

        newItemButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);

        if(currentCheckNote!=null)
        {
            editText.setText(currentCheckNote.getTitle());

            Set<Map.Entry<String,Boolean>> set = currentCheckNote.getCheckNoteMap().entrySet();

            List<CheckList.CheckViewData> cvdList = new ArrayList<>();

            for (Map.Entry<String,Boolean> m:set) {
                cvdList.add(new CheckList.CheckViewData(m.getKey(),m.getValue()));
            }
            checkList.addCheckViews(cvdList);
        }
        else
            checkList.newCheckView();

    }


    @Override
    public void onClick(View v) {

        if (v.getId() == newItemButton.getId()) {
            checkList.newCheckView();
        } else if (v.getId() == saveButton.getId()) {
            List<CheckList.CheckViewData> checkViewDataList = checkList.getCheckViewDataList();
            Map<String, Boolean> cMap = new HashMap<>();
            for (CheckList.CheckViewData cd : checkViewDataList) {
                cMap.put(cd.getText(), cd.isChecked());
            }

            CheckNote checkNote = new CheckNote(editText.getText().toString(), cMap);

            if(!NotesDatabaseHelper.isContextSet())
            NotesDatabaseHelper.setContext(getActivity());

            NotesDatabaseHelper.getInstance().insertCheckNote(checkNote);

            SavedCheckFragment savedCheckFragment = new SavedCheckFragment();
            savedCheckFragment.setCurrentCheckNote(checkNote);

            getFragmentManager().beginTransaction().replace(R.id.content_frame,savedCheckFragment).commit();

        }
    }
}
