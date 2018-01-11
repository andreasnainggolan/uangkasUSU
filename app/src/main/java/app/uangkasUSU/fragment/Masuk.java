package app.uangkasUSU.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

import app.uangkasUSU.R;
import app.uangkasUSU.helper.Config;

/**
 * A simple {@link Fragment} subclass.
 */
public class Masuk extends Fragment {

    EditText edit_jumlah, edit_keterangan;
    Button btn_simpan;
    RippleView rip_simpan;

    SharedPreferences sharedPreferences;
    String nim2;

    public Masuk() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_masuk, container, false);

        edit_jumlah     = (EditText) view.findViewById(R.id.edit_jumlah);
        edit_keterangan = (EditText) view.findViewById(R.id.edit_keterangan);
        btn_simpan      = (Button) view.findViewById(R.id.btn_simpan);
        rip_simpan      = (RippleView) view.findViewById(R.id.rip_simpan);

        sharedPreferences = this.getActivity().getSharedPreferences("uangkasUSU", Context.MODE_PRIVATE);
        nim2 = sharedPreferences.getString("nim", "");

        rip_simpan.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                if (edit_jumlah.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "Isi data dengan benar",
                            Toast.LENGTH_LONG).show();
                } else {
                    _save();
                }
            }
        });

        return view;
    }

    private void _save(){
        AndroidNetworking.post( Config.host + "add.php")
                .addBodyParameter("status", "MASUK")
                .addBodyParameter("nim2", nim2)
                .addBodyParameter("jumlah", edit_jumlah.getText().toString())
                .addBodyParameter("keterangan", edit_keterangan.getText().toString())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response

                        Log.d("response", response.toString() );

                        if (response.optString("response").equals("success")){
                            Toast.makeText(getActivity(), "Berhasil disimpan",
                                    Toast.LENGTH_LONG).show();

                            getActivity().finish();
                        } else {
                            Toast.makeText(getActivity(), "Gagal",
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
