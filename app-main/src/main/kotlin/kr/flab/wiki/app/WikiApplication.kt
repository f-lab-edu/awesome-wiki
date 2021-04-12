package kr.flab.wiki.app

import kr.flab.wiki.text.isNullOrUnicodeBlank

class WikiApplication {
    fun start(args: List<String>) {

        println("Hello World! Current app profile is: ${AppConfig.PROFILE}")
        println("test : ${"test".isNullOrUnicodeBlank()}")
        println("test2 : ${"test2".isNullOrUnicodeBlank()}")
        println("test3 : ${"test3".isNullOrUnicodeBlank()}")
        println("".isNullOrUnicodeBlank())

    }
}
