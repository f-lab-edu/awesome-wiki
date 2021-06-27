package kr.flab.wiki.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder

fun main(args: Array<String>) {
    WikiApplication().start(args)
}

@SpringBootApplication
class WikiApplication {
    fun start(args: Array<String>) {
        /*
         * "application.yml" 외에도 환경설정 파일을 추가로 더 읽을 수 있게 해 준다
         * 가령 local 에서 실행할 경우 "application.yml", "application-local.yml" 을 모두 읽는다.
         * 이때 나중에 로드하는 "application-local.yml" 의 설정은 "application.yml" 의 같은 설정을 override 한다.
         *
         * https://docs.spring.io/spring-boot/docs/2.5.1/reference/html/features.html#features.external-config.files
         */
        val configurationNames = arrayOf("application", "application-" + AppConfig.PROFILE.key)

        @Suppress("SpreadOperator") // Spread 한번만 하니까, 무시
        SpringApplicationBuilder(WikiApplication::class.java)
            .properties("spring.config.name:" + configurationNames.joinToString { it })
            .build()
            .run(*args)
    }
}
