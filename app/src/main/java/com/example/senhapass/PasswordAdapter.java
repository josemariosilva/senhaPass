package com.example.senhapass;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PasswordAdapter extends RecyclerView.Adapter<PasswordAdapter.ViewHolder> {

    Context context;
    List<PasswordModel> list;
    private OnItemClickListener listener;
    DBHelper db;

    public PasswordAdapter(Context context, List<PasswordModel> list) {
        this.context = context;
        this.list = list;
        this.db = new DBHelper(context);
    }

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

        // senha inicialmente oculta
        holder.txtSenha.setText("******");
        holder.isShowing = false;

        // Mostrar/ocultar senha com o ícone btnVer
        holder.btnVer.setOnClickListener(v -> {
            holder.isShowing = !holder.isShowing;
            holder.txtSenha.setText(holder.isShowing ? item.getSenha() : "******");
            // opcional: trocar ícone se tiver recursos customizados
            // holder.btnVer.setImageResource(holder.isShowing ? R.drawable.ic_visibility_off : R.drawable.ic_visibility);
        });

        // Clique para editar (via listener)
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(item);
        });

        // Long click: copiar senha (rápido) e confirmar
        holder.itemView.setOnLongClickListener(v -> {
            // Dialog simples: copiar ou excluir
            new AlertDialog.Builder(context)
                    .setTitle(item.getDescricao())
                    .setItems(new CharSequence[]{"Copiar senha", "Excluir"}, (dialog, which) -> {
                        if (which == 0) {
                            ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                            cm.setPrimaryClip(ClipData.newPlainText("senha", item.getSenha()));
                            Toast.makeText(context, "Senha copiada!", Toast.LENGTH_SHORT).show();
                        } else if (which == 1) {
                            new AlertDialog.Builder(context)
                                    .setTitle("Excluir")
                                    .setMessage("Deseja excluir esta senha?")
                                    .setPositiveButton("Sim", (d, w) -> {
                                        db.deletePassword(item.getId());
                                        list.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, list.size());
                                    })
                                    .setNegativeButton("Cancelar", null)
                                    .show();
                        }
                    })
                    .show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtDescricao, txtSenha;
        ImageView btnVer;
        boolean isShowing = false;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDescricao = itemView.findViewById(R.id.txtDescricao);
            txtSenha = itemView.findViewById(R.id.txtSenha);
            btnVer = itemView.findViewById(R.id.btnVer);
        }
    }
}
