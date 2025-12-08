package com.example.senhapass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PasswordAdapter extends RecyclerView.Adapter<PasswordAdapter.ViewHolder> {

    Context context;
    List<PasswordModel> list;
    private OnItemClickListener listener;

    public PasswordAdapter(Context context, List<PasswordModel> list) {
        this.context = context;
        this.list = list;
    }

    //=====================================================
    // INTERFACE PARA CLIQUE NO ITEM
    //=====================================================
    public interface OnItemClickListener {
        void onItemClick(PasswordModel item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_password, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PasswordModel item = list.get(position);
        holder.txtDescricao.setText(item.getDescricao());
        holder.txtSenha.setText(item.getSenha());

        // Clique do item funcionando
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtDescricao, txtSenha;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDescricao = itemView.findViewById(R.id.txtDescricao);
            txtSenha = itemView.findViewById(R.id.txtSenha);
        }
    }
}
