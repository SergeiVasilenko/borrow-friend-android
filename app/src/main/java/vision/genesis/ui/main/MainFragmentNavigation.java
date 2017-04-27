package vision.genesis.ui.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import vision.genesis.R;
import vision.genesis.ui.main.dashboard.DashboardFragment;
import vision.genesis.ui.main.dashboard.DashboardPresenter;
import vision.genesis.ui.main.loan.AskListPresenter;
import vision.genesis.ui.main.loan.ListFragment;
import vision.genesis.ui.main.loan.OfferListPresenter;


public class MainFragmentNavigation {

	private FragmentManager mFragmentManager;

	private DashboardPresenter mDashboardPresenter;
	private OfferListPresenter mOfferListPresenter;
	private AskListPresenter mAskListPresenter;

	public MainFragmentNavigation(FragmentManager fragmentManager,
								  OfferListPresenter offerListPresenter,
								  DashboardPresenter dashboardPresenter,
								  AskListPresenter askListPresenter) {
		mFragmentManager = fragmentManager;
		mOfferListPresenter = offerListPresenter;
		mDashboardPresenter = dashboardPresenter;
		mAskListPresenter = askListPresenter;
	}

	public void showDashboard() {
		FragmentManager fm = mFragmentManager;
		Fragment fragment = fm.findFragmentById(R.id.frame);
		removeFragmentsBeside(mFragmentManager, fragment);
		if (fragment instanceof DashboardFragment) {
			((DashboardFragment) fragment).setPresenter(mDashboardPresenter);
			return;
		}
		DashboardFragment contentFragment = new DashboardFragment();
		contentFragment.setPresenter(mDashboardPresenter);
		setControlsFragment(fm, fragment, contentFragment);
	}

	public void showOffers() {
		FragmentManager fm = mFragmentManager;
		Fragment fragment = fm.findFragmentById(R.id.frame);
		removeFragmentsBeside(mFragmentManager, fragment);
		if (fragment instanceof ListFragment) {
			((ListFragment) fragment).setPresenter(mOfferListPresenter);
			return;
		}
		ListFragment contentFragment = new ListFragment();
		contentFragment.setPresenter(mOfferListPresenter);
		setControlsFragment(fm, fragment, contentFragment);
	}

	public void showAsks() {
		FragmentManager fm = mFragmentManager;
		Fragment fragment = fm.findFragmentById(R.id.frame);
		removeFragmentsBeside(mFragmentManager, fragment);
		if (fragment instanceof ListFragment) {
			((ListFragment) fragment).setPresenter(mAskListPresenter);
			return;
		}
		ListFragment contentFragment = new ListFragment();
		contentFragment.setPresenter(mAskListPresenter);
		setControlsFragment(fm, fragment, contentFragment);
	}

	private void setControlsFragment(FragmentManager fm, Fragment prevFragment, Fragment nextFragment) {
		FragmentTransaction transaction = fm.beginTransaction();
		if (prevFragment != null) {
			transaction.remove(prevFragment);
		}
		transaction.add(R.id.frame, nextFragment);
		transaction.commit();
	}

	private void removeFragmentsBeside(FragmentManager fm, Fragment exclude) {
		Fragment controlsFragment = fm.findFragmentById(R.id.frame);
		FragmentTransaction transaction = fm.beginTransaction();
		if (controlsFragment != null && controlsFragment != exclude) {
			transaction.remove(controlsFragment);
		}
		transaction.commit();
	}
}
