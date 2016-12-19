package org.testmarket.util;

import java.io.Serializable;
import java.util.Map;

/**
 * Authentication Message.
 * 
 * @author Sergey Stotskiy
 *
 */
@SuppressWarnings("serial")
public class AuthenticationMsg implements Serializable {

    private String type;

    private String sequence_id;

    private Map<String, String> data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSequence_id() {
        return sequence_id;
    }

    public void setSequence_id(String sequence_id) {
        this.sequence_id = sequence_id;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "AuthenticationMsg [type=" + type + ", sequence_id=" + sequence_id
            + ", data=" + data + "]";
    }

}
