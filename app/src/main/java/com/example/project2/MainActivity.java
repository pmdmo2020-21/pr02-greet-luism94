package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.example.project2.databinding.MainActivityBinding;

public class MainActivity extends AppCompatActivity {

    private final int max_freeUser = 10;    //Maxima cantidad de saludos para el usuario free
    private int counter;                    //Cantidad de saludos actuales para el usuario
    private MainActivityBinding binding;    //Clase ViewBinding para el uso de variables con los atributos de las vistas de la aplicacion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Metodo generador de la actividad con todas sus vistas infladas
        super.onCreate(savedInstanceState);
        binding = MainActivityBinding.inflate(getLayoutInflater()); //Objeto tipo ViewBinding para obtener variables de cada vista definida
        setContentView(binding.getRoot());
        setupViews();   //Metodo para estructurar las vistas en la actividad cuando se generan
    }

    private void setupViews() {
        counter = 0;    //Contador de los saludos

        //Estado inicial del SwitchCompat
        binding.mainSwitch.setChecked(false);
        //Estado inicial del Checkbox
        binding.mainPoliteCheckbox.setChecked(false);
        //Estado inicial del texto contador
        setGreetProgress(counter);
        //Opcion seleccionada por defecto en el radioGroup
        binding.mainGenderPickerContainer.clearCheck();
        binding.mainGenderPickerContainer.check(binding.mainGenderButton1.getId());
        setIconGender(binding.mainGenderButton1.getId());
        //Se selecciona el icono del radioGroup dependiendo de la opcion seleccionada
        //setIconGender(binding.mainGenderButton1.getId());
        //Estado inicial de la barra de progreso
        binding.mainGreetProgressBar.setMax(max_freeUser);
        setBarProgress(counter);
        //Objeto Listener para el boton
        binding.mainButton.setOnClickListener(view -> showGreet());
        //Objeto Listener para las opciones de genero
        binding.mainGenderPickerContainer.setOnCheckedChangeListener((radioGroup, radioButton) -> setIconGender(radioButton));
        //Objeto Listener para el objeto switchCompat de la actividad
        binding.mainSwitch.setOnCheckedChangeListener((switchCompat, state) -> setPremiumScreen(state));
    }

    private void setPremiumScreen(boolean state) {
        //Metodo que define las vistas de la actividad.
        if (state) {
            //Si el usuario ha activado la version premium se resetea el contador del boton y el progreso de la barra
            //y desaparece la barra y la frase con el contador del boton
            counter = 0;
            binding.mainGreetProgressBar.setVisibility(View.GONE);
            binding.mainButtonCounter.setVisibility(View.GONE);
            setBarProgress(counter);
            setGreetProgress(counter);
        } else {
            //Si no, se muestra todas las vistas de forma normal
            binding.mainGreetProgressBar.setVisibility(View.VISIBLE);
            binding.mainButtonCounter.setVisibility(View.VISIBLE);
        }
    }

    private void setGreetProgress(int counter) {
        //Metodo para mostrar la frase de progreso con el contador de los saludos para el usuario Free
        binding.mainButtonCounter.setText(getString(R.string.main_button_counter, counter, max_freeUser));;
    }

    private void setIconGender(int selectedGenderOption) {
        //Metodo para seleccionar el Id del icono segun la opcion seleccionada
        int option1 = binding.mainGenderButton1.getId(), option2 = binding.mainGenderButton2.getId();
        if (selectedGenderOption == option1) {
            binding.mainIconGender.setImageResource(R.drawable.ic_man);
        } else if (selectedGenderOption == option2) {
            binding.mainIconGender.setImageResource(R.drawable.ic_woman);
        } else {
            binding.mainIconGender.setImageResource(R.drawable.ic_girl);
        }
    }

    private void setBarProgress(int counter) {
        //Asigno al progreso de la barra el valor recibido por parametro
        binding.mainGreetProgressBar.setProgress(counter);
    }

    private void showGreet() {
        //Metodo que recibe las opciones seleccionadas del usuario para mostrar el saludo
        boolean polite;
        String gender;

        if (!binding.mainUserNameInput.getText().toString().equals("") || !binding.mainUserLastNameInput.getText().toString().equals("") || binding.mainUserNameInput.getText().toString().equals(" ") || binding.mainUserLastNameInput.getText().toString().equals(" ")) {
            //Si el usuario no ha introducido nada en su nombre o en su apellido, o solo ha introducido un espacio en blanco en cualquiera de los dos, la aplicacion no hace nada
            if (binding.mainSwitch.isChecked()) {
                polite = binding.mainPoliteCheckbox.isChecked();
                gender = getGender();
                setGreet(gender, polite);   //Muestro el saludo en la vista final
            } else {
                counter++;
            }

            if (counter > max_freeUser) {
                //Si el contador ha superado el limite se pide al usuario que active la version premium
                showTrialEnd();
            } else {
                //Se muestra la barra y la frase con el valor del contador actual
                setBarProgress(counter);
                setGreetProgress(counter);
                polite = binding.mainPoliteCheckbox.isChecked();
                gender = getGender();
                setGreet(gender, polite);
            }
        }
    }

    @org.jetbrains.annotations.NotNull
    private String getGender(){
        //Metodo para obtener una cadena con el genero seleccionado de entre las opciones
        String gender = "";
        int optionId = binding.mainGenderPickerContainer.getCheckedRadioButtonId();

        if (optionId == binding.mainGenderButton1.getId()) {
            gender = binding.mainGenderButton1.getText().toString();
        } else if (optionId == binding.mainGenderButton2.getId()) {
            gender = binding.mainGenderButton2.getText().toString();
        } else {
            gender = binding.mainGenderButton3.getText().toString();
        }

        return gender;
    }

    private void showTrialEnd() {
        //Metodo que muestra por pantalla la indicacion al usuario para que actualice la suscripcion premium
        binding.mainTextResult.setText(R.string.main_trialEnd);
    }

    private void setGreet(String gender, boolean polite) {
        //Metodo que devuelve el saludo dependiendo de si se ha pedido un saludo formal y de la opcion elegida para el genero
        String greet;

        if (polite) {
            greet = "Good morning " + gender + " " + binding.mainUserNameInput.getText().toString() + " " + binding.mainUserLastNameInput.getText().toString() + ". Pleased to meet you.";
        } else {
            greet = "Hello " + binding.mainUserNameInput.getText().toString() + " " + binding.mainUserLastNameInput.getText().toString() + ". What's up?";
        }

        binding.mainTextResult.setText(greet);
    }
}