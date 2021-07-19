package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {
    //分页
    List<Comment> selectCommentByEntity(int entityType, int entityId, int offset, int limit);
    //查询总条数
    int selectCountByEntity(int entityType, int entityId);

    //insert
    int insertComment(Comment comment);
}
