package com.ly.base.common.util;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
//
//import org.apache.curator.framework.CuratorFramework;
//import org.apache.curator.framework.recipes.locks.InterProcessMutex;
//import org.apache.log4j.Logger;
//
//import com.scrum.net.comm.zk.lock.DistributedNewLock;
/**
 * @remark 并发锁方法调用工具类
 * @author 公子
 * @time 2016-03-11
 */
public class ZKLockUtil {

//    protected static final Logger logger = Logger.getLogger(ZKLockUtil.class);
    
    /**
     * 基于分布式锁反射调用方法
     * @param targeObj  目标对象
     * @param methodName 目标方法名
     * @param lockServer 并发锁服务器
     * @param lockKey 并发锁KEY
     * @param paras 方法参数
     * @return
     */
//    public static Object invokMethod(Object targeObj, String methodName,
//            String lockServer, String lockKey, Object... paras) {
//        // 分布式锁
//        InterProcessMutex lock = null;
//        CuratorFramework client = null;
//        try {
//            client = DistributedNewLock.getClient(lockServer);
//            // 用户锁
//            lock = DistributedNewLock.getLock(client, lockKey);
//            //寻找方法
//            Method method=getTargeMethod(targeObj.getClass().getDeclaredMethods(), methodName, paras);
//            if(ObjectUtil.hasNull(method)){
//                method=getTargeMethod(targeObj.getClass().getMethods(), methodName, paras);
//            }
//            if(ObjectUtil.hasNull(method)){
//                System.out.println("方法不存在:"+targeObj.getClass().getName()+"."+methodName);
//                return null;
//            }
//            //获取锁许可证
//            if (!lock.acquire(5 * 1000, TimeUnit.SECONDS)) {
//                return false;
//            }
//            method.setAccessible(true);
//            //执行方法
//            return method.invoke(targeObj, paras);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            // 释放锁
//            closeLock(lock);
//            closeClient(client);
//        }
//        return null;
//    }
//    /**
//     * 从对象中获取目标方法
//     * @param methods 方法数组
//     * @param methodName 方法名称
//     * @param paras 参数列表
//     * @return
//     */
//    private static Method getTargeMethod(Method []methods,String methodName,Object...paras){
//        for (Method m:methods) {
//                if(isTargeMethod(m, methodName, paras)){
//                    return m;
//                }
//        }
//        return null;
//    }
//    /**
//     * 判断目标是否是当前方法
//     * @param method 当前方法
//     * @param methodName 目标方法名
//     * @param paras 目标方法参数列表
//     * @return
//     */
//    private static boolean isTargeMethod(Method method,String methodName,Object...paras){
//        if(!method.getName().equals(methodName)){
//            return false;
//        }
//        Class[] clas=method.getParameterTypes();
//        if(ObjectUtil.isNullOrEmpty(clas)&&ObjectUtil.isNullOrEmpty(paras)){
//            return true;
//        }
//        if(ObjectUtil.isNullOrEmpty(clas)||ObjectUtil.isNullOrEmpty(paras)){
//            return false;
//        }
//        if(clas.length!=paras.length){
//            return false;
//        }
//        for (int i=0;i             System.out.println(paras[i].getClass().getName());
//            System.out.println(clas[i].getName());
//            if(!(clas[i].getName().equals(paras[i].getClass().getName()))){
//                return false;
//            }
//        }
//        return true;
//    }
//    //释放连接
//    private static void closeClient(CuratorFramework client) {
//        if (client != null) {
//            DistributedNewLock.closeClient(client);
//        }
//    }
//    //释放锁
//    private static void closeLock(InterProcessMutex lock) {
//        // 释放锁
//        if (lock != null) {
//            try {
//                lock.release();
//            } catch (Exception e) {
//                logger.error("释放失败", e);
//            }
//        }
//    }
}