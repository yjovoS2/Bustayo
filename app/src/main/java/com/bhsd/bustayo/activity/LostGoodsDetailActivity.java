package com.bhsd.bustayo.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bhsd.bustayo.R;
import com.bhsd.bustayo.dto.LostGoodsDetailInfo;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class LostGoodsDetailActivity extends AppCompatActivity {

    ImageView goodsImage;
    TextView lostTitle;
    TextView controlNumber;
    TextView lostTime;
    TextView lostLocation;
    TextView category;
    TextView lostState;
    TextView jurisdiction;
    TextView contact;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_goods_detail);

        LostGoodsDetailInfo info = (LostGoodsDetailInfo) getIntent().getSerializableExtra("goods_info");

        goodsImage = findViewById(R.id.lost_image);
        lostTitle = findViewById(R.id.lost_title);
        controlNumber = findViewById(R.id.control_number);
        lostTime = findViewById(R.id.lost_time);
        lostLocation = findViewById(R.id.lost_location);
        category = findViewById(R.id.category);
        lostState = findViewById(R.id.lost_state);
        jurisdiction = findViewById(R.id.jurisdiction);
        contact = findViewById(R.id.contact);

        findViewById(R.id.searchGoBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setTitle(info.getLgTitle());
        setViews(info);
    }

    void setToolbar(String title) {
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setTitle(title);
    }

    void setViews(LostGoodsDetailInfo info) {
        String imageURL = info.getLgImage();
        if(imageURL.equals("")) {
            goodsImage.setImageResource(R.drawable.ic_no_image);
        } else {
            new DownloadFilesTask(goodsImage).execute(imageURL);
        }
        lostTitle.setText(info.getLgTitle());
        controlNumber.setText(info.getLgNum());
        lostTime.setText(info.getLgTime());
        lostLocation.setText(info.getLgPlace());
        category.setText(info.getLgCategory());
        lostState.setText(info.getLgState());
        jurisdiction.setText(info.getLgJurisdiction());
        contact.setText(info.getLgContact());
    }

    private class DownloadFilesTask extends AsyncTask<String,Void, Bitmap> {
        ImageView image;
        DownloadFilesTask(ImageView image) {
            this.image = image;
        }
        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bmp = null;
            try {
                String img_url = strings[0]; //url of the image
                URL url = new URL(img_url);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bmp;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(Bitmap result) {
            // doInBackground 에서 받아온 total 값 사용 장소
            image.setImageBitmap(result);
        }
    }
}
