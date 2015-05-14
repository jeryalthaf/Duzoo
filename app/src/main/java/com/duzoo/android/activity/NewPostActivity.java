package com.duzoo.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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

import com.duzoo.android.R;
import com.duzoo.android.application.MyApplication;
import com.duzoo.android.application.UIController;
import com.duzoo.android.datasource.ParseLink;
import com.duzoo.android.util.DuzooConstants;
import com.duzoo.android.util.Util;

/**
 * Created by RRaju on 4/12/2015.
 */
public class NewPostActivity extends ActionBarActivity {

    EditText mPostContent;
    TextView mNewPostAdd;
    ImageView mPostImage;
    boolean imageAttached = false;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_new_post));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("New Post");
        mPostContent = (EditText) findViewById(R.id.new_post_box);
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
                onBackPressed();
                ParseLink.createPost(content, imageAttached, bitmap);
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
        }
        else if(item.getItemId() == android.R.id.home) {
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
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String path = cursor.getString(columnIndex);
                    cursor.close();

                    mPostImage.setVisibility(View.VISIBLE);
                    bitmap = BitmapFactory.decodeFile(path);
                    imageAttached = true;
                    mPostImage.setImageBitmap(bitmap);

                }
        }
    }
}
