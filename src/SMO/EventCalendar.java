package SMO;

public class EventCalendar {
    private String type;
    private Double time;
    private Request request;
    private int sign;

    public EventCalendar(String type, Double time, Request request, int sign) {
        this.type = type;
        this.time = time;
        this.request = request;
        this.sign = sign;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getTime() {
        return time;
    }

    public void setTime(Double time) {
        this.time = time;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public int getSign() {
        return sign;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        EventCalendar elem = (EventCalendar) obj;

        boolean boolReq = request == null && elem.request == null;
        if (!boolReq && request != null && elem.request != null) {
            boolReq = request.equals(elem.request);
        }

        return type.equals(elem.type) && time.equals(elem.time) && boolReq;
    }
}
