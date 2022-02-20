package i2f.commons.core.utils.net.core;

import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * @author ltb
 * @date 2022/2/20 18:16
 * @desc
 */
public class NetUtil {
    public static Set<NetworkInterface> getNetworkInterfaces() throws SocketException {
        Set<NetworkInterface> ret = new HashSet<>();
        Enumeration<NetworkInterface> allInterfaces = NetworkInterface.getNetworkInterfaces();
        while (allInterfaces.hasMoreElements()) {
            NetworkInterface net = (NetworkInterface) allInterfaces.nextElement();
            ret.add(net);
        }
        return ret;
    }
    public static Set<InetAddress> getNetworkInterfaceAddress(NetworkInterface net){
        Set<InetAddress> ret=new HashSet<>();
        Enumeration<InetAddress> addresses=net.getInetAddresses();
        while(addresses.hasMoreElements()){
            InetAddress addr=addresses.nextElement();
            ret.add(addr);
        }
        return ret;
    }
    public static Map<NetworkInterface,Set<InetAddress>> getIpAddresses() throws SocketException {
        Map<NetworkInterface,Set<InetAddress>> ret=new HashMap<>();
        Set<NetworkInterface> nets=getNetworkInterfaces();
        for(NetworkInterface item : nets){
            Set<InetAddress> addresses=getNetworkInterfaceAddress(item);
            ret.put(item,addresses);
        }
        return ret;
    }

    public static Set<InetAddress> scanLanAddresses(Inet4Address addr) throws InterruptedException {
        Set<InetAddress> ret=new HashSet<>();
        String ip= addr.getHostAddress();
        int idx = ip.lastIndexOf(".");
        String baseIp = ip.substring(0,idx+1);
        CountDownLatch latch=new CountDownLatch(255);
        TestIpLinked[] links=new TestIpLinked[255];
        Thread[] threads=new Thread[255];
        for (int i = 1; i <= 255; i++) {
            String pip = baseIp + i;
            TestIpLinked task = new TestIpLinked();
            task.pip = pip;
            task.latch = latch;
            links[i - 1] = task;
            threads[i - 1] = new Thread(task);
            threads[i - 1].start();
        }
        latch.await();
        for(int i=0;i<255;i++){
            if(links[i].isOk){
                ret.add(links[i].addr);
            }
        }
        return ret;
    }

    static class TestIpLinked implements Runnable{
        public String pip;
        public CountDownLatch latch;
        public boolean isOk;
        public InetAddress addr;
        public String hostName;
        @Override
        public void run() {
            isOk=false;
            addr=null;
            try{
                addr=InetAddress.getByName(pip);
                isOk=addr.isReachable(8000);
                if(isOk){
                    hostName=addr.getHostName();
                }
            }catch(UnknownHostException e){
                //System.out.println("unknown host of "+pip);
            }catch (Exception e) {
                //System.out.println("test link:"+pip+" of error:"+e.getMessage());
            }finally
            {
                latch.countDown();
            }
        }
    }

    /**
     * 返回所有网卡的所有绑定地址，如果地址是IPV4地址，则获取对应地址的局域网所有主机地址
     * @return
     * @throws SocketException
     * @throws InterruptedException
     */
    public static Map<NetworkInterface,Map<InetAddress,Set<InetAddress>>> getAllNetInfo() throws SocketException, InterruptedException {
        Map<NetworkInterface,Map<InetAddress,Set<InetAddress>>> ret=new HashMap<>();
        Map<NetworkInterface, Set<InetAddress>> address = getIpAddresses();
        for (Map.Entry<NetworkInterface, Set<InetAddress>> item : address.entrySet()) {
            NetworkInterface net = item.getKey();
            Set<InetAddress> addrs = item.getValue();
            if (net.isVirtual() || !net.isUp()) {
                continue;
            }
            Map<InetAddress, Set<InetAddress>> map = new HashMap<>();
            for (InetAddress addr : addrs) {
                if (addr instanceof Inet4Address) {
                    if (addr.isLoopbackAddress()) {
                        continue;
                    }
                    Set<InetAddress> scans = scanLanAddresses((Inet4Address) addr);
                    map.put(addr, scans);
                }
                if(addr instanceof Inet6Address){
                    map.put(addr,null);
                }
            }
            ret.put(net, map);
        }

        return ret;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Map<NetworkInterface,Map<InetAddress,Set<InetAddress>>> info=getAllNetInfo();

        for(Map.Entry<NetworkInterface,Map<InetAddress,Set<InetAddress>>> item : info.entrySet()){
            NetworkInterface net=item.getKey();
            Map<InetAddress,Set<InetAddress>> addrs=item.getValue();
            if(net.isVirtual() || !net.isUp()){
                continue;
            }
            System.out.println("---------------------------");
            System.out.println("name:"+net.getName());
            System.out.println("displayName:"+net.getDisplayName());
            System.out.println("virtual:"+net.isVirtual());
            System.out.println("up:"+net.isUp());
            System.out.println("loopback:"+net.isLoopback());
            System.out.println("p2p:"+net.isPointToPoint());
            for(InetAddress addr : addrs.keySet()){
                System.out.println("\t------------------------");
                System.out.println("\tname:"+addr.getHostName());
                if(addr instanceof Inet4Address){
                    System.out.println("\tipv4:"+addr.getHostAddress());
                    if(addr.isLoopbackAddress()){
                        continue;
                    }
                    Set<InetAddress> scans=addrs.get(addr);
                    for(InetAddress scan : scans){
                        if(scan.getHostAddress().equals(addr.getHostAddress())){
                            continue;
                        }
                        System.out.println("\t\t----------------------");
                        System.out.println("\t\t"+scan.getHostName());
                        System.out.println("\t\t"+scan.getHostAddress());
                    }
                }
                if(addr instanceof Inet6Address){
                    System.out.println("\tipv6:"+addr.getHostAddress());
                }
            }
        }
    }
}
