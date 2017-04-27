package vision.genesis.ui.main.loan;

import vision.genesis.model.Item;

public class ItemPresenter {

	private ItemView mItemView;

	private Item mItem;

	public void setView(ItemView view) {
		mItemView = view;
	}

	public void setItem(Item item) {
		mItem = item;
		updateView();
	}

	public Item getItem() {
		return mItem;
	}

	private void updateView() {
		mItemView.setItem(mItem);
	}

	public void openItem() {
		mItemView.showDeal(mItem);
	}
}
