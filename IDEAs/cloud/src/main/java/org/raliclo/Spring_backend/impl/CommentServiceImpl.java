package org.raliclo.Spring_backend.impl;

import org.raliclo.Spring_backend.dao.CommentDao;
import org.raliclo.Spring_backend.model.Comment;
import org.raliclo.Spring_backend.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentDao commentDao;

    public Comment save(Comment comment) {
        return commentDao.save(comment);
    }

    public Optional<Comment> findOne(Long commentId) {
        return commentDao.findOne(commentId);
    }

    public List<Comment> findByPhotoId(Long photoId) {
        return commentDao.findByPhotoId(photoId);
    }

}
