package vision.genesis.ui.order;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import vision.genesis.R;
import vision.genesis.model.Item;
import vision.genesis.model.User;


class DealAdapter extends RecyclerView.Adapter<DealViewHolder> {

	private List<List<User>> mFriends = Collections.emptyList();

	private Item mItem;

	private boolean mIsEmpty;

	public DealAdapter(Item item) {
		mFriends = Collections.emptyList();
		mItem = item;
	}

	public void setFriends(List<List<User>> friends) {
		mFriends = friends;
		mIsEmpty = friends.isEmpty();
		notifyDataSetChanged();
	}

	@Override
	public DealViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		switch (viewType) {
			case ViewType.HEADER: {
				View itemView = inflater.inflate(R.layout.deal_header, parent, false);
				HeaderPresenter presenter = new HeaderPresenter();
				HeaderViewHolder viewHolder = new HeaderViewHolder(itemView, presenter);
				presenter.setView(viewHolder);
				presenter.setItem(mItem);
				return viewHolder;
			}
			case ViewType.ITEM: {
				View itemView = inflater.inflate(R.layout.near_friend, parent, false);
				return new FriendsViewHolder(itemView);
			}
			case ViewType.EMPTY: {
				View itemView = inflater.inflate(R.layout.empty_friends, parent, false);
				return new EmptyFriendsViewHolder(itemView);
			}
			default:
				throw new UnsupportedOperationException("Unknown view type: " + viewType);
		}

	}

	@Override
	public void onBindViewHolder(DealViewHolder holder, int position) {
		if (holder.getType() == ViewType.ITEM) {
			FriendsViewHolder viewHolder = (FriendsViewHolder) holder;
			viewHolder.setFriendGraph(getFriendGraph(position));
		}
	}

	@Override
	public int getItemViewType(int position) {

		if (position == 0) {
			return ViewType.HEADER;
		} else {
			if (mIsEmpty) {
				return ViewType.EMPTY;
			}
			return ViewType.ITEM;
		}
	}

	public List<User> getFriendGraph(int position) {
		return mFriends.get(position - 1);
	}

	@Override
	public int getItemCount() {
		return mFriends.size() + 1 + (mIsEmpty ? 1 : 0);
	}
}
