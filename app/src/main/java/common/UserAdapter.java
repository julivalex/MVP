package common;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.java.note.mvp.R;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {

    List<User> data = new ArrayList<>();

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<User> users) {
        data.clear();
        data.addAll(users);
        notifyDataSetChanged();
        Log.d("TAG", "size  = " + getItemCount());
    }

    static class UserHolder extends RecyclerView.ViewHolder {

        TextView text;

        UserHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
        }

        void bind(User user) {
            text.setText(String.format("id: %s, name: %s, email: %s", user.getId(), user.getName(), user.getEmail()));
        }
    }
}
