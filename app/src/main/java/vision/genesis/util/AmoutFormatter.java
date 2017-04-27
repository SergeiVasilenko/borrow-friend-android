package vision.genesis.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Created on 23.04.17.
 *
 * @author Sergey Vasilenko (vasilenko.sn@gmail.com)
 */

public class AmoutFormatter {
	private static DecimalFormat mDecimalFormat;


	public static String format(BigDecimal amount) {
		if (mDecimalFormat == null) {
			DecimalFormatSymbols symbols = new DecimalFormatSymbols();
			symbols.setGroupingSeparator(',');
			symbols.setDecimalSeparator('.');
			String pattern = "#,##0.0#";
			DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
			decimalFormat.setParseBigDecimal(true);
			mDecimalFormat = decimalFormat;
		}
		return mDecimalFormat.format(amount);
	}
}
