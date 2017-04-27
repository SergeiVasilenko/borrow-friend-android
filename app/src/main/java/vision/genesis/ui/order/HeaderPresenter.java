package vision.genesis.ui.order;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Inject;

import vision.genesis.app.App;
import vision.genesis.model.Item;
import vision.genesis.network.Api;
import vision.genesis.network.request.PostDealRequest;
import vision.genesis.network.request.util.DefaultErrorHandler;
import vision.genesis.storage.Storage;

public class HeaderPresenter {
	private HeaderView mHeaderView;

	private Item mItem;

	@Inject
	Api          mApi;
	@Inject
	Storage      mStorage;
	@Inject
	ObjectMapper mObjectMapper;

	public HeaderPresenter() {
		App.get().getUiComponent().inject(this);
	}

	public void setView(HeaderView view) {
		mHeaderView = view;
	}

	public void setItem(Item item) {
		mItem = item;
		updateView();
	}

	public Item getItem() {
		return mItem;
	}

	private void updateView() {
		mHeaderView.setItem(mItem);
	}


	public void onActionClick() {
		PostDealRequest.create()
					   .userId(mStorage.getCurrentUser().getVkId())
					   .orderId(mItem.getId())
					   .api(mApi)
					   .errorHandler(new DefaultErrorHandler(mObjectMapper))
					   .onError(error -> mHeaderView.showError(error))
					   .onResult(result -> mHeaderView.close())
					   .build()
					   .send();
	}

	public void openProfile() {
		mHeaderView.showSocialProfile(mItem.getVkId());

	}
}
