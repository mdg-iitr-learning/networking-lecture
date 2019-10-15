package com.sachan.prateek.retrofitsample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sachan.prateek.retrofitsample.models.Data;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String APIKEY = "5753db64c667d9ed89962813328c19be";
    APIInterface apiInterface;
    TextView pressure, temp, wind, desc, cityName;
    EditText editText;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instantiateViews();
        apiInterface = ApiClient.getClient().create(APIInterface.class);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String zipCode = editText.getText().toString();
                editText.getText().clear();
                editText.clearFocus();
                fetchData(zipCode);
            }
        });
    }

    private void instantiateViews() {
        pressure = findViewById(R.id.pressure);
        temp = findViewById(R.id.temperature);
        wind = findViewById(R.id.wind);
        desc = findViewById(R.id.description);
        cityName = findViewById(R.id.city_name);
        editText = findViewById(R.id.zipcode);
        submit = findViewById(R.id.submit);

    }

    private void fetchData(String zipCode) {
        Call<Data> call = apiInterface.getWeatherData(APIKEY, zipCode + ",in");
        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(@NonNull Call<Data> call, @NonNull Response<Data> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        Data data = response.body();
                        cityName.setText(data.getName());
                        double tempCelsius = data.getMain().getTemp() - 273;
                        temp.setText(String.format("%.1f", tempCelsius) + "\u2103");
                        pressure.setText(String.valueOf(data.getMain().getPressure()));
                        desc.setText(data.getWeather().get(0).getDescription());
                        double windSpeed = data.getWind().getSpeed();
                        wind.setText(String.valueOf(windSpeed));
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Invalid Zip Code", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Data> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
