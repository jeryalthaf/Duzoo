
package com.duzoo.android.datasource;

/**
 * Created by RRaju on 4/13/2015.
 */
public class Message {

    String  name, content,id;
    boolean sentByMe;
    double  timeStamp;
    int     interestType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getInterestType() {
        return interestType;
    }

    public void setInterestType(int interestType) {
        this.interestType = interestType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSentByMe() {
        return sentByMe;
    }

    public void setSentByMe(int sentByMe) {
        if (sentByMe == 1)
            this.sentByMe = true;
        else
            this.sentByMe = false;
    }

    public double getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(double timeStamp) {
        this.timeStamp = timeStamp;
    }
}
