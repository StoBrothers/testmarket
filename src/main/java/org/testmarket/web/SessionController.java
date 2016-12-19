/*
 * Copyright 2002-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.testmarket.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.testmarket.service.SessionHandler;
import org.testmarket.util.WSSessionWrapper;

/**
 * Show WebSocket sessions.
 * 
 * @author Sergey Stotskiy
 */

@RestController
public class SessionController {

    @Autowired
    private SessionHandler sessionHandler;

    /**
     * Get activity session.
     * 
     * @return
     */
    @RequestMapping(path = "/sessions", method = RequestMethod.GET)
    public List<String> listSessions() {
        Map<String, WSSessionWrapper> map = sessionHandler.getSessions();
        List<String> list = new ArrayList<String>(map.size());
        for (WSSessionWrapper currentSession : map.values()) {
            list.add(currentSession.toString());
        }
        return list;

    }

}
