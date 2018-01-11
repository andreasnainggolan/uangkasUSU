package app.uangkasUSU;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import app.uangkasUSU.fragment.Keluar;
import app.uangkasUSU.fragment.Masuk;

public class NewActivity extends AppCompatActivity {

    private final String[] PAGE_TITLES = new String[] {
            "PEMASUKAN",
            "PENGELUARAN"
    };

    private final Fragment[] PAGES = new Fragment[] {
            new Masuk(),
            new Keluar()
    };

    private ViewPager view_pager;
    String nim2;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        view_pager = (ViewPager) findViewById(R.id.view_pager);
        view_pager.setAdapter(new MyPagerAdapter(getFragmentManager()));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(view_pager);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Tambah");
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return PAGES[position];
        }

        @Override
        public int getCount() {
            return PAGES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return PAGE_TITLES[position];
        }

    }
}
