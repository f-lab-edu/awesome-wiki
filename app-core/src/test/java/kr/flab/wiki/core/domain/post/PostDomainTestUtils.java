package kr.flab.wiki.core.domain.post;

import kr.flab.wiki.core.domain.StringRandomUtils;
import kr.flab.wiki.core.domain.post.persistence.PostEntity;
import kr.flab.wiki.core.domain.user.UserDomainTestUtils;

import java.time.LocalDateTime;

public class PostDomainTestUtils {
    public static PostEntity createRandomPost(int titleLength, int textLength){
        PostEntity post = new PostEntity();
        post.setTitle(StringRandomUtils.getFaker().lorem().characters(titleLength));
        post.setDisplayCount((int)(Math.random()*10000));
        post.setWriter(UserDomainTestUtils.createRandomUserEntity());
        post.setText(StringRandomUtils.getFaker().lorem().characters(textLength));
        post.setCreatedAt(LocalDateTime.now());
        return post;
    }
}
