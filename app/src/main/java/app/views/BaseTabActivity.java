package app.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout.LayoutParams;

import com.sunshine.antilose2.R;

import java.util.ArrayList;




public abstract class BaseTabActivity extends BaseFragmentActivity
        implements OnClickListener {

    public static final String EXTRA_SHOW_TAB = "extra.show_tab";
    public static final String EXTRA_SHOW_TAB_ARGUMENTS = "extra.show_tab_arguments";
    public static final String EXTRA_TO_TOP = "extra.to_top";

    private static final String SAVED_CURRENT_TAB = "saved_current_tab";

    /**
     * Tab容器子类
     *
     */
    public static abstract class Tab extends BaseFragment {

        private boolean mSelected;

        /**
         * 获取这个tab的button的未选中的图片
         *
         * @return
         */
        public abstract int getTabButtonOffSrc();

        /**
         * 获取这个tab的button的选中的图片
         *
         * @return
         */
        public abstract int getTabButtonOnSrc();

        /**
         * 获取这个tab的button的未选中的图片
         *
         * @return
         */
        public abstract int getTabButtonOffBg();

        /**
         * 获取这个tab的button的选中的图片
         *
         * @return
         */
        public abstract int getTabButtonOnBg();

        /**
         * 获取这个tab的button的title
         *
         * @return
         */
        public abstract String getTitle(Context context);

        public abstract String getButtonTitle(Context context);

        public View getTitleView(Context context) {
            return null;
        }

        public int getLeftTitleButtonBackground() {
            return -1;
        }

        public int getRightTitleButtonBackground() {
            return -1;
        }

        public int getLeftTitleButtonSrc() {
            return -1;
        }

        public int getRightTitleButtonSrc() {
            return -1;
        }

        public String getRightTitleButtonText(Context context) {
            return null;
        }

        public int getTitleViewBackground() {
            return R.drawable.bg_titlebar;
        }

        public boolean needBadge() {
            return false;
        }

        public int getBadgeNum() {
            return 0;
        }

        public void onLeftTitleButtonClick() {

        }

        public void onRigthTitleButtonClick() {

        }

        public void onTitleClick() {

        }

        public void onTop(boolean anim) {

        }

        public void onNewIntent(Intent intent) {

        }

        /**
         * Activity重新打开，onCreate或onNewIntent
         * @param tabArgs
         */
        public void onEnter(@Nullable Bundle tabArgs) {

        }

        public int getLeftBadge() {
            return 0;
        }

        public int getRightBadge() {
            return 0;
        }

        /**
         * Tab是否被选中
         * @param selected true：选中，false：没选中
         */
        public void setSelected(boolean selected) {
            mSelected = selected;
        }

        public boolean isSelected() {
            return mSelected;
        }

    }

    /**
     * 需要显示的tab
     */
    private ArrayList<Tab> tabs;
    /**
     * 需要显示的tab对应的button
     */
    private ArrayList<TabButton> tabButtons;

    /**
     * 当前显示的tab的index
     */
    private int currentTabIndex = 0;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int tabIndex = 0;
        if (savedInstanceState == null) {
//            tabIndex = getIntent().getIntExtra(EXTRA_SHOW_TAB,
//                    MainActivity.HomeTab.DEFAULT_HOME_TAB.ordinal());
        } else {
//            tabIndex = savedInstanceState.getInt(SAVED_CURRENT_TAB,
//                    MainActivity.HomeTab.DEFAULT_HOME_TAB.ordinal());
        }

        setContentView(R.layout.activity_base_tab);

        tabs = new ArrayList<>();
        tabButtons = new ArrayList<>();
        Tab[] tempTabs = generateTabs();
        addTab(tempTabs);
        currentTabIndex = tabIndex;
        updateTab();

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (savedInstanceState == null) {
            Bundle tabArgs = getIntent().getBundleExtra(EXTRA_SHOW_TAB_ARGUMENTS);
            tabs.get(currentTabIndex).onEnter(tabArgs);
        }
    }

    /**
     * 添加一系列新tab
     *
     * @param tempTabs
     */
    protected void addTab(Tab... tempTabs) {
        for (Tab tab : tempTabs) {
            if (tab == null) {
                continue;
            }
            tabs.add(tab);
        }

        initTabs(true);
    }

    protected void addTabAt(int index, Tab tab) {
        if (tab == null) {
            return;
        }
        tabs.add(index, tab);
        initTabs(true);
        updateTab();
    }

    /**
     * 删除一个tab
     *
     * @param tab
     */
    protected void removeTab(Tab tab) {
        tabs.remove(tab);
        initTabs(true);
        if (tabs.size() <= currentTabIndex) {
            currentTabIndex = 0;
            updateTab();
        }
    }

    /**
     * 删除一个tab
     *
     * @param index
     */
    protected void removeTab(int index) {
        if (index < 0 || index >= tabs.size()) {
            return;
        }
        tabs.remove(index);
        initTabs(true);
        if (tabs.size() <= currentTabIndex) {
            currentTabIndex = 0;
            updateTab();
        }
    }

    protected int getTabCount() {
        return tabs.size();
    }

    protected Tab getTabAt(int index) {
        if (index < 0 || index >= tabs.size()) {
            return null;
        }
        return tabs.get(index);
    }

    protected Tab getTabByClass(Class<Tab> clazz) {

        for (Tab tab : tabs) {
            if (clazz.isInstance(tab)) {
                return tab;
            }
        }
        return null;
    }

    protected int getTabInndex(Tab tab) {
        return tabs.indexOf(tab);
    }

    public int getCurrentTabIndex() {
        return currentTabIndex;
    }

    /**
     * 子类需要实现，返回初始需要显示的tab 当需要动态添加tab时，调用
     * @return
     */
    protected abstract Tab[] generateTabs();

    private void initTabs(boolean restore) {
        tabButtons.clear();
        LinearLayoutCompat tabButtonContent = (LinearLayoutCompat) findViewById(R.id.tabButtonContent);
        tabButtonContent.removeAllViews();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        for (int i = 0; i < tabs.size(); i++) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(String.valueOf(i));
            if (restore) {
                if (fragment != null) {
                    tabs.remove(i);
                    tabs.add(i, (Tab) fragment);
                    transaction.hide(fragment);
                } else {
                    transaction.add(R.id.content, tabs.get(i), String.valueOf(i));
                }
            } else {
                if (fragment != null) {
                    transaction.remove(fragment);
                }
                transaction.add(R.id.content, tabs.get(i), String.valueOf(i));
            }

            TabButton tabButton = new TabButton(this);
            tabButton.setNeedBadge(tabs.get(i).needBadge());
            tabButton.setTag(i);
            if (i == currentTabIndex) {
                int id = tabs.get(i).getTabButtonOnSrc();
                if (id != 0) {
                    tabButton.setIcon(id);
                }
                id = tabs.get(i).getTabButtonOnBg();
                if (id != 0) {
                    tabButton.setBackgroundResource(id);
                }
                tabButton.setTextColor(getResources().getColor(R.color.tabbar_text_selected_color));
            } else {
                int id = tabs.get(i).getTabButtonOffSrc();
                if (id != 0) {
                    tabButton.setIcon(id);
                }
                id = tabs.get(i).getTabButtonOffBg();
                if (id != 0) {
                    tabButton.setBackgroundResource(id);
                }
                tabButton.setTextColor(getResources().getColor(R.color.tabbar_text_normal_color));
            }
            tabButton.setTitle(tabs.get(i).getButtonTitle(this));
            LinearLayoutCompat.LayoutParams lp = new LinearLayoutCompat.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1);
            tabButtonContent.addView(tabButton, lp);
            tabButtons.add(tabButton);
            tabButtons.get(i).setOnClickListener(this);
        }

        transaction.commitAllowingStateLoss();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_CURRENT_TAB, currentTabIndex);
    }

    @Override
    protected void onLeftTitleButtonClick() {
        tabs.get(currentTabIndex).onLeftTitleButtonClick();
    }

    @Override
    protected void onRightTitleButtonClick() {
        tabs.get(currentTabIndex).onRigthTitleButtonClick();
    }

    @Override
    protected void onTitleClick() {
        tabs.get(currentTabIndex).onTitleClick();
    }
/*

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        tabs.get(currentTabIndex).onActivityResult(requestCode, resultCode,
                data);
    }
*/

    @Override
    protected void onNewIntent(Intent intent) {
        int tabIndex = intent.getIntExtra(EXTRA_SHOW_TAB, 0);
        Bundle tabArgs = intent.getBundleExtra(EXTRA_SHOW_TAB_ARGUMENTS);
        boolean toTop = intent.getBooleanExtra(EXTRA_TO_TOP, false);

        if (tabIndex >= 0 && tabIndex < tabs.size()) {
            showTab(tabIndex);
            tabs.get(tabIndex).onNewIntent(intent);
            if (toTop) {
                tabs.get(tabIndex).onTop(false);
            }
        }

        tabs.get(tabIndex).onEnter(tabArgs);
    }

    public void updateTab() {
        for (int i = 0; i < tabs.size(); i++) {
            int id = tabs.get(i).getTabButtonOffSrc();
            if (id != 0) {
                tabButtons.get(i).setIcon(id);
            }
            tabButtons.get(i).setTitle(tabs.get(i).getButtonTitle(this));
            id = tabs.get(i).getTabButtonOffBg();
            if (id != 0) {
                tabButtons.get(i).setBackgroundResource(id);
            }
            tabButtons.get(i).setTextColor(getResources().getColor(R.color.tabbar_text_normal_color));
            tabButtons.get(i).setNeedBadge(tabs.get(i).needBadge());
        }
        Tab temp = tabs.get(currentTabIndex);

        int id = temp.getTabButtonOnSrc();
        if (id != 0) {
            tabButtons.get(currentTabIndex).setIcon(id);
        }

        tabButtons.get(currentTabIndex).setTextColor(getResources().getColor(R.color.tabbar_text_selected_color));
        tabButtons.get(currentTabIndex).setTitle(temp.getButtonTitle(this));
        id = tabs.get(currentTabIndex).getTabButtonOnBg();
        if (id != 0) {
            tabButtons.get(currentTabIndex).setBackgroundResource(id);
        }

        String title = temp.getTitle(this);
        View view = temp.getTitleView(this);
        if (view != null) {
            setTitleContent(view);
        } else if (title != null) {
            setTitle(title);
        }
        int titleBackground = temp.getTitleViewBackground();
        getTitleView().setBackgroundResource(titleBackground);
        setRightTitleImageButton(temp.getRightTitleButtonSrc(),
                temp.getRightTitleButtonBackground());
        getTitleView().setRightTitleTextButton(temp.getRightTitleButtonText(getApplicationContext()),
                temp.getRightTitleButtonBackground());
        setLeftTitleImageButton(temp.getLeftTitleButtonSrc(),
                temp.getLeftTitleButtonBackground());


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for (int i= 0; i < tabs.size(); i++) {
            Tab tab = (Tab) getSupportFragmentManager().findFragmentByTag(String.valueOf(i));
            if (tab == null) {
                tab = tabs.get(i);
            }
            if (i == currentTabIndex) {
                transaction.show(tab);
                tab.setSelected(true);
            } else {
                transaction.hide(tab);
                tab.setSelected(false);
            }
        }
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onClick(View v) {
        int tabIndex = (Integer) v.getTag();

        boolean isStartingShowLog = false;
        if (tabIndex == 0 || tabIndex == 1) {

        }

        if (!isStartingShowLog) {
            if (currentTabIndex == tabIndex) {
                tabs.get(currentTabIndex).onTop(true);
            }
        }

        if (currentTabIndex != tabIndex) {
            currentTabIndex = tabIndex;
            updateTab();
        }

    }

    public void showTab(int tabIndex) {
        if (currentTabIndex != tabIndex) {
            currentTabIndex = tabIndex;
            updateTab();
        }
    }

}