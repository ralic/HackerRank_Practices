package org.raliclo.Spring_backend.service;

import org.raliclo.Spring_backend.model.Comment;

import java.util.List;

public interface CommentService {
    Comment save(Comment comment);

    Comment findOne(Long commentId);

    List<Comment> findByPhotoId(Long photo);
}
