package marabillas.loremar.taskador.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import marabillas.loremar.taskador.R;
import marabillas.loremar.taskador.ui.adapter.MainInappViewPagerAdapter;

public class MainInAppActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maininapp);

        ViewPager pager = findViewById(R.id.activity_maininapp_viewpager);
        FragmentManager fm = getSupportFragmentManager();
        pager.setAdapter(new MainInappViewPagerAdapter(fm));
    }
}
