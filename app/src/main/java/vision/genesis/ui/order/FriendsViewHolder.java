package vision.genesis.ui.order;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import vision.genesis.R;
import vision.genesis.model.User;
import vision.genesis.view.drawable.CircleTransformation;


public class FriendsViewHolder extends DealViewHolder {

	private List<User> mGraph;

	@BindView(R.id.avatar)
	ImageView mMainAvatarView;
	@BindView(R.id.avatar1)
	ImageView mAvatarView1;
	@BindView(R.id.avatar2)
	ImageView mAvatarView2;
	@BindView(R.id.avatar3)
	ImageView mAvatarView3;

	@BindView(R.id.avatarLayout0)
	FrameLayout mAvatarLayout0;
	@BindView(R.id.avatarLayout1)
	FrameLayout mAvatarLayout1;
	@BindView(R.id.avatarLayout2)
	FrameLayout mAvatarLayout2;
	@BindView(R.id.avatarLayout3)
	FrameLayout mAvatarLayout3;

	@BindView(R.id.name1)
	TextView mNameView1;
	@BindView(R.id.name2)
	TextView mNameView2;

	public FriendsViewHolder(View itemView) {
		super(itemView);

		ButterKnife.bind(this, itemView);

		mMainAvatarView.setOnClickListener(v -> clickOnFriend(0));
		mAvatarView1.setOnClickListener(v -> clickOnFriend(1));
		mAvatarView2.setOnClickListener(v -> clickOnFriend(2));
		mAvatarView3.setOnClickListener(v -> clickOnFriend(3));
	}

	@Override
	public int getType() {
		return ViewType.ITEM;
	}

	public void setFriendGraph(List<User> graph) {
		mGraph = graph;
		updateView();
	}

	private void updateView() {
		List<User> users = mGraph;
		setAvatar(users.get(0).getPhoto(), mMainAvatarView);
		mNameView1.setText(users.get(0).getUserName());
		if (users.size() > 1) {
			mAvatarLayout1.setVisibility(View.VISIBLE);
			mAvatarView1.setVisibility(View.VISIBLE);
			setAvatar(users.get(1).getPhoto(), mAvatarView1);
		} else {
			mAvatarLayout1.setVisibility(View.GONE);
			mAvatarView1.setVisibility(View.GONE);
		}
		if (users.size() > 3) {
			mAvatarLayout2.setVisibility(View.VISIBLE);
			mAvatarView2.setVisibility(View.VISIBLE);
			setAvatar(users.get(2).getPhoto(), mAvatarView2);
		} else {
			mAvatarLayout2.setVisibility(View.GONE);
			mAvatarView2.setVisibility(View.GONE);
		}
		User secondUser = users.get(users.size() > 3 ? 3 : 2);
		setAvatar(secondUser.getPhoto(), mAvatarView3);
		mNameView2.setText(secondUser.getUserName());

		mAvatarView3.setOnClickListener(v -> clickOnFriend(users.size() > 3 ? 3 : 2));
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

	private void clickOnFriend(int position) {
		User user = mGraph.get(position);
		String url = "https://vk.com/id" + user.getProviderUserId();
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		getContext().startActivity(i);
	}
}
