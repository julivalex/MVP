package mvp;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import common.User;
import common.UserTable;
import database.DbHelper;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class UsersModel {

    private final DbHelper dbHelper;

    UsersModel(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void loadUsers(LoadUserCallback callback) {

        Observable.fromCallable(() -> {
            List<User> users = new LinkedList<>();
            Cursor cursor = dbHelper.getReadableDatabase().query(UserTable.TABLE,
                    null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                User user = new User();
                user.setId(cursor.getLong(cursor.getColumnIndex(UserTable.COLUMN.ID)));
                user.setName(cursor.getString(cursor.getColumnIndex(UserTable.COLUMN.NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(UserTable.COLUMN.EMAIL)));
                users.add(user);
            }
            cursor.close();
            return users;
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(users -> {
                    if (callback != null) {
                        callback.onLoad(users);
                    }
                });
    }

    public void addUser(ContentValues contentValues, CompleteCallback callback) {

        Observable.fromCallable(() -> {
            dbHelper.getWritableDatabase().insert(UserTable.TABLE, null, contentValues);

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (callback != null) {
                        callback.onComplete();
                    }
                });
    }

    public void clearUsers(CompleteCallback callback) {

        Observable.fromCallable(() -> {
            dbHelper.getWritableDatabase().delete(UserTable.TABLE, null, null);

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (callback != null) {
                        callback.onComplete();
                    }
                });
    }

    interface LoadUserCallback {
        void onLoad(List<User> users);
    }

    interface CompleteCallback {
        void onComplete();
    }
}
