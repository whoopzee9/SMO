package SMO;

public class BufferElement {
    private int num;
    private Request request;
    private double requestTime;

    public BufferElement(int num, Request request) {
        this.num = num;
        this.request = request;
        this.requestTime = request.gettPost();
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public double getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(double requestTime) {
        this.requestTime = requestTime;
    }
}
