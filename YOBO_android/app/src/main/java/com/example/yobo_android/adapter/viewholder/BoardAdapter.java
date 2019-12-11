package com.example.yobo_android.adapter.viewholder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.yobo_android.R;
import com.example.yobo_android.activity.MainActivity;
import com.example.yobo_android.activity.RecipeMainActivity;
import com.example.yobo_android.api.RetrofitClient;
import com.example.yobo_android.etc.Recipe;
import com.squareup.picasso.Picasso;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class  BoardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    static final String ImageURL = "http://45.119.146.82:8081/yobo/recipe/getImage/?filePath=";
    private static final int TYPE_ITEM = 1;
    private ArrayList<Recipe> listRecipe = new ArrayList<>();
    private Context context;

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        private String recipeId;
        private int recipeDescriptionNum;
        private TextView recipeName;
        private TextView recipeSubContents;
        private TextView recipeWriter;
        private TextView recipeScore;
        private ImageView recipeImage;

        ItemViewHolder(View itemView) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.recipeName);
            recipeSubContents = itemView.findViewById(R.id.recipeSubContents);
            recipeWriter = itemView.findViewById(R.id.recipeWriter);
            recipeScore = itemView.findViewById(R.id.recipeScore);
            recipeImage = itemView.findViewById(R.id.recipeImage);
        }

        void onBind(Recipe recipe, int position) {
            String temp = recipe.getCooking_description().get(0).getImage();
            temp = temp.replace("/", "%2F");
            String sum = ImageURL + temp;
            try {
                URL url = new URL(sum);
                Picasso.get().load(url.toString()).into(recipeImage);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            recipeId = recipe.get_id();
            recipeDescriptionNum = recipe.getCooking_description().size();
            recipeName.setText(recipe.getRecipe_name());
            recipeSubContents.setText(recipe.getCooking_description().get(0).getDescription());
            recipeWriter.setText(recipe.getWriter_name());
            recipeScore.setText(""+Math.round(recipe.getRating()*10)/10.0);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition() ;
            if (pos != RecyclerView.NO_POSITION) {
                Intent intent = new Intent(context, RecipeMainActivity.class);
                intent.putExtra("recipeId",recipeId);
                ((Activity)context).startActivityForResult(intent,RecipeMainActivity.REQUEST_MODIFY1);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            int pos = getAdapterPosition() ;
            if (pos != RecyclerView.NO_POSITION) {
                if(listRecipe.get(pos).getWriter_id().equals(MainActivity.u_id)){
                    showDeleteAlertDialog(v,pos);
                }
            }
            return false;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        RecyclerView.ViewHolder holder;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board, parent, false);
        holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        itemViewHolder.onBind(listRecipe.get(position), position);
    }

    @Override
    public int getItemViewType(int position){
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return listRecipe.size();
    }

    public void addItem(Recipe recipe, int position) {
        listRecipe.add(recipe);
        notifyItemChanged(position);
    }

    public void deleteItem(int position){
        listRecipe.remove(position);
        notifyItemChanged(position);
    }

    private void showDeleteAlertDialog(View view, final int pos){
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
        builder.setIcon(R.drawable.ic_error_outline_24dp);
        builder.setTitle("레시피 삭제");
        builder.setMessage("레시피를 삭제 하시겠습니까?");
        builder.setPositiveButton("삭제",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                            deleteInServer(listRecipe.get(pos).get_id());
                            listRecipe.remove(pos);
                        notifyDataSetChanged();
                    }
                });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    }

    private void deleteInServer(String Did){
        Call<ResponseBody> call = RetrofitClient.getInstance().getApiService().deleteRecipeByDid(Did);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

}
