package com.hpi.hpiUtils;

/**
 * sCLIProgressBar is from the command line parameter, --progressBar, either
 * true or false. Determines whether the bar is shown or not.
 */
public class CMProgressBarCLI {

    private final String sCLIPProgressBar;
    private StringBuilder progress;

    /**
     * initialize progress bar properties.
     *
     * @param sCLIProgressBar
     */
    public CMProgressBarCLI(String sCLIProgressBar) {
        this.sCLIPProgressBar = sCLIProgressBar;
        initCustom();
    }

    private void initCustom() {
        this.progress = new StringBuilder(60);
    }

    /**
     * called whenever the progress bar needs to be updated.
     *
     * @param done  an int representing the work done so far
     * @param total an int representing the total
     * @param label
     */
    private void update(int done, int total, String label) {
        if (total == 0) {
            return;
        }
        char[] workchars = {
            '|', '/', '-', '\\'
        };
        String format = "\r%s:\t%3d%% %s %c";

        int percent = (++done * 100) / total;
        int extrachars = (percent / 3) - this.progress.length();

        while (extrachars-- > 0) {
            this.progress.append('#');
        }

        System.out.printf(format, label, percent, progress,
            workchars[done % workchars.length]);

        if (done == total) {
            System.out.flush();
            System.out.println();
            this.initCustom();
        }
    }
    
    public void barUpdate(Integer numerator, Integer denominator){
        this.barUpdate(numerator, denominator, "");
    }

    public void barUpdate(Integer numerator, Integer denominator,
        String label) {
        if (this.sCLIPProgressBar.equalsIgnoreCase("false")) {
            //not showing the bar
            return;
        }

        this.update(numerator, denominator, label);
    }

    public void barLabel(String sLabel) {
        // barLabel change indicates new progress bar
        this.initCustom();

        if (this.sCLIPProgressBar.equalsIgnoreCase("true")) {
            System.out.print(System.lineSeparator());
            System.out.print(sLabel);
            System.out.print(System.lineSeparator());
        }
    }
}
