package com.nauka.purchases;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.nauka.purchases.R.layout.add_purchase_fragment_view;

public class AddPurchasesFragment extends DialogFragment {
    /*Объявляем int RESULT_OK который вернет -1 если активити камеры отработало отлично
    и вернуло фото*/
    private static final int RESULT_OK = -1;
    /*Объявляем int CAMERA_REQUEST который вернет 1 если активити камеры отработало отлично
    и вернуло фото*/
    private static final int CAMERA_REQUEST = 1;
    //Объявляем класс DBHelper создания базы данных
    private DBHelper dbHelper;
    //Объявляем ContentValues контейнер для вставки в базу данных
    private ContentValues cv;
    //Объявляем SQLiteDatabase для обеспечения чтения/записи БД
    private SQLiteDatabase db;
    //Объявляем int Date сюда попадает текущая дата из класса Calendar
    private int Date;
    /*Объявляем int Month сюда попадает текущий месяц из класса Calendar
    к нему нужно добавить +1 так как месяца считаются от 0*/
    private int Month;
    //Объявляем int Year сюда попадает текущий год из класса Calendar
    private int Year;
    /*Объявляем long dateMillis сюда попадает вся дата в формате millisecunds
    из класса Calendar*/
    private long dateMillis;
    //Объявляем View для отображения myFragmentView
    private View myFragmentView;
    /*Объявляем класс Calendar для получения текущей даты и для установки выбранной
    даты*/
    private Calendar calendar;
    /*Объявляем EditText изначально в нем отображается текущяя дата. Так
    же в него вставляется выбранная дата*/
    private EditText editText2;
    /*Объявляем Button при нажатии которой будет происходить добавление
    нового чека в БД*/
    private Button add_button;
    //Объявляем Context контекст данного класса привязанный к editText2
    private Context mContext;
    //Объявляем EditText куда вводится сумма чека
    private EditText summPurchases;
    //
    private ByteArrayOutputStream steam;
    //Объявляем ImageButton при нажатии которой открывается камера
    private ImageButton img;
    //
    private Uri outputFileUri;
    //
    Intent cameraIntent;
    //
    private String mImageFileLocation = "";
    //Объявляем Uri для сохранения пути сделанного фото в базу данных
    Uri photoURI;
    //Объявляем File для сохранения сделанного фото в памяти
    File photoFile;

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Инициализация myFragmentView
        myFragmentView = inflater.inflate(add_purchase_fragment_view,
                container, false);
        //Инициализация editText2
        editText2 = myFragmentView.findViewById(R.id.editText2);
        //Инициализация mContext
        mContext = editText2.getContext();
        //Инициализация класса базы данных
        dbHelper = new DBHelper(mContext);
        //Инициализируем подключение к БД
        db = dbHelper.getWritableDatabase();
        //Инициализируем объект для данных
        cv = new ContentValues();
        //Инициализация calendar
        calendar = Calendar.getInstance();
        //Инициализация summPurchases
        summPurchases = myFragmentView.findViewById(R.id.summPurchases);
        //Инициализация add_button
        add_button = myFragmentView.findViewById(R.id.add_button);
        //Инициализация img
        img = myFragmentView.findViewById(R.id.img);
        //Инициализация Date
        Date = calendar.get(Calendar.DAY_OF_MONTH);
        //Инициализация Month
        Month = calendar.get(Calendar.MONTH);
        //Инициализация Year
        Year = calendar.get(Calendar.YEAR);
        //Вставлеяем в editText2 полученную с класса calendar текущую дату
        editText2.setText(Date + "." + (Month + 1) + "." + Year);
        //Получаем текущуюю дату в миллисекундах
        dateMillis = calendar.getTimeInMillis();

        //При нажатии на editText2 открывается диалог выбора даты
        editText2.setOnTouchListener(new View.OnTouchListener() {
            Context mContext = editText2.getContext();
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    final DatePickerDialog datePickerDialog = new DatePickerDialog(mContext,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    //В строку editTextDateParam получаем выбранную дату
                                    String editTextDateParam = dayOfMonth + "." + (month + 1) + "." + year;
                                    //Вставляум полученную editTextDateParam в editText2
                                    editText2.setText(editTextDateParam);
                                    //Устанавливаем в Calendar выбранную дату
                                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                    //Устанавливаем в Calendar выбранный месяц
                                    calendar.set(Calendar.MONTH, month);
                                    //Устанавливаем в Calendar выбранный год
                                    calendar.set(Calendar.YEAR, year);
                                    /*Для нативного выбора даты без физического раскрытия и выбора
                                    текущая дата вставляется автоматом*/
                                    Date = dayOfMonth;
                                    Month = month;
                                    Year = year;
                                    /*После выбора даты, получение текущей даты в виде
                                    миллисекунд*/
                                    dateMillis = calendar.getTimeInMillis();
                                }
                            }, Year, Month, Date);
                    datePickerDialog.show();
                }
                return false;
            }
        });

        //При клике на кнопку img выполняется открытие камеры
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //Выполняем класс dispatchTakePictureIntent
                    dispatchTakePictureIntent();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        /*При нажатии на кнопку add_button происходит сохранение введенных данных
        в БД и обновление информации в MainActivity*/
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Вставка в базу данных введенных данных
                cv.put("purchases", dateMillis);
                cv.put("summ", String.valueOf(summPurchases.getText()));
                cv.put("imageaddres", String.valueOf(photoFile));
                cv.put("day", String.valueOf(Date));
                cv.put("mount", String.valueOf(Month + 1));
                cv.put("year", String.valueOf(Year));
                if (photoFile != null) {
                    cv.put("imageaddesview", "ok");
                }
                db.insert("purchasesTable", null, cv);
                //Обновление информации в MainActivity
                ((MainActivity) getActivity()).listItem.clear();
                ((MainActivity) getActivity()).readStart();
                ((MainActivity) getActivity()).adapterMy.notifyDataSetChanged();
                ((MainActivity) getActivity()).reStart();
                //Закрытие диалога
                dismiss();
            }
        });
        return myFragmentView;
    }

    //Класс возвращающий результат выполнения активити камера
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {

        }
    }

    //
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Убедитесь, что есть камера для обработки намерения
        if (takePictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
            // Создайте файл, куда должна идти фотография
            photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Произошла ошибка при создании файла
                Toast.makeText(mContext, "Error!", Toast.LENGTH_SHORT).show();
            }
            // Продолжить, только если файл был успешно создан
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(mContext,
                        "com.example.android.provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
            }
        }
    }

    //Класс в котором формируется имя файла
    private File createImageFile() throws IOException {
        //Формируем строку timeStamp которая содержит текущую дату и время
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        //Формируем строку imageFileName из "IMAGE_" + timeStamp + "_
        String imageFileName = "IMAGE_" + timeStamp + "_";
        //Путь сохранения - в данном случае это внутренняя память
        File storageDirectory = mContext.getFilesDir();
        //Создаем темп файл imageFile состоящий из имени файла imageFileName + пути storageDirectory
        File imageFile = File.createTempFile(imageFileName, ".png", storageDirectory);
        mImageFileLocation = imageFile.getAbsolutePath();
        //Возвращаем имя файла с полным путем в формате FILE
        return imageFile;
    }
}
