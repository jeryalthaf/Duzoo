package com.duzoo.android.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.duzoo.android.R;
import com.duzoo.android.application.DuzooPreferenceManager;
import com.duzoo.android.application.MyApplication;
import com.duzoo.android.application.UIController;
import com.duzoo.android.datasource.ParseLink;
import com.duzoo.android.util.DuzooConstants;
import com.duzoo.android.util.Util;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by RRaju on 4/12/2015.
 */
public class NewPostActivity extends ActionBarActivity {

    EditText mPostContent;
    TextView mNewPostAdd;
    ImageView mPostImage;
    boolean imageAttached = false;
    Bitmap bitmap;
    ProgressDialog dialog;
    String path;
    boolean gif;
    ParseFile image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_new_post));

        dialog = new ProgressDialog(this);
        dialog.setTitle("Please wait");
        dialog.setMessage("Adding your post ...");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("New Post");
        mPostContent = (EditText) findViewById(R.id.new_post_box);
        mPostContent.setCursorVisible(true);
        mNewPostAdd = (TextView) findViewById(R.id.new_post_submit);
        mPostImage = (ImageView) findViewById(R.id.image);
        Util.focusKeyBoard(this, mPostContent);
        UIController.newPostActivity = this;
        mNewPostAdd.setClickable(true);
        mNewPostAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPostContent.getText().toString().replaceAll(" ", "").contentEquals("") && !imageAttached) {
                    Toast.makeText(MyApplication.getContext(), "Post cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                String content = new String(mPostContent.getText().toString());
                Util.hideKeyBoard(UIController.newPostActivity);
                createPost(content, imageAttached, bitmap);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            Util.hideKeyBoard(this);
            mNewPostAdd = null;
            mPostContent = null;

        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_new_post, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.menu_camera) {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, DuzooConstants.RESULT_LOAD_IMAGE);
            Util.hideKeyBoard(this);
        } else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case DuzooConstants.RESULT_LOAD_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    path = cursor.getString(columnIndex);
                    cursor.close();

                    mPostImage.setVisibility(View.VISIBLE);
                    bitmap = BitmapFactory.decodeFile(path);
                    imageAttached = true;
                    Glide.with(this).load(path).placeholder(R.drawable.placeholder).into(mPostImage);
                }
        }
    }

    public void createPost(String content, boolean imageAttached, Bitmap bitmap) {
        if (DuzooActivity.isNetworkAvailable()) {
            if (dialog != null) {
                dialog.show();
                dialog.setCancelable(false);
            }
            final ParseObject post = new ParseObject("Post");
            post.put(DuzooConstants.PARSE_POST_CONTENT, content);
            post.put(DuzooConstants.PARSE_POST_USER_NAME,
                    DuzooPreferenceManager.getKey(DuzooConstants.KEY_USER_NAME));
            post.put(DuzooConstants.PARSE_POST_USER_IMAGE,
                    DuzooPreferenceManager.getKey(DuzooConstants.KEY_USER_IMAGE));
            post.put(DuzooConstants.PARSE_POST_UPVOTES, 0);
            post.put(DuzooConstants.PARSE_POST_DOWNVOTES, 0);
            post.put(DuzooConstants.PARSE_POST_IS_FLAGGED, false);
            post.put(DuzooConstants.PARSE_POST_FACEBOOK_ID,
                    DuzooPreferenceManager.getKey(DuzooConstants.KEY_FACEBOOK_ID));
            post.put(DuzooConstants.PARSE_POST_INTEREST_TYPE,
                    DuzooPreferenceManager.getIntKey(DuzooConstants.KEY_INTEREST_TYPE));
            post.put(DuzooConstants.PARSE_POST_TIMESTAMP, System.currentTimeMillis());
            post.put(DuzooConstants.PARSE_POST_COMMENT_COUNT, 0);
            post.put(DuzooConstants.PARSE_POST_HAS_MEDIA, imageAttached);
            post.put(DuzooConstants.PARSE_POST_FAVORITE, false);
            post.put(DuzooConstants.PARSE_POST_MY_VOTE, 0);
            post.put(DuzooConstants.PARSE_POST_DELETED, false);
            if (imageAttached) {
                String name = path.substring(path.lastIndexOf("/"));
                if (path.endsWith("gif") || path.endsWith("GIF"))
                    gif = true;
                else
                    gif = false;
                if (gif) {
                    File file = new File(path);
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    try {
                        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));

                        int read;
                        byte[] buff = new byte[1024];
                        while ((read = in.read(buff)) > 0) {
                            out.write(buff, 0, read);
                        }
                        out.flush();
                        byte[] bytes = out.toByteArray();

                        image = new ParseFile(name, bytes);
                    } catch (FileNotFoundException ex) {
                    } catch (IOException ex) {
                    }

                } else
                    image = new ParseFile(name, Util.convertBitmapToBytes(bitmap));
                image.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            post.put(DuzooConstants.PARSE_POST_IMAGE, image);
                            post.pinInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (dialog.isShowing())
                                        dialog.dismiss();
                                    returnToHomeActivity();
                                }
                            });
                            post.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    returnToHomeActivity();
                                }
                            });
                        }
                    }
                });
            } else {
                post.pinInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (dialog.isShowing())
                            dialog.dismiss();
                        returnToHomeActivity();
                    }
                });
                post.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        returnToHomeActivity();
                    }
                });
            }
        } else
            Toast.makeText(this, "Sorry, no internet connection available", Toast.LENGTH_SHORT).show();
    }

    private void returnToHomeActivity() {
        Intent intent = new Intent(this, HomeViewPagerActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //       super.onSaveInstanceState(outState);
    }
}
