package marcoscampos.culinaria.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import marcoscampos.culinaria.R;
import marcoscampos.culinaria.db.ReciperContract;
import marcoscampos.culinaria.interfaces.OnRecyclerClick;
import marcoscampos.culinaria.pojos.PageResult;

import static marcoscampos.culinaria.db.ReciperContract.ReciperEntry.COLUMN_INGREDIENTS;
import static marcoscampos.culinaria.utils.Utils.getThumbnailFromRecipe;

/**
 * Created by Marcos on 29/10/2017.
 */

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<PageResult> pagerAdapter;
    Context context;
    OnRecyclerClick recyclerClick;

    public MainAdapter(List<PageResult> pagerAdapter, Context c, OnRecyclerClick recyclerClick) {
        this.pagerAdapter = pagerAdapter;
        this.context = c;
        this.recyclerClick = recyclerClick;
        notifyDataSetChanged();
    }

    public void add(List<PageResult> recipe) {
        pagerAdapter.addAll(recipe);
        notifyDataSetChanged();
    }

    public void clear() {
        pagerAdapter.clear();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_main, parent, false);
        return new ItemHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemHolder) {
            ItemHolder item = (ItemHolder) holder;
            PageResult recipe = pagerAdapter.get(position);
            item.title.setText(recipe.getName());
            item.serving.setText(String.format("Serve %s pessoas ", recipe.getServings()));
            Glide.with(context).load(getThumbnailFromRecipe(recipe)).thumbnail(0.1f).into(item.imageMovie);
        }
    }

    @Override
    public int getItemCount() {
        return pagerAdapter.size();
    }


    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageMovie;
        TextView title;
        TextView serving;

        public ItemHolder(View itemView) {
            super(itemView);
            imageMovie = (ImageView) itemView.findViewById(R.id.image_receita);
            title = (TextView) itemView.findViewById(R.id.txtitle);
            serving = (TextView) itemView.findViewById(R.id.txrendiment);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recyclerClick.onItemClick(pagerAdapter.get(getAdapterPosition()));
        }
    }
}
