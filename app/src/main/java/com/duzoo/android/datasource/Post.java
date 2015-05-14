
package com.duzoo.android.datasource;

/**
 * Created by RRaju on 3/27/2015.
 */
public class Post {
    String id, name, content, userImage;
    int    upvotes, downvotes;
    int    vote;
    boolean hasMedia;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isHasMedia() {
        return hasMedia;
    }

    public void setHasMedia(int hasMedia) {
        if(hasMedia==1)
            this.hasMedia=true;
        else
            this.hasMedia=false;
    }

    String imageUrl;

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    int commentCount;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    int type;
    Double timestamp;

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Double getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Double timestamp) {
        this.timestamp = timestamp;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }

    public int getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(int downvotes) {
        this.downvotes = downvotes;
    }
}
