package com.jdbc.controller;

import com.google.gson.Gson;
import com.jdbc.models.EventsEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import com.jdbc.dao.DaoEventFactory;
import com.jdbc.dao.DaoUserFactory;
import com.jdbc.dao.ParentEventDao;
import com.jdbc.dao.ParentUserDao;
import org.springframework.web.bind.annotation.RequestMethod;
import java.util.ArrayList;
import java.sql.Date;


@Controller
@SessionAttributes("loggedinuser")
public class HomeController {


    private Session getSession() {
        Configuration cfg = new Configuration().configure("hibernate.cfg.xml");
        SessionFactory sessionFact = cfg.buildSessionFactory();
        Session selectAll = sessionFact.openSession();
        selectAll.beginTransaction();
        selectAll.close();
        return selectAll;

    }

    private ParentUserDao userDao = DaoUserFactory.getDaoInstance(ParentUserDao.HIBERNATE_DAO);
    private ParentEventDao eventDao = DaoEventFactory.getDaoInstance(ParentEventDao.HIBERNATE_DAO);


    @RequestMapping(value = "/listeventsfiltered")
//added string parameter
    //list should be filtered, added jsp file to show filtered list
    public ModelAndView listEventsFiltered(@RequestParam("sport") String sport) {
        ArrayList<EventsEntity> eventList = eventDao.eventListFiltered(sport);
        return new ModelAndView("listeventsfiltered", "cList", eventList);
    }
    @RequestMapping(value = "/data")
    public ModelAndView data(){
        ArrayList<EventsEntity> eventList = eventDao.eventList();

        String jsonArray = new Gson().toJson(eventList);
        //System.out.println(jsonArray);

        return new ModelAndView("data", "json", jsonArray);
    }

    @RequestMapping(value="/allmarkers")
    public String allMarkers(){
        return "allmarkers";
    }

    @RequestMapping(value = "/listevents")
    //original list with no filters
    public ModelAndView listEvents() {
        ArrayList<EventsEntity> eventList = eventDao.eventList();

        return new ModelAndView("listevents", "cList", eventList);
    }

    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public ModelAndView updateEvent(Model model, @RequestParam("id") int eventId, @RequestParam("peopleGoing") int peopleGoing,
                                    @RequestParam("latitude") double latitude, @RequestParam("longitude") double longitude, @RequestParam("name") String name,
                                    @RequestParam("sport") String sport, @RequestParam("address") String address,
                                    @RequestParam("description") String description, @RequestParam("time") String time) {

        System.out.println(peopleGoing);
        model.addAttribute("eventId", eventId);
        model.addAttribute("peopleGoing", peopleGoing);
        model.addAttribute("latitude", latitude);
        model.addAttribute("longitude", longitude);
        model.addAttribute("name", name);
        model.addAttribute("sport", sport);
        model.addAttribute("address", address);
        model.addAttribute("description", description);
        model.addAttribute("time", time);

        return new ModelAndView("updateeventform", "", "");
    }

    @RequestMapping("/updateform")

    public ModelAndView updateForm(Model model, @RequestParam("eventId") int eventID, @RequestParam("peopleGoing") int peopleGoing) {

        eventDao.updateEvent(eventID, peopleGoing);



        EventsEntity editEvent = eventDao.getEvent(eventID);
        System.out.println(editEvent.getName());
        if (peopleGoing == editEvent.getMinNeeded() || peopleGoing > editEvent.getMinNeeded()) {

            Notification.sendNotification();
        }
        //added request param and sport argument
        ArrayList<EventsEntity> eventList = eventDao.eventList();


        return new ModelAndView("redirect:confirmation", "cList", eventList);
    }


    @RequestMapping("/addeventsuccess")
    public String addnewItem(@RequestParam("name") String name,
                             @RequestParam("sport") String sport,
                             @RequestParam("address") String address,
                             @RequestParam("day") Date day,
                             @RequestParam("description") String description,
                             @RequestParam("peopleGoing") int peopleGoing,
                             @RequestParam("min") int minNeeded,
                             @RequestParam("time") String time,
                             @RequestParam("lat") double lat,
                             @RequestParam("lng") double lng,
                             Model model) {

        return eventDao.addEvent(name, sport, address, day, description, peopleGoing, minNeeded, time, lat, lng, model);
    }

    @RequestMapping("/listofsports")
    public ModelAndView listOfSports() {
        return new ModelAndView("listofsports", "sportlist", "SPORTS");
    }

    @RequestMapping("/addevent")
    // the String method returns the jsp page that we want to show
    public String addevent() {
        return "addevent";
    }

    @RequestMapping("deleteevents")
    public String deleteEvent() {

        eventDao.deleteEvent();


        return "login";
    }

    @RequestMapping("/confirmation")
    public String confirmation() {
        return "confirmation";
    }
}
