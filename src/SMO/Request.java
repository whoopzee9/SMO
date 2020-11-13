package SMO;

public class Request {
    private int sourceNum;
    private int requestNum;
    private double tPost;
    private double tBuffPost;
    private double tBuffExit;
    private double tEnd;
    private int proc;
    private int denied;

    public Request(int sourceNum, int requestNum, double time) {
        this.sourceNum = sourceNum;
        this.requestNum = requestNum;
        tPost = time;
        tBuffPost = 0;
        tBuffExit = 0;
        tEnd = 0;
        proc = 0;
        denied = 0;
    }

    public double gettPost() {
        return tPost;
    }

    public int getSourceNum() {
        return sourceNum;
    }

    public int getDenied() {
        return denied;
    }

    public int getProc() {
        return proc;
    }

    public void setProc() {
        this.proc = 1;
    }

    public void setDenied() {
        this.denied = 1;
    }

    public void settBuffPost(double tBuffPost) {
        this.tBuffPost = tBuffPost;
    }

    public void settBuffExit(double tBuffExit) {
        this.tBuffExit = tBuffExit;
    }

    public void settEnd(double tEnd) {
        this.tEnd = tEnd;
    }

    public double getServiceStartTime() {
        return (tBuffExit == 0) ? tPost : tBuffExit;
    }

    public int getRequestNum() {
        return requestNum;
    }

    public double getTWait() {
        return tBuffExit - tBuffPost;
    }

    public double gettEnd() {
        return tEnd;
    }

    public double getTPreb() {
        //System.out.println("tEnd: " + tEnd + " tPost: " + tPost);
        return tEnd - tPost;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        Request req = (Request) obj;
        return sourceNum == req.sourceNum && requestNum == req.requestNum && tPost == req.tPost && tBuffPost == req.tBuffPost
            && tBuffExit == req.tBuffExit && tEnd == req.tEnd && proc == req.proc && denied == req.denied;
    }

    @Override
    public String toString() {
        return String.valueOf(sourceNum) + "." + String.valueOf(requestNum);
    }
}
