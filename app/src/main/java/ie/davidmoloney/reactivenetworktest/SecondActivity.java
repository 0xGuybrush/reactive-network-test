package ie.davidmoloney.reactivenetworktest;

import android.os.Bundle;

public class SecondActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(R.layout.activity_second, MainActivity.class, "Main Activity", "Go to first Activity");
    }
}
