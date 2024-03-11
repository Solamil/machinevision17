package cz.vision.machinevision2017.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.squareup.otto.Bus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.vision.machinevision2017.R;
import cz.vision.machinevision2017.activities.BusProvider;


public class ContentFragment extends Fragment {

    private Bus bus;
    @BindView(R.id.button_back)
    ImageButton buttonBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content, container, false);

        ButterKnife.bind(this, view);
        ObjectFragment objectFragment = new ObjectFragment();

        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_content, objectFragment).commit();
        bus = BusProvider.getInstance();
        return view;
    }

    @OnClick(R.id.button_back)
    public void onButtonBack(){
        bus.post(buttonBack);
        bus.post(this);
    }
    @Override
    public void onResume(){
        super.onResume();
        bus.register(this);
    }
    @Override
    public void onPause(){
        super.onPause();
        bus.unregister(this);
    }

}
