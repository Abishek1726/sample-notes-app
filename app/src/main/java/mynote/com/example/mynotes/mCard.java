package mynote.com.example.mynotes;

import android.support.v7.widget.CardView;
import android.view.ViewGroup;

/**
 * Created by Abi on 10/12/2016.
 */
public interface mCard {

int getCardType();
void onBindView(CardView cardView);

}
