package vision.genesis.ui.order;

import vision.genesis.model.Item;
import vision.genesis.model.error.Error;

/**
 * Created on 22.04.17.
 *
 * @author Sergey Vasilenko (vasilenko.sn@gmail.com)
 */

public interface HeaderView {

	void showSocialProfile(long id);

	void setItem(Item item);

	void close();

	void showError(Error error);
}
