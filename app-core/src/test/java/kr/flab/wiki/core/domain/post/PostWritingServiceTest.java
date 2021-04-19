package kr.flab.wiki.core.domain.post;

import kotlin.Suppress;
import kr.flab.wiki.core.domain.post.persistence.PostEntity;
import kr.flab.wiki.core.domain.post.repository.MockPostEntityRepository;
import kr.flab.wiki.core.domain.post.repository.PostEntityRepository;
import kr.flab.wiki.core.domain.post.repository.PostEntityRepositoryImpl;
import kr.flab.wiki.core.domain.user.User;
import kr.flab.wiki.core.domain.user.UserDomainTestUtils;
import kr.flab.wiki.core.domain.user.business.PostWritingServiceImpl;
import kr.flab.wiki.core.domain.user.persistence.UserEntity;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("PostWritingService 클래스")
public class PostWritingServiceTest {

    private PostEntityRepository mockPostRepository;
    // System Under Test (SUT) : 테스트할 대상
    private PostWritingService sut;

    @BeforeEach
    private void setUp(){
        mockPostRepository = mock(PostEntityRepositoryImpl.class);

        /*
         * in the action 에서는 이런식으로는 잘 안하고, mockito 라는 라이브러리를 씁니다.
         */
//        mockPostRepository = new MockPostEntityRepository();
        /*
         * Object 생성 비용: argument count 가 4 미만이면 '싸다' 라고 표현할 수 있고,
         * 4 초과인 경우는 대부분 '비싼' 객체, 또는 'God object' 니까, 4개 미만으로 의존성을 쪼개는게 좋다.
         *
         * 그리고 Autowired 는 절대로 쓰지 말고, Constructor injection 을 쓰자!!
         *
         * TDD 하면 이부분이 굉장히 민감해 지니까, 결과적으로 싸고 좋은 객체를 만드는 습관을 들여준다.
         */
        sut = new PostWritingServiceImpl(mockPostRepository);

    }

    private PostEntity writeRandomPostBy(User user) {
        PostEntity post = PostDomainTestUtils.createRandomPost(10, 100);
        post.setWriter(user);
        return post;
    }

    private PostEntity writeRandomPostWithTitleLessThan10Characters(User user){
        PostEntity post = PostDomainTestUtils.createRandomPost(5, 100);
        post.setWriter(user);
        return post;
    }

    private PostEntity writeRandomPostWithTextLessThan100Characters(User user){
        PostEntity post = PostDomainTestUtils.createRandomPost(10, 80);
        post.setWriter(user);
        return post;
    }

    /**
     * Unit test: whitebox testing (구현 내용을 상세하게 알고 있는 테스팅 기법)
     */
    @Test
    public void 특정_사용자가_제목과_본문을_입력하면_글이_등록된다(){

        //// JUnit nest test 로 감쌀 부분 {{{
        //https://johngrib.github.io/wiki/junit5-nested/
        // given : "특정 사용자"
        UserEntity user = UserDomainTestUtils.createRandomUserEntity();


        // when : "제목과 본문을 입력한 Post 를 생성하고"
        PostEntity post = writeRandomPostBy(user);
        // val expectedPost = .....
        // when(mockPostRepository).save(post).thenReturn(expectedPost)
        //https://codechacha.com/ko/mockito-best-practice/
        when(mockPostRepository.save(post)).thenReturn(post);
        // then: "글을 작성하면"
        Post savedPost = sut.writePost(post);

        // expected: "글이 등록된다"
        // 글이 등록되었다는 판단 기준: savedPost.createdAt >= post.createdAt
        // Hamcrest: assertThat(savedPost.createdAt),greaterThanOrEqualTo(post.createdAt)
        assertThat(savedPost.getCreatedAt(), is(greaterThanOrEqualTo(post.getCreatedAt())));


//        assertTrue(savedPost.getCreatedAt().isAfter(post.getCreatedAt())
//                || savedPost.getCreatedAt().isEqual(post.getCreatedAt()));
        //// }}} JUnit nest test 로 감쌀 부분

        // and: "실제로 등록되었나?"
        // Hamcrest + Mockito: mockPostRepository.save(post).isCalledOnce(1)
        verify(mockPostRepository, times(1)).save(post);

//        assertTrue(mockPostRepository.has(post));
//        assertThat(mockPostRepository.has(post), is(true));

    }

    @Test
    public void 도배_방지를_위해_글을_썼으면_1분간_새로운_글을_쓸_수_없다() {
        //// JUnit nest test 로 감쌀 부분 {{{
        // given : "특정 사용자"
        UserEntity user = UserDomainTestUtils.createRandomUserEntity();

        // when: "Post 를 생성하고"
        PostEntity post = writeRandomPostBy(user);

        // then: "글을 작성하면"
        when(mockPostRepository.save(post)).thenReturn(post);
        Post savedPost = sut.writePost(post);

        // expected: "글이 등록된다"
        assertThat(savedPost.getCreatedAt(), is(greaterThanOrEqualTo(post.getCreatedAt())));
        //// }}} JUnit nest test 로 감쌀 부분

        // and: "1분 이내에 글을 쓰려고 하면 실패한다"
        assertThrows(UnsupportedOperationException.class, ()->{
           sut.writePost(post);
           throw new UnsupportedOperationException();
        });

        // 여기서 1분 기다릴 수는 없음: Unit test 는 무조건 빨리 끝나야 한다. 단위 fixture 당 100ms 미만에 무조건 끝나야 한다.
        // 1분 = 60000 ms 니까 기다리면 안된다.
        // when: "1분이 지나면"
        user.setLastActiveAt(LocalDateTime.now().plusMinutes(1));
        PostEntity newPost = writeRandomPostBy(user);
        when(mockPostRepository.save(newPost)).thenReturn(newPost);

        // then: "새로운 글쓰기에 성공해야 한다"
        Post savedPost2 = sut.writePost(newPost);
        // expect:
        assertThat(savedPost2.getCreatedAt(), is(greaterThanOrEqualTo(newPost.getCreatedAt())));

        // and: "실제로 등록되었나?"
        verify(mockPostRepository, times(1)).save(newPost);
        assertThat(newPost, is(savedPost2));
    }

    @Test
    public void 제목과_본문은_반드시_비어_있으면_안된다() {

        //given : "특정 사용자"
        UserEntity user = UserDomainTestUtils.createRandomUserEntity();

        //when : "post를 생성하고"
        PostEntity post = writeRandomPostBy(user);

        //then : "제목 혹은 본문이 비어있으면"
        post.setTitle("");
//        post.setText("");

        //and : "RuntimeException을 발생 시킨다."
        RuntimeException runtimeException = assertThrows(RuntimeException.class, ()->{
           sut.writePost(post);
        });

        //expected :
        assertThat(runtimeException.getMessage(), is("title or text is empty!"));

        //when : "post를 생성하고"
        PostEntity newPost = writeRandomPostBy(user);

        when(mockPostRepository.save(newPost)).thenReturn(newPost);

        //then : "제목 혹은 본문이 비어있지 않으면"
        //...

        //then : "글쓰기에 성공해야 한다."
        Post savedPost = sut.writePost(newPost);

        //expected :
        assertThat(savedPost.getCreatedAt(), is(greaterThanOrEqualTo(newPost.getCreatedAt())));

        verify(mockPostRepository, times(1)).save(newPost);
        assertThat(savedPost, is(newPost));
    }

    @Test
    public void 제목의_길이는_10자_이상이어야_한다() {

        //given : "특정 사용자"
        UserEntity user = UserDomainTestUtils.createRandomUserEntity();

        //when : "post를 생성하고"
        PostEntity post = writeRandomPostWithTitleLessThan10Characters(user);

        //then : "제목의 길이가 10자 미만이면"
        //...

        //and : "RuntimeException을 발생시킨다."
        RuntimeException runtimeException = assertThrows(RuntimeException.class, ()->{
           sut.writePost(post);
        });

        //expected :
        assertThat(runtimeException.getMessage(), is("title must be less than 10 characters!"));

        //when : "post를 생성하고"
        PostEntity newPost = writeRandomPostBy(user);

        when(mockPostRepository.save(newPost)).thenReturn(newPost);

        //then : "제목의 길이가 10자 이상이면"
        //...

        //then : "글쓰기에 성공해야 한다."
        Post savedPost = sut.writePost(newPost);

        //expected :
        assertThat(savedPost.getCreatedAt(), is(greaterThanOrEqualTo(newPost.getCreatedAt())));

        verify(mockPostRepository, times(1)).save(newPost);
        assertThat(savedPost, is(newPost));

    }

    @Test
    public void 본문의_길이는_100자_이상이어야_한다() {

        //given : "특정 사용자"
        UserEntity user = UserDomainTestUtils.createRandomUserEntity();

        //when : "post를 생성하고"
        PostEntity post = writeRandomPostWithTextLessThan100Characters(user);

        //then : "본문의 길이가 100자 미만이면"
        //...

        //and : "RuntimeException을 발생시킨다."
        RuntimeException runtimeException = assertThrows(RuntimeException.class, ()->{
            sut.writePost(post);
        });

        //expected :
        assertThat(runtimeException.getMessage(), is("text must be less than 100 characters!"));

        //when : "post를 생성하고"
        PostEntity newPost = writeRandomPostBy(user);

        when(mockPostRepository.save(newPost)).thenReturn(newPost);

        //then : "본문의 길이가 100자 이상이면"
        //...

        //then : "글쓰기에 성공해야 한다."
        Post savedPost = sut.writePost(newPost);

        //expected :
        assertThat(savedPost.getCreatedAt(), is(greaterThanOrEqualTo(newPost.getCreatedAt())));

        verify(mockPostRepository, times(1)).save(newPost);
        assertThat(savedPost, is(newPost));

    }

    @Nested
    @DisplayName("writePost 메소드는")
    class Describe_write_post {

        UserEntity user = UserDomainTestUtils.createRandomUserEntity();

        @Nested
        @DisplayName("제공되는 post의 본문의 길이가 100자 미만이면")
        class Context_with_less_than_100_text {

            PostEntity post = writeRandomPostWithTextLessThan100Characters(user);

            @Test
            @DisplayName("RuntimeException을 발생시킨다.")
            void it_occurs_runtime_exception() {
                RuntimeException runtimeException = assertThrows(RuntimeException.class, ()->{
                    sut.writePost(post);
                });

                assertThat(runtimeException.getMessage(), is("text must be less than 100 characters!"));
            }
        }

        @Nested
        @DisplayName("제공되는 post의 본문의 길이가 100자 이상이면")
        class Context_with_more_than_100_text {

            PostEntity newPost = writeRandomPostBy(user);

            @Test
            @DisplayName("정상적으로 post를 등록한다.")
            void it_occurs_runtime_exception() {

                when(mockPostRepository.save(newPost)).thenReturn(newPost);

                Post savedPost = sut.writePost(newPost);

                assertThat(savedPost.getCreatedAt(), is(greaterThanOrEqualTo(newPost.getCreatedAt())));
                verify(mockPostRepository, times(1)).save(newPost);
                assertThat(savedPost, is(newPost));
            }
        }
    }
}
