package com.abdilahstudio.apiclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

// model
import com.abdilahstudio.apiclient.HomeActivity;
import com.abdilahstudio.apiclient.R;
import com.abdilahstudio.apiclient.model.User;
import com.bumptech.glide.Glide;

import java.text.BreakIterator;
import java.util.List;
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> userList;
    private Context context;

    public UserAdapter(List<User> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.textViewName.setText(user.getName());
        holder.textViewEmail.setText(user.getEmail());
        Glide.with(holder.itemView.getContext())
                .load(user.getPhotoUrl())
                .into(holder.imageViewPhoto);
        holder.textViewTglLahir.setText(user.getTglLahir());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof HomeActivity) {
                    ((HomeActivity) context).showUpdateDialog(user);
                }
            }
        });

        holder.btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof HomeActivity) {
                    ((HomeActivity) context).deleteUser(user);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        public BreakIterator textViewTglLahir2;
        TextView textViewName;
        TextView textViewEmail;
        ImageView imageViewPhoto;
        TextView textViewTglLahir;

        Button btnHapus;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewEmail = itemView.findViewById(R.id.textViewEmail);
            imageViewPhoto = itemView.findViewById(R.id.imageViewPhoto);
            textViewTglLahir = itemView.findViewById(R.id.textViewTglLahir);
            btnHapus = itemView.findViewById(R.id.btnHapus);
        }
    }
}