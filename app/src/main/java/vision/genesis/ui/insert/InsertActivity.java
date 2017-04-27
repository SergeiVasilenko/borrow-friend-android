package vision.genesis.ui.insert;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import vision.genesis.R;
import vision.genesis.app.App;
import vision.genesis.model.ItemType;
import vision.genesis.network.Api;
import vision.genesis.network.request.PostOrderRequest;
import vision.genesis.network.request.util.DefaultErrorHandler;
import vision.genesis.storage.Storage;
import vision.genesis.ui.common.BaseActivity;

public class InsertActivity extends BaseActivity {

	private static final String EXTRA_TYPE = "EXTRA_TYPE";

	@BindView(R.id.percent)
	EditText mPercentText;
	@BindView(R.id.sum)
	EditText mSumText;
	@BindView(R.id.durationMin)
	EditText mDurationMinText;
	@BindView(R.id.durationMax)
	EditText mDurationMaxText;
	@BindView(R.id.aimCredit)
	EditText mAimCreditText;

	@BindView(R.id.action)
	Button   mActionButton;

	@BindView(R.id.percentLayout)
	TextInputLayout mPercentLayout;
	@BindView(R.id.sumLayout)
	TextInputLayout mSumLayout;
	@BindView(R.id.durationMinLayout)
	TextInputLayout mDurationMinLayout;
	@BindView(R.id.durationMaxLayout)
	TextInputLayout mDurationMaxLayout;
	@BindView(R.id.aimCreditLayout)
	TextInputLayout mAimCreditLayout;

	@Inject
	Api          mApi;
	@Inject
	Storage      mStorage;
	@Inject
	ObjectMapper mObjectMapper;


	private ItemType mItemType;

	private DecimalFormat mDecimalFormat;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_insert);

		ButterKnife.bind(this);

		App.get().getUiComponent().inject(this);

		mActionButton.setOnClickListener(v -> onActionClick());

		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setGroupingSeparator(',');
		symbols.setDecimalSeparator('.');
		String pattern = "#,##0.0#";
		DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
		decimalFormat.setParseBigDecimal(true);
		mDecimalFormat = decimalFormat;

		onNewIntent(getIntent());
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		mItemType = (ItemType) intent.getSerializableExtra(EXTRA_TYPE);

		if (mItemType == ItemType.ASK) {
			mAimCreditLayout.setVisibility(View.VISIBLE);
			mActionButton.setText(R.string.post_ask);
		} else {
			mAimCreditLayout.setVisibility(View.GONE);
			mActionButton.setText(R.string.post_offer);
		}

		setupActionBar();
	}

	private void setupActionBar() {
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		ActionBar ab = getSupportActionBar();
		if (ab == null) {
			return;
		}

		ab.setDisplayHomeAsUpEnabled(true);
		ab.setHomeButtonEnabled(true);
		ab.setDisplayShowTitleEnabled(false);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home: {
				onBackPressed();
				return true;
			}
			default: {
				return super.onOptionsItemSelected(item);
			}
		}
	}

	private void onActionClick() {
		BigDecimal amount = getAmount();
		if (amount == null) {
			return;
		}
		BigDecimal percent = getPercentPerDay();
		if (percent == null) {
			return;
		}
		int durationMin = getMinDuration();
		if (durationMin == -1) {
			return;
		}
		int durationMax = getMaxDuration();
		if (durationMax == -1) {
			return;
		}
		String title = getAim();
		PostOrderRequest.create()
						.userId(mStorage.getCurrentUser().getVkId())
						.amount(amount)
						.percentPerDay(percent)
						.durationMax(durationMax)
						.durationMin(durationMin)
						.title(title)
						.orderType(mItemType)
						.api(mApi)
						.errorHandler(new DefaultErrorHandler(mObjectMapper))
						.onError(error -> {
							Toast.makeText(InsertActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
						})
						.onResult(result -> finish())
						.build()
						.send();
	}

	private BigDecimal getAmount() {
		BigDecimal amount = null;
		mSumLayout.setErrorEnabled(false);
		try {
			amount = (BigDecimal) mDecimalFormat.parse(mSumText.getText().toString());
		} catch (ParseException e) {
			mSumLayout.setErrorEnabled(true);
			mSumLayout.setError(getString(R.string.error));
		}
		return amount;
	}

	private BigDecimal getPercentPerDay() {
		BigDecimal amount = null;
		mPercentLayout.setErrorEnabled(false);
		try {
			amount = (BigDecimal) mDecimalFormat.parse(mPercentText.getText().toString());
		} catch (ParseException e) {
			mPercentLayout.setErrorEnabled(true);
			mPercentLayout.setError(getString(R.string.error));
		}
		return amount;
	}

	private int getMinDuration() {
		int durationMin = -1;
		mDurationMinLayout.setErrorEnabled(false);
		try {
			durationMin = Integer.valueOf(mDurationMinText.getText().toString());
		} catch (NumberFormatException e) {
			mDurationMinLayout.setError(getString(R.string.error));
			mDurationMinLayout.setErrorEnabled(true);
		}
		return durationMin;
	}

	private int getMaxDuration() {
		int durationMax = -1;
		mDurationMaxLayout.setErrorEnabled(false);
		try {
			durationMax = Integer.valueOf(mDurationMaxText.getText().toString());
		} catch (NumberFormatException e) {
			mDurationMaxLayout.setError(getString(R.string.error));
			mDurationMaxLayout.setErrorEnabled(true);
		}
		return durationMax;
	}

	private String getAim() {
		return mAimCreditText.getText().toString();
	}


	public static void show(Context context, ItemType type) {
		Intent intent = new Intent(context, InsertActivity.class);
		intent.putExtra(EXTRA_TYPE, type);
		context.startActivity(intent);
	}

}
