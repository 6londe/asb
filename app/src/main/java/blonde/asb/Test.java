package blonde.asb;

class Test {

    private int repeat[] = { 2000, 4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000 };

    void run(int test_case, DatabaseHelper databaseHelper) {

        switch (test_case) {
            case 0:
                databaseHelper.preparing(repeat);
                break;
            case 1:
                databaseHelper.test_case_1(repeat[0]);
                break;
            case 2:
                databaseHelper.test_case_2(repeat[1]);
                break;
            case 3:
                databaseHelper.test_case_3(repeat[2]);
                break;
            case 4:
                databaseHelper.test_case_4(repeat[3]);
                break;
            case 5:
                databaseHelper.test_case_5(repeat[4]);
                break;
            case 6:
                databaseHelper.test_case_6();
                break;
            case 7:
                databaseHelper.test_case_7(repeat[5]);
                break;
            case 8:
                databaseHelper.test_case_8(repeat[6]);
                break;
            case 9:
                databaseHelper.test_case_9(repeat[7]);
                break;
            case 10:
                databaseHelper.test_case_10(repeat[8]);
                break;
            case 11:
                databaseHelper.test_case_11();
                break;
            case 12:
                databaseHelper.test_case_12(repeat[9]);
                break;
            case 13:
                databaseHelper.test_case_13(repeat[10]);
                break;
            case 14:
                databaseHelper.test_case_14();
                break;
        }
    }
}
