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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;

//import static org.junit.jupiter.api.Assertions.assertTrue;
@Suppress(names = "NonAsciiCharacters")
public class PostWritingServiceTest {

    private PostEntityRepository mockPostRepository;
    // System Under Test (SUT) : 테스트할 대상
    private PostWritingService sut;

    @BeforeEach
    private void init(){
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
        PostEntity post = PostDomainTestUtils.createRandomPost();
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
        //mock의 save 메서드 호출 시 원하는 결과를 세팅했음.
        //이건 실제 레포지토리가 내부적으로 어떻게 동작하는지 신경안쓰고 결과만 신경 씀.
        //레포지토리 내부 set이 계속 초기화 안돼서 (null) 애먹었는데 테스트 이렇게 하는게 맞는건가?
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
        verify(mockPostRepository, times(2)).save(post);
//        assertTrue(mockPostRepository.has(post));
//        assertThat(mockPostRepository.has(post), is(true));

    }

//    @Test
//    fun `(도배 방지를 위해) 글을 썼으면, 1분간 새로운 글을 쓸 수 없다`() {
//        //// JUnit nest test 로 감쌀 부분 {{{
//        // given: "특정 사용자"
//        val user = createRandomUserEntity()
//
//        // when: "Post 를 생성하고"
//        val post = writeRandomPostBy(user)
//
//        // then: "글을 작성하면"
//        val savedPost = sut.writePost(post)
//
//        // expected: "글이 등록된다"
//        assertTrue(savedPost.createdAt >= post.createdAt)
//        //// }}} JUnit nest test 로 감쌀 부분
//
//        // and: "1분 이내에 글을 쓰려고 하면 실패한다"
//        assertThrows<UnsupportedOperationException> {
//            sut.writePost(post)
//        }
//
//        // 여기서 1분 기다릴 수는 없음: Unit test 는 무조건 빨리 끝나야 한다. 단위 fixture 당 100ms 미만에 무조건 끝나야 한다.
//        // 1분 = 60000 ms 니까 기다리면 안된다.
//        // when: "1분이 지나면"
//        user.lastActiveAt = LocalDateTime.now().plusMinutes(1)
//
//        // then: "새로운 글쓰기에 성공해야 한다"
//        val savedPost2 = PostEntity.from(sut.writePost(writeRandomPostBy(user)))
//
//        // expect:
//        assertTrue(savedPost2.createdAt >= post.createdAt)
//
//        // and: "실제로 등록되었나?"
//        assertTrue(mockPostRepository.has(savedPost2))
//    }
//
//    @Test
//    @Disabled
//    fun `제목과 본문은 반드시 비어 있으면 안된다`() {
//
//    }
//
//    @Test
//    @Disabled
//    fun `제목의 길이는 10자 이상이어야 한다`() {
//
//    }
//
//    @Test
//    @Disabled
//    fun `본문의 길이는 100자 이상이어야 한다`() {
//
//    }
}
