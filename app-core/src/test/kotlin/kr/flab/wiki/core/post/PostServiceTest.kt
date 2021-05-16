package kr.flab.wiki.core.post

import kr.flab.wiki.core.domain.PostLengthPolicy
import kr.flab.wiki.core.domain.PostLengthPolicyImpl
import kr.flab.wiki.core.domain.PostService
import kr.flab.wiki.core.domain.PostServiceImpl
import kr.flab.wiki.core.domain.post.Post
import kr.flab.wiki.core.domain.repository.PostRepository
import kr.flab.wiki.core.domain.user.User
import kr.flab.wiki.core.util.TestUtils
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

//테스트에서 한글 사용
@Suppress("NonAsciiCharacters")
class PostServiceTest {
    lateinit var mockPostRepository: PostRepository
    lateinit var mockPostLengthPolicy: PostLengthPolicy
    lateinit var sut: PostService

    @BeforeEach
    fun setup() {
        mockPostRepository = mock(PostRepository::class.java)
        mockPostLengthPolicy = PostLengthPolicyImpl(100, 10000)
        sut = PostServiceImpl(mockPostRepository, mockPostLengthPolicy)
    }

    @Nested
    inner class `랜덤 유저가` {
        lateinit var randomUser: User

        @BeforeEach
        fun beforeEach() {
            randomUser = TestUtils.createRandomUser()
        }

        @Nested
        inner class `랜덤 항목을` {

            lateinit var randomPost: Post

            @BeforeEach
            fun beforeEach() {
                randomPost = TestUtils.createRandomPost(user = randomUser)
                setRepositoryAction(mockPostRepository, randomPost)

            }

            @Nested
            inner class `등록하는 경우` {
                @Test
                fun `1회는 성공`() {
                    assertThat(true, `is`(sut.create(randomPost)))
                }

                @Test
                fun `연달아서 할 경우 실패`() {
                    val firstResult = sut.create(randomPost)
                    val secondResult = sut.create(randomPost)
                    assertRepeatedPost(firstResult = firstResult, secondResult = secondResult)
                }
            }
        }

        @Nested
        inner class `제목없는 항목을` {

            lateinit var randomPost: Post

            @BeforeEach
            fun beforeEach() {
                randomPost = TestUtils.createRandomPost(user = randomUser, title = "")
                setRepositoryAction(mockPostRepository, randomPost)

            }

            @Test
            fun `등록하는 경우 실패`() {
                val result = sut.create(randomPost)
                assertThat(false, `is`(result))
            }
        }

        @Nested
        inner class `제목이 100자 이상인 항목을` {

            lateinit var randomPost: Post

            @BeforeEach
            fun beforeEach() {
                randomPost = TestUtils.createRandomPost(user = randomUser, title = "title".repeat(25))
                setRepositoryAction(mockPostRepository, randomPost)

            }

            @Test
            fun `등록하는 경우 실패`() {
                assertThat(false, `is`(sut.create(randomPost)))
            }
        }

        @Nested
        inner class `본문이 10000만 이상인 항목을` {

            lateinit var randomPost: Post

            @BeforeEach
            fun beforeEach() {
                randomPost = TestUtils.createRandomPost(user = randomUser, mainText = "titletitle".repeat(1000))
                setRepositoryAction(mockPostRepository, randomPost)

            }

            @Test
            fun `등록하는 경우 실패`() {
                assertThat(false, `is`(sut.create(randomPost)))
            }
        }
    }

    companion object {
        fun assertRepeatedPost(firstResult: Boolean, secondResult: Boolean) {
            assertThat(true, `is`(firstResult))
            assertThat(false, `is`(secondResult))
        }

        fun setRepositoryAction(mockPostRepository: PostRepository, post: Post) {
            `when`(mockPostRepository.get(post.title)).thenReturn(null).thenReturn(post)
            `when`(mockPostRepository.create(post)).thenReturn(true).thenReturn(false)
        }
    }
}
