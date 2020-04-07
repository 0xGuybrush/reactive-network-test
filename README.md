# reactive-network-test

Repo to provide a minimal, complete, verifiable example of ReactiveNetwork issue

## Steps to reproduce

1. Build with Gradle and install on a device with debugging enabled
2. Open the app.
3. Open logcat
4. Notice that there are two subscriptions to the network state: 
   * One that will toast once if internet is lost, one that will poll every five seconds while internet is available
   * Both are using the field `internetStatus` at the top of their Observable chain.
5. Click the floating action button and go to the Second Activity.

## Current outcome 

6. Notice that Logcat has recorded an error:

```
2020-04-07 14:28:49.155 19793-19793/ie.davidmoloney.reactivenetworktest E/ReactiveNetwork: could not unregister network callback
    java.lang.IllegalArgumentException: NetworkCallback was already unregistered
        at com.android.internal.util.Preconditions.checkArgument(Preconditions.java:47)
        at android.net.ConnectivityManager.unregisterNetworkCallback(ConnectivityManager.java:3496)
        at com.github.pwittchen.reactivenetwork.library.rx2.network.observing.strategy.MarshmallowNetworkObservingStrategy.tryToUnregisterCallback(MarshmallowNetworkObservingStrategy.java:140)
        at com.github.pwittchen.reactivenetwork.library.rx2.network.observing.strategy.MarshmallowNetworkObservingStrategy$3.run(MarshmallowNetworkObservingStrategy.java:84)
        at io.reactivex.internal.operators.flowable.FlowableDoOnLifecycle$SubscriptionLambdaSubscriber.cancel(FlowableDoOnLifecycle.java:115)
        at io.reactivex.internal.subscribers.BasicFuseableSubscriber.cancel(BasicFuseableSubscriber.java:158)
        at io.reactivex.internal.operators.flowable.FlowableFlatMap$MergeSubscriber.cancel(FlowableFlatMap.java:358)
        at io.reactivex.internal.subscriptions.SubscriptionArbiter.drainLoop(SubscriptionArbiter.java:221)
        at io.reactivex.internal.subscriptions.SubscriptionArbiter.drain(SubscriptionArbiter.java:190)
        at io.reactivex.internal.subscriptions.SubscriptionArbiter.cancel(SubscriptionArbiter.java:182)
        at io.reactivex.internal.subscribers.BasicFuseableSubscriber.cancel(BasicFuseableSubscriber.java:158)
        at io.reactivex.internal.operators.observable.ObservableFromPublisher$PublisherSubscriber.dispose(ObservableFromPublisher.java:70)
        at io.reactivex.internal.operators.mixed.ObservableSwitchMapSingle$SwitchMapSingleMainObserver.dispose(ObservableSwitchMapSingle.java:165)
        at io.reactivex.internal.disposables.DisposableHelper.dispose(DisposableHelper.java:124)
        at io.reactivex.internal.operators.observable.ObservableSubscribeOn$SubscribeOnObserver.dispose(ObservableSubscribeOn.java:73)
        at io.reactivex.internal.observers.BasicFuseableObserver.dispose(BasicFuseableObserver.java:152)
        at io.reactivex.internal.disposables.DisposableHelper.dispose(DisposableHelper.java:124)
        at io.reactivex.internal.operators.observable.ObservableSubscribeOn$SubscribeOnObserver.dispose(ObservableSubscribeOn.java:73)
        at io.reactivex.internal.operators.observable.ObservableObserveOn$ObserveOnObserver.dispose(ObservableObserveOn.java:146)
        at io.reactivex.internal.disposables.DisposableHelper.dispose(DisposableHelper.java:124)
        at io.reactivex.internal.observers.LambdaObserver.dispose(LambdaObserver.java:102)
        at io.reactivex.disposables.CompositeDisposable.dispose(CompositeDisposable.java:240)
        at io.reactivex.disposables.CompositeDisposable.dispose(CompositeDisposable.java:82)
        at ie.davidmoloney.reactivenetworktest.BaseActivity.onPause(BaseActivity.java:65)
        at android.app.Activity.performPause(Activity.java:7337)
        at android.app.Instrumentation.callActivityOnPause(Instrumentation.java:1487)
        at android.app.ActivityThread.performPauseActivityIfNeeded(ActivityThread.java:4241)
        at android.app.ActivityThread.performPauseActivity(ActivityThread.java:4206)
        at android.app.ActivityThread.handlePauseActivity(ActivityThread.java:4158)
        at android.app.servertransaction.PauseActivityItem.execute(PauseActivityItem.java:45)
        at android.app.servertransaction.TransactionExecutor.executeLifecycleState(TransactionExecutor.java:145)
        at android.app.servertransaction.TransactionExecutor.execute(TransactionExecutor.java:70)
        at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1977)
        at android.os.Handler.dispatchMessage(Handler.java:106)
        at android.os.Looper.loop(Looper.java:193)
        at android.app.ActivityThread.main(ActivityThread.java:6923)
        at java.lang.reflect.Method.invoke(Native Method)
        at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:493)
        at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:870)
2020-04-07 14:28:49.161 19793-19793/ie.davidmoloney.reactivenetworktest E/ReactiveNetwork: could not unregister receiver
    java.lang.IllegalArgumentException: Receiver not registered: com.github.pwittchen.reactivenetwork.library.rx2.network.observing.strategy.MarshmallowNetworkObservingStrategy$4@fafb087
        at android.app.LoadedApk.forgetReceiverDispatcher(LoadedApk.java:1280)
        at android.app.ContextImpl.unregisterReceiver(ContextImpl.java:1519)
        at android.content.ContextWrapper.unregisterReceiver(ContextWrapper.java:659)
        at com.github.pwittchen.reactivenetwork.library.rx2.network.observing.strategy.MarshmallowNetworkObservingStrategy.tryToUnregisterReceiver(MarshmallowNetworkObservingStrategy.java:148)
        at com.github.pwittchen.reactivenetwork.library.rx2.network.observing.strategy.MarshmallowNetworkObservingStrategy$3.run(MarshmallowNetworkObservingStrategy.java:85)
        at io.reactivex.internal.operators.flowable.FlowableDoOnLifecycle$SubscriptionLambdaSubscriber.cancel(FlowableDoOnLifecycle.java:115)
        at io.reactivex.internal.subscribers.BasicFuseableSubscriber.cancel(BasicFuseableSubscriber.java:158)
        at io.reactivex.internal.operators.flowable.FlowableFlatMap$MergeSubscriber.cancel(FlowableFlatMap.java:358)
        at io.reactivex.internal.subscriptions.SubscriptionArbiter.drainLoop(SubscriptionArbiter.java:221)
        at io.reactivex.internal.subscriptions.SubscriptionArbiter.drain(SubscriptionArbiter.java:190)
        at io.reactivex.internal.subscriptions.SubscriptionArbiter.cancel(SubscriptionArbiter.java:182)
        at io.reactivex.internal.subscribers.BasicFuseableSubscriber.cancel(BasicFuseableSubscriber.java:158)
        at io.reactivex.internal.operators.observable.ObservableFromPublisher$PublisherSubscriber.dispose(ObservableFromPublisher.java:70)
        at io.reactivex.internal.operators.mixed.ObservableSwitchMapSingle$SwitchMapSingleMainObserver.dispose(ObservableSwitchMapSingle.java:165)
        at io.reactivex.internal.disposables.DisposableHelper.dispose(DisposableHelper.java:124)
        at io.reactivex.internal.operators.observable.ObservableSubscribeOn$SubscribeOnObserver.dispose(ObservableSubscribeOn.java:73)
        at io.reactivex.internal.observers.BasicFuseableObserver.dispose(BasicFuseableObserver.java:152)
        at io.reactivex.internal.disposables.DisposableHelper.dispose(DisposableHelper.java:124)
        at io.reactivex.internal.operators.observable.ObservableSubscribeOn$SubscribeOnObserver.dispose(ObservableSubscribeOn.java:73)
        at io.reactivex.internal.operators.observable.ObservableObserveOn$ObserveOnObserver.dispose(ObservableObserveOn.java:146)
        at io.reactivex.internal.disposables.DisposableHelper.dispose(DisposableHelper.java:124)
        at io.reactivex.internal.observers.LambdaObserver.dispose(LambdaObserver.java:102)
        at io.reactivex.disposables.CompositeDisposable.dispose(CompositeDisposable.java:240)
        at io.reactivex.disposables.CompositeDisposable.dispose(CompositeDisposable.java:82)
        at ie.davidmoloney.reactivenetworktest.BaseActivity.onPause(BaseActivity.java:65)
        at android.app.Activity.performPause(Activity.java:7337)
        at android.app.Instrumentation.callActivityOnPause(Instrumentation.java:1487)
        at android.app.ActivityThread.performPauseActivityIfNeeded(ActivityThread.java:4241)
        at android.app.ActivityThread.performPauseActivity(ActivityThread.java:4206)
        at android.app.ActivityThread.handlePauseActivity(ActivityThread.java:4158)
        at android.app.servertransaction.PauseActivityItem.execute(PauseActivityItem.java:45)
        at android.app.servertransaction.TransactionExecutor.executeLifecycleState(TransactionExecutor.java:145)
        at android.app.servertransaction.TransactionExecutor.execute(TransactionExecutor.java:70)
        at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1977)
        at android.os.Handler.dispatchMessage(Handler.java:106)
        at android.os.Looper.loop(Looper.java:193)
        at android.app.ActivityThread.main(ActivityThread.java:6923)
        at java.lang.reflect.Method.invoke(Native Method)
        at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:493)
        at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:870)
```

7. Notice that error does not occur if one of the disposables is commented out — Disposable is then cleaned up correctly and can change between activities without error


## Desired outcome 

* Can use one observable in both Disposables without error 
