package ie.davidmoloney.reactivenetworktest;

import android.os.Bundle;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(R.layout.activity_main, SecondActivity.class, "2nd Activity", "Go to second Activity");
    }
}
