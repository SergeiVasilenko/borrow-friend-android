package vision.genesis.ui.main.dashboard;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import vision.genesis.R;
import vision.genesis.model.Loan;


class DashboardAdapter extends RecyclerView.Adapter<DashboardViewHolder> {

	private List<? extends Loan> mItems = Collections.emptyList();

	private DashboardPresenter mDashboardPresenter;

	public DashboardAdapter() {
		mItems = Collections.emptyList();
	}

	public void setItems(List<? extends Loan> items) {
		mItems = items;
		notifyDataSetChanged();
	}

	public void setDashboardPresenter(DashboardPresenter presenter) {
		mDashboardPresenter = presenter;
		notifyDataSetChanged();
	}

	@Override
	public DashboardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		switch (viewType) {
			case ViewType.HEADER: {
				View itemView = inflater.inflate(R.layout.dashboard_header, parent, false);
				HeaderHeaderViewHolder viewHolder = new HeaderHeaderViewHolder(itemView);
				return viewHolder;
			}
			case ViewType.ITEM: {
				View itemView = inflater.inflate(R.layout.loan_item, parent, false);
				return new LoanViewHolder(itemView);
			}
			default:
				throw new UnsupportedOperationException("Unknown view type: " + viewType);
		}

	}

	@Override
	public void onBindViewHolder(DashboardViewHolder holder, int position) {
		if (holder.getType() == ViewType.ITEM) {
			LoanViewHolder viewHolder = (LoanViewHolder) holder;
			viewHolder.setItem(getLoan(position));
		} else if (holder.getType() == ViewType.HEADER) {
			HeaderHeaderViewHolder headerViewHolder = (HeaderHeaderViewHolder) holder;
			headerViewHolder.setPresenter(mDashboardPresenter);
			mDashboardPresenter.setHeaderView(headerViewHolder);
		}
	}

	@Override
	public int getItemViewType(int position) {
		if (position == 0) {
			return ViewType.HEADER;
		} else {
			return ViewType.ITEM;
		}
	}

	public Loan getLoan(int position) {
		return mItems.get(position - 1);
	}

	@Override
	public int getItemCount() {
		return mItems.size() + 1;
	}
}
