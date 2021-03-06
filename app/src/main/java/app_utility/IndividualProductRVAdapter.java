package app_utility;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.autochip.trufrost.HomeScreenActivity;
import com.autochip.trufrost.IndividualFragment;
import com.autochip.trufrost.R;

import java.io.File;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class IndividualProductRVAdapter extends RecyclerView.Adapter<IndividualProductRVAdapter.ProductItemTabHolder> {

    private Context context;
    private RecyclerView recyclerView;
    TextView tvPrevious;
    private FragmentManager fragmentManager;
    private String sMainMenuName;
    String sTag;
    String[] sImagesPath;
    ArrayList<DataBaseHelper> alDb;
    DatabaseHandler dbh;

    ArrayList<String> alName = new ArrayList<>();
    ArrayList<String> alDescription = new ArrayList<>();
    ArrayList<String> alImagesPath = new ArrayList<>();
    ArrayList<Integer> alDBID = new ArrayList<>();

    public IndividualProductRVAdapter(Context context, RecyclerView recyclerView, ArrayList<String> alImagesPath) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.alImagesPath = alImagesPath;
        /*dbh = new DatabaseHandler(context);
        alDb = new ArrayList<>(dbh.getAllProductsData1());
        for(int i=0; i<alDb.size(); i++){
            if(alDb.get(i).get_product_category_names().equals(sTag)){
                alName.add(alDb.get(i).get_individual_product_names());
                alDBID.add(alDb.get(i).get_id());
                alDescription.add(alDb.get(i).get_individual_product_description());
                alImagePath.add(alDb.get(i).get_individual_product_images_path());
            }
        }*/
    }

    @NonNull
    @Override
    public ProductItemTabHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_products_grid_layout, parent, false);

        return new ProductItemTabHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductItemTabHolder holder, final int position) {

        //holder.tvProductName.setText(alName.get(position));
        holder.tvProductName.setVisibility(View.GONE);

        Uri uri = Uri.fromFile(new File(alImagesPath.get(position)));

        /*BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(new File(uri.getPath()).getAbsolutePath(), options);
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

        holder.ivProducts.setImageURI(uri);

        holder.ivProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IndividualFragment.mListener.onFragmentMessage("IMAGE_CLICKED", position, "",alImagesPath.get(position));
            }
        });

        /*holder.ivProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeScreenActivity.onFragmentInteractionListener.onFragmentMessage("OPEN_INDIVIDUAL_FRAGMENT", alDBID.get(position), alName.get(position),alDescription.get(position));
            }
        });

        holder.tvProductName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeScreenActivity.onFragmentInteractionListener.onFragmentMessage("OPEN_INDIVIDUAL_FRAGMENT", alDBID.get(position), alName.get(position),alDescription.get(position));
            }
        });*/



       /* holder.tvNumber.setText(String.valueOf(position+1));

        holder.tvNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tvPrevious!=null)
                    tvPrevious.setBackgroundColor(context.getResources().getColor((R.color.colorPrimaryDark)));
                holder.tvNumber.setBackgroundColor(context.getResources().getColor((R.color.colorGold)));
                String sValue = holder.tvNumber.getText().toString();
                LoginActivity.onAsyncInterfaceListener.onResultReceived("NUMBER_RECEIVED", 1, sValue, null);
                tvPrevious = holder.tvNumber;
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return alImagesPath.size(); //alBeaconInfo.size()
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ProductItemTabHolder extends RecyclerView.ViewHolder {
        //TextView tvNumber;
        ImageView ivProducts;
        TextView tvProductName;

        ProductItemTabHolder(View itemView) {
            super(itemView);
            ivProducts = itemView.findViewById(R.id.iv_products);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            //tvNumber = itemView.findViewById(R.id.tv_number);
        }
    }

}