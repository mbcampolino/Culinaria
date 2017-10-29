package marcoscampos.culinaria;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity  {

    private static final String URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json" ;

    @ViewById(R.id.title_toolbar)
    TextView titleToolbar;
    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @AfterViews
    public void afterViews() {
        prepareToolbar();
    }

    private void prepareToolbar() {
        setSupportActionBar(toolbar);
    }
}
