package vision.genesis.ui.main.loan;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

import javax.inject.Inject;

import vision.genesis.app.App;
import vision.genesis.model.ItemType;
import vision.genesis.model.Offer;
import vision.genesis.network.Api;
import vision.genesis.network.request.GetOffersRequest;
import vision.genesis.network.request.util.DefaultErrorHandler;
import vision.genesis.storage.Storage;

public class OfferListPresenter extends ListPresenter {

	@Inject
	Api          mApi;
	@Inject
	Storage      mStorage;
	@Inject
	ObjectMapper mObjectMapper;

	public OfferListPresenter() {
		App.get().getUiComponent().inject(this);
	}

	@Override
	void updateView() {
		GetOffersRequest.create()
						.userId(mStorage.getCurrentUser().getVkId())
						.api(mApi)
						.errorHandler(new DefaultErrorHandler(mObjectMapper))
						.onProgressStart(() -> mView.showProgress())
						.onProgressEnd(() -> mView.hideProgress())
						.onError(error -> mView.showError(error))
						.onResult(result -> handleResult(result))
						.build()
						.send();
	}

	@Override
	void addItem() {
		mView.showAddItem(ItemType.ASK);
	}

	private void handleResult(ArrayList<Offer> result) {
		mView.setItems(result);
	}
}
