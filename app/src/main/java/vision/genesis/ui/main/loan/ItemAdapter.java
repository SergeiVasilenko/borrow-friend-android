package vision.genesis.ui.main.loan;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import vision.genesis.R;
import vision.genesis.model.Item;


class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {

	private List<? extends Item>     mItems;

	public ItemAdapter() {
		mItems = Collections.emptyList();
	}

	public void setItems(List<? extends Item> items) {
		mItems = items;
		notifyDataSetChanged();
	}

	public List<? extends Item> getItems() {
		return mItems;
	}

	@Override
	public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View itemView = inflater.inflate(R.layout.item, parent, false);
		ItemPresenter presenter = new ItemPresenter();
		ItemViewHolder viewHolder = new ItemViewHolder(itemView, presenter);
		presenter.setView(viewHolder);
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(ItemViewHolder holder, int position) {
		holder.getPresenter().setItem(mItems.get(position));
	}

	@Override
	public int getItemCount() {
		return mItems.size();
	}
}
