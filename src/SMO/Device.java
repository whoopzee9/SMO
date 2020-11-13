package SMO;

public class Device {
    private int number;
    private double tServiceStart;
    private double tOsv;
    private double tau;
    private double tWork;
    private double tTotal;
    private double utilizationRate;
    private int sign; //Признак
    private Request currRequest;

    public Device(int num, double tau) {
        number = num;
        this.tau = tau;
        tServiceStart = 0;
        tOsv = 0;
        tWork = 0;
        tTotal = 0;
        sign = 1;
    }

    public void service(Request request) {
        tServiceStart = request.getServiceStartTime();
        tOsv = tServiceStart + tau;
        currRequest = request;
        request.setProc();
        request.settEnd(tOsv);
        tTotal = tOsv;
        tWork += tau;
        sign = 0;
    }

    public double gettOsv() {
        return tOsv;
    }

    public void setSign(int sign) {
        this.sign = sign;
        if (sign == 1) {
            tOsv = 0;
        }
    }

    public int getSign() {
        return sign;
    }

    public double gettTotal() {
        return tTotal;
    }

    public double gettWork() {
        return tWork;
    }

    public double getUtilizationRate() {
        return tWork / tTotal;
    }

    public int getNumber() {
        return number;
    }

    public Request getCurrRequest() {
        return currRequest;
    }
}
