package kr.flab.wiki.core.domain.document

interface DocumentFormatPolicy {
    val minTitleLength: Int
    val maxTitleLength: Int
    val minBodyLength: Int
    val maxBodyLength: Int

    companion object {
        const val DEFAULT_MINIMUM_TITLE_LENGTH = 0
        const val DEFAULT_MAXIMUM_TITLE_LENGTH = 80
        const val DEFAULT_MINIMUM_BODY_LENGTH = -1
        const val DEFAULT_MAXIMUM_BODY_LENGTH = 10000

        val DEFAULT = object : DocumentFormatPolicy {
            override val minTitleLength = DEFAULT_MINIMUM_TITLE_LENGTH
            override val maxTitleLength = DEFAULT_MAXIMUM_TITLE_LENGTH
            override val minBodyLength = DEFAULT_MINIMUM_BODY_LENGTH
            override val maxBodyLength = DEFAULT_MAXIMUM_BODY_LENGTH
        }
    }
}
