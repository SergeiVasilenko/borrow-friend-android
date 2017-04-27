package vision.genesis.ui.order;

import android.view.View;

public class EmptyFriendsViewHolder extends DealViewHolder {
	public EmptyFriendsViewHolder(View itemView) {
		super(itemView);
	}

	@Override
	public int getType() {
		return ViewType.EMPTY;
	}
}
