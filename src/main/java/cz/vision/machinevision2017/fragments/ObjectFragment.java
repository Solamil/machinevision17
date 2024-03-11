package cz.vision.machinevision2017.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.vision.machinevision2017.MainActivity;
import cz.vision.machinevision2017.R;
import cz.vision.machinevision2017.activities.BusProvider;
import cz.vision.machinevision2017.jsonmoshi.MoshiJson;
import cz.vision.machinevision2017.jsonmoshi.Symbol;
import cz.vision.machinevision2017.vision.recognitionobject.detectobject.ObjectInfo;
import cz.vision.mylog.MyLog;


public class ObjectFragment extends Fragment {

    private Bus bus;
    private ObjectInfo objectInfo;
    private Bundle bundle = new Bundle();

    private List<Symbol> symbols;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.description_text)
    TextView descriptionText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_object, container, false);

        symbols = MoshiJson.moshiJsonInfoObject();
        if(savedInstanceState != null) {
            if (savedInstanceState.containsKey("object")) {
                bundle = savedInstanceState.getBundle("object");
            }
        }
        bus = BusProvider.getInstance();

        ButterKnife.bind(this, view);


        return view;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBundle("object", bundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        bus.register(this);
        if (!bundle.isEmpty()) {
//            bundle = savedInstanceState;
            titleText.setText(bundle.getString("title"));
            descriptionText.setText(bundle.getString("description"));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        bus.unregister(this);
    }

    @Subscribe
    public void setInfo(ObjectInfo objectInfo) {
        this.objectInfo = objectInfo;
        for (Symbol symbol : symbols) {
            if (symbol.getName().contains(this.objectInfo.getName())) {
                MyLog.i(MyLog.TAG, this.objectInfo.getName());
                bundle.putString("title", symbol.getTitle());
                bundle.putString("description", symbol.getDescription());
            }
        }


    }

}
