package ie.davidmoloney.reactivenetworktest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public abstract class BaseActivity extends AppCompatActivity {

    private InternetStatus internetStatus;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    protected void init(final int layout, final Class<? extends BaseActivity> targetActivity, final String heading, final String text) {
        setContentView(layout);
        setSupportActionBar(findViewById(R.id.toolbar));

        this.<FloatingActionButton>findViewById(R.id.fab).setOnClickListener(view -> {
            Snackbar.make(view, heading, Snackbar.LENGTH_LONG).setAction(text, v -> {
                Intent intent = new Intent(this, targetActivity);
                startActivity(intent);
                finish();
            }).show();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        internetStatus = new InternetStatus(getApplicationContext());
        if (compositeDisposable == null || compositeDisposable.isDisposed()) {
            compositeDisposable = new CompositeDisposable();
        }

        compositeDisposable.add(toastWhenNoInternet(getApplicationContext()));
        compositeDisposable.add(internetStatus.pollWhileInternetAvailable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(hasInternet -> {
                    if (hasInternet) {
                        final String still_has_internet = String.format("Still has internet? %s", hasInternet);
                        Toast.makeText(getApplicationContext(), still_has_internet, Toast.LENGTH_SHORT).show();
                    }
                }, System.err::println)

        );
    }

    @Override
    public void onPause() {
        super.onPause();
        compositeDisposable.dispose();
        internetStatus = null;
    }

    private Disposable toastWhenNoInternet(@NonNull final Context context) {
        return  internetStatus.isAvailable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(hasInternet -> {
                    if (!hasInternet) {
                        Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
                    }
                }, System.err::println);
    }
}
