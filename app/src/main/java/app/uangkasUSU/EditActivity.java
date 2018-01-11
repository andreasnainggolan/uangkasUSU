package app.uangkasUSU;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import app.uangkasUSU.helper.Config;
import app.uangkasUSU.helper.CurrentDate;

public class EditActivity extends AppCompatActivity {

    MainActivity M = new MainActivity();

    RadioGroup radio_status;
    RadioButton radio_masuk, radio_keluar;

    EditText edit_jumlah, edit_keterangan, edit_tanggal;
    Button btn_simpan;
    RippleView rip_simpan;

    Cursor cursor;

    String status, tanggal;
    SharedPreferences sharedPreferences;
    String nim2;

    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        status = ""; tanggal = "";

        radio_status    = (RadioGroup)  findViewById(R.id.radio_status);
        radio_masuk     = (RadioButton) findViewById(R.id.radio_masuk);
        radio_keluar    = (RadioButton) findViewById(R.id.radio_keluar);

        edit_jumlah     = (EditText)    findViewById(R.id.edit_jumlah);
        edit_keterangan = (EditText)    findViewById(R.id.edit_keterangan);
        edit_tanggal    = (EditText)    findViewById(R.id.edit_tanggal);
        btn_simpan      = (Button)      findViewById(R.id.btn_simpan);
        rip_simpan      = (RippleView) findViewById(R.id.rip_simpan);
        sharedPreferences = getSharedPreferences("uangkasUSU", Context.MODE_PRIVATE);
        nim2 = sharedPreferences.getString("nim", "");

        Detail2();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit");
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void Detail2(){

        status = M.status;
        switch (M.status){
            case "MASUK":
                radio_masuk.setChecked(true); break;
            case "KELUAR":
                radio_keluar.setChecked(true); break;
        }

        radio_status.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch(checkedId){
                    case R.id.radio_masuk:
                        status = "MASUK";
                        break;
                    case R.id.radio_keluar:
                        status = "KELUAR";
                        break;
                }

                Log.d("Log status", status);
            }
        });

        edit_jumlah.setText( M.jumlah );
        edit_keterangan.setText( M.keterangan );

        tanggal = M.tanggal2;
        edit_tanggal.setText( M.tanggal );

        edit_tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(EditActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month_of_year, int day_of_month) {
                        // set day of month , month and year value in the edit text
                        NumberFormat numberFormat = new DecimalFormat("00");
                        tanggal = year + "-" + numberFormat.format(( month_of_year +1 )) + "-" +
                                numberFormat.format(day_of_month);
                        edit_tanggal.setText(numberFormat.format(day_of_month) + "/" + numberFormat.format(( month_of_year +1 )) +
                                "/" + year );
                    }
                }, CurrentDate.year, CurrentDate.month, CurrentDate.day);
                datePickerDialog.show();
            }
        });

        rip_simpan.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                if (status.equals("") || edit_jumlah.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Isi data dengan benar",
                            Toast.LENGTH_LONG).show();
                } else {

                    AndroidNetworking.post( Config.host + "update.php")
                            .addBodyParameter("transaksi_id",   M.transaksi_id)
                            .addBodyParameter("status",         status)
                            .addBodyParameter("nim2",           nim2)
                            .addBodyParameter("jumlah",         edit_jumlah.getText().toString())
                            .addBodyParameter("keterangan",     edit_keterangan.getText().toString())
                            .addBodyParameter("tanggal",        tanggal)
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    // do anything with response
                                    Log.d("response", response.toString() );

                                    if (response.optString("response").equals("success")){
                                        Toast.makeText(getApplicationContext(), "Perubahan berhasil disimpan",
                                                Toast.LENGTH_LONG).show();
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Gagal",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                                @Override
                                public void onError(ANError error) {
                                    // handle error
                                }
                            });
                }
            }
        });

    }
}
