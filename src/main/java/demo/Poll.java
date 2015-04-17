package demo;



public class Poll {
	
	private String poll_id;
	private String question;
	private String started_at;
	private String expired_at;
	private String []choice;
	
	
	public String getId() {
		return poll_id;
	}
	public void setId(String poll_id) {
		this.poll_id = poll_id;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getStarted_at() {
		return started_at;
	}
	public void setStarted_at(String started_at) {
		this.started_at = started_at;
	}
	public String getExpired_at() {
		return expired_at;
	}
	public void setExpired_at(String expired_at) {
		this.expired_at = expired_at;
	}
	public String[] getChoice() {
		return choice;
	}
	public void setChoice(String[] choice) {
		this.choice = choice;
	}

}
 