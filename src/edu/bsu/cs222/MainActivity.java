package edu.bsu.cs222;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import edu.bsu.cs222.ui.AnimTabsView;
import edu.bsu.cs222.ui.AnimTabsView.IAnimTabsItemViewChangeListener;
import edu.bsu.cs222.ui.ViewPagerCompat;

public class MainActivity extends FragmentActivity {

	private AnimTabsView tabsView;
	private ViewPagerCompat viewPager;
	private EditText etInput;
	private Button btnAdd;
	private LinearLayout llInput;
	
	private ArrayList<BaseFragment> fragmentList;
	private int currentIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE );
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		InitTabView();
		InitViewPager();

		etInput=(EditText) findViewById(R.id.et_input);
		etInput.setOnEditorActionListener(editorActionListener);
		btnAdd=(Button) findViewById(R.id.btn_add);
		llInput=(LinearLayout) findViewById(R.id.ll_input);
		enterInput(false);

	}

	private void InitTabView() {
		tabsView = (AnimTabsView) findViewById(R.id.publiclisten_tab);
		tabsView.addItem("Book");
		tabsView.addItem("Word");
		tabsView.addItem("Share");

		tabsView.setOnAnimTabsItemViewChangeListener(viewChangeListener);
	}

	private void InitViewPager() {
		viewPager = (ViewPagerCompat) findViewById(R.id.viewpager);
		fragmentList = new ArrayList<BaseFragment>();
		BaseFragment bookFragment = new BookFragment();
		BaseFragment wordFragment = new WordFragment();
		BaseFragment shareFragment = new ShareFragment();
		fragmentList.add(bookFragment);
		fragmentList.add(wordFragment);
		fragmentList.add(shareFragment);

		viewPager.setAdapter(fragmentPagerAdapter);
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(pageChangeListener);
		viewPager.setPageMargin(getResources().getDimensionPixelSize(
				R.dimen.page_margin_width));
		viewPager.setPageMarginDrawable(R.color.page_viewer_margin_color);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		case R.id.action_search:
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void enterInput(boolean input){
		InputMethodManager inputmanger = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		if(input){
			btnAdd.setVisibility(View.GONE);
			llInput.setVisibility(View.VISIBLE);
			etInput.setText("");
			etInput.requestFocus();
			inputmanger.showSoftInput(etInput, 0);
		}else{
			btnAdd.setVisibility(View.VISIBLE);
			llInput.setVisibility(View.GONE);
			inputmanger.hideSoftInputFromWindow(etInput.getWindowToken(), 0);
		}
	}
	
	public void add_click(View view){
		enterInput(true);
	}
	
	public void cancel_click(View view){
		enterInput(false);
	}
	
	private OnEditorActionListener editorActionListener=new OnEditorActionListener(){

		@Override
		public boolean onEditorAction(TextView v, int actionId,
				KeyEvent event) {
			if(actionId==EditorInfo.IME_ACTION_DONE){
				BaseFragment fragment=fragmentList.get(currentIndex);
				String name=etInput.getText().toString().trim();
				if(!TextUtils.isEmpty(name)){
					if(fragment.add(name)){
						etInput.setText("");
					}
				}
				return true;
			}
			return false;
		}
		
	};
	
	private FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(
			getSupportFragmentManager()) {

		@Override
		public Fragment getItem(int position) {
			return fragmentList.get(position);
		}

		@Override
		public int getCount() {
			return fragmentList.size();
		}

	};

	private IAnimTabsItemViewChangeListener viewChangeListener = new IAnimTabsItemViewChangeListener() {
		@Override
		public void onChange(AnimTabsView tabsView, int oldPosition,
				int currentPosition) {
			if (oldPosition != currentPosition) {
				viewPager.setCurrentItem(oldPosition);
			}
		}
	};

	private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

		@Override
		public void onPageScrollStateChanged(int index) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int index) {
			if (currentIndex != index) {
				currentIndex = index;
				BaseFragment fragment=fragmentList.get(index);
				tabsView.selecteItem(currentIndex);
				if(fragment.hasAdd()){
					btnAdd.setVisibility(View.VISIBLE);
				}else{
					btnAdd.setVisibility(View.GONE);
				}
				enterInput(false);
				fragment.active();
			}
		}

	};
}
