package kr.flab.wiki.app

import kr.flab.wiki.core.AppProfile

object AppConfig {
    //detekt에서 Maybe Const 메세지 보여주면서 빌드 실패.
    //val APP_NAME = "awesome-wiki"
    const val APP_NAME = "awesome-wiki"

    val PROFILE = AppProfile.LOCAL
}
