package vision.genesis.ui.main.dashboard;

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
import vision.genesis.model.Loan;
import vision.genesis.model.error.Error;

public class DashboardFragment extends Fragment implements DashboardView {

	@BindView(R.id.progress)
	View         mProgressView;
	@BindView(R.id.recycler)
	RecyclerView mRecyclerView;
	@BindView(R.id.add)
	FloatingActionButton mAddButton;

	private boolean mIsViewCreated;

	private DashboardPresenter mPresenter;

	private DashboardAdapter mDashboardAdapter;

	public void setPresenter(DashboardPresenter presenter) {
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

		mDashboardAdapter = new DashboardAdapter();

		mAddButton.setVisibility(View.GONE);

		Context context = getContext();
		mRecyclerView.setHasFixedSize(true);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
		mRecyclerView.setAdapter(mDashboardAdapter);
		mRecyclerView.addItemDecoration(new ListItemDecorator(context));

		mProgressView.setVisibility(View.GONE);

		mIsViewCreated = true;

		setupPresenter();
	}

	private void setupPresenter() {
		if (!mIsViewCreated || mPresenter == null) {
			return;
		}

		mDashboardAdapter.setDashboardPresenter(mPresenter);

		mPresenter.setView(this);
	}

	@Override
	public void setItems(List<? extends Loan> items) {
		mDashboardAdapter.setItems(items);
	}

	@Override
	public void showError(Error error) {
		Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
	}
}
