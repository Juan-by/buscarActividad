package com.juanR.panascoop;

import com.framents.filtros;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adapters.ActivitiesAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.models.Activity;

import android.widget.CheckBox;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import android.content.Intent;
import android.view.animation.Animation;
import android.widget.LinearLayout;

public class BuscarActividad extends AppCompatActivity implements filtros.FilterListener {
    private EditText etSearch;
    private Button btnSearch, btnFilters, btnNewActivity;
    private RecyclerView rvActivities;
    private ActivitiesAdapter adapter;
    private List<Activity> activitiesList = new ArrayList<>();
    private List<Activity> filteredList = new ArrayList<>();
    private LinearLayout menuDrawer;
    private boolean isMenuOpen = false;
    private Animation slideIn, slideOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buscar_actividad);

        slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in);
        slideOut = AnimationUtils.loadAnimation(this, R.anim.slide_out);

        menuDrawer = findViewById(R.id.menuDrawer);
        AppCompatImageButton btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(v -> toggleMenu());

        setupMenuButtons();

        etSearch = findViewById(R.id.etSearch);
        btnSearch = findViewById(R.id.btnSearch);
        btnFilters = findViewById(R.id.btnFilters);
        btnNewActivity = findViewById(R.id.btnNewActivity);
        rvActivities = findViewById(R.id.rvActivities);

        rvActivities.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ActivitiesAdapter(filteredList);
        rvActivities.setAdapter(adapter);

        loadSampleData();
        btnSearch.setOnClickListener(v -> performSearch());
        btnFilters.setOnClickListener(v -> showFiltersDialog());
        btnNewActivity.setOnClickListener(v -> startActivity(new Intent(this, CrearActividadActivity.class)));
    }

    private void toggleMenu() {
        if (isMenuOpen) {
            menuDrawer.startAnimation(slideOut);
            menuDrawer.setVisibility(View.GONE);
        } else {
            menuDrawer.startAnimation(slideIn);
            menuDrawer.setVisibility(View.VISIBLE);
        }
        isMenuOpen = !isMenuOpen;
    }

    private void setupMenuButtons() {
        findViewById(R.id.btnActivityList).setOnClickListener(v -> {
            startActivity(new Intent(this, ListaActividadesActivity.class));
            closeMenu();
        });

        findViewById(R.id.btnSearchFilter).setOnClickListener(v -> closeMenu());

        findViewById(R.id.btnPromotedActivities).setOnClickListener(v -> {
            startActivity(new Intent(this, ActividadesPromocionadasActivity.class));
            closeMenu();
        });

        findViewById(R.id.btnNotificationSettings).setOnClickListener(v -> {
            startActivity(new Intent(this, ConfigurarNotificacionesActivity.class));
            closeMenu();
        });

        findViewById(R.id.btnSocialMedia).setOnClickListener(v -> {
            startActivity(new Intent(this, PublicarRedesSocialesActivity.class));
            closeMenu();
        });

        findViewById(R.id.btnAttendanceManagement).setOnClickListener(v -> {
            startActivity(new Intent(this, GestionarAsistenciaActivity.class));
            closeMenu();
        });
    }

    private void closeMenu() {
        if (isMenuOpen) {
            menuDrawer.startAnimation(slideOut);
            menuDrawer.setVisibility(View.GONE);
            isMenuOpen = false;
        }
    }

    @Override
    public void onBackPressed() {
        if (isMenuOpen) {
            closeMenu();
        } else {
            super.onBackPressed();
        }
    }

    private void loadSampleData() {
        activitiesList.clear();
        filteredList.clear();

        Calendar calendar = Calendar.getInstance();

        calendar.set(2025, Calendar.OCTOBER, 10);
        activitiesList.add(new Activity("Recogida de basura", "Ayuda a limpiar el parque central", calendar.getTime(), "Parque Central", "upcoming"));

        calendar.set(2025, Calendar.SEPTEMBER, 15);
        activitiesList.add(new Activity("Donación de sangre", "Campaña de donación de sangre", calendar.getTime(), "Hospital General", "completed"));

        filteredList.addAll(activitiesList);
        adapter.notifyDataSetChanged();
    }

    private void performSearch() {
        String searchText = etSearch.getText().toString().toLowerCase().trim();
        filteredList.clear();

        if (searchText.isEmpty()) {
            filteredList.addAll(activitiesList);
        } else {
            for (Activity activity : activitiesList) {
                if (activity.getTitle().toLowerCase().contains(searchText) ||
                        activity.getDescription().toLowerCase().contains(searchText)) {
                    filteredList.add(activity);
                }
            }
        }

        adapter.notifyDataSetChanged();

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No se encontraron actividades", Toast.LENGTH_SHORT).show();
        }
    }

    private void showFiltersDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.activity_filtros, null);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();

        Button btnApplyFilters = view.findViewById(R.id.btnApplyFilters);
        btnApplyFilters.setOnClickListener(v -> {
            applyFilters(view);
            bottomSheetDialog.dismiss();
        });
    }

    private void applyFilters(View filtersView) {
        EditText etDate = filtersView.findViewById(R.id.etDate);
        EditText etLocation = filtersView.findViewById(R.id.etLocation);
        CheckBox cbCompleted = filtersView.findViewById(R.id.cbCompleted);
        CheckBox cbUpcoming = filtersView.findViewById(R.id.cbUpcoming);
        CheckBox cbOngoing = filtersView.findViewById(R.id.cbOngoing);

        String dateFilter = etDate.getText().toString();
        String locationFilter = etLocation.getText().toString().toLowerCase();

        filteredList.clear();

        for (Activity activity : activitiesList) {
            boolean matchesDate = dateFilter.isEmpty() || formatDate(activity.getDate()).contains(dateFilter);
            boolean matchesLocation = locationFilter.isEmpty() || activity.getLocation().toLowerCase().contains(locationFilter);
            boolean matchesStatus = (!cbCompleted.isChecked() && !cbUpcoming.isChecked() && !cbOngoing.isChecked()) ||
                    (cbCompleted.isChecked() && activity.getStatus().equals("completed")) ||
                    (cbUpcoming.isChecked() && activity.getStatus().equals("upcoming")) ||
                    (cbOngoing.isChecked() && activity.getStatus().equals("ongoing"));

            if (matchesDate && matchesLocation && matchesStatus) {
                filteredList.add(activity);
            }
        }

        adapter.notifyDataSetChanged();

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No hay actividades con estos filtros", Toast.LENGTH_SHORT).show();
        }
    }

    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(date);
    }

    @Override
    public void onFiltersApplied(String date, String location, String status) {
        filteredList.clear();

        for (Activity activity : activitiesList) {
            boolean matchesDate = date.isEmpty() || formatDate(activity.getDate()).contains(date);
            boolean matchesLocation = location.isEmpty() || activity.getLocation().toLowerCase().contains(location);
            boolean matchesStatus = status.isEmpty() || activity.getStatus().equals(status);

            if (matchesDate && matchesLocation && matchesStatus) {
                filteredList.add(activity);
            }
        }

        adapter.notifyDataSetChanged();

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No hay actividades con estos filtros", Toast.LENGTH_SHORT).show();
        }
    }
}
