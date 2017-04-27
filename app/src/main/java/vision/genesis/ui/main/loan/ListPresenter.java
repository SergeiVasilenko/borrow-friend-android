package vision.genesis.ui.main.loan;


public abstract class ListPresenter {

	protected LoanListView mView;

	public void setView(LoanListView view) {
		mView = view;
		updateView();
	}

	abstract void updateView();

	abstract void addItem();
}
