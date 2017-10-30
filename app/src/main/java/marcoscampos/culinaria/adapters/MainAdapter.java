package marcoscampos.culinaria.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import marcoscampos.culinaria.R;
import marcoscampos.culinaria.pojos.PageResult;
import marcoscampos.culinaria.utils.ImageFilePath;

/**
 * Created by Marcos on 29/10/2017.
 */

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<PageResult> pagerAdapter;
    Context context;

    public MainAdapter(List<PageResult> pagerAdapter, Context c) {
        this.pagerAdapter = pagerAdapter;
        this.context = c;
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

        }
    }

}
