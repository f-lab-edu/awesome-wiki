package kr.flab.wiki.core.domain.document.repository

import kr.flab.wiki.core.domain.document.Document
import kr.flab.wiki.core.domain.user.User
import java.time.LocalDateTime

interface UserHistoryRepository {
    fun getHistory(user: User, range: ClosedRange<LocalDateTime>): List<Document>
}
