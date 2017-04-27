package vision.genesis.ui.main.loan;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import vision.genesis.R;
import vision.genesis.model.Item;
import vision.genesis.model.ItemType;
import vision.genesis.model.error.Error;
import vision.genesis.ui.insert.InsertActivity;


public class ListFragment extends Fragment implements LoanListView {
	@BindView(R.id.progress)
	View mProgressView;
	@BindView(R.id.recycler)
	RecyclerView mRecyclerView;
	@BindView(R.id.add)
	FloatingActionButton mAddButton;

	private ItemAdapter mAdapter;

	private ListPresenter mPresenter;

	private boolean mIsViewCreated;

	public void setPresenter(ListPresenter presenter) {
		mPresenter = presenter;
		setupPresenter();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

		ButterKnife.bind(this, view);

		mAdapter = new ItemAdapter();

		Context context = getContext();
		mRecyclerView.setHasFixedSize(true);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
		mRecyclerView.setAdapter(mAdapter);
		mRecyclerView.addItemDecoration(new ListItemDecorator(context));

		mProgressView.setVisibility(View.GONE);

		mAddButton.setOnClickListener(v -> mPresenter.addItem());

		mIsViewCreated = true;

		setupPresenter();
	}

	private void setupPresenter() {
		if (mPresenter == null || !mIsViewCreated) {
			return;
		}
		mPresenter.setView(this);
	}

	@Override
	public void showProgress() {
		mProgressView.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideProgress() {
		mProgressView.setVisibility(View.GONE);
	}

	@Override
	public void showError(Error error) {
		Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
	}

	@Override
	public void setItems(List<? extends Item> items) {
		mAdapter.setItems(items);
	}

	@Override
	public void showAddItem(ItemType type) {
		InsertActivity.show(getContext(), type);
	}
}
