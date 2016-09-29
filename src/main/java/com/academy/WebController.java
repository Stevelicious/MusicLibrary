package com.academy;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Controller
public class WebController {
    @Autowired
    DBRepository dBRepository;

    @RequestMapping(method = RequestMethod.GET, path = "/login")
    public ModelAndView goToLoginPage(HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView("login");
        String loginMessage = (String) httpSession.getAttribute("loginAttempt");
        modelAndView.addObject("loginMessage", loginMessage);
        return modelAndView;
    }

    @PostMapping("/login")
    public String login(HttpSession httpSession, @RequestParam String username, @RequestParam String password) {
        boolean validLogin = dBRepository.isPasswordValid(username, password);
        User user = dBRepository.getUser(username);
        httpSession.setAttribute("user", user);
        httpSession.setAttribute("loginAttempt", validLogin ? null : "Login failed. Please try again.");
        return validLogin ? "redirect:./lists" : "redirect:./login";
    }

    @GetMapping("/lists")
    public ModelAndView viewLists(HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView("lists");
        User user = (User) httpSession.getAttribute("user");
        modelAndView.addObject("user", user);
        List<LinkList> lists = dBRepository.getLists(user.getUserID());
        modelAndView.addObject("lists", lists);
        return modelAndView;
    }


    @RequestMapping(method = RequestMethod.GET, path = "/lists/{listID}")
    public ModelAndView viewLinks(HttpSession httpSession, @PathVariable Long listID) {
        List<Link> linkList = dBRepository.getLinks(listID);
        User user = (User) httpSession.getAttribute("user");
        String userName = user.getUsername();
        return new ModelAndView("linkList").addObject("linkList", linkList).addObject("user", userName);
    }


    @RequestMapping(value = "/dbtest", produces = "text/plain")
    @ResponseBody
    public String testDb() throws SQLException {
        int two = dBRepository.db();
        return "Database connectivity seems " + (two == 2 ? "OK" : "Not OK");

    }
}
