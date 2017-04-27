package vision.genesis.model;

/**
 * Created on 22.04.17.
 *
 * @author Sergey Vasilenko (vasilenko.sn@gmail.com)
 */

public class ItemTypeConverter {

	public static String convert(ItemType type) {
		switch (type) {
			case OFFER:
				return "offer";
			case ASK:
				return "ask";
			default:
				throw new UnsupportedOperationException("Unsupported type: " + type);
		}
	}
}
