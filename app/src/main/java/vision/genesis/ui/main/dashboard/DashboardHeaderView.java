package vision.genesis.ui.main.dashboard;


public interface DashboardHeaderView {

	void setName(String name);
	void setAvatar(String uri);
	void setIncoming(int incoming);
	void setOutcoming(int outcoming);
	void setBalance(int amount);
}
