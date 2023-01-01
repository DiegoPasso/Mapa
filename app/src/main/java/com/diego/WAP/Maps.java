package com.diego.WAP;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import com.google.android.gms.location.LocationRequest;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.diego.WAP.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnSuccessListener;

public class Maps extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener  {

    private static final int REQUEST_LAST_LOCATION = 1;
    private static final int REQUEST_LOCATION_UPDATES = 2;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private CameraPosition cameraPosition;
    private SharedPreferences sharedPrefs;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        habilitaMyLocation();
        UiSettings mapUI = mMap.getUiSettings();
        mapUI.setZoomControlsEnabled(true);
        mapUI.setRotateGesturesEnabled(true);

        sharedPrefs = getSharedPreferences("MySettings", Context.MODE_PRIVATE);

        int tra = sharedPrefs.getInt("Trafego", 1);
        int tipo = sharedPrefs.getInt("Tipo", 1);


        if (tra == 1) {
            googleMap.setTrafficEnabled(false);
        } else {
            googleMap.setTrafficEnabled(true);
        }
        switch (tipo) {
            case 1:
                tipo = GoogleMap.MAP_TYPE_NORMAL;
                googleMap.setMapType(tipo);
                break;
            case 2:
                tipo = GoogleMap.MAP_TYPE_SATELLITE;
                googleMap.setMapType(tipo);
                break;
        }


        // Add a marker in Sydney and move the camera
        LatLng Unifacs = new LatLng(-12.97938, -38.50982);

        Marker marker = mMap.addMarker(new MarkerOptions().position(Unifacs).snippet("Viva a Universidade").title("UNIFACS"));
        marker.setAnchor(0.5f, 0.5f);
        marker.setRotation(0);
        mMap.setIndoorEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Unifacs, 15.0f));

        cameraPosition = new CameraPosition.Builder()
                .target(Unifacs)
                .zoom(15)
                .bearing(00)
                .tilt(45)
                .build();
    }
    private void startLocationUpdates() {
        // Se a app já possui a permissão, ativa a calamada de localização
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            // A permissão foi dada
            // Cria o cliente FusedLocation
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            // Configura solicitações de localização
            mLocationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
                    .setWaitForAccurateLocation(false)
                    .setMinUpdateIntervalMillis(500)
                    .setMaxUpdateDelayMillis(1000)
                    .build();
            // Programa o evento a ser chamado em intervalo regulares de tempo
            mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    Location location = locationResult.getLastLocation();

                }
            };

            mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        } else {
            // Solicite a permissão
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_UPDATES);
        }
    }

    private void habilitaMyLocation() {

        // Registra esta atividade como escutadora do click no botão de localização
        mMap.setOnMyLocationButtonClickListener((GoogleMap.OnMyLocationButtonClickListener) this);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            // A permissão foi dada
            mMap.setMyLocationEnabled(true);

        } else {
            // Solicite a permissão
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LAST_LOCATION);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LAST_LOCATION) {
            if (grantResults.length == 1 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                // O usuário acabou de dar a permissão
                habilitaMyLocation();

            } else {
                // O usuário não deu a permissão solicitada
                Toast.makeText(this, "Sem permissão para mostrar sua localização",
                        Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == REQUEST_LAST_LOCATION) {
            if (grantResults.length == 1 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                // O usuário acabou de dar a permissão

            } else {
                // O usuário não deu a permissão solicitada
                Toast.makeText(this, "Sem permissão para mostrar sua última localização",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        if (requestCode == REQUEST_LOCATION_UPDATES) {
            if (grantResults.length == 1 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                // O usuário acabou de dar a permissão
                startLocationUpdates();
            } else {
                // O usuário não deu a permissão solicitada
                Toast.makeText(this, "Sem permissão para mostrar atualizações da sua localização",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
//
            }
        });
        return false;
    }
}