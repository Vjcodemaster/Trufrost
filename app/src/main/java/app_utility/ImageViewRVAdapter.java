package app_utility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.autochip.trufrost.HomeScreenActivity;
import com.autochip.trufrost.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class ImageViewRVAdapter extends RecyclerView.Adapter<ImageViewRVAdapter.MenuItemTabHolder> {

    private Context context;
    private RecyclerView recyclerView;
    TextView tvPrevious;
    private FragmentManager fragmentManager;
    private String sMainMenuName;

    public ImageViewRVAdapter(Context context, RecyclerView recyclerView, FragmentManager fragmentManager) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public MenuItemTabHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_grid_layout, parent, false);

        return new MenuItemTabHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MenuItemTabHolder holder, final int position) {

        switch (position) {
            case 0:
                sMainMenuName = "Commercial Kitchen";
                holder.tvProductName.setText(sMainMenuName);
                holder.ivProducts.setImageResource(R.drawable.commercial_kitchen);
                break;
            case 1:
                sMainMenuName = "Bar & Pubs";
                holder.ivProducts.setImageResource(R.drawable.bars_pubs);
                holder.tvProductName.setText(sMainMenuName);
                break;
            case 2:
                sMainMenuName = "Cake & Sweet shops";
                holder.ivProducts.setImageResource(R.drawable.cake_sweet);
                holder.tvProductName.setText(sMainMenuName);
                break;
            case 3:
                sMainMenuName = "Food Retail";
                holder.ivProducts.setImageResource(R.drawable.food_retail);
                holder.tvProductName.setText(sMainMenuName);
                break;
            case 4:
                sMainMenuName = "Food Preservation";
                holder.ivProducts.setImageResource(R.drawable.cold_storage);
                holder.tvProductName.setText(sMainMenuName);
                break;
            case 5:
                sMainMenuName = "Bio Medical";
                holder.ivProducts.setImageResource(R.drawable.bio_medical);
                holder.tvProductName.setText(sMainMenuName);
                break;
        }

        holder.ivProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //openFragment();
                switch (position){
                    case 0:
                        sMainMenuName = "Commercial Kitchen";
                        break;
                    case 1:
                        sMainMenuName = "Bar & Pubs";
                        break;
                    case 2:
                        sMainMenuName = "Cake & Sweet shops";
                        break;
                    case 3:
                        sMainMenuName = "Food Retail";
                        break;
                    case 4:
                        sMainMenuName = "Food Preservation";
                        break;
                    case 5:
                        sMainMenuName = "Bio Medical";
                        break;
                }
                HomeScreenActivity.onFragmentInteractionListener.onFragmentMessage("OPEN_SUB_CATEGORY_FRAGMENT", position, "", sMainMenuName);

            }
        });

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

    /*private void openFragment() {
        Fragment newFragment;
        FragmentTransaction transaction;
        //Bundle bundle = new Bundle();
        //bundle.putInt("index", 0);
        newFragment = AboutProductFragment.newInstance("", "");
        //newFragment.setArguments(bundle);

        //String sBackStackParent = newFragment.getClass().getName();
        transaction = fragmentManager.beginTransaction();
        //transaction.setCustomAnimations(R.anim.t2b, R.anim.b2t);
        transaction.replace(R.id.fl_menu, newFragment, null);
        transaction.addToBackStack(null);
        transaction.commit();
    }*/
    @Override
    public int getItemCount() {
        return 6; //alBeaconInfo.size()
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