package mvp;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.AsyncTask;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import common.User;
import common.UserTable;
import database.DbHelper;

public class UsersModel {

    private final DbHelper dbHelper;

    UsersModel(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void loadUsers(LoadUserCallback callback) {
        LoadUsersTask loadUsersTask = new LoadUsersTask(callback);
        loadUsersTask.execute();
    }

    public void addUser(ContentValues contentValues, CompleteCallback callback) {
        AddUserTask addUserTask = new AddUserTask(callback);
        addUserTask.execute(contentValues);
    }

    public void clearUsers(CompleteCallback callback) {
        ClearUserTask clearUserTask = new ClearUserTask(callback);
        clearUserTask.execute();
    }

    interface LoadUserCallback {
        void onLoad(List<User> users);
    }

    interface CompleteCallback {
        void onComplete();
    }

    class LoadUsersTask extends AsyncTask<Void, Void, List<User>> {

        private final LoadUserCallback callback;

        LoadUsersTask(LoadUserCallback callback) {
            this.callback = callback;
        }

        @Override
        protected List<User> doInBackground(Void... voids) {
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
        }

        @Override
        protected void onPostExecute(List<User> users) {
            if(callback != null) {
                callback.onLoad(users);
            }
        }
    }

    class AddUserTask extends AsyncTask<ContentValues, Void, Void> {

        private final CompleteCallback callback;

        AddUserTask(CompleteCallback callback) {
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            dbHelper.getWritableDatabase().insert(UserTable.TABLE, null, contentValues[0]);

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (callback != null) {
                callback.onComplete();
            }
        }
    }

    class ClearUserTask extends AsyncTask<Void, Void, Void> {

        private final CompleteCallback callback;

        ClearUserTask(CompleteCallback callback) {
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dbHelper.getWritableDatabase().delete(UserTable.TABLE, null, null);

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(callback != null) {
                callback.onComplete();
            }
        }
    }

}
