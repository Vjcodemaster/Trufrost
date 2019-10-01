package app_utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.autochip.trufrost.HomeScreenActivity;
import com.autochip.trufrost.R;

import java.io.File;
import java.util.ArrayList;

public class FirstSubCategoryRVAdapter extends RecyclerView.Adapter<FirstSubCategoryRVAdapter.SubCategoryItemTabHolder> {

    RecyclerView recyclerView;
    Context context;
    ArrayList<String> alFirstSubCategoryNames;
    ArrayList<String> alFirstSCImagesPath;

    public FirstSubCategoryRVAdapter(Context context, RecyclerView recyclerView,ArrayList<String> alFirstSubCategoryNames, ArrayList<String> alFirstSCImagesPath){
        this.context = context;
        this.recyclerView = recyclerView;
        this.alFirstSubCategoryNames = alFirstSubCategoryNames;
        this.alFirstSCImagesPath = alFirstSCImagesPath;
    }

    @NonNull
    @Override
    public SubCategoryItemTabHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_first_sub_category, parent, false);

        return new FirstSubCategoryRVAdapter.SubCategoryItemTabHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SubCategoryItemTabHolder holder, final int position) {
        //holder.ivCategoryImage.setImageDrawable(context.getResources().getDrawable(R.drawable.commercial_kitchen));

        holder.tvCategoryName.setText(alFirstSubCategoryNames.get(position));
        if(alFirstSCImagesPath!=null && alFirstSCImagesPath.size()>position) {
            Uri imageUri = Uri.fromFile(new File(alFirstSCImagesPath.get(position)));
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(new File(imageUri.getPath()).getAbsolutePath(), options);
            int imageHeight = options.outHeight;
            int imageWidth = options.outWidth;
            int height;
            int width;
            if(imageHeight>imageWidth){
                height = (int) (250f);
                width = (int) (210f);
            } else {
                height = (int) (150f);
                width = (int) (250f);
            }
            ViewGroup.LayoutParams params = holder.ivCategoryImage.getLayoutParams();
            params.height = height;
            params.width = width;

            holder.ivCategoryImage.setLayoutParams(params);
            //int height = (int) (250f);
            //int width = (int) (250f);

            holder.ivCategoryImage.setImageURI(imageUri);
        }

        holder.ivCategoryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeScreenActivity.onFragmentInteractionListener.onFragmentMessage("OPEN_SUB_CATEGORY_FRAGMENT", position, "", holder.tvCategoryName.getText().toString());
            }
        });

        holder.tvCategoryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeScreenActivity.onFragmentInteractionListener.onFragmentMessage("OPEN_SUB_CATEGORY_FRAGMENT", position, "", holder.tvCategoryName.getText().toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return alFirstSubCategoryNames.size();
    }

    static class SubCategoryItemTabHolder extends RecyclerView.ViewHolder {
        //TextView tvNumber;
        ImageView ivCategoryImage;
        TextView tvCategoryName;

        SubCategoryItemTabHolder(View itemView) {
            super(itemView);
            ivCategoryImage = itemView.findViewById(R.id.iv_category_image);
            tvCategoryName = itemView.findViewById(R.id.tv_category_name);
            //tvNumber = itemView.findViewById(R.id.tv_number);
        }
    }
}
