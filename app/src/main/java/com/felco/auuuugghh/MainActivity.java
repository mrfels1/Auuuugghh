package com.felco.auuuugghh;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

import com.felco.auuuugghh.databinding.ActivityMainBinding;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText inputTemperatureEditText;
    private Spinner fromScaleSpinner;
    private Spinner toScaleSpinner;
    private EditText resultTextView;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // инициализация пользовательского интерфейса
        inputTemperatureEditText = binding.inputTemperatureEditText; // вводимое число
        fromScaleSpinner = binding.fromScaleSpinner; // выбор из
        toScaleSpinner = binding.toScaleSpinner; // выбор в

        // настройка Spiner
        setupSpinners();

        // Установка слушателя изменений в поле ввода температуры
        inputTemperatureEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        binding.button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SecondActivity.class);
                String inputTemperatureStr = inputTemperatureEditText.getText().toString();
                double inputTemperature = parseTemperature(inputTemperatureStr);
                if (!Double.isNaN(inputTemperature)) {
                    int fromScale = fromScaleSpinner.getSelectedItemPosition();
                    int toScale = toScaleSpinner.getSelectedItemPosition();

                    double convertedTemperature = convert(inputTemperature, fromScale, toScale);
                    i.putExtra("res",String.valueOf(convertedTemperature));
                } else {
                    i.putExtra("res",String.valueOf(0.0));
                }

                startActivity(i);
            }
        });
    }

    private void setupSpinners() {
        String[] scales = getResources().getStringArray(R.array.weight_scales); // массив названия шкал

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, scales); // адаптер для спинеров
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // вид элементов списка

        fromScaleSpinner.setAdapter(adapter);//
        toScaleSpinner.setAdapter(adapter); // отображение шкал темпиратур

        AdapterView.OnItemSelectedListener scaleItemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        };

        fromScaleSpinner.setOnItemSelectedListener(scaleItemSelectedListener);
        toScaleSpinner.setOnItemSelectedListener(scaleItemSelectedListener);
    }



    // переобразуем число с плавующей точкой
    private double parseTemperature(String temperatureStr) {
        try {
            return Double.parseDouble(temperatureStr);
        } catch (NumberFormatException e) {
            return Double.NaN;
        }
    }

    public double convert(double value, int fromUnit, int toUnit) {
        Map<String, Double> conversionFactors = new HashMap<>();
        conversionFactors.put("0", 0.0002);
        conversionFactors.put("1", 0.001);
        conversionFactors.put("2", 1.0);
        conversionFactors.put("3", 100.0);
        conversionFactors.put("4", 1000.0);
        conversionFactors.put("5", 0.45359);
        conversionFactors.put("6", 0.02835);

        return value * conversionFactors.get(Integer.toString(fromUnit)) / conversionFactors.get(Integer.toString(toUnit));
    }
}