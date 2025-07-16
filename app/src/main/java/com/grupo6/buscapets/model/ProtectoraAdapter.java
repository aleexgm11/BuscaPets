package com.grupo6.buscapets.model;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.grupo6.buscapets.R;

import java.util.ArrayList;

public class ProtectoraAdapter extends RecyclerView.Adapter<ProtectoraAdapter.ViewHolder> {

    private ArrayList<Protectora> protectoraList;

    public ProtectoraAdapter(ArrayList<Protectora> protectoraList) {
        this.protectoraList = protectoraList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_protectora, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Protectora protectora = protectoraList.get(position);
        holder.textViewName.setText(protectora.getName());
        holder.textViewAddress.setText(protectora.getAddress());
        holder.textViewPhone.setText(protectora.getPhone());

        // Cargar la imagen usando el nombre del recurso
        Context context = holder.itemView.getContext();
        int imageId = context.getResources().getIdentifier(protectora.getImageName(), "drawable", context.getPackageName());
        if (imageId != 0) {
            holder.imageViewProtectora.setImageResource(imageId);
        } else {
            holder.imageViewProtectora.setImageResource(R.drawable.ic_launcher_foreground); // Imagen por defecto
        }

        // Manejar el clic en el ítem para abrir el detalle de la protectora
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProtectoraDetailActivity.class);
            intent.putExtra("protectora", protectora);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return protectoraList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewAddress, textViewPhone;
        ImageView imageViewProtectora;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewProtectoraName);
            textViewAddress = itemView.findViewById(R.id.textViewProtectoraAddress);
            textViewPhone = itemView.findViewById(R.id.textViewProtectoraPhone);
            imageViewProtectora = itemView.findViewById(R.id.imageViewProtectora);
        }
    }
}
