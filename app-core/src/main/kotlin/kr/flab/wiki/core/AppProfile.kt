package kr.flab.wiki.core

/**
 * Application 의 실행 단계를 지정합니다. Gradle 의 build profile 설정을 따릅니다.
 * 구현 내용은 반드시 /buildScripts/packaging.gradle 의 내용과 일치해야 합니다.
 */
enum class AppProfile(val key: String) {
    LOCAL("local"),
    ALPHA("alpha"),
    BETA("beta"),
    RELEASE("release");

    companion object {
        fun byKey(key: String?) = values().firstOrNull { it.key == key } ?: LOCAL
    }
}
