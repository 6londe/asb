package blonde.asb;

import android.os.AsyncTask;

public class Task extends AsyncTask<Void, Void, Long> {

    private DatabaseHelper databaseHelper;
    private MainActivity.UIHandler uiHandler;
    private int testCase;

    boolean cancelRequest = false;

    Task(DatabaseHelper databaseHelper, int testCase, MainActivity.UIHandler uiHandler) {
        this.testCase = testCase;
        this.databaseHelper = databaseHelper;
        this.uiHandler = uiHandler;
    }

    protected void onPreExecute() {

        if (cancelRequest) this.cancel(false);

        uiHandler.waiting(testCase);
    }

    protected void onProgressUpdate(Void... voids) {
        uiHandler.running(testCase);
    }

    protected Long doInBackground(Void... voids) {
        Test benchmark = new Test();
        long timeStart, timeEnd;

        publishProgress();
        timeStart = System.currentTimeMillis();
        benchmark.run(testCase, databaseHelper);
        timeEnd = System.currentTimeMillis();

        return (timeEnd - timeStart);
    }

    protected void onPostExecute(Long runtime) {
        uiHandler.ending(testCase, runtime);
    }

    protected void onCancelled() {
        uiHandler.canceling(testCase);
    }
}