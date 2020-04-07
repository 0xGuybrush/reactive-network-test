package ie.davidmoloney.reactivenetworktest;

import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public abstract class BaseActivity extends AppCompatActivity {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    protected void init(final int layout, final Class<? extends BaseActivity> targetActivity, final String heading, final String text) {
        /**
         * Set up layout & FAB.
         */
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
        if (compositeDisposable == null || compositeDisposable.isDisposed()) {
            compositeDisposable = new CompositeDisposable();
        }

        final Observable<Boolean> hasInternet = ReactiveNetwork
                .observeNetworkConnectivity(getApplicationContext())
                .switchMapSingle(connectivity -> ReactiveNetwork.checkInternetConnectivity());

        final Disposable toastWhenHasInternet = hasInternet.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(this::toastIfYes, System.err::println);
        final Disposable toastWhenNoInternet = hasInternet.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(this::toastIfNo, System.err::println);

        compositeDisposable.add(toastWhenHasInternet);
        compositeDisposable.add(toastWhenNoInternet);
    }

    private void toastIfNo(final boolean hasInternet) {
        if (!hasInternet) {
            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void toastIfYes(final boolean hasInternet) {
        if (hasInternet) {
            Toast.makeText(getApplicationContext(), "Has internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        compositeDisposable.dispose();
    }
}
