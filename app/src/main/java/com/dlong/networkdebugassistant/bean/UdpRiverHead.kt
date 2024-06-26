package com.dlong.networkdebugassistant.bean

import com.dlong.jsonentitylib.annotation.DLField
import java.io.Serializable

/**
 * @Author ZhiQiang
 * @Date 2024/6/26
 * @Description River广播配置头
 */
/*SiteCmd 命令type值为1；
RiverCmd消息命令type值为2；
SPCmd消息命令type值为3；
WorkCmd命令的type值为9；
SleepCmd命令的type值为10；
FlowDataCmd流速查询命令的type值为5；
MsgCmd调试消息命令的type值为100；*/
//WorkCmd，SleepCmd和FlowDataCmd消息，上位机只发消息头，以type值区分不同的消息。
data class UdpRiverHead (

    /** 本地端口 */
    @DLField
    var cmd: Int = 9,

    /** 目标地址 */
    @DLField
    var len: Int = 0,

    /** 目标端口 */
    @DLField
    var devtype: String = "ridar200"

) : Serializable {
}