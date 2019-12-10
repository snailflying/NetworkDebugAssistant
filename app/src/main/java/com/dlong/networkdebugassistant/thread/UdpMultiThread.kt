package com.dlong.networkdebugassistant.thread

import android.content.Context
import android.net.wifi.WifiManager
import android.os.Handler
import android.os.Message
import com.dlong.networkdebugassistant.activity.BaseSendReceiveActivity
import com.dlong.networkdebugassistant.bean.ReceiveInfo
import com.dlong.networkdebugassistant.utils.DateUtils
import java.net.*

/**
 * udp 组播线程
 *
 * @author D10NG
 * @date on 2019-12-09 10:24
 */
class UdpMultiThread constructor(
    private val mContext: Context,
    private val mHandler: Handler,
    private val multiAddress: String,
    private val port: Int
) : BaseThread() {

    private lateinit var mLock: WifiManager.MulticastLock
    private lateinit var mcSocket: MulticastSocket
    private var isRun = false

    override fun run() {
        super.run()
        val manager = mContext.applicationContext
            .getSystemService(Context.WIFI_SERVICE) as WifiManager
        mLock = manager.createMulticastLock("test wifi")
        mcSocket = MulticastSocket(port)
        val group = InetAddress.getByName(multiAddress)
        mcSocket.joinGroup(group)
        mcSocket.timeToLive = 5
        mcSocket.loopbackMode = false

        isRun = true
        var packet: DatagramPacket
        val by = ByteArray(1024)
        while (isRun) {
            // 循环等待接收
            acquireLock()
            packet = DatagramPacket(by, by.size)
            mcSocket.receive(packet)

            // 拿到广播地址
            val address = packet.address.toString().replace("/", "")
            // 拿到数据
            val data = packet.data.copyOfRange(0, packet.length)

            // 包装
            val receive = ReceiveInfo()
            receive.byteData = data
            receive.time = DateUtils.curTime
            receive.ipAddress = address
            receive.port = packet.port

            val m = Message.obtain()
            m.what = BaseSendReceiveActivity.RECEIVE_MSG
            m.obj = receive
            mHandler.sendMessage(m)
        }
        // 关闭
        mcSocket.leaveGroup(group)
        mcSocket.disconnect()
        mcSocket.close()
        releaseLock()
    }

    @Synchronized
    private fun acquireLock() {
        if (!mLock.isHeld) {
            mLock.acquire()
        }
    }

    @Synchronized
    private fun releaseLock() {
        if (!mLock.isHeld) {
            mLock.release()
        }
    }

    override fun send(address: String, toPort: Int, data: ByteArray) {
        acquireLock()
        Thread(Runnable {
            val packet = DatagramPacket(data, data.size, InetAddress.getByName(address), toPort)
            mcSocket.send(packet)
        }).start()
    }

    override fun send(data: ByteArray) {}

    override fun close() {
        isRun = false
    }
}