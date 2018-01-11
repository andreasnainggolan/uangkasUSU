package app.uangkasUSU;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class IntroActivity extends AppIntro {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(AppIntroFragment.newInstance("SELAMAT DATANG", "Aplikasi yang mampu mengatur keuangan anda!", R.drawable.uk_usu, getResources().getColor(R.color.colorPrimary)));
        addSlide(AppIntroFragment.newInstance("Pengenalan", "Aplikasi yang mampu mengatur keuangan anda! Aplikasinya khusus untuk mahasiswa USU",
                R.drawable.person, getResources().getColor(R.color.colorPrimary)));
        addSlide(AppIntroFragment.newInstance("UangKas USU", "Mari memulai menggunakan aplikasi ini.", R.drawable.uk_usu, getResources().getColor(R.color.colorPrimary)));

        setBarColor(getResources().getColor(R.color.colorPrimary));
        setSeparatorColor(getResources().getColor(R.color.colorBackground));

        // Hide Skip/Done button.
        showSkipButton(true);
        setProgressButtonEnabled(true);

        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permission in Manifest.
        setVibrate(true);
        setVibrateIntensity(30);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}
