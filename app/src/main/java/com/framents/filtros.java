package com.framents;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import androidx.fragment.app.Fragment;
import com.juanR.panascoop.R;

public class filtros extends Fragment {
    private EditText etDate, etLocation;
    private CheckBox cbCompleted, cbUpcoming, cbOngoing;
    private Button btnApplyFilters;

    // Interfaz para comunicar con la Activity
    public interface FilterListener {
        void onFiltersApplied(String date, String location, String status);
    }

    private FilterListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FilterListener) {
            listener = (FilterListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FilterListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_filtros, container, false);

        // Inicializar las vistas
        etDate = view.findViewById(R.id.etDate);
        etLocation = view.findViewById(R.id.etLocation);
        cbCompleted = view.findViewById(R.id.cbCompleted);
        cbUpcoming = view.findViewById(R.id.cbUpcoming);
        cbOngoing = view.findViewById(R.id.cbOngoing);
        btnApplyFilters = view.findViewById(R.id.btnApplyFilters);

        // Configurar el botón para aplicar los filtros
        btnApplyFilters.setOnClickListener(v -> applyFilters());

        return view;
    }

    private void applyFilters() {
        String date = etDate.getText().toString();
        String location = etLocation.getText().toString();
        String status = getSelectedStatus();

        if (listener != null) {
            listener.onFiltersApplied(date, location, status);
        }
    }

    private String getSelectedStatus() {
        if (cbCompleted.isChecked()) return "completed";
        if (cbUpcoming.isChecked()) return "upcoming";
        if (cbOngoing.isChecked()) return "ongoing";
        return "";
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
