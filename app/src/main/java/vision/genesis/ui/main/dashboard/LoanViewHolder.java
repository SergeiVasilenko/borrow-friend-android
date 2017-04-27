package vision.genesis.ui.main.dashboard;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import vision.genesis.R;
import vision.genesis.model.FriendGraph;
import vision.genesis.model.ItemType;
import vision.genesis.model.Loan;
import vision.genesis.view.drawable.CircleTransformation;


public class LoanViewHolder extends DashboardViewHolder {

	private FriendGraph mGraph;

	@BindView(R.id.avatar)
	ImageView mAvatarView;
	@BindView(R.id.name)
	TextView mNameView;
	@BindView(R.id.duration)
	TextView mDurationView;
	@BindView(R.id.ownRelationship)
	TextView mOwnRelationship;
	@BindView(R.id.amount)
	TextView mAmountView;

	private Loan mItem;

	public LoanViewHolder(View itemView) {
		super(itemView);

		ButterKnife.bind(this, itemView);

		itemView.setOnClickListener(v -> clickOnLoan());
	}

	@Override
	public int getType() {
		return ViewType.ITEM;
	}

	public void setItem(Loan item) {
		mItem = item;
		updateView();
	}

	private void updateView() {
		setAvatar(mItem.getPhoto(), mAvatarView);
		mNameView.setText(mItem.getName());
		Resources res = getContext().getResources();
		Resources.Theme theme = getContext().getTheme();
		int textColor;
		if (mItem.getItemType() == ItemType.OFFER) {
			mOwnRelationship.setText(R.string.owns_to_you);
			textColor = ResourcesCompat.getColor(res, R.color.green, theme);
		} else {
			mOwnRelationship.setText(R.string.you_own);
			textColor = ResourcesCompat.getColor(res, R.color.red, theme);
		}
		mOwnRelationship.setTextColor(textColor);
		mAmountView.setTextColor(textColor);
		String amount = res.getString(R.string.rub, mItem.getAmount());
		mAmountView.setText(amount);

		mDurationView.setText(mItem.getPaymentDeadline().substring(0, 10));
	}

	private Context getContext() {
		return itemView.getContext();
	}

	private void setAvatar(String uri, ImageView imageView) {
		Glide.with(getContext())
			 .load(uri)
			 .fitCenter()
			 .centerCrop()
			 .transform(new CircleTransformation(getContext()))
			 .into(imageView);
	}

	private void clickOnLoan() {
		// TODO return loan
	}
}
