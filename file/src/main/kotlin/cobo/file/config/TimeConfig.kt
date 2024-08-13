package cobo.file.config

import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration
import java.util.*

@Configuration
class TimeConfig {

    @PostConstruct
    fun setTimeZone(){
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"))
    }
}