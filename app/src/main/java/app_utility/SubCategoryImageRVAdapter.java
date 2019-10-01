package app_utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.autochip.trufrost.HomeScreenActivity;
import com.autochip.trufrost.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SubCategoryImageRVAdapter extends RecyclerView.Adapter<SubCategoryImageRVAdapter.MenuItemTabHolder> {

    private Context context;
    private RecyclerView recyclerView;
    TextView tvPrevious;
    private FragmentManager fragmentManager;
    private String sMainMenuName;
    ArrayList<String> alSubCategoryNames;
    ArrayList<String> alSubCategoryImagePath;

    public SubCategoryImageRVAdapter(Context context, RecyclerView recyclerView, ArrayList<String> alSubCategoryNames,
                                     ArrayList<String> alSubCategoryImagePath) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.alSubCategoryNames = alSubCategoryNames;
        this.alSubCategoryImagePath = alSubCategoryImagePath;
    }

    @NonNull
    @Override
    public MenuItemTabHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_grid_layout, parent, false);

        return new MenuItemTabHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MenuItemTabHolder holder, final int position) {

        holder.tvProductName.setText(alSubCategoryNames.get(position));

        if(alSubCategoryImagePath!=null && alSubCategoryImagePath.size()>position) {
            Uri imageUri = Uri.fromFile(new File(alSubCategoryImagePath.get(position)));
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
                Bitmap bmp = BitmapScaler.scaleToFitWidth(bitmap, 250) ;
                holder.ivProducts.setImageBitmap(bmp);
            } catch (IOException e) {
                e.printStackTrace();
            }


            /*BitmapFactory.Options options = new BitmapFactory.Options();
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
            ViewGroup.LayoutParams params = holder.ivProducts.getLayoutParams();
            params.height = height;
            params.width = width;

            holder.ivProducts.setLayoutParams(params);*/

            //holder.ivProducts.setImageURI(imageUri);
        }

        holder.ivProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeScreenActivity.onFragmentInteractionListener.onFragmentMessage("OPEN_PRODUCTS_FRAGMENT", position, "", holder.tvProductName.getText().toString());
            }
        });

        holder.tvProductName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeScreenActivity.onFragmentInteractionListener.onFragmentMessage("OPEN_PRODUCTS_FRAGMENT", position, "", holder.tvProductName.getText().toString());
            }
        });

    }

    @Override
    public int getItemCount() {
        return alSubCategoryNames.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class MenuItemTabHolder extends RecyclerView.ViewHolder {
        //TextView tvNumber;
        ImageView ivProducts;
        TextView tvProductName;

        MenuItemTabHolder(View itemView) {
            super(itemView);
            ivProducts = itemView.findViewById(R.id.iv_products);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            //tvNumber = itemView.findViewById(R.id.tv_number);
        }
    }

    /*public static class BitmapScaler{
        // Scale and maintain aspect ratio given a desired width
// BitmapScaler.scaleToFitWidth(bitmap, 100);
        public static Bitmap scaleToFitWidth(Bitmap b, int width)
        {
            float factor = width / (float) b.getWidth();
            return Bitmap.createScaledBitmap(b, width, (int) (b.getHeight() * factor), true);
        }


        // Scale and maintain aspect ratio given a desired height
        // BitmapScaler.scaleToFitHeight(bitmap, 100);
        public static Bitmap scaleToFitHeight(Bitmap b, int height)
        {
            float factor = height / (float) b.getHeight();
            return Bitmap.createScaledBitmap(b, (int) (b.getWidth() * factor), height, true);
        }
    }*/
}