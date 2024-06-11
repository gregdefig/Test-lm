package adeo.leroymerlin.cdp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class EventService {

    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> getEvents() {
        return eventRepository.findAllBy();
    }

    public Event getEvent(long id) {
        return eventRepository.findById(id).orElse(null);
    }

    public void delete(Long id) {
        eventRepository.findById(id).ifPresentOrElse(eventRepository::delete,
            () -> System.out.println("Event not found") /* use log or do exceptions management */);
    }

    public List<Event> getFilteredEvents(String query) {
        List<Event> events = eventRepository.findAllBy();
        // Filter the events list in pure JAVA here
        List<Event> filterEvents = getFilterEvents(query, events);

        // add count
        addCountOnEventsTitleAndBandsName(filterEvents);

        return filterEvents;
    }



    /**
     * filter the events who contain at least one band has a member with the name matching the given
     * pattern.
     * @param query the pattern
     * @param events events to filter
     * @return events who contain at least one band has a member with the name matching the given
     */
    protected List<Event> getFilterEvents(String query, List<Event> events) {
        return events.stream()
            .filter(event -> event.getBands().stream()
                .flatMap(band -> band.getMembers().stream())
                .anyMatch(c -> c.getName().contains(query))).toList(); //possible to do tolowercase on name and query for ignore case
    }

    /**
     * add count bands on event title and  add count of members on bands name
     * @param events list of event to change
     */
    protected void addCountOnEventsTitleAndBandsName(List<Event> events) {
        events.forEach(event ->{
            event.setTitle(event.getTitle() + " [" + event.getBands().size() + "]");
            addCountMembersOnBandsName(event.getBands());
        });
    }


    /**
     * add count of members on bands name
     * @param bands list of bands to change the name
     */
    protected void addCountMembersOnBandsName(Set<Band> bands) {
        bands.forEach(band -> {
            String name = " [" + band.getMembers().size() + "]";
            if(!band.getName().endsWith(name)) {
                band.setName(band.getName() + name);
            }
        });
    }


    public void updateEvent(Long id, Event event) {
        eventRepository.findById(id).ifPresentOrElse(loadEvent -> saveUpdatedEvent(event,loadEvent), () -> System.out.println("Event not found") /* use log or do exceptions management for api */);
    }

    /**
     * update loadEvent with comment and nbStarts from event object
     * @param event from front
     * @param loadEvent to update
     */
    private void saveUpdatedEvent(Event event,Event loadEvent) {
        loadEvent.setComment(event.getComment());
        loadEvent.setNbStars(event.getNbStars());
        eventRepository.save(loadEvent);

    }
}
