package Server;

import javax.swing.JPanel;

public class User {
	private String userName;
	private JPanel jPanel;
	private JPanel jArchivePanel;
	
	public User(String userName, JPanel jPanel,JPanel jArchivePanel) {
		
		this.userName = userName;
		this.jPanel = jPanel;
		this.jArchivePanel = jArchivePanel;
	}

	public JPanel getjArchivePanel() {
		return jArchivePanel;
	}

	public void setjArchivePanel(JPanel jArchivePanel) {
		this.jArchivePanel = jArchivePanel;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public JPanel getjPanel() {
		return jPanel;
	}

	public void setjPanel(JPanel jPanel) {
		this.jPanel = jPanel;
	}

}
