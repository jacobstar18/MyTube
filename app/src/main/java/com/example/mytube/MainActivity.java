package com.example.mytube;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainActivity extends YouTubeBaseActivity {
    YouTubePlayerView YouTubePlayer;
    private YouTubePlayerView youtubeViewer;
    Elements contents;
    Document doc = null;
    Button mDown;
    ImageView mImgTrans;
    Bitmap mBitmap;
    EditText editText;
    LinearLayout ll;
    TextView tv;

    List<String> lists = new ArrayList<>();
    YouTubePlayer.OnInitializedListener listener = new YouTubePlayer.OnInitializedListener() {
        @Override
        public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
            /* 초기화 성공하면 유튜브 동영상 ID를 전달하여 동영상 재생*/
            youTubePlayer.loadVideo("SGWohw86Xk8"); // 동영상 ID는 URL 상단의 마지막 부분이다.
        }

        @Override
        public void onInitializationFailure(com.google.android.youtube.player.YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        settingDex();
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

        }
        editText = findViewById(R.id.edit_text);
        Button playBtn = findViewById((R.id.playBtn));
        youtubeViewer = findViewById(R.id.youtubeViewer);

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* 파라미터1: API키
                 * 파라미터2: 리스너 객체 */
                youtubeViewer.initialize("AIzaSyB2-ytj3SHdTEmUrf2LTUuA0J6gynwCXJs", listener);
            }
        });


        Button search = findViewById((R.id.search));
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searching();

            }
        });


        // 사진 크롤링 해서 셋팅

        /*
        Button crawling = findViewById((R.id.crawling));
        crawling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bitmap imgBitmap = GetImageFromURL("https://ssl.pstatic.net/static/newsstand/up/2018/0917/nsd10411518.png");

                if (imgBitmap != null) {
                    ImageView imgView = (ImageView) findViewById(R.id.imageView);
                    imgView.setImageBitmap(imgBitmap);
                }

                imgBitmap = GetImageFromURL("https://ssl.pstatic.net/static/newsstand/up/2013/0222/nsd134836259.gif");


                String[] strArray = new String[5];
                strArray[0]="https://ssl.pstatic.net/static/newsstand/up/2013/0222/nsd13453152.gif";
                strArray[1]="https://ssl.pstatic.net/static/newsstand/up/2013/0222/nsd134537285.gif";
                strArray[2]="https://ssl.pstatic.net/static/newsstand/up/2014/0213/nsd133843266.gif";
                strArray[3]="https://ssl.pstatic.net/static/newsstand/up/2013/0819/nsd18281501.gif";
                strArray[4]="https://ssl.pstatic.net/static/newsstand/up/2014/0708/nsd105753545.gif";


                for (int k=0; k<strArray.length ; k++){
                    // XML에 있는 레이아웃을 가져온다.
                    LinearLayout l = (LinearLayout)findViewById(R.id.imageggView);
                    // 이미지뷰에 등록할 비트맵을 생성한다.
                    Bitmap bm = GetImageFromURL(strArray[k]);

                    // 매트릭스를 생성하고 초기화한다.
                    Matrix m = new Matrix();
                    m.postTranslate(50, 50);    // 이미지뷰에 등록할 비트맵의 초기위치.

                    // 이미지뷰를 생성하고 초기화한다.
                    ImageView v2 = new ImageView(getBaseContext());
                    v2.setLayoutParams(new LinearLayout.LayoutParams(300, 300));
                    v2.setScaleType(ImageView.ScaleType.MATRIX);
                    v2.setImageBitmap(bm);      // 이미지를 등록한다.
                    v2.setImageMatrix(m);       // 매트릭스를 이미지뷰에 적용한다.
                    v2.setAdjustViewBounds(true);

                    l.addView(v2);      // 레이아웃에 이미지뷰를 등록한다.
                }
            }
        });
    */
    }


    private Bitmap GetImageFromURL(String strImageURL) {
        Bitmap imgBitmap = null;

        try {
            URL url = new URL(strImageURL);
            URLConnection conn = url.openConnection();
            conn.connect();

            int nSize = conn.getContentLength();
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(), nSize);
            imgBitmap = BitmapFactory.decodeStream(bis);

            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    return imgBitmap;
    }



    private class ImageDownload extends AsyncTask<String, Void, Void> {
        private String fileName;
        private final String SAVE_FOLDER = "C:\\이미지다운로드";
        @Override protected Void doInBackground(String... params) {

            String savePath = Environment.getExternalStorageDirectory().toString() + SAVE_FOLDER;
            File dir = new File(savePath);
            if (!dir.exists()) {dir.mkdirs();

            }
            Date day = new Date();SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.KOREA);
            fileName = String.valueOf(sdf.format(day));
            String fileUrl = params[0];
            if (new File(savePath + "/" + fileName).exists() == false) { } else { }
            String localPath = savePath + "/" + fileName + ".jpg";
            try { URL imgUrl = new URL(fileUrl);
                HttpURLConnection conn = (HttpURLConnection)imgUrl.openConnection();
                int len = conn.getContentLength();
                byte[] tmpByte = new byte[len];
                InputStream is = conn.getInputStream(); File file = new File(localPath);
                FileOutputStream fos = new FileOutputStream(file); int read;
                for (;;) { read = is.read(tmpByte); if (read <= 0) { break; } fos.write(tmpByte, 0, read);
                } is.close(); fos.close(); conn.disconnect(); } catch (Exception e) { e.printStackTrace(); } return null;

        }


        }
    private void searching(){
        String editString = editText.getText().toString();
        final String Url_id = "https://m.youtube.com/results?search_query="+editString;

                try {
                    lists.clear();;
                    Document doc2 = Jsoup.connect(Url_id).get();
                    // div 이며 id 가 post-area
                    //Elements titles  = doc2.select("div.yt-lockup-content h3 a");
                    Elements titles  = doc2.select("img.style-scope yt-img-shadow");

                    for (Element e : titles){
                        System.out.println(e.toString());
                        String strTmp = e.toString();
                        int indexStr = strTmp.indexOf("href=");
                        int indexEnd = strTmp.indexOf("class=");
                        String href = strTmp.substring(indexStr+6,indexEnd-2);

                       // System.out.println("www.youtube.com"+href);

                        indexStr = strTmp.indexOf("dir=\"ltr\">");
                        indexEnd = strTmp.indexOf("</a>");
                        String title = strTmp.substring(indexStr+10,indexEnd);;

                        lists.add(title);

                    }



                }

                catch (IOException e) {
                    e.printStackTrace();
                }
                finally {

                    settingDex();
                }


    }
    public void settingDex(){
        for (int k = 0; k < lists.size(); k++) {
            ll = (LinearLayout) findViewById(R.id.ll);
            tv = new TextView(this);
            tv.setText(lists.get(k).toString());
            ll.addView(tv);
        }

    }



}