package vision.genesis.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import javax.inject.Inject;

import vision.genesis.R;
import vision.genesis.app.App;
import vision.genesis.storage.Storage;
import vision.genesis.ui.auth.AuthActivity;
import vision.genesis.ui.common.BaseActivity;
import vision.genesis.ui.main.dashboard.DashboardPresenter;
import vision.genesis.ui.main.loan.AskListPresenter;
import vision.genesis.ui.main.loan.OfferListPresenter;

public class MainActivity extends BaseActivity {

	@Inject
	Storage mStorage;

	private MainFragmentNavigation mMainFragmentNavigation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (!App.get().isLoggedIn()) {
			AuthActivity.show(this);
			return;
		}

		setContentView(R.layout.activity_main);

		mMainFragmentNavigation = new MainFragmentNavigation(getSupportFragmentManager(),
															 new OfferListPresenter(),
															 new DashboardPresenter(),
															 new AskListPresenter());

		BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
		navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
		navigation.setSelectedItemId(R.id.navigation_dashboard);
	}


	private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
			= new BottomNavigationView.OnNavigationItemSelectedListener() {

		@Override
		public boolean onNavigationItemSelected(@NonNull MenuItem item) {
			switch (item.getItemId()) {
				case R.id.navigation_get:
					mMainFragmentNavigation.showOffers();
					return true;
				case R.id.navigation_dashboard:
					mMainFragmentNavigation.showDashboard();
					return true;
				case R.id.navigation_give:
					mMainFragmentNavigation.showAsks();
					return true;
			}
			return false;
		}
	};


	public static void show(Context context) {
		Intent intent = new Intent(context, MainActivity.class);
		context.startActivity(intent);
	}
}
