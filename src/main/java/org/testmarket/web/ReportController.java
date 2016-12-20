package org.testmarket.web;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.testmarket.reports.ReportService;

/**
 * Generated report file.
 * 
 * @author Sergey Stotskiy
 *
 */
@RestController
public class ReportController {


    @Autowired
    private ReportService reportService;

    @RequestMapping(path = "/rest/report/", method = RequestMethod.GET)
    public String findAccounts() {
        try {
            return reportService.doReportFile();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

}
