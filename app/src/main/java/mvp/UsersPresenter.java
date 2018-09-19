package mvp;

import android.content.ContentValues;
import android.text.TextUtils;

import com.java.note.mvp.R;

import common.UserTable;

public class UsersPresenter {

    private UsersContractView view;
    private final UsersModel model;

    UsersPresenter(UsersModel model) {
        this.model = model;
    }

    public void attachView(UsersContractView view) {
        this.view = view;
    }

    public void detachView() {
        view = null;
    }

    public void viewIsReady() {
        loadUsers();
    }

    private void loadUsers() {
        model.loadUsers(users -> view.showUsers(users));
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
        model.addUser(contentValues, () -> {
            view.hideProgress();
            loadUsers();
        });

    }

    public void clear() {
        view.showProgress();
        model.clearUsers(() -> {
            view.hideProgress();
            loadUsers();
        });
    }
}
