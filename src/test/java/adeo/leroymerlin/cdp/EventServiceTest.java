package adeo.leroymerlin.cdp;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EventServiceTest {


    @Autowired
    EventService eventService;

    @Test
    public void whenCallGetEventsReturnAllEvents() {
        assertEquals(eventService.getEvents().size(), 5);
    }

    @Test
    public void whenUpdateEventNbStartsChangeIsStore(){
        Event event =eventService.getEvent(1000L);
        assertNull(event.getNbStars());

        event.setNbStars(5);
        eventService.updateEvent(1000L,event);
        assertEquals(eventService.getEvent(1000L).getNbStars(),Integer.valueOf(5));
    }

    public void whenUpdateEventCommentChangeIsStore(){
        Event event =eventService.getEvent(1000L);
        assertNull(event.getComment());

        event.setComment("test");
        eventService.updateEvent(1000L,event);
        assertEquals(eventService.getEvent(1000L).getComment(),"test");
    }

    @Test
    public void whenFilterEventsReturnOnlyMatchingEvent() {
        List<Event> events = eventService.getEvents();
        List<Event> filterEvents = eventService.getFilterEvents("Wa", events);
        assertEquals(filterEvents.size(), 1);
    }

    public void whenCountBandsEventTitleChange(){
        Event event = eventService.getEvent(1000L);
        assertEquals(event.getTitle(),"GrasPop Metal Meeting");

        eventService.addCountOnEventsTitleAndBandsName(List.of(event));
        assertEquals(event.getTitle(),"GrasPop Metal Meeting [5]");
    }

    public void whenCountMemberBandNameChange(){
        Event event = eventService.getEvent(1000L);
        Band band = event.getBands().stream().findFirst().orElse(null);

        assertEquals(band.getName(),"Metallica");

        eventService.addCountMembersOnBandsName(event.getBands());
        assertEquals(band.getName(),"Metallica [4]");
    }

    @Test
    public void whenDeleteEventGetEventsReturnAllEventsMinusOne() {
        assertEquals(eventService.getEvents().size(), 5);

        eventService.delete(1000L);
        assertEquals(eventService.getEvents().size(), 4);
    }

}
