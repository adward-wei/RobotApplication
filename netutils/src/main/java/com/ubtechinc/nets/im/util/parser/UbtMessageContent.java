package com.ubtechinc.nets.im.util.parser;

/**
 * @author adward
 * Created by Administrator on 2016/12/15.
 */
public class UbtMessageContent {

    private String category;
    private String data;
    private String identifier;


    public String getCategory() {
        return category;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }


}
