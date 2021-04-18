package kr.flab.wiki.core.domain.post.persistence;

import kr.flab.wiki.core.domain.post.Post;
import kr.flab.wiki.core.domain.user.User;
import kr.flab.wiki.core.domain.user.persistence.UserEntity;

import java.time.LocalDateTime;
import java.util.Objects;

public class PostEntity implements Post {
    private String title;
    private int displayCount;
    private User writer;
    private String text;
    private LocalDateTime createdAt;

    public PostEntity(){
        this.writer = User.EMPTY;
        this.text = "";
        this.createdAt = LocalDateTime.MIN;
    }

    // 자바의신 : Java 1.7에서 바뀐 것 참고
    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }
        if (obj == null){
            return false;
        }
        if(this.getClass() != obj.getClass()){
            return false;
        }
//        if(obj instanceof PostEntity == false){
//            return false;
//        }
        PostEntity other = (PostEntity) obj;

        return Objects.equals(this.title, other.title) &&
                Objects.equals(this.displayCount, other.displayCount) &&
                Objects.equals(this.writer, other.writer) &&
                Objects.equals(this.text, other.text);
    }

    @Override
    public int hashCode(){
        return Objects.hash(this.title, this.displayCount, this.writer, this.text);
    }


    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int getDisplayCount() {
        return displayCount;
    }

    public void setDisplayCount(int displayCount) {
        this.displayCount = displayCount;
    }

    @Override
    public User getWriter() {
        return writer;
    }

    public void setWriter(User writer) {
        this.writer = writer;
    }

    @Override
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public static final PostEntity from(Post src){
        PostEntity post = new PostEntity();
        post.setTitle(src.getTitle());
        post.setDisplayCount(src.getDisplayCount());
        post.setWriter(src.getWriter());
        post.setText(src.getText());
        post.setCreatedAt(src.getCreatedAt());
        return post;
    }

}
