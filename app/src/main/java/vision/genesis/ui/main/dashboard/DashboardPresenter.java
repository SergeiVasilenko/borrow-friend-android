package vision.genesis.ui.main.dashboard;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;

import java.util.ArrayList;

import javax.inject.Inject;

import vision.genesis.app.App;
import vision.genesis.model.Balance;
import vision.genesis.model.ItemType;
import vision.genesis.model.Loan;
import vision.genesis.network.Api;
import vision.genesis.network.request.AddBalanceRequest;
import vision.genesis.network.request.GetBalanceRequest;
import vision.genesis.network.request.WithdrawBalanceRequest;
import vision.genesis.network.request.util.DefaultErrorHandler;
import vision.genesis.storage.Storage;
import vision.genesis.util.log.LogManager;

public class DashboardPresenter {

	private static final LogManager log = new LogManager("Dashboard");

	@Inject
	Storage      mStorage;
	@Inject
	Api          mApi;
	@Inject
	ObjectMapper mObjectMapper;

	private DashboardHeaderView mHeaderView;

	private DashboardView mView;

	private VKApiUserFull mUser;

	private Balance mBalance;

	public DashboardPresenter() {
		App.get().getUiComponent().inject(this);
	}

	public void setView(DashboardView view) {
		mView = view;
		setupView();
	}

	public void setHeaderView(DashboardHeaderView headerView) {
		mHeaderView = headerView;
		setupHeaderView();

	}

	private void setupHeaderView() {
		initVkData();
		updateHeader();
	}

	private void initVkData() {

		VKParameters parameters = new VKParameters();
		parameters.put("fields", "photo_200");
		VKRequest request = VKApi.users().get(parameters);
		request.parseModel = true;
		request.executeWithListener(new VKRequest.VKRequestListener() {
			@Override
			public void onComplete(VKResponse response) {
				//noinspection unchecked
				VKList<VKApiUserFull> users = (VKList<VKApiUserFull>) response.parsedModel;
				// Log.v(TAG, response.responseString);
				updateUser(users.get(0));
			}

			@Override
			public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
				log.v("request: attemptFailed");
			}

			@Override
			public void onError(VKError error) {
				log.e("request: onError: " + error);
			}
		});
	}

	private void updateUser(VKApiUserFull user) {
		log.d("updateUser: photo: %s", user.photo_200);
		mUser = user;
		mHeaderView.setAvatar(user.photo_200);
		mHeaderView.setName(user.first_name + ' ' + user.last_name);
	}

	private void getBalance() {
		GetBalanceRequest.create()
						 .userId(mStorage.getCurrentUser().getVkId())
						 .api(mApi)
						 .errorHandler(new DefaultErrorHandler(mObjectMapper))
						 .onError(error -> mView.showError(error))
						 .onResult(this::onBalanceGot)
						 .build()
						 .send();
	}

	private void onBalanceGot(Balance balance) {
		mBalance = balance;
		updateHeader();

		ArrayList<Loan> loans = new ArrayList<>();
		for (Loan loan : balance.getDebitList()) {
			loan.setItemType(ItemType.OFFER);
			loans.add(loan);
		}
		for (Loan loan : balance.getCreditList()) {
			loan.setItemType(ItemType.ASK);
			loans.add(loan);
		}
		mView.setItems(loans);
	}

	private void updateHeader() {
		if (mBalance == null || mHeaderView == null) {
			return;
		}
		mHeaderView.setBalance(mBalance.getBalance());
		mHeaderView.setIncoming(mBalance.getDebitSum());
		mHeaderView.setOutcoming(mBalance.getCreditSum());
	}

	private void setupView() {
		getBalance();
	}

	public void addBalance(int amount) {
		AddBalanceRequest.create()
						 .userId(mStorage.getCurrentUser().getVkId())
						 .amount(amount)
						 .api(mApi)
						 .errorHandler(new DefaultErrorHandler(mObjectMapper))
						 .onError(error -> mView.showError(error))
						 .onResult(statusResponse -> getBalance())
						 .build()
						 .send();
	}

	public void withdrawBalance(int amount) {
		WithdrawBalanceRequest.create()
							  .userId(mStorage.getCurrentUser().getVkId())
							  .amount(amount)
							  .api(mApi)
							  .errorHandler(new DefaultErrorHandler(mObjectMapper))
							  .onError(error -> mView.showError(error))
							  .onResult(statusResponse -> getBalance())
							  .build()
							  .send();
	}
}
