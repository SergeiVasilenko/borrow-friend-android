package vision.genesis.ui.order;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class DealViewHolder extends RecyclerView.ViewHolder {

	public DealViewHolder(View itemView) {
		super(itemView);
	}

	public abstract int getType();
}
