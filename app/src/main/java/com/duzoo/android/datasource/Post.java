
package com.duzoo.android.datasource;

import com.duzoo.android.util.DuzooConstants;
import com.parse.ParseObject;

/**
 * Created by RRaju on 3/27/2015.
 */

public class Post {
    String id;
    int vote;
    boolean isFavorite, isDeleted;

    public Post() {
    }

    public Post(ParseObject parseObject) {

        id = parseObject.getObjectId();
        vote = parseObject.getInt(DuzooConstants.PARSE_POST_MY_VOTE);
        isFavorite = parseObject.getBoolean(DuzooConstants.PARSE_POST_FAVORITE);
        isDeleted = parseObject.getBoolean(DuzooConstants.PARSE_POST_DELETED);
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        if (isDeleted == 1)
            this.isDeleted = true;
        else
            this.isDeleted = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(int isFavorite) {
        if (isFavorite == 1)
            this.isFavorite = true;
        else
            this.isFavorite = false;
    }
}