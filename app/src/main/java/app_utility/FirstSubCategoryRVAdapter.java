package app_utility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.autochip.trufrost.HomeScreenActivity;
import com.autochip.trufrost.R;

import java.util.ArrayList;

public class FirstSubCategoryRVAdapter extends RecyclerView.Adapter<FirstSubCategoryRVAdapter.SubCategoryItemTabHolder> {

    RecyclerView recyclerView;
    Context context;
    ArrayList<String> alFirstSubCategoryNames;

    public FirstSubCategoryRVAdapter(Context context, RecyclerView recyclerView,ArrayList<String> alFirstSubCategoryNames){
        this.context = context;
        this.recyclerView = recyclerView;
        this.alFirstSubCategoryNames = alFirstSubCategoryNames;
    }

    @NonNull
    @Override
    public SubCategoryItemTabHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_first_sub_category, parent, false);

        return new FirstSubCategoryRVAdapter.SubCategoryItemTabHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SubCategoryItemTabHolder holder, final int position) {
        holder.ivCategoryImage.setImageDrawable(context.getResources().getDrawable(R.drawable.commercial_kitchen));
        holder.tvCategoryName.setText(alFirstSubCategoryNames.get(position));

        holder.ivCategoryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeScreenActivity.onFragmentInteractionListener.onFragmentMessage("OPEN_SUB_CATEGORY_FRAGMENT_NEW", position, "", holder.tvCategoryName.getText().toString());
            }
        });

        holder.tvCategoryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeScreenActivity.onFragmentInteractionListener.onFragmentMessage("OPEN_SUB_CATEGORY_FRAGMENT_NEW", position, "", holder.tvCategoryName.getText().toString());
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
