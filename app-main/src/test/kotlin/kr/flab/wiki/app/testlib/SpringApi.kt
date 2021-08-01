package kr.flab.wiki.app.testlib

import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.mvc.method.RequestMappingInfo
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
import java.util.*
import javax.inject.Inject


@Component
class SpringApi {

    @Inject
    private lateinit var context: ApplicationContext

    private val random: Random = Random()

    fun getAuthenticatedApiPatternList(): List<String> {
        val requestMappingHandlerMapping = context
            .getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping::class.java)
        val map = requestMappingHandlerMapping
            .handlerMethods

        return map.filter { (key: RequestMappingInfo?, value: HandlerMethod?) ->
            !key.patternValues.contains("/login") && !key.patternValues.contains("/error")
        }.map { (key: RequestMappingInfo?, value: HandlerMethod?) ->
            key.patternValues.toString()
        }
    }

    fun getRandomAuthenticatedApiPattern(): String {
        val patternList = getAuthenticatedApiPatternList()
        return patternList[random.nextInt(patternList.size)]
    }

}
