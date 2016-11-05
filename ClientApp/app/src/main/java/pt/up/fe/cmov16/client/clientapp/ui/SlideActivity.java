package pt.up.fe.cmov16.client.clientapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import io.swagger.client.model.Voucher;
import pt.up.fe.cmov16.client.clientapp.R;
import pt.up.fe.cmov16.client.clientapp.logic.ProductMenuItem;
import pt.up.fe.cmov16.client.clientapp.ui.slides.HistoricFragment;
import pt.up.fe.cmov16.client.clientapp.ui.slides.NamedFragment;
import pt.up.fe.cmov16.client.clientapp.ui.slides.ProductsFragment;
import pt.up.fe.cmov16.client.clientapp.ui.slides.VouchersFragment;

public class SlideActivity extends FragmentActivity {

    private ViewPager mPager;
    private NamedFragment[] fragments;
    private TextView tittle;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide);

        fragments = new NamedFragment[]{
                ProductsFragment.newInstance(0),
                HistoricFragment.newInstance(1),
                VouchersFragment.newInstance(2)
        };

        floatingActionButton = (FloatingActionButton) findViewById(R.id.fabButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HistoricFragment)fragments[1]).refresh();
            }
        });
        floatingActionButton.hide();
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mPager.setOffscreenPageLimit(3);

        //The pager adapter, which provides the pages to the view pager widget.
        ScreenSlidePagerAdapter mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //change title when page change
                tittle.setText(fragments[position].toString());
                ((AppBarLayout) findViewById(R.id.appBarLayout)).setExpanded(true, true);
                //fragments[position].focusObtained(SlideActivity.this);
                if(position==1){
                    floatingActionButton.show();
                }else {
                    floatingActionButton.hide();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mPager.setAdapter(mPagerAdapter);

        tittle = (TextView) findViewById(R.id.frag_tittle);
        tittle.setText(fragments[0].toString());

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(mPager);

        findViewById(R.id.cartButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SlideActivity.this, CartActivity.class);
                ArrayList<ProductMenuItem> ps = ((ProductsFragment) fragments[0]).getProducts();

                i.putExtra(CartActivity.PRODUCTS_ARRAY_KEY, ps);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (fragments[mPager.getCurrentItem()].getChildFragmentManager().getBackStackEntryCount() > 0) {
            //closes any open fragment if there's one in the current
            fragments[mPager.getCurrentItem()].getChildFragmentManager().popBackStack();
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            //fragment if there is a fragment above the main, closes it
            getSupportFragmentManager().popBackStack();
        } else if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    /**
     * Pager adapter
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return fragments[position].toString();
        }
    }

    /**
     * Animation handler between screens of viewPager
     */
    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}
