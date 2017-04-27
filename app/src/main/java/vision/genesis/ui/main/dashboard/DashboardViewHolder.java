package vision.genesis.ui.main.dashboard;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class DashboardViewHolder extends RecyclerView.ViewHolder {

	public DashboardViewHolder(View itemView) {
		super(itemView);
	}

	public abstract int getType();
}
