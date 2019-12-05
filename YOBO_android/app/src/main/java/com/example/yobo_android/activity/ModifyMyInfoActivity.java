package com.example.yobo_android.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.yobo_android.R;
import com.example.yobo_android.api.ApiService;
import com.example.yobo_android.etc.Recipe;
import com.example.yobo_android.etc.UserData;
import com.ipaulpro.afilechooser.utils.FileUtils;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ModifyMyInfoActivity extends AppCompatActivity {

    private static final int PICK_FROM_ALBUM = 1000;
    private static final int PICK_FROM_ALBUM2 = 2000;

    UserData userData;
    Uri userPicture;
    ArrayList<String> userAddress = new ArrayList<>();
    ArrayList<String> userInterestCategory = new ArrayList<>();

    ImageView mEdieUserPicture;
    EditText mEditUserName;
    EditText mEditUserAddress1;
    EditText mEditUserAddress2;
    Spinner mUserFavorite1;
    Spinner mUserFavorite2;
    Spinner mUserFavorite3;

    Button btnModify;
    Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_my_info);

        mEdieUserPicture = findViewById(R.id.edif_userPicture);
        mEditUserName = findViewById(R.id.edit_userName);
        mEditUserAddress1 = findViewById(R.id.edit_userAddress1);
        mEditUserAddress2 = findViewById(R.id.edit_userAddress2);
        mUserFavorite1 = findViewById(R.id.userFavorite1);
        mUserFavorite2 = findViewById(R.id.userFavorite2);
        mUserFavorite3 = findViewById(R.id.userFavorite3);
        btnModify = findViewById(R.id.btnModify);
        btnCancel = findViewById(R.id.btnCancel);

        OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        okhttpClientBuilder.addInterceptor(logging);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okhttpClientBuilder.build())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<UserData> call = apiService.getbyDid(MainActivity.u_id);
        if (call != null) {
            call.enqueue(new Callback<UserData>() {
                @Override
                public void onResponse(Call<UserData> call, Response<UserData> response) {
                    userData = response.body();
                    mEditUserName.setText(userData.getUser_name());
                    mEditUserAddress1.setText(userData.getUser_address().get(0));
                    mEditUserAddress2.setText(userData.getUser_address().get(1));
                    if(userData.getInterest_category().size() > 0)
                        mUserFavorite1.setSelection(getSpinnerIndex(mUserFavorite1, userData.getInterest_category().get(0)));
                    if(userData.getInterest_category().size() > 1)
                        mUserFavorite2.setSelection(getSpinnerIndex(mUserFavorite2, userData.getInterest_category().get(1)));
                    if(userData.getInterest_category().size() > 2)
                        mUserFavorite3.setSelection(getSpinnerIndex(mUserFavorite3, userData.getInterest_category().get(2)));
                }
                @Override
                public void onFailure(Call<UserData> call, Throwable t) {
                    Toast.makeText(getApplicationContext(),"asd",Toast.LENGTH_SHORT).show();
                }
            });
        }

        mEdieUserPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,PICK_FROM_ALBUM);
            }
        });

        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userAddress.add(mEditUserAddress1.getText().toString());
                userAddress.add(mEditUserAddress2.getText().toString());

                userInterestCategory.add(mUserFavorite1.getSelectedItem().toString());
                userInterestCategory.add(mUserFavorite2.getSelectedItem().toString());
                userInterestCategory.add(mUserFavorite3.getSelectedItem().toString());

                userData.setUser_name(mEditUserName.getText().toString());
                userData.setUser_address(userAddress);
                userData.setInterest_category(userInterestCategory);

                OkHttpClient.Builder okhttpClientBuilder2 = new OkHttpClient.Builder();
                HttpLoggingInterceptor logging2 = new HttpLoggingInterceptor();
                logging2.setLevel(HttpLoggingInterceptor.Level.BODY);
                okhttpClientBuilder2.addInterceptor(logging2);
                Retrofit retrofit2 = new Retrofit.Builder()
                        .baseUrl(ApiService.API_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(okhttpClientBuilder2.build())
                        .build();
                ApiService apiService2 = retrofit2.create(ApiService.class);
                Call<ResponseBody> call2 = null;

                if(userPicture == null){ // image 수정 X
                    call2 = apiService2.updateUserWithoutImage(null, userData);
                }
                else { // image 수정
                    MultipartBody.Part image  = prepareFileParts("image", userPicture);
                    call2 = apiService2.updateUserWithImage(image, userData);
                }

                if (call2 != null) {
                    call2.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call2, Response<ResponseBody> response) {
                            Toast.makeText(ModifyMyInfoActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        @Override
                        public void onFailure(Call<ResponseBody> call2, Throwable t) {
                            Toast.makeText(getApplicationContext(),"asd",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModifyMyInfoActivity.super.onBackPressed();
            }
        });
    }

    private int getSpinnerIndex(Spinner spinner, String myString){
        int index = 0;
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
                return index;
            }
        }
        return index;
    }

    @NonNull
    private MultipartBody.Part prepareFileParts(String partName, Uri fileUri){
        File file = FileUtils.getFile(this,fileUri);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse(getContentResolver().getType(fileUri)), file);
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    private boolean pictureSelected(int resultCode, @Nullable Intent data) {
        return resultCode == RESULT_OK && data != null;
    }

    private float getSampleRatio(int width, int height) {
        //상한
        final int targetWidth = 1280;
        final int targetHeight = 1280;

        float ratio;

        if(width > height){
            //Landscape
            if(width > targetWidth)
                ratio = (float)width / (float)targetWidth;
            else
                ratio = 1f;
        }
        else{
            //Portrait
            if(height > targetHeight)
                ratio=(float)height/(float)targetHeight;
            else
                ratio = 1f;
        }

        return Math.round(ratio);
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {

        ParcelFileDescriptor parcelFileDescriptor = getApplicationContext().getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();

        BitmapFactory.Options opts = new BitmapFactory.Options();
        //세부 정보 말고 크기 정보만 갖고 온다
        opts.inJustDecodeBounds = true;

        // ex) 4096 * 3800
        int width = opts.outWidth;
        int height=opts.outHeight;

        float sampleRatio = getSampleRatio(width, height);

        opts.inJustDecodeBounds=false;
        opts.inSampleSize=(int)sampleRatio;

        Bitmap resizedBitmap=BitmapFactory.decodeFileDescriptor(fileDescriptor, null, opts);
        Log.d("Resizing", "Resized Width / Height" + resizedBitmap.getWidth() + "/" + resizedBitmap.getHeight());
        parcelFileDescriptor.close();
        return resizedBitmap;
    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(),
                inImage, "ResizeImage", null);
        return Uri.parse(path);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(pictureSelected(resultCode,data)){
            Uri imageUri = null;
            try {
                Bitmap resize = getBitmapFromUri(data.getData());
                imageUri = getImageUri(getApplicationContext(), resize);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(requestCode == PICK_FROM_ALBUM){
                userPicture = imageUri;
                Picasso.get().load(imageUri).into(mEdieUserPicture);
            }
        }
    }
}
