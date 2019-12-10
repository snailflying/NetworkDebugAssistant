package com.dlong.networkdebugassistant.activity

import android.os.Bundle
import android.text.InputType
import android.view.View
import androidx.databinding.DataBindingUtil
import com.dlong.dialog.BaseDialog
import com.dlong.dialog.ButtonStyle
import com.dlong.dialog.EditDialog
import com.dlong.dialog.OnBtnClick
import com.dlong.networkdebugassistant.R
import com.dlong.networkdebugassistant.bean.UdpBroadConfiguration
import com.dlong.networkdebugassistant.constant.DBConstant
import com.dlong.networkdebugassistant.databinding.ActivityUdpBroadSettingBinding
import com.dlong.networkdebugassistant.utils.StringUtils
import java.lang.StringBuilder

class UdpBroadSettingActivity : BaseSettingActivity() {

    private lateinit var binding: ActivityUdpBroadSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_udp_broad_setting)

        // 设置返回按钮
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener { finish() }

        initContentBinding(binding.contentBase)
        initConfig()
    }

    override fun initConfig() {
        super.initConfig()
        // 初始化配置信息
        binding.contentBase.config = DBConstant.getInstance(this).getUdpBroadConfiguration()
        updateConfigShow()
    }

    override fun updateConfigShow() {
        binding.contentBase.config = binding.contentBase.config?: UdpBroadConfiguration()
        val cc = binding.contentBase.config as UdpBroadConfiguration
        binding.localPort = "${cc.localPort}"
        binding.targetIpAddress = cc.targetIpAddress
        binding.targetPort = "${cc.targetPort}"
        // 保存配置信息
        DBConstant.getInstance(this).setUdpBroadConfiguration(cc)
    }

    fun setLocalPort(view: View) {
        val cc = binding.contentBase.config as UdpBroadConfiguration
        val tag = "port"
        EditDialog(this)
            .setTittle(resources.getString(R.string.prompt))
            .setMsg(resources.getString(R.string.local_port))
            .addEdit(tag, "${cc.localPort}", resources.getString(R.string.please_input_port))
            .setInputType(tag, InputType.TYPE_CLASS_NUMBER)
            .addAction(resources.getString(R.string.sure), ButtonStyle.THEME, object : OnBtnClick{
                override fun click(d0: BaseDialog<*>, text: String) {
                    val dialog = d0 as EditDialog
                    val value = dialog.getInputText(tag)
                    if (value.isEmpty()) {
                        dialog.setError(tag, resources.getString(R.string.input_port_can_not_empty))
                    } else {
                        val num = value.toIntOrNull()?: -1
                        if (num in 0x0000..0xffff) {
                            cc.localPort = num
                            updateConfigShow()
                            dialog.dismiss()
                        } else {
                            dialog.setError(tag, resources.getString(R.string.input_port_can_not_over_range))
                        }
                    }
                }
            })
            .addAction(resources.getString(R.string.cancel), ButtonStyle.NORMAL, null)
            .create().show()
    }

    fun setTargetIpAddress(view: View) {
        val cc = binding.contentBase.config as UdpBroadConfiguration
        val tag = "ip"
        EditDialog(this)
            .setTittle(resources.getString(R.string.prompt))
            .setMsg(resources.getString(R.string.target_ip_address))
            .addEdit(tag, cc.targetIpAddress, resources.getString(R.string.please_input_ip_address))
            .setInputType(tag, InputType.TYPE_NUMBER_FLAG_DECIMAL)
            .addAction(resources.getString(R.string.sure), ButtonStyle.THEME, object : OnBtnClick{
                override fun click(d0: BaseDialog<*>, text: String) {
                    val dialog = d0 as EditDialog
                    val value = dialog.getInputText(tag)
                    if (value.isEmpty()) {
                        dialog.setError(tag, resources.getString(R.string.input_ip_address_wrong_format))
                        return
                    }
                    val items = value.split(".")
                    if (items.size != 4) {
                        dialog.setError(tag, resources.getString(R.string.input_ip_address_wrong_format))
                        return
                    }
                    val builder = StringBuilder()
                    for (str in items.iterator()) {
                        if (!StringUtils.isNumeric(str)) {
                            dialog.setError(tag, resources.getString(R.string.input_ip_address_wrong_format))
                            return
                        }
                        val num = str.toIntOrNull()?: -1
                        if (num < 0 || num > 255) {
                            dialog.setError(tag, resources.getString(R.string.input_ip_address_wrong_format))
                            return
                        }
                        builder.append(num).append(".")
                    }
                    cc.targetIpAddress = builder.substring(0, builder.length -1)
                    updateConfigShow()
                    dialog.dismiss()
                }
            })
            .addAction(resources.getString(R.string.cancel), ButtonStyle.NORMAL, null)
            .create().show()
    }

    fun setTargetPort(view: View) {
        val cc = binding.contentBase.config as UdpBroadConfiguration
        val tag = "port"
        EditDialog(this)
            .setTittle(resources.getString(R.string.prompt))
            .setMsg(resources.getString(R.string.target_port))
            .addEdit(tag, "${cc.targetPort}", resources.getString(R.string.please_input_port))
            .setInputType(tag, InputType.TYPE_CLASS_NUMBER)
            .addAction(resources.getString(R.string.sure), ButtonStyle.THEME, object : OnBtnClick{
                override fun click(d0: BaseDialog<*>, text: String) {
                    val dialog = d0 as EditDialog
                    val value = dialog.getInputText(tag)
                    if (value.isEmpty()) {
                        dialog.setError(tag, resources.getString(R.string.input_port_can_not_empty))
                    } else {
                        val num = value.toIntOrNull()?: -1
                        if (num in 0x0000..0xffff) {
                            cc.targetPort = num
                            updateConfigShow()
                            dialog.dismiss()
                        } else {
                            dialog.setError(tag, resources.getString(R.string.input_port_can_not_over_range))
                        }
                    }
                }
            })
            .addAction(resources.getString(R.string.cancel), ButtonStyle.NORMAL, null)
            .create().show()
    }
}