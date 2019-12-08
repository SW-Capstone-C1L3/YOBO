package com.example.yobo_android.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.yobo_android.R;
import com.example.yobo_android.api.RetrofitClient;
import com.example.yobo_android.etc.UserData;
import com.ipaulpro.afilechooser.utils.FileUtils;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static android.media.ExifInterface.ORIENTATION_NORMAL;
import static android.media.ExifInterface.TAG_ORIENTATION;

public class MyPageActivity extends AppCompatActivity {

    private static final int PICK_FROM_ALBUM = 1000;

    UserData userData;
    Uri userPicture = null;
    ArrayList<String> userAddress = new ArrayList<>();
    ArrayList<String> userInterestCategory = new ArrayList<>();

    ImageView mEdieUserPicture;
    EditText mEditUserName;
    EditText mEditUserAddress1;
    EditText mEditUserAddress2;
    ArrayList<Spinner> mUserFavorites = new ArrayList<>();
    Boolean flagImageChange = false;
    String flagInfo;
    Button btnModify;
    Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        mEdieUserPicture = findViewById(R.id.edif_userPicture);
        mEditUserName = findViewById(R.id.edit_userName);
        mEditUserAddress1 = findViewById(R.id.edit_userAddress1);
        mEditUserAddress2 = findViewById(R.id.edit_userAddress2);
        mUserFavorites.add((Spinner)findViewById(R.id.userFavorite1));
        mUserFavorites.add((Spinner)findViewById(R.id.userFavorite2));
        mUserFavorites.add((Spinner)findViewById(R.id.userFavorite3));
        btnModify = findViewById(R.id.btnModify);
        btnCancel = findViewById(R.id.btnCancel);

        Call<UserData> call = RetrofitClient.getInstance().getApiService().getbyDid(MainActivity.u_id);
        if (call != null) {
            call.enqueue(new Callback<UserData>() {
                @Override
                public void onResponse(Call<UserData> call, Response<UserData> response) {
                    userData = response.body();
                    mEditUserName.setText(userData.getUser_name());
                    mEditUserAddress1.setText(userData.getUser_address().get(0));
                    mEditUserAddress2.setText(userData.getUser_address().get(1));
                    for(int i=0; i< userData.getInterest_category().size(); i++)
                        mUserFavorites.get(i).setSelection(getSpinnerIndex(mUserFavorites.get(i), userData.getInterest_category().get(i)));

                    if(userData.getImage() != null){
                        String temp =  userData.getImage();
                        temp = temp.replace("/", "%2F");
                        String sum = "http://45.119.146.82:8081/yobo/recipe/getImage/?filePath=" + temp;
                        Uri uri = Uri.parse(sum);
                        Picasso.get().load(uri).fit().centerInside().error(R.drawable.user).into(mEdieUserPicture);
                    }
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

                for(int i=0; i< mUserFavorites.size(); i++)
                    userInterestCategory.add(mUserFavorites.get(i).getSelectedItem().toString());

                userData.setUser_name(mEditUserName.getText().toString());
                userData.setUser_address(userAddress);
                userData.setInterest_category(userInterestCategory);

                MultipartBody.Part imagePart = null;
                Call<ResponseBody> call2 = null;

                if(userPicture == null){
                    userData.setImage("exit");
                    call2 = RetrofitClient.getInstance().getApiService().updateUser(null,userData);
                }
                else{
                    imagePart = prepareFileParts("image",userPicture);
                    userData.setImage("change");
                    call2 = RetrofitClient.getInstance().getApiService().updateUser(imagePart, userData);
                }

                if (call2 != null) {
                    call2.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call2, Response<ResponseBody> response) {
                            Toast.makeText(MyPageActivity.this, "Success", Toast.LENGTH_SHORT).show();

                            if (flagImageChange) {
                                Intent intent = new Intent();
                                intent.putExtra("myImageChange", true);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                            else
                                finish();
                        }
                        @Override
                        public void onFailure(Call<ResponseBody> call2, Throwable t) {
                            Toast.makeText(getApplicationContext(),"Fail",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyPageActivity.super.onBackPressed();
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
                RequestBody.create(file, MediaType.parse(getContentResolver().getType(fileUri)));
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

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
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
                flagImageChange = true;
            }
        }
    }
}
