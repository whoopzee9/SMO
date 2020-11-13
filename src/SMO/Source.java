package SMO;

import java.util.ArrayList;

public class Source {
    private int number;
    private int totalAmount;
    private int procAmount;
    private int deniedAmount;
    //private double tPost;
    private Request currRequest;
    private double tWait;
    private double avgTWait; //TODO Нужно ли их хранить?
    private double tPreb; //TODO придумать нормальное имя
    private double avgTPreb; //TODO нужно ли хранить?
    private double avgServiceTime;
    private double TWaitDispersion;
    private double TServiceDispersion;
    private double deniedProb;
    private double lambda;
    private ArrayList<Double> tWaitList;
    private ArrayList<Double> tServiceList;

    public Source(int num, double lam) {
        number = num;
        totalAmount = 0;
        procAmount = 0;
        deniedAmount = 0;
        //tPost = 0;
        tWait = 0;
        avgTWait = 0;
        tPreb = 0;
        avgTPreb = 0;
        deniedProb = 0;
        lambda = lam;
        tWaitList = new ArrayList<>();
        tServiceList = new ArrayList<>();
        currRequest = new Request(number, totalAmount, 0);
    }

    public void generateNewRequest() {
        double t = Math.random();
        double tau = -1 / lambda * Math.log(t);
        double tPost = currRequest.gettPost();
        tPost += tau;
        totalAmount++;
        currRequest = new Request(number, totalAmount, tPost);
    }

    public void handleRequestResults(Request request) {
        if (request != null) {
            procAmount += request.getProc();
            deniedAmount += request.getDenied();
            if (request.getDenied() == 0) {   //TODO узнать правильно ли это
                tWait += request.getTWait();
                tPreb += request.getTPreb();
                tWaitList.add(request.getTWait());
                tServiceList.add(request.getTPreb() - request.getTWait());
                //System.out.println("tPreb: " + request.getTPreb());
                //System.out.println("tWait: " + request.getTWait());
            }
        }
    }

    /*public double gettPost() {
        return tPost;
    }*/

    public int getNumber() {
        return number;
    }

    public int getTotalAmount() {
        return totalAmount - 1;
    }

    public int getProcAmount() {
        return procAmount;
    }

    public int getDeniedAmount() {
        return deniedAmount;
    }

    public Request getCurrRequest() {
        return currRequest;
    }

    public double gettPreb() {
        return tPreb;
    }

    public double gettWait() {
        return tWait;
    }

    public double getAvgTPreb() {
        return tPreb / procAmount;
    }

    public double getAvgTWait() {
        return tWait / procAmount;
    }

    public double getDeniedProb() {
        return (double)deniedAmount / (totalAmount - 1) * 100;
    }

    public double getAvgServiceTime() {
        return (tPreb - tWait) / procAmount; //TODO поменять tPreb и TWait (к каким заявкам они относятся)
    }

    public double getTWaitDispersion() {
        double avg = tWait / procAmount;
        double sum = 0;
        for (Double time : tWaitList) {
            sum += (time - avg) * (time - avg);
        }
        return sum / tWaitList.size();
    }

    public double getTServiceDispersion() {
        double avg = (tPreb - tWait) / procAmount;
        double sum = 0;
        for (Double time : tServiceList) {
            sum += (time - avg) * (time - avg);
        }
        return sum / tServiceList.size();
    }


}
