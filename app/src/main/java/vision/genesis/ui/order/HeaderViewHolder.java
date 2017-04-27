package vision.genesis.ui.order;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import vision.genesis.R;
import vision.genesis.model.Item;
import vision.genesis.model.ItemType;
import vision.genesis.model.error.Error;
import vision.genesis.util.ContextUtils;
import vision.genesis.view.drawable.CircleTransformation;

public class HeaderViewHolder extends DealViewHolder implements HeaderView {

	@BindView(R.id.action)
	Button    mActionButton;
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
	View mRatingLayout;
	@BindView(R.id.durationMax)
	TextView mDurationMaxView;
	@BindView(R.id.durationMin)
	TextView mDurationMinView;
	@BindView(R.id.askTitle)
	TextView mAskTitleView;

	private HeaderPresenter mPresenter;

	public HeaderViewHolder(View itemView, HeaderPresenter presenter) {
		super(itemView);
		ButterKnife.bind(this, itemView);

		mPresenter = presenter;

		mAvatarView.setOnClickListener(v -> mPresenter.openProfile());
		mNameView.setOnClickListener(v -> mPresenter.openProfile());
	}


	@Override
	public int getType() {
		return ViewType.HEADER;
	}

	@Override
	public void showSocialProfile(long id) {
		String url = "https://vk.com/id" + id;
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		getContext().startActivity(i);
	}

	@Override
	public void setItem(Item item) {
		Resources res = getContext().getResources();
		setAvatar(item.getAvatar());
		mNameView.setText(item.getName());
		mAmountView.setText(res.getString(R.string.rub, item.getAmount()));
		mPercentPerDayView.setText(String.format("%.1f%%", item.getPercentPerDay()));
		mDurationMaxView.setText(String.valueOf(item.getDurationMax()));
		mDurationMinView.setText(String.valueOf(item.getDurationMin()));
		if (item.getType() == ItemType.OFFER) {
			mActionButton.setVisibility(View.GONE);
		} else {
			mActionButton.setVisibility(View.VISIBLE);
		}
		mActionButton.setOnClickListener(v -> mPresenter.onActionClick());
		mActionButton.setText(item.getType() == ItemType.OFFER ? R.string.put_offer : R.string.put_ask);
		if (TextUtils.isEmpty(item.getTitle())) {
			mAskTitleView.setVisibility(View.GONE);
		} else {
			mAskTitleView.setVisibility(View.VISIBLE);
			mAskTitleView.setText(item.getTitle());
		}
		mRatingView.setText(String.format(Locale.GERMAN, "%.2f", item.getRating() / 100f));
		mRatingLayout.setVisibility(item.getType() == ItemType.OFFER ? View.GONE : View.VISIBLE);
		int ratingColor;
		if (item.getRating() < 350) {
			ratingColor = res.getColor(R.color.red);
		} else {
			ratingColor = res.getColor(R.color.green);
		}
		mRatingView.setTextColor(ratingColor);
	}

	@Override
	public void close() {
		ContextUtils.getBaseActivity(itemView.getContext()).finish();
	}

	@Override
	public void showError(Error error) {
		Toast.makeText(itemView.getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
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
