package vision.genesis.model;

import android.os.Parcelable;

import java.math.BigDecimal;

public interface Item extends Parcelable {

	long getId();
	long getUserId();
	long getVkId();
	String getName();
	BigDecimal getPercentPerDay();
	int getAmount();
	int getRating();
	String getAvatar();
	ItemType getType();
	int getDurationMin();
	int getDurationMax();
	String getTitle();
}
