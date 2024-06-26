package com.dlong.networkdebugassistant

import com.dlong.networkdebugassistant.utils.ByteUtils
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.Serializable

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    val string1 = "{name:abc,age:18}"
    val string2 = """{"name":"abc","age":18}"""
    val string3 = "{name=abc,age=18}"

    @Test
    fun serialization_isCorrect1() {
        assertEquals(string1.toByteArray(), ByteUtils.convertObjectToBytes(Data()))
    }
    @Test
    fun serialization_isCorrect2() {
        assertEquals(string2.toByteArray(), ByteUtils.convertObjectToBytes(Data()))
    }
    @Test
    fun serialization_isCorrect3() {
        print(ByteUtils.convertObjectToBytesByMarshalling(Data()))
        assertEquals(ByteUtils.convertObjectToBytesByMarshalling(Data()), ByteUtils.convertObjectToBytes(Data()))
    }
    class Data : Serializable {
        var name: String = "abc"
        var age: Int = 18
    }
}
