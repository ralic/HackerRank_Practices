package org.raliclo.Spring_backend.dao;

import org.raliclo.Spring_backend.model.Comment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentDao extends CrudRepository<Comment, Long> {
    Comment save(Comment comment);

    Optional<Comment> findOne(Long commentId);

    List<Comment> findByPhotoId(Long photo);
}
