package vision.genesis.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Balance {
	@JsonProperty("balance")
	private int balance;

	@JsonProperty("debitSum")
	private int debitSum;

	@JsonProperty("creditSum")
	private int creditSum;

	@JsonProperty("debitList")
	private ArrayList<Loan> debitList;

	@JsonProperty("creditList")
	private ArrayList<Loan> creditList;

	public int getBalance() {
		return balance;
	}

	public int getDebitSum() {
		return debitSum;
	}

	public int getCreditSum() {
		return creditSum;
	}

	public ArrayList<Loan> getDebitList() {
		return debitList;
	}

	public ArrayList<Loan> getCreditList() {
		return creditList;
	}
}
