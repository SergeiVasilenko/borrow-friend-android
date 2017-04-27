package vision.genesis.ui.common;

import vision.genesis.model.error.Error;

public interface RequestView {

	void showProgress();
	void hideProgress();
	void showError(Error error);
}
