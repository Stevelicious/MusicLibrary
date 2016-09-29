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
    public ModelAndView goToLoginPage() {
        return new ModelAndView("login");
    }

    @PostMapping("/login")
    public String login(HttpSession httpSession, @RequestParam String username, @RequestParam String password) {
        boolean validLogin = true;
        User user = dBRepository.getUser(username);
        httpSession.setAttribute("user", user);
        return validLogin ? "redirect:./lists" : "redirect:./";
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


    @RequestMapping(method = RequestMethod.GET, path = "/list/{listID}")
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
