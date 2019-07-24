package app_utility;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.autochip.trufrost.HomeScreenActivity;
import com.autochip.trufrost.R;

import java.io.File;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

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
            holder.ivProducts.setImageURI(imageUri);
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

}