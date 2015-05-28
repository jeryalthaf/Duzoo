package com.duzoo.android.datasource;

import com.duzoo.android.util.DuzooConstants;
import com.parse.ParseObject;

import java.util.Date;

/**
 * Created by RRaju on 4/27/2015.
 */
public class Comment {

    String name, content, post_id,user_image_url;
    double timestamp;
    String createdAt;

    public String getUser_image_url() {
        return user_image_url;
    }

    public void setUser_image_url(String user_image_url) {
        this.user_image_url = user_image_url;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Comment(ParseObject comment) {

        name = comment.getString(DuzooConstants.PARSE_COMMENT_USER_NAME);
        content = comment.getString(DuzooConstants.PARSE_COMMENT_CONTENT);
        post_id = comment.getString(DuzooConstants.PARSE_COMMENT_POST_ID);
        timestamp = comment.getDouble(DuzooConstants.PARSE_COMMENT_TIMESTAMP);
        user_image_url = comment.getString(DuzooConstants.PARSE_COMMENT_USER_IMAGE);

    }

    public String getName() {
        return name;
    }

    public String getContent() {return content;}

    public String getPost_id() {
        return post_id;
    }

    public double getTimestamp() {return timestamp;}


}
