package vision.genesis.ui.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import vision.genesis.R;
import vision.genesis.app.App;
import vision.genesis.model.Ask;
import vision.genesis.model.Item;
import vision.genesis.model.ItemType;
import vision.genesis.model.Offer;
import vision.genesis.model.User;
import vision.genesis.model.UsersIntersects;
import vision.genesis.network.Api;
import vision.genesis.network.request.GetUsersIntersectRequest;
import vision.genesis.network.request.util.DefaultErrorHandler;
import vision.genesis.storage.Storage;
import vision.genesis.ui.common.BaseActivity;


public class DealActivity extends BaseActivity {

	private static final String EXTRA_TYPE           = "EXTRA_TYPE";
	private static final String EXTRA_ITEM           = "EXTRA_ITEM";
	private static final String EXTRA_SECOND_USER_ID = "EXTRA_SECOND_USER_ID";

	@BindView(R.id.recycler)
	RecyclerView mRecyclerView;
	@BindView(R.id.progress)
	View         mProgressView;

	@Inject
	Storage      mStorage;
	@Inject
	Api          mApi;
	@Inject
	ObjectMapper mObjectMapper;

	private Item mItem;

	private long mSecondUserId;

	private DealAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deal);

		ButterKnife.bind(this);

		mProgressView.setVisibility(View.GONE);

		App.get().getUiComponent().inject(this);

		onNewIntent(getIntent());
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		ItemType itemType = (ItemType) intent.getSerializableExtra(EXTRA_TYPE);
		if (itemType == ItemType.OFFER) {
			Offer offer = intent.getParcelableExtra(EXTRA_ITEM);
			mItem = offer;
		} else {
			Ask ask = intent.getParcelableExtra(EXTRA_ITEM);
			mItem = ask;
		}

		mSecondUserId = intent.getLongExtra(EXTRA_SECOND_USER_ID, -1);
		if (mSecondUserId == -1) {
			throw new IllegalArgumentException("You should set second user id");
		}

		setupActionBar();
		setupRecyclerView();

		getUsersIntersects();
	}

	private void setupActionBar() {
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		ActionBar ab = getSupportActionBar();
		if (ab == null) {
			return;
		}

		ab.setDisplayHomeAsUpEnabled(true);
		ab.setHomeButtonEnabled(true);
		ab.setDisplayShowTitleEnabled(false);
	}

	private void setupRecyclerView() {
		mAdapter = new DealAdapter(mItem);

		mRecyclerView.setHasFixedSize(true);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
		mRecyclerView.setAdapter(mAdapter);
		mRecyclerView.addItemDecoration(new ListItemDecorator(this));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home: {
				onBackPressed();
				return true;
			}
			default: {
				return super.onOptionsItemSelected(item);
			}
		}
	}

	private void getUsersIntersects() {

		GetUsersIntersectRequest.create()
								.firstId(mStorage.getCurrentUser().getVkId())
								.secondId(mSecondUserId)
								.api(mApi)
								.errorHandler(new DefaultErrorHandler(mObjectMapper))
								.onProgressStart(this::showProgress)
								.onProgressEnd(this::hideProgress)
								.onError(this::showError)
								.onResult(this::onUsersIntersectsGot)
								.build()
								.send();
	}

	private void onUsersIntersectsGot(UsersIntersects usersIntersects) {
		List<List<User>> graph = new ArrayList<>();
		for (User user : usersIntersects.getFriendsDirect()) {
			graph.add(Arrays.asList(usersIntersects.getFirst(), user, usersIntersects.getSecond()));
		}
		for (List<User> list : usersIntersects.getFriendsIndirect()) {
			ArrayList<User> arrayList = new ArrayList<>();
			arrayList.add(usersIntersects.getFirst());
			arrayList.addAll(list);
			arrayList.add(usersIntersects.getSecond());
			graph.add(arrayList);
		}
		mAdapter.setFriends(graph);
	}

	private void showProgress() {
 		mProgressView.setVisibility(View.VISIBLE);
	}

	private void hideProgress() {
		mProgressView.setVisibility(View.GONE);
	}


	public static void show(Context context, Item item, long secondUserId) {
		Intent intent = new Intent(context, DealActivity.class);
		intent.putExtra(EXTRA_TYPE, item.getType());
		intent.putExtra(EXTRA_ITEM, item);
		intent.putExtra(EXTRA_SECOND_USER_ID, secondUserId);

		context.startActivity(intent);
	}
}
