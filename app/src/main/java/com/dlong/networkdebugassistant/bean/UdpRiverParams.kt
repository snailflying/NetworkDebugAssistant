package com.dlong.networkdebugassistant.bean

import com.dlong.jsonentitylib.annotation.DLField
import java.io.Serializable

/**
 * @Author ZhiQiang
 * @Date 2024/6/26
 * @Description River广播配置头
 */
data class UdpRiverParams (

    @DLField
    var dev_code: String = "",
    var site_code: String = "",
    var ant_altitude: String = "",
    var ant_dis: String = "",
) : Serializable {
}