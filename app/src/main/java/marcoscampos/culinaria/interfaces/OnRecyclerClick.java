package marcoscampos.culinaria.interfaces;

import marcoscampos.culinaria.pojos.PageResult;

/**
 * Created by Marcos on 31/10/2017.
 */

public interface OnRecyclerClick {

    void onItemClick(PageResult item);
    void onFavoriteClick(PageResult item, boolean isFavorite);
}
