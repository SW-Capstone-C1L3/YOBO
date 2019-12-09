package com.example.yobo_android.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yobo_android.R;
import com.example.yobo_android.adapter.viewholder.BasketIngredientAdapter;
import com.example.yobo_android.api.RequestHttpURLConnection;
import com.example.yobo_android.api.RetrofitClient;
import com.example.yobo_android.etc.BasketLogData;
import com.example.yobo_android.etc.IngredientsBasketData;
import com.example.yobo_android.etc.ProductData;
import com.example.yobo_android.fragment.BottomSheetFragBasket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import kr.co.bootpay.Bootpay;
import kr.co.bootpay.BootpayAnalytics;
import kr.co.bootpay.enums.Method;
import kr.co.bootpay.enums.PG;
import kr.co.bootpay.enums.UX;
import kr.co.bootpay.listener.CancelListener;
import kr.co.bootpay.listener.CloseListener;
import kr.co.bootpay.listener.ConfirmListener;
import kr.co.bootpay.listener.DoneListener;
import kr.co.bootpay.listener.ErrorListener;
import kr.co.bootpay.listener.ReadyListener;
import kr.co.bootpay.model.BootExtra;
import kr.co.bootpay.model.BootUser;

public class BasketActivity extends AppCompatActivity{
    ArrayList<String> deleteList = new ArrayList<>();
    ArrayList<Integer> quantity = new ArrayList<>();
    ArrayList<ProductData> productDataList = new ArrayList<>();
    private SearchView mSearchview;
    private TextView mtoolbarTitle;
    private BasketIngredientAdapter adapter;
    private Button mbtnBuyAll;
    private Integer sum_all_price=0;
    private String user_id;
    Integer deletePos=0;
    Integer len;
    private int stuck = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        Toolbar toolbar = findViewById(R.id.toolbar_enroll_recipe);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled(false);

        mtoolbarTitle = findViewById(R.id.toolbar_title);
        mSearchview = findViewById(R.id.action_search);
        mbtnBuyAll = findViewById(R.id.btnBuyAll);
        mbtnBuyAll.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetFragBasket fragment = new BottomSheetFragBasket();
                fragment.show(getSupportFragmentManager(), "TAG");
            }
        });
        recyclerViewInit();
        new BasketActivity.RequestAsync().execute();
        BootpayAnalytics.init(this, "5dd65cad0627a800257a260e");
    }
    public void jsonParser(String json) {
        try {
            JSONObject temp = new JSONObject(json);
            JSONArray BasketIngredientList = temp.getJSONArray("basket");
            len = BasketIngredientList.length();
            Log.i("kkkkk size: ",String.valueOf(BasketIngredientList.length()));
            for (int i = 0; i < BasketIngredientList.length(); i++) {
                IngredientsBasketData basketitem = new IngredientsBasketData();
                JSONObject basket = BasketIngredientList.getJSONObject(i);
                basketitem.setIngredientDescription(basket.getString("product_description"));
                basketitem.setBasket_product_id(basket.getString("product_id"));
//                deleteList.add(basket.getString("product_id"));
                basketitem.setIngredientImage(basket.getString("product_image"));
                basketitem.setIngredientName(basket.getString("product_name"));
                basketitem.setIngredientPrice(basket.getInt("product_price"));
                basketitem.setBasket_qty(basket.getInt("qty"));
                basketitem.setProvided_company_id(basket.getString("company_id"));
//                quantity.add(basket.getInt("qty"));
                productDataList.add(new ProductData(basket.getString("product_id"),basket.getInt("qty"),basket.getString("company_id"),basket.getInt("product_price"),basket.getString("product_name")));
                sum_all_price += basket.getInt("qty") * basket.getInt("product_price");
                basketitem.setUser_id(MainActivity.u_id);
                adapter.addItem(basketitem);
            }
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String giveSum(){
        return String.valueOf(sum_all_price);
    }

    public void delete(String str,String name, int num, int cost){
        final String ing_name = name;
        HashMap<String,Object> hashMap = new HashMap<>();
        deletePos = num;

        user_id = MainActivity.u_id;
        hashMap.put("Product_id", str);
        hashMap.put("User_id",user_id);
        Call<ResponseBody> call = RetrofitClient.getInstance().getApiService().DeleteBasket(hashMap);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(BasketActivity.this,ing_name+" 삭제되었습니다",Toast.LENGTH_LONG).show();
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(BasketActivity.this,"실패",Toast.LENGTH_LONG).show();
            }
        });
        productDataList.remove(deletePos);
        adapter.removeItem(deletePos);
        sum_all_price-=cost;
    }

    public void buy(Integer total, final String destination){
        BootUser bootUser = new BootUser().setPhone("010-1234-5678");
        BootExtra bootExtra = new BootExtra().setQuotas(new int[] {0,2,3});

        Bootpay.init(getFragmentManager())
                .setApplicationId("5dd65cad0627a800257a260e") // 해당 프로젝트(안드로이드)의 application id 값
                .setPG(PG.KCP) // 결제할 PG 사
                .setMethod(Method.CARD) // 결제수단
                .setContext(this)
                .setBootUser(bootUser)
                .setBootExtra(bootExtra)
                .setUX(UX.PG_DIALOG)
//                .setUserPhone("010-1234-5678") // 구매자 전화번호
                .setName("재료") // 결제할 상품명
                .setOrderId("1234") // 결제 고유번호expire_month
                .setPrice(total) // 결제할 금액
                .addItem("마우's 스", 1, "ITEM_CODE_MOUSE", 100) // 주문정보에 담길 상품정보, 통계를 위해 사용
                .addItem("키보드", 1, "ITEM_CODE_KEYBOARD", 200, "패션", "여성상의", "블라우스") // 주문정보에 담길 상품정보, 통계를 위해 사용
                .onConfirm(new ConfirmListener() { // 결제가 진행되기 바로 직전 호출되는 함수로, 주로 재고처리 등의 로직이 수행
                    @Override
                    public void onConfirm(@Nullable String message) {
                        if (0 < stuck) Bootpay.confirm(message); // 재고가 있을 경우.
                        else Bootpay.removePaymentWindow(); // 재고가 없어 중간에 결제창을 닫고 싶을 경우
                        Log.d("confirm", message);
                    }
                })
                .onDone(new DoneListener() { // 결제완료시 호출, 아이템 지급 등 데이터 동기화 로직을 수행합니다
                    @Override
                    public void onDone(@Nullable String message) {
                        Log.d("done", message);
                        for(int i=0;i<productDataList.size();i++)
                            deleteAll(productDataList.get(i).getProduct_id(),user_id);
                        //destination 주소에다가 보내도록

                        final BasketLogData basketLogData = new BasketLogData(productDataList,sum_all_price,"배송 준비중",MainActivity.u_id,destination,MainActivity.u_email,MainActivity.u_phone,MainActivity.u_name);

                        Call<ResponseBody> call = RetrofitClient.getInstance().getApiService().createTransaction(basketLogData);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                Toast.makeText(BasketActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Log.e("ERROR", t.toString());
                                Log.e("ERROR", call.toString());
                            }
                        });
                        finish();
                    }
                })
                .onReady(new ReadyListener() { // 가상계좌 입금 계좌번호가 발급되면 호출되는 함수입니다.
                    @Override
                    public void onReady(@Nullable String message) {
                        Log.d("ready", message);
                    }
                })
                .onCancel(new CancelListener() { // 결제 취소시 호출
                    @Override
                    public void onCancel(@Nullable String message) {

                        Log.d("cancel", message);
                    }
                })
                .onError(new ErrorListener() { // 에러가 났을때 호출되는 부분
                    @Override
                    public void onError(@Nullable String message) {
                        Log.d("error", message);
                    }
                })
                .onClose(
                        new CloseListener() { //결제창이 닫힐때 실행되는 부분
                            @Override
                            public void onClose(String message) {
                                Log.d("close", "close");
                            }
                        })
                .request();
    }

    public void deleteAll(String p_id, String u_id){
        HashMap<String,Object> hashMap = new HashMap<>();

        user_id = MainActivity.u_id;
        hashMap.put("Product_id", p_id);
        hashMap.put("User_id",user_id);
        Call<ResponseBody> call = RetrofitClient.getInstance().getApiService().DeleteBasket(hashMap);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //Toast.makeText(BasketActivity.this,ing_name+" 삭제되었습니다",Toast.LENGTH_LONG).show();
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(BasketActivity.this,"실패",Toast.LENGTH_LONG).show();
            }
        });
    }

    public class RequestAsync extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                //GET Request
                /*************현재는 doc_id를 임의의 값으로 배정**********/
                /*************나중에 바꿔야됨***************/
                return RequestHttpURLConnection.sendGet("http://45.119.146.82:8081/yobo/basket/getBasket?User_id="+MainActivity.u_id);
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }
        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                jsonParser(s);
            }
        }
    }

    private void recyclerViewInit() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.recyclerShopIngredientView);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new BasketIngredientAdapter();
        recyclerView.setAdapter(adapter);
    }
}