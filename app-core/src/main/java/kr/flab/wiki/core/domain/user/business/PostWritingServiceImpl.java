package kr.flab.wiki.core.domain.user.business;

import kr.flab.wiki.core.domain.post.Post;
import kr.flab.wiki.core.domain.post.PostWritingService;
import kr.flab.wiki.core.domain.post.persistence.PostEntity;
import kr.flab.wiki.core.domain.post.repository.PostEntityRepository;
import kr.flab.wiki.text.StringUtils;

public class PostWritingServiceImpl implements PostWritingService {

    private final PostEntityRepository postRepository;

    public PostWritingServiceImpl(PostEntityRepository postRepository){
        this.postRepository = postRepository;
    }

    @Override
    public Post writePost(Post post) {
        if(StringUtils.isBlank(post.getTitle())
                || StringUtils.isBlank(post.getText())){
            throw new RuntimeException("title or text is empty!");
        }
        if(post.getTitle().length() < 10){
            throw new RuntimeException("title must be less than 10 characters!");
        }
        if(post.getText().length() < 100){
            throw new RuntimeException("text must be less than 100 characters!");
        }
        return this.postRepository.save(PostEntity.from(post));
    }
}
