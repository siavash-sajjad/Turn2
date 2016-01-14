package ir.sadeghlabs.turn.management;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import ir.sadeghlabs.turn.entity.Turn;

/**
 * Created by Siavash on 11/9/2015.
 */
public class TurnManagement extends OrmLiteSqliteOpenHelper {
    private DatabaseManagement databaseManagement;
    private Context mContext;



    public TurnManagement(Context context, String databaseName, int databaseVersion) {
        super(context, databaseName, null, databaseVersion);
        this.mContext = context;
        databaseManagement = new DatabaseManagement(mContext);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }


/*    public List<Turn> getAllTemplateList() {
        try {
            List<Turn> templateList = new ArrayList<>();

            Dao<Turn, Integer> templateDao = getDao(Turn.class);

            QueryBuilder<Turn, Integer> templateQb = templateDao.queryBuilder();
            PreparedQuery<Turn> preparedQuery = templateQb.prepare();

            templateList = templateDao.query(preparedQuery);

            return templateList;
        } catch (SQLException e) {
            return null;
        }
    }*/

    public List<Turn> getTurnList() {
        try {
            List<Turn> templateList = new ArrayList<>();

            Dao<Turn, Integer> turnDao = getDao(Turn.class);

            QueryBuilder<Turn, Integer> turnQb = turnDao.queryBuilder();
            turnQb.where().eq("Status", 0);
            PreparedQuery<Turn> preparedQuery = turnQb.prepare();

            templateList = turnDao.query(preparedQuery);

            return templateList;
        } catch (SQLException e) {
            return null;
        }
    }

    public int saveTurn(Turn turn, boolean isUpdated) {
        try {
            Dao turnDao = getDao(Turn.class);
            if (isUpdated) {
                turnDao.update(turn);
            } else {
                turnDao.create(turn);
            }

            return turn.getTurn_Id();
        } catch (SQLException e) {
            return 0;
        }
    }

    public Turn findTurnById(int turnId) {
        try {
            Dao<Turn, Integer> turnDao = getDao(Turn.class);

            return turnDao.queryForId(turnId);
        } catch (SQLException e) {
            return null;
        }
    }

    public boolean deleteTurn(int turnId){
        try {
            Dao<Turn, Integer> turnDao = getDao(Turn.class);
            Turn turn = findTurnById(turnId);

            turnDao.delete(turn);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}
