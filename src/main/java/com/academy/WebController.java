package com.academy;


import com.sun.xml.internal.bind.v2.TODO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Controller
public class WebController {

    @Autowired
    DataSource dataSource;
    DBRepository dBRepository = new DBRepository();

    @RequestMapping(method= RequestMethod.GET, path="/linklist")
    public ModelAndView viewLinks(){
        long listID=1; //hårdkodat
        List<Link> linkList=dBRepository.getList(listID);
        String userName="TestUser"; //Hårdkodat TODO:

        return new ModelAndView("linkList").addObject("linkList",linkList).addObject("user",userName);
    }



    @RequestMapping(value="/dbtest" , produces = "text/plain")
    @ResponseBody
    public String testDb() throws SQLException {
        try (Connection conn = dataSource.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT 1+1")) {
            rs.next();
            int two = rs.getInt(1);
            return "Database connectivity seems " + (two==2? "OK" : "Not OK");
        }

    }
}
