package com.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.juanR.panascoop.R;
import com.models.Activity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesAdapter.ActivityViewHolder> {

    private List<Activity> activities;

    public ActivitiesAdapter(List<Activity> activities) {
        this.activities = activities;
    }

    @Override
    public ActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflamos el layout de cada ítem de la lista
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_items, parent, false);
        return new ActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ActivityViewHolder holder, int position) {
        // Obtenemos la actividad actual
        Activity activity = activities.get(position);

        // Configuramos los datos en las vistas correspondientes
        holder.tvTitle.setText(activity.getTitle());
        holder.tvDescription.setText(activity.getDescription());
        holder.tvDate.setText(formatDate(activity.getDate()));
        holder.tvLocation.setText(activity.getLocation());

        // Configuración del estado
        if ("completed".equals(activity.getStatus())) {
            holder.tvStatus.setText("Completado");
        } else if ("upcoming".equals(activity.getStatus())) {
            holder.tvStatus.setText("Próxima");
        } else if ("ongoing".equals(activity.getStatus())) {
            holder.tvStatus.setText("En curso");
        }
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    // Clase ViewHolder para las vistas de cada ítem
    public static class ActivityViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription, tvDate, tvLocation, tvStatus;

        public ActivityViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }

    // Método auxiliar para formatear la fecha
    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(date);
    }
}
