package vision.genesis.model;

import java.util.ArrayList;

/**
 * Created on 23.04.17.
 *
 * @author Sergey Vasilenko (vasilenko.sn@gmail.com)
 */

public class FriendGraph {

	private ArrayList<LocalUser> mUsers;

	private FriendGraph() {
	}

	public FriendGraph(ArrayList<LocalUser> users) {
		mUsers = users;
	}

	public ArrayList<LocalUser> getUsers() {
		return mUsers;
	}
}
