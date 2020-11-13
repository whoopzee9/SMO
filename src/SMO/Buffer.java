package SMO;

import java.util.LinkedList;

public class Buffer {
    private int N;
    private LinkedList<BufferElement> buff;

    public Buffer(int n) {
        N = n;
        buff = new LinkedList<>();
    }

    public Request addRequestToBuffer(Request req) {
        Request r = null;
        if (buff.size() == N) {
            r = buff.getFirst().getRequest();
            r.settEnd(req.gettPost());
            r.settBuffExit(req.gettPost());
            r.setDenied();
            buff.removeFirst();
        }
        req.settBuffPost(req.gettPost());
        BufferElement elem = new BufferElement(buff.size(), req);
        buff.add(elem);
        return r;
    }

    public Request getRequestFromBuffer(double time) {
        Request req = buff.removeLast().getRequest();
        req.settBuffExit(time);
        return req;
    }

    public boolean isEmpty() {
        return buff.isEmpty();
    }

    public int size() {
        return buff.size();
    }

    public void displayBuffer() {
        System.out.println("\n SMO.Buffer: ");
        for (int i = 0; i < buff.size(); i++) {
            Request r = buff.get(i).getRequest();
            System.out.println(buff.get(i).getNum() + ": SMO.Source: " + r.getSourceNum() + ", Num: " + r.getRequestNum());
        }
    }

    public LinkedList<BufferElement> getElements() {
        return buff;
    }
}
