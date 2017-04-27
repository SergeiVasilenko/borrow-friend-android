package vision.genesis.ui.main.loan;

import java.util.List;

import vision.genesis.model.Item;
import vision.genesis.model.ItemType;
import vision.genesis.ui.common.RequestView;


public interface LoanListView extends RequestView {

	void setItems(List<? extends Item> items);
	void showAddItem(ItemType type);
}
