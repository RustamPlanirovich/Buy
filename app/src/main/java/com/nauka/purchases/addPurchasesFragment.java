package com.nauka.purchases;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;

import static com.nauka.purchases.R.layout.add_purchase_fragment_view;
import static com.nauka.purchases.R.layout.add_purchase_fragment_view1;

public class addPurchasesFragment extends DialogFragment {
    private static final int RESULT_OK = -1;
    private static final int CAMERA_REQUEST = 1;
    //Объявляем класс базы данных
    private DBHelper dbHelper;
    //Объявляем объект для данных
    private ContentValues cv;
    //Объявляем подключение к БД
    private SQLiteDatabase db;
    private int Date;
    private int Month;
    private int Year;
    private long dateMillis;
    private View myFragmentView;
    private Calendar calendar;
    private EditText editText;
    private  Button confirm_btn;
    private  Context mContext;
    private  EditText sumPurchses;
    private  ByteArrayOutputStream steam;
    private ImageButton img;
    private Uri outputFileUri;
    Intent cameraIntent;
    private String mImageFileLocation = "";
    Uri photoURI;
    File photoFile;
    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myFragmentView = inflater.inflate(add_purchase_fragment_view1,
                container, false);
        setStyle(DialogFragment.STYLE_NO_FRAME, 0);
        editText = myFragmentView.findViewById(R.id.editText);
        mContext = editText.getContext();
        //Инициализация класса базы данных
        dbHelper = new DBHelper(mContext);

        //Инициализируем подключение к БД
        db = dbHelper.getWritableDatabase();

        //Инициализируем объект для данных
        cv = new ContentValues();
        calendar = Calendar.getInstance();

        sumPurchses = myFragmentView.findViewById(R.id.summPurchases2);
        confirm_btn = myFragmentView.findViewById(R.id.confirm_btn);
        img = myFragmentView.findViewById(R.id.img2);
        Date = calendar.get(Calendar.DAY_OF_MONTH);
        Month = calendar.get(Calendar.MONTH);
        Year = calendar.get(Calendar.YEAR);
        editText.setText(Date+"."+(Month+1)+"."+Year);

        dateMillis = calendar.getTimeInMillis();

        editText.setOnTouchListener(new View.OnTouchListener() {
            Context mContext = editText.getContext();
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    final DatePickerDialog datePickerDialog = new DatePickerDialog(mContext,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                                    String editTextDateParam = dayOfMonth+"."+(month+1)+"."+year;
                                    editText.setText(editTextDateParam);

                                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                    calendar.set(Calendar.MONTH, month);
                                    calendar.set(Calendar.YEAR, year);

                                    Date = dayOfMonth;
                                    Month = month;
                                    Year = year;

                                    dateMillis = calendar.getTimeInMillis();
                                }
                            },Year,Month,Date);
                    datePickerDialog.show();
                }
                return false;
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //saveFullImage();
                    dispatchTakePictureIntent();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MY","yes");
                cv.put("purchases", dateMillis);
                cv.put("summ", String.valueOf(sumPurchses.getText()));
                cv.put("imageaddres", String.valueOf(photoFile));
                cv.put("day",String.valueOf(Date));
                cv.put("mount",String.valueOf(Month+1));
                cv.put("year",String.valueOf(Year));
                if (photoFile != null) {
                    cv.put("imageaddesview", "ok");
                }
                db.insert("purchasesTable", null, cv);
                ((MainActivity) getActivity()).listItem.clear();
                ((MainActivity) getActivity()).readStart();
                ((MainActivity) getActivity()).adapterMy.notifyDataSetChanged();
                ((MainActivity) getActivity()).reStart();

                dismiss();
            }
        });


        return myFragmentView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            // Фотка сделана, извлекаем картинку

            //Bitmap thumbnailBitmap = (Bitmap) data.getExtras().get("data");
            //Bitmap thumbnailBitmap = data.getParcelableExtra("data");
            //steam = new ByteArrayOutputStream();
            //thumbnailBitmap.compress(Bitmap.CompressFormat.WEBP,100,steam);
        }
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
            // Create the File where the photo should go
            photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(mContext, "Error!", Toast.LENGTH_SHORT).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(mContext,
                        "com.example.android.provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMAGE_" + timeStamp + "_";
        File storageDirectory = mContext.getFilesDir();

        File imageFile = File.createTempFile(imageFileName, ".png", storageDirectory);
        mImageFileLocation = imageFile.getAbsolutePath();

        return imageFile;
    }
}
