package marcoscampos.culinaria.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Marcos on 10/10/2017.
 */

public class ReciperDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "recipe.db";
    private static final int DATABASE_VERSION = 6;

    public ReciperDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE = "CREATE TABLE " + ReciperContract.ReciperEntry.TABLE_NAME + " (" +
                ReciperContract.ReciperEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ReciperContract.ReciperEntry.COLUMN_ID + " INTEGER NOT NULL, " +
                ReciperContract.ReciperEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                ReciperContract.ReciperEntry.COLUMN_SERVINGS + " INTEGER NOT NULL, " +
                ReciperContract.ReciperEntry.COLUMN_INGREDIENTS + " TEXT NOT NULL, " +
                ReciperContract.ReciperEntry.COLUMN_IMAGE + " TEXT)";

        db.execSQL(SQL_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ReciperContract.ReciperEntry.TABLE_NAME);
        onCreate(db);
    }
}
