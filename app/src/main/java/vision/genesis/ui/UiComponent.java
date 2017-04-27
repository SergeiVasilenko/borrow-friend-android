package vision.genesis.ui;

import dagger.Subcomponent;
import vision.genesis.ui.auth.AuthActivity;
import vision.genesis.ui.common.BaseActivity;
import vision.genesis.ui.insert.InsertActivity;
import vision.genesis.ui.main.MainActivity;
import vision.genesis.ui.main.dashboard.DashboardPresenter;
import vision.genesis.ui.main.loan.AskListPresenter;
import vision.genesis.ui.main.loan.OfferListPresenter;
import vision.genesis.ui.order.DealActivity;
import vision.genesis.ui.order.HeaderPresenter;

@UiScope
@Subcomponent
public interface UiComponent {
	void injectToBaseActivity(BaseActivity activity);

	void inject(MainActivity activity);

	void inject(AuthActivity authActivity);

	void inject(OfferListPresenter offerListPresenter);

	void inject(AskListPresenter askListPresenter);

	void inject(InsertActivity insertActivity);

	void inject(DealActivity dealActivity);

	void inject(HeaderPresenter headerPresenter);

	void inject(DashboardPresenter presenter);

	@Subcomponent.Builder
	interface UiComponentBuilder {
		UiComponent build();
	}
}
