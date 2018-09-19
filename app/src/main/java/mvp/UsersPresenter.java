package mvp;

import android.content.ContentValues;
import android.text.TextUtils;

import com.java.note.mvp.R;

import java.util.List;

import common.User;
import common.UserTable;

public class UsersPresenter {

    private UsersActivity view;
    private final UsersModel model;

    UsersPresenter(UsersModel model) {
        this.model = model;
    }

    public void attachView(UsersActivity usersActivity) {
        view = usersActivity;
    }

    public void detachView() {
        view = null;
    }

    public void viewIsReady() {
        loadUsers();
    }

    private void loadUsers() {
        model.loadUsers(new UsersModel.LoadUserCallback() {
            @Override
            public void onLoad(List<User> users) {
                view.showUsers(users);
            }
        });
    }

    public void add() {
        UserData userData = view.getUserData();
        if(TextUtils.isEmpty(userData.getName()) || TextUtils.isEmpty(userData.getEmail())) {
            view.showToast(R.string.empty_values);
        }

        ContentValues contentValues = new ContentValues(2);
        contentValues.put(UserTable.COLUMN.NAME, userData.getName());
        contentValues.put(UserTable.COLUMN.EMAIL, userData.getEmail());

        view.showProgress();
        model.addUser(contentValues, new UsersModel.CompleteCallback() {
            @Override
            public void onComplete() {
                view.hideProgress();
                loadUsers();
            }
        });

    }

    public void clear() {
        view.showProgress();
        model.clearUsers(new UsersModel.CompleteCallback() {
            @Override
            public void onComplete() {
                view.hideProgress();
                loadUsers();
            }
        });
    }
}
