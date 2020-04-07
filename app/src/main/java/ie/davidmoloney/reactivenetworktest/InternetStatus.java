package ie.davidmoloney.reactivenetworktest;

import android.content.Context;

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.github.pwittchen.reactivenetwork.library.rx2.internet.observing.InternetObservingSettings;
import com.github.pwittchen.reactivenetwork.library.rx2.internet.observing.strategy.SocketInternetObservingStrategy;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class InternetStatus {

    private final Observable<Boolean> isAvailable;

    public InternetStatus(final Context context) {
        final InternetObservingSettings settings = InternetObservingSettings.builder()
                .host("www.example.com")
                .strategy(new SocketInternetObservingStrategy())
                .build();

        final Context applicationContext = context.getApplicationContext();
        isAvailable = ReactiveNetwork.observeNetworkConnectivity(applicationContext)
                .doOnSubscribe(subscription -> System.out.println(String.format("DEBUG::: ReactiveNetwork.observeNetworkConnectivity - subscribed. :%s", subscription)))
                .doOnError(error -> System.out.println(String.format("DEBUG::: ReactiveNetwork.observeNetworkConnectivity - errored. %s", error.getMessage())))
                .doOnComplete(() -> System.out.println("DEBUG::: ReactiveNetwork.observeNetworkConnectivity - completed"))
                .doOnTerminate(() -> System.out.println("DEBUG::: ReactiveNetwork.observeNetworkConnectivity - terminated"))
                .switchMapSingle(connectivity -> ReactiveNetwork.checkInternetConnectivity(settings))
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(subscription -> System.out.println(String.format("DEBUG::: ReactiveNetwork.checkInternetConnectivity - subscribed. :%s", subscription)))
                .doOnError(error -> System.out.println(String.format("DEBUG::: ReactiveNetwork.checkInternetConnectivity - errored. %s", error.getMessage())))
                .doOnComplete(() -> System.out.println("DEBUG::: ReactiveNetwork.checkInternetConnectivity - completed"))
                .doOnTerminate(() -> System.out.println("DEBUG::: ReactiveNetwork.checkInternetConnectivity - terminated"))
                .distinctUntilChanged()
                .doOnSubscribe(subscription -> System.out.println(String.format("DEBUG::: Subscribed to internet status. :%s", subscription)))
                .doOnNext(hasInternet -> {
                    System.out.println(String.format("DEBUG::: Internet status :%s", hasInternet));
                })
                .doOnComplete(() -> System.out.println("DEBUG::: InternetStatus completed"))
                .doOnError(error -> System.out.println(String.format("DEBUG::: InternetStatus errored %s", error.getMessage())))
                .doOnTerminate(() -> System.out.println("DEBUG::: InternetStatus terminated"));
    }

    public Observable<Boolean> pollWhileInternetAvailable() {
        final long noInternetIndex = -1L;

        return Observable.combineLatest(
                Observable.interval(0, 5, TimeUnit.SECONDS), isAvailable,
                (counter, hasInternet) -> hasInternet ? counter : noInternetIndex
        )
                .distinctUntilChanged()
                .map(counter -> counter > noInternetIndex)
                .doOnSubscribe(subscription -> System.out.println(String.format("DEBUG::: Subscribed to InternetStatus.pollWhileInternetAvailable. :%s", subscription)))
                .doOnNext(next -> System.out.println(String.format("DEBUG::: InternetStatus.pollWhileInternetAvailable :%s", next)))
                .doOnComplete(() -> System.out.println("DEBUG::: InternetStatus.pollWhileInternetAvailable completed"))
                .doOnError(error -> System.out.println(String.format("DEBUG::: InternetStatus.pollWhileInternetAvailable errored, %s", error.getMessage())))
                .doOnTerminate(() -> System.out.println("DEBUG::: InternetStatus.pollWhileInternetAvailable terminated"));
    }

    public Observable<Boolean> isAvailable() {
        return isAvailable;
    }
}

