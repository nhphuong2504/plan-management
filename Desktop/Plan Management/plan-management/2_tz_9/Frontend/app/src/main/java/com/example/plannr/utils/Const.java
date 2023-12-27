package com.example.plannr.utils;

import com.example.plannr.god;

/**
 * class that contains important urls for requests with backend to send and get info
 */
public class Const {
    // Used for initial Object Test
    // Used for Array Test

    public static final String JSON_SERVER_REQUEST = "http://coms-309-040.class.las.iastate.edu:8080/student";

    public static final String JSON_DELETE_REQUEST = "http://coms-309-040.class.las.iastate.edu:8080/student";

    public static final String JSON_GET_EVENTS_REQUEST = "https://47a60566-a5b9-4966-8394-9a0277b9c8d4.mock.pstmn.io";

    public static final String JSON_GET_CHAT_LOG = "http://coms-309-040.class.las.iastate.edu:8080/chat/getLogs/chat=";

    public static final String JSON_GET_CHATS_LIST = "http://coms-309-040.class.las.iastate.edu:8080/chat/getAllChats/user=";

    public static final String JSON_POST_CHAT = "http://coms-309-040.class.las.iastate.edu:8080/chat/postChatLog/chat=";

    public static final String getEvents = "http://coms-309-040.class.las.iastate.edu:8080/event/getEvents";

    public static final String base = "http://coms-309-040.class.las.iastate.edu:8080/event/getEvents/joinedEvents/user="; //+ user id

    public static final String charge1URL = "http://coms-309-040.class.las.iastate.edu:8080/user/role?email=";

    public static final String leaveEvent = "http://coms-309-040.class.las.iastate.edu:8080/event/leaveEvent/event=";//god.selectedEvent.eventID + "/user=" + god.userId;

    public static final String joinEvent = "http://coms-309-040.class.las.iastate.edu:8080/event/joinEvent/event="; //+ god.selectedEvent.eventID + "/user=" + god.userId;

    public static final String addEvent = "http://coms-309-040.class.las.iastate.edu:8080/event/postEvent/event/add/user=" + god.userId;

    public static final String deleteEvent = "http://coms-309-040.class.las.iastate.edu:8080/event/deleteEvent/event=";// + god.selectedEvent.eventID + "/user=" + god.userId;

    public static final String personalEvents = "http://coms-309-040.class.las.iastate.edu:8080/event/getEvents/joinedEvents/user="+ god.userId;

    public static final String allEvents = "http://coms-309-040.class.las.iastate.edu:8080/event/getEvents";
    public static final String myEvents = "http://coms-309-040.class.las.iastate.edu:8080/event/getEvents/ownedEvents/user="+ god.userId;

    //change url to the correct one
    public static final String editEvent = "http://coms-309-040.class.las.iastate.edu:8080/event/modifyEventByTitle/user="; //{eventTitle}/user={userId}/


    // Requests below are for friendsList

    public static final String GET_FRIENDS = "http://coms-309-040.class.las.iastate.edu:8080/users/" + god.userId + "/friends";

    public static final String staticGet = "http://coms-309-040.class.las.iastate.edu:8080/users/7/friends";

    public static final String staticPost = "http://coms-309-040.class.las.iastate.edu:8080/users/7/friends/9";

    public static final String staticPostEmail = "http://coms-309-040.class.las.iastate.edu:8080/users/7/friends/by-email/";

    public static final String staticDelete = "http://coms-309-040.class.las.iastate.edu:8080/users/7/friends/";

    public static final String CONFIRM_FRIEND = "http://coms-309-040.class.las.iastate.edu:8080/users/" + god.userId + "/friends/";

    public static final String DENY_FRIEND = "http://coms-309-040.class.las.iastate.edu:8080/users/" + god.userId + "/friends/";

    public static final String ADD_FRIEND_EMAIL = "http://coms-309-040.class.las.iastate.edu:8080/users/" + god.userId + "/friends/by-email/";

    public static final String chargeURL = "http://coms-309-040.class.las.iastate.edu:8080/";

    public static final String GET_MUSIC_OPTIONS = "http://coms-309-040.class.las.iastate.edu:8080/music/getAllMusic";

    public static final String SET_MUSIC_PREFERENCE = "http://coms-309-040.class.las.iastate.edu:8080/music/changePreference/user=";

    public static final String CREATE_CHAT = "http://coms-309-040.class.las.iastate.edu:8080/chat/createChat/owner=";

}

