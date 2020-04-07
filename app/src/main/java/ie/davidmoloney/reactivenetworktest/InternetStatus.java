package ie.davidmoloney.reactivenetworktest;

import android.content.Context;

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.github.pwittchen.reactivenetwork.library.rx2.internet.observing.InternetObservingSettings;
import com.github.pwittchen.reactivenetwork.library.rx2.internet.observing.strategy.SocketInternetObservingStrategy;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

class InternetStatus {

    private final Observable<Boolean> isAvailable;

    InternetStatus(final Context context) {
        final InternetObservingSettings settings = InternetObservingSettings
                                                        .builder()
                                                        .host("www.example.com")
                                                        .strategy(new SocketInternetObservingStrategy())
                                                        .build();

        final Context applicationContext = context.getApplicationContext();

        isAvailable = ReactiveNetwork
                        .observeNetworkConnectivity(applicationContext)
                        .switchMapSingle(connectivity -> ReactiveNetwork.checkInternetConnectivity(settings))
                        .subscribeOn(Schedulers.io())
                        .distinctUntilChanged();
    }

    Observable<Boolean> pollWhileInternetAvailable() {
        final long noInternetIndex = -1L;

        return Observable.combineLatest(
                    Observable.interval(0, 5, TimeUnit.SECONDS), isAvailable,
                    (counter, hasInternet) -> hasInternet ? counter : noInternetIndex
                )
                .distinctUntilChanged()
                .map(counter -> counter > noInternetIndex);
    }

    Observable<Boolean> isAvailable() {
        return isAvailable;
    }
}

