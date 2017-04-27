package vision.genesis.ui.main.dashboard;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;
import vision.genesis.R;
import vision.genesis.model.error.Error;
import vision.genesis.util.log.LogManager;
import vision.genesis.view.drawable.CircleTransformation;

public class HeaderHeaderViewHolder extends DashboardViewHolder implements HeaderView, DashboardHeaderView {

	private static final LogManager log = new LogManager("DashboardHeader");

	@BindView(R.id.avatar)
	ImageView mAvatarView;
	@BindView(R.id.name)
	TextView  mNameView;
	@BindView(R.id.incoming)
	TextView  mIncomingView;
	@BindView(R.id.outcoming)
	TextView  mOutcomingView;
	@BindView(R.id.balance)
	TextView  mBalanceView;
	@BindView(R.id.addBalance)
	Button    mAddBalanceButton;
	@BindView(R.id.withdrawBalance)
	Button    mWithdrawBalanceButton;

	private DecimalFormat mDecimalFormat;

	private DashboardPresenter mPresenter;

	public HeaderHeaderViewHolder(View itemView) {
		super(itemView);

		ButterKnife.bind(this, itemView);

		mAddBalanceButton.setOnClickListener(v -> showAddBalanceDialog());
		mWithdrawBalanceButton.setOnClickListener(v -> showWithdrawBalanceDialog());

	}

	public void setPresenter(DashboardPresenter presenter) {
		mPresenter = presenter;
	}


	@Override
	public int getType() {
		return ViewType.HEADER;
	}

	@Override
	public void showError(Error error) {
		Toast.makeText(itemView.getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
	}

	Context getContext() {
		return itemView.getContext();
	}

	@Override
	public void setName(String name) {
		mNameView.setText(name);
	}

	@Override
	public void setAvatar(String uri) {
		Glide.with(getContext())
			 .load(uri)
			 .fitCenter()
			 .centerCrop()
			 .transform(new CircleTransformation(getContext()))
			 .into(mAvatarView);
	}

	@Override
	public void setIncoming(int incoming) {
		mIncomingView.setText(String.valueOf(incoming));
	}

	@Override
	public void setOutcoming(int outcoming) {
		mOutcomingView.setText(String.valueOf(outcoming));
	}

	@Override
	public void setBalance(int amount) {
		mBalanceView.setText(String.valueOf(amount));
	}

	private void showAddBalanceDialog() {
		showInputDialog(R.string.add_balance_title, R.string.add_balance, amount -> mPresenter.addBalance(amount));
	}

	private void showWithdrawBalanceDialog() {
		showInputDialog(R.string.withdraw_title,
						R.string.withdraw_balance,
						amount -> mPresenter.withdrawBalance(amount));
	}

	private void showInputDialog(int titleResId, int actionResId, Consumer<Integer> consumer) {
		Context context = getContext();
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.input_text, null, false);
		EditText editText = (EditText) view.findViewById(R.id.editText);
		TextInputLayout inputLayout = (TextInputLayout) view.findViewById(R.id.inputLayout);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		Dialog dialog = builder.setTitle(titleResId)
							   .setView(view)
							   .setNegativeButton(R.string.cancel, null)
							   .setPositiveButton(actionResId, null)
							   .create();

		dialog.setOnShowListener(dialog1 -> {
			Button button = ((AlertDialog) dialog1).getButton(AlertDialog.BUTTON_POSITIVE);
			button.setOnClickListener(v -> {
				if (TextUtils.isEmpty(editText.getText())) {
					inputLayout.setError(context.getString(R.string.error_balance));
					return;
				}
				int amount = Integer.valueOf(editText.getText().toString());
				if (amount <= 0) {
					inputLayout.setError(context.getString(R.string.error_balance));
					return;
				}
				try {
					consumer.accept(amount);
					dialog1.dismiss();
				} catch (Exception e) {
					log.e("WTF!", e);
				}
			});
		});

		dialog.show();
	}
}
