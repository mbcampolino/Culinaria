package marcoscampos.culinaria.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import marcoscampos.culinaria.R;
import marcoscampos.culinaria.interfaces.OnIngredientClick;
import marcoscampos.culinaria.pojos.Ingredient;

/**
 * Created by Marcos on 29/10/2017.
 */

public class IngredientsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Ingredient> ingredients;
    Context context;
    OnIngredientClick recyclerClick;

    public IngredientsAdapter(List<Ingredient> ingredients, Context c, OnIngredientClick recyclerClick) {
        this.ingredients = ingredients;
        this.context = c;
        this.recyclerClick = recyclerClick;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_ingredient, parent, false);
        return new ItemHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemHolder) {
            ItemHolder item = (ItemHolder) holder;
            Ingredient recipe = ingredients.get(position);
            item.title.setText(String.format("%s %s of the %s ", recipe.getQuantity(), recipe.getMeasure(), recipe.getIngredient()));
        }
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;

        public ItemHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.item_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recyclerClick.onIngredientClick(ingredients.get(getAdapterPosition()));
        }
    }

}
