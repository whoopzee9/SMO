package SMO;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Scanner;

public class Calculations extends Thread {

    private int bufferSize;
    private long requestAmount;
    private boolean stepsFlag;
    private final Object monitor;

    private ArrayList<Source> sources;
    private ArrayList<Device> devices;
    private Buffer buffer;
    private ArrayList<Source> startSources;
    private ArrayList<Device> startDevices;
    private Buffer startBuffer;
    private PropertyChangeSupport support = new PropertyChangeSupport(this);


    public Calculations(ArrayList<Source> sources, ArrayList<Device> devices, int bufferSize, int requestAmount, Object monitor, boolean stepFlag) {

        this.bufferSize = bufferSize;
        this.buffer = new Buffer(bufferSize);
        this.requestAmount = requestAmount;
        this.stepsFlag = stepFlag;
        this.sources = sources;
        this.devices = devices;
        startSources = new ArrayList<>();
        startDevices = new ArrayList<>();
        this.monitor = monitor;
    }

    @Override
    public void run() {

        /*Source s = new Source(0, 2);
        sources.add(s);
        s = new Source(1, 2.2);
        sources.add(s);
        s = new Source(2, 3);
        sources.add(s);
        s = new SMO.Source(3, 2.4);
        sources.add(s);
        s = new SMO.Source(4, 3.45);
        sources.add(s);*/

        /*Device d = new Device(0, 1.2);
        devices.add(d);
        d = new Device(1, 0.9);
        devices.add(d);
        d = new Device(2, 1.1);
        devices.add(d);
        d = new Device(3, 2.1);
        devices.add(d);
        d = new Device(4, 0.5);
        devices.add(d);*/

        int currSum = 0;
        for (Source value : sources) {
            currSum += value.getProcAmount();
            value.generateNewRequest();
        }

        boolean flag = false;
        double prevProb = 0;
        while (!flag) {
            while (currSum < requestAmount) {
                //Определение наименьшего времени
                Source minSource = getMinSourceTime();
                Device minDevice = getMinDeviceTime();
                //System.out.println("---------------------------------------------------------------------");

                if (stepsFlag) {
                    startSources = sources;
                    startDevices = devices;
                    startBuffer = buffer;
                    support.firePropertyChange("stepFlag", true, false);
                }

                if (stepsFlag) {
                /*System.out.println("Before step started ");
                System.out.println(minSource.getCurrRequest().gettPost());
                System.out.println(minDevice.gettOsv());
                System.out.println(minDevice.getSign());*/
                    synchronized (monitor) {
                        try {
                            monitor.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }

                if (minSource.getCurrRequest().gettPost() < minDevice.gettOsv() || minDevice.getSign() == 1) {
                    //System.out.println("request");
                    Request r = minSource.getCurrRequest();
                    minSource.generateNewRequest();
                    Device dev = getFreeDevice();
                    if (dev != null) {
                        dev.service(r);
                        minSource.handleRequestResults(r);
                    } else {
                        minSource.handleRequestResults(buffer.addRequestToBuffer(r));
                    }
                } else {
                    //System.out.println("device");
                    if (buffer.isEmpty()) {
                        //System.out.println("sign set!");
                        minDevice.setSign(1);
                    } else {
                        Request r = buffer.getRequestFromBuffer(minDevice.gettOsv());
                        minDevice.service(r);
                        sources.get(r.getSourceNum()).handleRequestResults(r);
                    }
                }

                currSum = 0;
                for (Source source : sources) {
                    currSum += source.getProcAmount();
                }

            /*if (stepsFlag) {
                System.out.println("Press Enter to continue... Type 's' to skip ");
                synchronized (monitor) {
                    try {
                        monitor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }*/
            }

            double currAvgProb = getAvgProbability() / 100;

            System.out.println("amount: " + requestAmount);
            System.out.println("currAvgProb: " + currAvgProb * 100);
            long newAmount = Math.round((1.643 * 1.643 * (1 - currAvgProb)) / (currAvgProb * 0.1 * 0.1));
            System.out.println("new amount: " + newAmount);
            if (prevProb != 0) {
                if (Math.abs(prevProb - currAvgProb) < 0.1 * prevProb) {
                    flag = true;
                } else {
                    requestAmount = newAmount;
                }
            } else {
                requestAmount = newAmount;
            }
            prevProb = currAvgProb;

        }

        support.firePropertyChange("requestAmount", 0, requestAmount);

    }

    public void addListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    public void nextStep() {
        synchronized (monitor) {
            monitor.notify();
        }
    }

    public void setStepsFlag(boolean stepsFlag) {
        this.stepsFlag = stepsFlag;
    }

    public ArrayList<Source> getStartSources() {
        return startSources;
    }

    public ArrayList<Device> getStartDevices() {
        return startDevices;
    }

    public ArrayList<Source> getSources() {
        return sources;
    }

    public ArrayList<Device> getDevices() {
        return devices;
    }

    public Buffer getStartBuffer() {
        return startBuffer;
    }

    private Device getFreeDevice() {
        for (Device device : devices) {
            if (device.getSign() == 1) {
                return device;
            }
        }
        return null;
    }

    private Source getMinSourceTime() {
        Source sMin = sources.get(0);
        double min = sMin.getCurrRequest().gettPost();
        for (int i = 1; i < sources.size(); i++) {
            Source s = sources.get(i);
            if (s.getCurrRequest().gettPost() < min) {
                min = s.getCurrRequest().gettPost();
                sMin = s;
            }
        }

        return sMin;
    }

    private Device getMinDeviceTime() {
        Device dMin = null;
        double min = Double.MAX_VALUE;
        for (int i = 0; i < devices.size(); i++) {
            Device d = devices.get(i);
            if (d.gettOsv() < min && d.getSign() == 0) {
                min = d.gettOsv();
                dMin = d;
            }
        }

        if (dMin == null) {
            dMin = getFreeDevice();
        }

        return dMin;
    }

    private double getAvgProbability() {
        double sum = 0;
        for (Source source: sources) {
            sum += source.getDeniedProb();
        }
        return sum / sources.size();
    }
}
