package kr.flab.wiki.core.util

import com.github.javafaker.Faker
import kr.flab.wiki.core.domain.post.Post
import kr.flab.wiki.core.domain.post.PostImpl
import kr.flab.wiki.core.domain.user.User
import kr.flab.wiki.core.domain.user.UserImpl
import java.time.LocalDateTime

sealed class TestUtils {
    companion object {
        fun createRandomUser(): User {
            return UserImpl(
                Faker.instance().name().username(),
                Faker.instance().number().randomNumber(), LocalDateTime.now()
            );
        }

        fun createRandomPost(
            user: User,
            title: String = Faker.instance().starTrek().location(),
            mainText: String = Faker.instance().lorem().sentence(),
            idx: Long = 0,
            version: Long = 0,
            lastModified: LocalDateTime = LocalDateTime.now()
        ): Post {
            return PostImpl(
                creator = user,
                title = title,
                mainText = mainText,
                idx = idx,
                version = version,
                lastModified = lastModified
            )


            /*
           return PostImpl.PostFactory(user, title).mainText(mainText).idx(idx).version(version)
               .lastModified(lastModified).build()
*/

        }
    }
}
