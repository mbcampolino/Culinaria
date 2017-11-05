package marcoscampos.culinaria.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import marcoscampos.culinaria.R;
import marcoscampos.culinaria.interfaces.OnStepClick;
import marcoscampos.culinaria.pojos.Steps;

/**
 * Created by Marcos on 29/10/2017.
 */

public class StepsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Steps> steps;
    Context context;
    OnStepClick recyclerClick;

    public StepsAdapter(List<Steps> steps, Context c, OnStepClick recyclerClick) {
        this.steps = steps;
        this.context = c;
        this.recyclerClick = recyclerClick;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_step, parent, false);
        return new ItemHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemHolder) {
            ItemHolder item = (ItemHolder) holder;
            Steps recipe = steps.get(position);
            if (position !=0 ) {
                item.textStep.setText(String.format("Step %s", recipe.getId()));
            } else {
                item.textStep.setText("Intro");
            }

            if (!recipe.getShortDescription().isEmpty()) {
                item.textShortDescription.setText(recipe.getShortDescription());
            } else if (!recipe.getDescription().isEmpty()) {
                item.textShortDescription.setText(recipe.getDescription());
            } else {
                item.textShortDescription.setText("no description.");
            }
            if (!recipe.getThumbnailURL().isEmpty()) {
                Glide.with(context).load(recipe.getThumbnailURL()).thumbnail(0.1f).into(item.imageStep);
            } else if (!recipe.getVideoURL().isEmpty()) {
                Glide.with(context).load(recipe.getVideoURL()).thumbnail(0.1f).into(item.imageStep);
            } else {
                Glide.with(context).load(R.drawable.recipeplaceholder).thumbnail(0.1f).into(item.imageStep);
            }
        }
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textStep;
        TextView textShortDescription;
        ImageView imageStep;

        public ItemHolder(View itemView) {
            super(itemView);
            textStep = (TextView) itemView.findViewById(R.id.item_text_step);
            textShortDescription = (TextView) itemView.findViewById(R.id.item_short_description);
            imageStep = (ImageView) itemView.findViewById(R.id.image_step);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recyclerClick.onStepClick(getAdapterPosition());
        }
    }

}
