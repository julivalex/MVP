package mvp;

import java.util.List;

import common.User;

public interface UsersContractView {

    UserData getUserData();
    void showUsers(List<User> users);
    void showToast(int resId);
    void showProgress();
    void hideProgress();
}
