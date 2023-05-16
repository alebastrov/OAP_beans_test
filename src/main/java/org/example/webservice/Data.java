package org.example.webservice;

import lombok.AllArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
@AllArgsConstructor
public class Data {
    public String id;
    public List<String> param = new ArrayList<>();
    public StatusType bucket;

    public enum StatusType {
        IGNORED, TRANSFORM_TO_JSON, TRANSFORM_TO_HTML, PASS_THROUGH, BOTH;
    }
}
