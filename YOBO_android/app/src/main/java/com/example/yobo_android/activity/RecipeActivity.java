package com.example.yobo_android.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import com.example.yobo_android.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RecipeActivity extends AppCompatActivity {

    private ImageView recipeImage;
    private TextView recipeOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        //String html;
        //txtview = (TextView)findViewById(R.id.txtView);
        //html = DownloadHtml("http://www.google.co.kr/");
        //txtview.setText(html);

        recipeImage = (ImageView)findViewById(R.id.recipeImage);
        recipeOrder = (TextView)findViewById(R.id.recipeOrder);
        recipeOrder.setMovementMethod(new ScrollingMovementMethod());
    }


    private String DownloadHtml(String addr) {
        StringBuilder html = new StringBuilder();
        try {
            //인터넷상의 자원이나 서비스 주소값을 URL 객체로 생성합니다.
            URL url = new URL(addr);

            //해당 UTL로 접속합니다.
            //접속에 성공하면 양방향 통신이 가능한 연결 객체(HttpURLConnection)가 리턴됩니다.
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if( conn != null ) {
                //연결 제한 시간을 1/1000 초 단위로 지정합니다.
                //0이면 무한 대기입니다.
                conn.setConnectTimeout(10000);

                //읽기 제한 시간을 지정합니다. 0이면 무한 대기합니다.
                //conn.setReadTimeout(0);

                //캐쉬 사용여부를 지정합니다.
                conn.setUseCaches(false);

                //http 연결의 경우 요청방식을 지정할수 있습니다.
                //지정하지 않으면 디폴트인 GET 방식이 적용됩니다.
                //conn.setRequestMethod("GET" | "POST");

                //서버에 요청을 보내가 응답 결과를 받아옵니다.
                int resCode = conn.getResponseCode();

                //요청이 정상적으로 전달되엇으면 HTTP_OK(200)이 리턴됩니다.
                //URL이 발견되지 않으면 HTTP_NOT_FOUND(404)가 리턴됩니다.
                //인증에 실패하면 HTTP_UNAUTHORIZED(401)가 리턴됩니다.
                if( resCode == HttpURLConnection.HTTP_OK ) {

                    //요청에 성공했으면 getInputStream 메서드로 입력 스트림을 얻어 서버로부터 전송된 결과를 읽습니다.
                    InputStreamReader isr = new InputStreamReader(conn.getInputStream());

                    //스트림을 직접읽으면 느리고 비효율 적이므로 버퍼를 지원하는 BufferedReader 객체를 사용합니다.
                    BufferedReader br = new BufferedReader(isr);
                    for(;;) {
                        String line  = br.readLine();
                        if( line == null ) break;
                        html.append(line + "\n");
                    }
                    br.close();
                }
            }
            conn.disconnect();
        } catch(Exception e) {}
        //html 이 리턴값
        return html.toString();
    }


}
