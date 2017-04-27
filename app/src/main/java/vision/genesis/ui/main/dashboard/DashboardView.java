package vision.genesis.ui.main.dashboard;

import java.util.List;

import vision.genesis.model.Loan;
import vision.genesis.model.error.Error;

public interface DashboardView {

	void setItems(List<? extends Loan> items);

	void showError(Error error);
}
