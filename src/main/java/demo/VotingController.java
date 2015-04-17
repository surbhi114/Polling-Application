package demo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicInteger;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//import dao.PollDAO;

@RestController
@RequestMapping("/api/v1/*")
public class VotingController {

	HashMap<Integer, HashMap<String, PollDAO>> modPollMap = new HashMap<Integer, HashMap<String, PollDAO>>();

	private static HashMap<Integer, Moderator> modList = new HashMap<Integer, Moderator>();
	private static HashMap<String, Poll> pollList = new HashMap<String, Poll>();
	private final AtomicInteger modCounter = new AtomicInteger();
	private final AtomicInteger pollCounter = new AtomicInteger();

	// 1 POST : Create Moderator

	@RequestMapping(value = "/moderators", method = RequestMethod.POST)
	public ResponseEntity<Moderator> createModerator(
			@Valid @RequestBody Moderator mod) {

		System.out.println("creating moderator");

		TimeZone timeZone = TimeZone.getTimeZone("UTC");

		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'z'");
		ft.setTimeZone(timeZone);

		mod.setId(modCounter.incrementAndGet());

		mod.setCreated_at(((ft.format(new Date()))));

		modList.put(mod.getId(), mod);

		return new ResponseEntity<Moderator>(mod, HttpStatus.CREATED);

	}

	// 2 GET : View Moderator

	@RequestMapping(value = "/moderators/{moderator_id}", method = RequestMethod.GET)
	public ResponseEntity<Moderator> getModerator(@PathVariable int moderator_id) {

		if (modList.get(moderator_id) != null) {

			return new ResponseEntity<Moderator>(modList.get(moderator_id),
					HttpStatus.OK);
		} else {

			return new ResponseEntity<Moderator>(HttpStatus.NOT_FOUND);
		}

	}

	// 3 PUT : Update Moderator

	@RequestMapping(value = "/moderators/{moderator_id}", method = RequestMethod.PUT)
	public ResponseEntity<Moderator> updateModerator(
			@PathVariable int moderator_id, @Valid @RequestBody Moderator mod1) {

		modList.get(moderator_id).setEmail(mod1.getEmail());
		modList.get(moderator_id).setPassword(mod1.getPassword());
		return new ResponseEntity<Moderator>(modList.get(moderator_id),
				HttpStatus.CREATED);

	}

	// 4 POST : Create a new Poll

	@RequestMapping(value = "/moderators/{moderator_id}/polls", method = RequestMethod.POST)
	public ResponseEntity<Poll> createPoll(@PathVariable int moderator_id,
			@Valid @RequestBody Poll poll) {

		poll.setId(Integer.toString(pollCounter.incrementAndGet()));
		pollList.put(poll.getId(), poll);

		if (modPollMap.containsKey(moderator_id)) {

			modPollMap.get(moderator_id).put(poll.getId(), new PollDAO(poll));
		} else {
			HashMap<String, PollDAO> map = new HashMap<String, PollDAO>();
			map.put(poll.getId(), new PollDAO(poll));
			modPollMap.put(moderator_id, map);
		}

		return new ResponseEntity<Poll>(poll, HttpStatus.CREATED);

	}

	// 5 GET : list poll details for particular poll_id without results

	@RequestMapping(value = "/polls/{poll_id}", method = RequestMethod.GET)
	public ResponseEntity<Poll> getPoll(@PathVariable String poll_id) {

		if (pollList.get(poll_id) != null) {

			return new ResponseEntity<Poll>(pollList.get(poll_id),
					HttpStatus.OK);

		} else {

			return new ResponseEntity<Poll>(HttpStatus.NOT_FOUND);
		}
	}

	// 6 GET : list poll with result

	@RequestMapping(value = "/moderators/{moderator_id}/polls/{poll_id}", method = RequestMethod.GET)
	public ResponseEntity<PollDAO> getPollWithResult(
			@PathVariable String poll_id, @PathVariable int moderator_id) {

		if (modPollMap.get(moderator_id) != null) {

			return new ResponseEntity<PollDAO>(modPollMap.get(moderator_id)
					.get(poll_id), HttpStatus.OK);

		} else {
			return new ResponseEntity<PollDAO>(HttpStatus.NOT_FOUND);
		}
	}

	// 7 GET : list all polls

	@RequestMapping(value = "/moderators/{moderator_id}/polls", method = RequestMethod.GET)
	public ResponseEntity<List<PollDAO>> getAllPolls(
			@PathVariable int moderator_id) {

		List<PollDAO> result = new LinkedList<PollDAO>();

		HashMap<String, PollDAO> map = new HashMap<String, PollDAO>();

		if (modPollMap.get(moderator_id) != null) {

			map = modPollMap.get(moderator_id);

			Iterator it = map.values().iterator();

			while (it.hasNext()) {
				result.add((PollDAO) it.next());
			}

			return new ResponseEntity<List<PollDAO>>(result, HttpStatus.OK);

		} else {
			return new ResponseEntity<List<PollDAO>>(HttpStatus.NOT_FOUND);
		}
	}

	// 8 DELETE : delete a poll

	@RequestMapping(value = "/moderators/{moderator_id}/polls/{poll_id}", method = RequestMethod.DELETE)
	public ResponseEntity deletePoll(@PathVariable int moderator_id,
			@PathVariable String poll_id) {

		pollList.remove(poll_id);
		modPollMap.get(moderator_id).remove(poll_id);

		return new ResponseEntity(HttpStatus.NO_CONTENT);

	}

	// 9 PUT : Vote a poll

	@RequestMapping(value = "/polls/{poll_id}", method = RequestMethod.PUT)
	public ResponseEntity votePoll(@PathVariable String poll_id,
			@Valid @RequestParam("choice") int choice_index) {

		int resultArray[];
		Iterator<Entry<Integer, HashMap<String, PollDAO>>> mapOut = modPollMap
				.entrySet().iterator();

		while (mapOut.hasNext()) {

			Iterator<Entry<String, PollDAO>> mapIn = mapOut.next().getValue()
					.entrySet().iterator();

			while (mapIn.hasNext()) {

				Entry<String, PollDAO> obj = mapIn.next();

				if (obj.getKey().equalsIgnoreCase(poll_id)) {

					resultArray = obj.getValue().getResults();

					if (choice_index == 0) // choice is yes=0
					{

						++resultArray[0];

					} else // choice is no=1
					{
						++resultArray[1];
					}

					obj.getValue().setResults(resultArray);

					break;
				} else {

					System.out
					.println("didnt find poll with given id to vote for");
				}
			}

		}

		return new ResponseEntity(HttpStatus.NO_CONTENT);

	}

}
