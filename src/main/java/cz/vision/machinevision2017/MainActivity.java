package cz.vision.machinevision2017;


import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnPageChange;
import cz.vision.machinevision2017.activities.BusProvider;
import cz.vision.machinevision2017.activities.LockableViewPager;
import cz.vision.machinevision2017.activities.MainPagerAdapter;
import cz.vision.machinevision2017.fragments.CameraFragment;
import cz.vision.machinevision2017.fragments.ContentFragment;
import cz.vision.machinevision2017.jsonmoshi.MoshiJson;
import cz.vision.machinevision2017.jsonmoshi.Symbol;
import cz.vision.machinevision2017.vision.recognitionobject.detectobject.ObjectInfo;
import cz.vision.mylog.MyLog;

public class MainActivity extends AppCompatActivity {

    private MainPagerAdapter pagerAdapter;
    private Bus bus;
    @BindView(R.id.main_view_pager)
    LockableViewPager mainViewPager;
    @BindView(R.id.nav_toolbar)
    Toolbar navToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        pagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        mainViewPager.setAdapter(pagerAdapter);

        setPage(0);
        setSupportActionBar(navToolbar);
        mainViewPager.setSwipeable(false);
        bus = BusProvider.getInstance();
    }


    public void setPage(int position){
        mainViewPager.setCurrentItem(position);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu_button, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.action_exit:
                System.runFinalization();
                System.exit(0);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
    @OnPageChange(R.id.main_view_pager)
    public void onViewPager(int position) {

        if (position == 0) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            getSupportActionBar().hide();
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            getSupportActionBar().show();
        }
    }
    @Subscribe
    public void buttonTrack(CameraFragment cameraFragment){
        setPage(1);
    }

    @Subscribe
    public void buttonBack(ContentFragment contentFragment){
        setPage(0);
    }


}
