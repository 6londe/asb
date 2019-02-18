package blonde.asb;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.Arrays;

public class MainActivity extends Activity {

    private AdView mAdView;
    DatabaseHelper databaseHelper;
    Task task[];
    TextView result[];
    TextView overall;
    Button startButton, cancelButton;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, getString(R.string.admob_app_id));
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        databaseHelper = new DatabaseHelper(this);

        task = new Task[15];
        Arrays.fill(task, null);

        result = new TextView[15];
        result[1] = this.findViewById(R.id.test1);
        result[2] = this.findViewById(R.id.test2);
        result[3] = this.findViewById(R.id.test3);
        result[4] = this.findViewById(R.id.test4);
        result[5] = this.findViewById(R.id.test5);
        result[6] = this.findViewById(R.id.test6);
        result[7] = this.findViewById(R.id.test7);
        result[8] = this.findViewById(R.id.test8);
        result[9] = this.findViewById(R.id.test9);
        result[10] = this.findViewById(R.id.test10);
        result[11] = this.findViewById(R.id.test11);
        result[12] = this.findViewById(R.id.test12);
        result[13] = this.findViewById(R.id.test13);
        result[14] = this.findViewById(R.id.test14);

        overall = this.findViewById(R.id.overall);
        startButton = this.findViewById(R.id.start);
        cancelButton = this.findViewById(R.id.cancel);

        cancelButton.setEnabled(false);

        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                overall.setTextColor(Color.BLACK);
                startButton.setEnabled(false);
                cancelButton.setEnabled(true);

                for (int test_case = 0; test_case <= 14; test_case++) {
                    task[test_case] = new Task(databaseHelper, test_case, new UIHandler() {

                        public void waiting(int tc) {
                            if (tc == 0) startButton.setText(getString(R.string.waiting));
                            else result[tc].setText(getString(R.string.waiting));
                        }

                        public void running(int tc) {
                            if (tc == 0) {
                                startButton.setTextColor(Color.RED);
                                startButton.setText(getString(R.string.preparing));
                            } else {
                                result[tc].setTextColor(Color.RED);
                                result[tc].setText(getString(R.string.running));
                            }
                        }

                        public void ending(int tc, Long runtime) {
                            if (tc == 0) {
                                startButton.setText(getString(R.string.benchmarking));
                            } else {
                                if (tc == 1) {
                                    overall.setTextColor(Color.BLACK);
                                    overall.setText(runtime.toString());
                                } else if (tc == 14) {
                                    overall.setText(overall.getText() + " ms");
                                    overall.setTextColor(Color.RED);

                                    startButton.setTextColor(Color.WHITE);
                                    startButton.setText("Start Benchmark");

                                    startButton.setEnabled(true);
                                    cancelButton.setEnabled(false);
                                } else {
                                    Long sum = Long.parseLong((String) overall.getText()) + runtime;
                                    overall.setText(sum.toString());
                                }
                                result[tc].setTextColor(Color.WHITE);
                                result[tc].setText(runtime + " ms");
                            }
                        }

                        public void canceling(int tc) {
                            if (tc != 0) {
                                result[tc].setTextColor(Color.WHITE);
                                result[tc].setText(getString(R.string.canceled));
                            }

                            startButton.setTextColor(Color.WHITE);
                            startButton.setText(getString(R.string.start));

                            startButton.setEnabled(true);
                            cancelButton.setEnabled(false);
                        }
                    });

                    task[test_case].cancelRequest = false;
                    task[test_case].execute();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                for (int test_case = 0; test_case <= 14; test_case++) {
                    task[test_case].cancel(false);
                    task[test_case].cancelRequest = true;
                }
            }
        });
    }

    interface UIHandler {
        void waiting(int tc);
        void running(int tc);
        void ending(int tc, Long runtime);
        void canceling(int tc);
    }
}