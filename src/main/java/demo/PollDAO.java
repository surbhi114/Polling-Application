package demo;
import demo.*;

public class PollDAO {
	
	private String poll_id;
	private String question;
	private String started_at;
	private String expired_at;
	private String []choice;
	private int [] results={0,0};  // only two options for reply yes=0; no=1 : initial count for both options 0
	
	public PollDAO(Poll poll){
		
		this.poll_id=poll.getId();
		this.question=poll.getQuestion();
		this.started_at=poll.getStarted_at();
		this.expired_at=poll.getExpired_at();
		this.choice=poll.getChoice();
	    
	}
	
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
	
	public int[] getResults() {
		return results;
	}
	public void setResults(int[] results) {
		this.results = results;
	}
	
	

}
