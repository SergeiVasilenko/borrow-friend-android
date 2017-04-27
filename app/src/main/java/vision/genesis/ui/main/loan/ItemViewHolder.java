package vision.genesis.ui.main.loan;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import vision.genesis.R;
import vision.genesis.model.Item;
import vision.genesis.model.ItemType;
import vision.genesis.ui.order.DealActivity;
import vision.genesis.view.drawable.CircleTransformation;

public class ItemViewHolder extends RecyclerView.ViewHolder implements ItemView {

	@BindView(R.id.avatar)
	ImageView mAvatarView;
	@BindView(R.id.name)
	TextView  mNameView;
	@BindView(R.id.percentPerDay)
	TextView  mPercentPerDayView;
	@BindView(R.id.amount)
	TextView  mAmountView;
	@BindView(R.id.rating)
	TextView  mRatingView;
	@BindView(R.id.ratingLayout)
	View      mRatingLayout;

	private ItemPresenter mPresenter;

	public ItemViewHolder(View itemView, ItemPresenter presenter) {
		super(itemView);
		ButterKnife.bind(this, itemView);
		mPresenter = presenter;
		itemView.setOnClickListener(v -> mPresenter.openItem());
	}

	@Override
	public void setItem(Item item) {
		Resources res = getContext().getResources();
		setAvatar(item.getAvatar());
		mNameView.setText(item.getName());
		mAmountView.setText(res.getString(R.string.rub, item.getAmount()));
		mPercentPerDayView.setText(String.format(Locale.GERMAN, "%.1f%%", item.getPercentPerDay()));
		mRatingView.setText(String.format(Locale.GERMAN, "%.2f", item.getRating() / 100f));
		mRatingLayout.setVisibility(item.getType() == ItemType.OFFER ? View.GONE : View.VISIBLE);
		ViewGroup.LayoutParams params = itemView.getLayoutParams();
		if (item.getType() == ItemType.OFFER) {

		}
		int ratingColor;
		if (item.getRating() < 350) {
			ratingColor = res.getColor(R.color.red);
		} else {
			ratingColor = res.getColor(R.color.green);
		}
		mRatingView.setTextColor(ratingColor);
	}

	@Override
	public void showDeal(Item item) {
		DealActivity.show(getContext(), item, item.getVkId());
	}

	ItemPresenter getPresenter() {
		return mPresenter;
	}

	Context getContext() {
		return itemView.getContext();
	}

	private void setAvatar(String avatar) {
		Glide.with(getContext())
			 .load(avatar)
			 .fitCenter()
			 .centerCrop()
			 .transform(new CircleTransformation(getContext()))
			 .into(mAvatarView);
	}
}
