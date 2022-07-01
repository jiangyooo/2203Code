package com.qf.dao;

import com.qf.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

/**
 * @author JiangBing
 * @date 2022/6/30 17:18
 */
public interface UserDao {

    /**
     * 查询所有
     * @return
     */
    @Select("select * from t_users")
    public List<User> findAll(Integer page,Integer limit);

    /**
     * 新增
     * @return
     */
    @Insert(" insert into t_users values (null,#{user.name},#{user.password},#{user.sex},#{user.birthday},#{user.registTime}) ")
    public Integer insert(@Param("user")User user);

    /**
     * 删除
     * @return
     */
    @Delete(" delete from t_users where id=#{id} ")
    public Integer delete(Integer id);

    /**
     * 更新
     * @return
     */
    @Update(" update t_users SET name = #{user.name},password = #{user.password},sex=#{user.sex},birthday=#{user.birthday},registTime=#{user.registTime} where id=#{user.id} ")
    Integer Update(@Param("user")User user);


}
