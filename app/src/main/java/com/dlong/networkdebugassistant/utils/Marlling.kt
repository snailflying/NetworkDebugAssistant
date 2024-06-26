import org.jboss.marshalling.Marshalling
import org.jboss.marshalling.MarshallingConfiguration
import org.jboss.marshalling.SimpleClassResolver
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.ObjectOutputStream


object MarshallingUtils {

    private fun config(): MarshallingConfiguration {
        val configuration = MarshallingConfiguration()
        configuration.version = 4
        configuration.classCount = 10
        configuration.bufferSize = 8096
        configuration.instanceCount = 100
//        configuration.exceptionListener = MarshallingException()
        configuration.classResolver = SimpleClassResolver(javaClass.classLoader)
        return configuration
    }

    @Throws(IOException::class)
    fun marshallingWrite(obj: Any?): ByteArray {
        // 使用river作为marshalling的方式
        val marshallerFactory = Marshalling.getProvidedMarshallerFactory("river")
        // 创建marshalling的配置
        val configuration = MarshallingConfiguration()
        // 使用版本号4
        configuration.version = 4

        val marshaller = marshallerFactory.createMarshaller(configuration)

        val boas = ByteArrayOutputStream()
        ObjectOutputStream(boas).use { ois ->
            marshaller.start(Marshalling.createByteOutput(ois))
            marshaller.writeObject(obj)
            marshaller.finish()
            return boas.toByteArray()
        }
    }
}