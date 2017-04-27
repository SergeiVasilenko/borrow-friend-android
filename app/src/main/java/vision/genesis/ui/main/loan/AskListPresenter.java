package vision.genesis.ui.main.loan;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

import javax.inject.Inject;

import vision.genesis.app.App;
import vision.genesis.model.Ask;
import vision.genesis.model.ItemType;
import vision.genesis.network.Api;
import vision.genesis.network.request.GetAsksRequest;
import vision.genesis.network.request.util.DefaultErrorHandler;
import vision.genesis.storage.Storage;

public class AskListPresenter extends ListPresenter {

	@Inject
	Api          mApi;
	@Inject
	Storage      mStorage;
	@Inject
	ObjectMapper mObjectMapper;

	public AskListPresenter() {
		App.get().getUiComponent().inject(this);
	}

	@Override
	void updateView() {
		GetAsksRequest.create()
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
		mView.showAddItem(ItemType.OFFER);
	}

	private void handleResult(ArrayList<Ask> result) {
		mView.setItems(result);
	}
}
