package com.qzt.module.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.qzt.common.annotation.DataScope;
import com.qzt.common.constant.GlobalErrorCodeConstants;
import com.qzt.common.constant.SysErrorCodeConstants;
import com.qzt.common.core.domain.AjaxResult;
import com.qzt.common.core.domain.entity.SysRole;
import com.qzt.common.core.domain.entity.SysUser;
import com.qzt.common.exception.ServiceException;
import com.qzt.common.utils.SecurityUtils;
import com.qzt.common.utils.StringUtils;
import com.qzt.common.utils.bean.BeanValidators;
import com.qzt.common.utils.sign.ToolUtil;
import com.qzt.common.utils.spring.SpringUtils;
import com.qzt.module.system.domain.SysUserRole;
import com.qzt.module.system.domain.vo.user.UserImportRespVO;
import com.qzt.module.system.mapper.SysRoleMapper;
import com.qzt.module.system.mapper.SysUserMapper;
import com.qzt.module.system.mapper.SysUserRoleMapper;
import com.qzt.module.system.service.ISysConfigService;
import com.qzt.module.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.validation.Validator;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户 业务层处理
 *
 * @author
 */
@Service
public class SysUserServiceImpl implements ISysUserService
{
    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    protected Validator validator;

    /**
     * 根据条件分页查询用户列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(userAlias = "u")
    public List<SysUser> selectUserList(SysUser user)
    {
        return userMapper.selectUserList(user);
    }

    /**
     * 根据条件分页查询已分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(userAlias = "u")
    public List<SysUser> selectAllocatedList(SysUser user)
    {
        return userMapper.selectAllocatedList(user);
    }

    /**
     * 根据条件分页查询未分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(userAlias = "u")
    public List<SysUser> selectUnallocatedList(SysUser user)
    {
        return userMapper.selectUnallocatedList(user);
    }

    /**
     * 通过 用户名/手机号/邮箱 查询用户
     *
     * @param username 用户名/手机号/邮箱
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByUsername(String username)
    {
        return userMapper.selectUserByUsername(username);
    }

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserById(Long userId)
    {
        return userMapper.selectUserById(userId);
    }

    /**
     * 查询用户所属角色组
     *
     * @param username 用户名
     * @return 结果
     */
    @Override
    public String selectUserRoleGroup(String username)
    {
        List<SysRole> list = roleMapper.selectRolesByUsername(username);
        if (CollectionUtils.isEmpty(list))
        {
            return StringUtils.EMPTY;
        }
        return list.stream().map(SysRole::getRoleName).collect(Collectors.joining(","));
    }

    /**
     * 校验用户是否允许操作
     *
     * @param user 用户信息
     */
    @Override
    public void checkUserAllowed(SysUser user)
    {
        if (StringUtils.isNotNull(user.getUserId()) && user.isAdmin())
        {
            throw new ServiceException(GlobalErrorCodeConstants.FORBIDDEN);
        }
    }

    /**
     * 校验用户是否有数据权限
     *
     * @param userId 用户id
     */
    @Override
    public void checkUserDataScope(Long userId)
    {
        if (!SysUser.isAdmin(SecurityUtils.getUserId()))
        {
            SysUser user = new SysUser();
            user.setUserId(userId);
            List<SysUser> users = SpringUtils.getAopProxy(this).selectUserList(user);
            if (StringUtils.isEmpty(users))
            {
                throw new ServiceException(GlobalErrorCodeConstants.FORBIDDEN);
            }
        }
    }

    /**
     * 新增保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public Long insertUser(SysUser user)
    {
        validateUserForCreateOrUpdate(null,user.getUsername(),user.getMobile(),user.getEmail());
        // 新增用户信息
        userMapper.insertUser(user);
        // 新增用户与角色管理
        insertUserRole(user);
        return user.getUserId();
    }

    /**
     * 注册用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public boolean registerUser(SysUser user)
    {
        return userMapper.insertUser(user) > 0;
    }

    /**
     * 修改保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateUser(SysUser user)
    {
        validateUserForCreateOrUpdate(user.getUserId(),user.getUsername(),user.getMobile(),user.getEmail());
        Long userId = user.getUserId();
        // 删除用户与角色关联
        userRoleMapper.deleteUserRoleByUserId(userId);
        // 新增用户与角色管理
        insertUserRole(user);
        return userMapper.updateUser(user);
    }

    /**
     * 用户授权角色
     *
     * @param userId 用户ID
     * @param roleIds 角色组
     */
    @Override
    @Transactional
    public void insertUserAuth(Long userId, Long[] roleIds)
    {
        userRoleMapper.deleteUserRoleByUserId(userId);
        insertUserRole(userId, roleIds);
    }

    /**
     * 修改用户状态
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserStatus(SysUser user)
    {
        return userMapper.updateUser(user);
    }

    /**
     * 修改用户基本信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserProfile(SysUser user)
    {
        validateUserForCreateOrUpdate(user.getUserId(),user.getUsername(),user.getMobile(),user.getEmail());
        return userMapper.updateUser(user);
    }

    /**
     * 修改用户头像
     *
     * @param username 用户名
     * @param avatar 头像地址
     * @return 结果
     */
    @Override
    public boolean updateUserAvatar(String username, String avatar)
    {
        return userMapper.updateUserAvatar(username, avatar) > 0;
    }

    /**
     * 重置用户密码
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int resetPwd(SysUser user)
    {
        return userMapper.updateUser(user);
    }

    /**
     * 修改密码
     *
     * @param userId 用户Id
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 结果
     */
    @Override
    public int resetUserPwd(Long userId, String oldPassword,String newPassword)
    {
        validatePassword(userId,oldPassword,newPassword);
        return userMapper.resetUserPwd(userId, newPassword);
    }

    /**
     * 新增用户角色信息
     *
     * @param user 用户对象
     */
    public void insertUserRole(SysUser user)
    {
        this.insertUserRole(user.getUserId(), user.getRoleIds());
    }


    /**
     * 新增用户角色信息
     *
     * @param userId 用户ID
     * @param roleIds 角色组
     */
    public void insertUserRole(Long userId, Long[] roleIds)
    {
        if (StringUtils.isNotEmpty(roleIds))
        {
            // 新增用户与角色管理
            List<SysUserRole> list = new ArrayList<SysUserRole>(roleIds.length);
            for (Long roleId : roleIds)
            {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(userId);
                ur.setRoleId(roleId);
                list.add(ur);
            }
            userRoleMapper.batchUserRole(list);
        }
    }

    /**
     * 通过用户ID删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteUserById(Long userId)
    {
        // 删除用户与角色关联
        userRoleMapper.deleteUserRoleByUserId(userId);
        return userMapper.deleteUserById(userId);
    }

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteUserByIds(Long[] userIds)
    {
        for (Long userId : userIds)
        {
            checkUserAllowed(new SysUser(userId));
            checkUserDataScope(userId);
        }
        // 删除用户与角色关联
        userRoleMapper.deleteUserRole(userIds);
        return userMapper.deleteUserByIds(userIds);
    }

    /**
     * 导入用户数据
     *
     * @param importUsers 用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @return 结果
     */
    @Override
    public UserImportRespVO importUser(List<SysUser> importUsers, Boolean isUpdateSupport)
    {
        if (CollUtil.isEmpty(importUsers))
        {
            throw new ServiceException(SysErrorCodeConstants.USER_IMPORT_LIST_IS_EMPTY);
        }
        UserImportRespVO respVO = UserImportRespVO.builder().createUsernames(new ArrayList<>())
                .updateUsernames(new ArrayList<>()).failureUsernames(new LinkedHashMap<>()).build();
        String initPassword = configService.selectConfigByKey("sys.user.initPassword");
        String operName = SecurityUtils.getLoginUser().getUsername();
        importUsers.forEach(importUser -> {
            // 校验，判断是否有不符合的原因
            try {
                BeanValidators.validateWithException(validator, importUser);
                checkUserAllowed(importUser);
                validateUserForCreateOrUpdate(null, null, importUser.getMobile(), importUser.getEmail());
            } catch (ServiceException ex) {
                respVO.getFailureUsernames().put(importUser.getUsername(), ex.getMessage());
                return;
            }
            // 判断如果不存在，在进行插入
            SysUser existUser = userMapper.selectByUsername(importUser.getUsername());
            if (existUser == null) {
                // 设置默认密码
                importUser.setPassword(SecurityUtils.encryptPassword(initPassword));
                importUser.setCreator(operName);
                userMapper.insertUser(importUser);
                respVO.getCreateUsernames().add(importUser.getUsername());
                return;
            }
            // 如果存在，判断是否允许更新
            if (!isUpdateSupport) {
                respVO.getFailureUsernames().put(importUser.getUsername(), SysErrorCodeConstants.USER_USERNAME_EXISTS.getMsg());
                return;
            }
            importUser.setUserId(existUser.getUserId());
            importUser.setUpdater(operName);
            userMapper.updateUser(importUser);
            respVO.getUpdateUsernames().add(importUser.getUsername());
        });
        return respVO;

    }

    void validateUserExists(Long id) {
        if (id == null) {
            return;
        }
        SysUser user = userMapper.selectUserById(id);
        if (user == null) {
            throw new ServiceException(SysErrorCodeConstants.USER_NOT_EXISTS);
        }
    }

    void validateUsernameUnique(Long id, String username) {
        if (StrUtil.isBlank(username)) {
            return;
        }
        SysUser user = userMapper.selectUserByUsername(username);
        if (user == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的用户
        if (id == null) {
            throw new ServiceException(SysErrorCodeConstants.USER_USERNAME_EXISTS);
        }
        if (!user.getUserId().equals(id)) {
            throw new ServiceException(SysErrorCodeConstants.USER_USERNAME_EXISTS);
        }
    }

    void validateEmailUnique(Long id, String email) {
        if (StrUtil.isBlank(email)) {
            return;
        }
        SysUser user = userMapper.selectByEmail(email);
        if (user == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的用户
        if (id == null) {
            throw new ServiceException(SysErrorCodeConstants.USER_EMAIL_EXISTS);
        }
        if (!user.getUserId().equals(id)) {
            throw new ServiceException(SysErrorCodeConstants.USER_EMAIL_EXISTS);
        }
    }

    void validateMobileUnique(Long id, String mobile) {
        if (StrUtil.isBlank(mobile)) {
            return;
        }
        SysUser user = userMapper.selectByMobile(mobile);
        if (user == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的用户
        if (id == null) {
            throw new ServiceException(SysErrorCodeConstants.USER_MOBILE_EXISTS);
        }
        if (!user.getUserId().equals(id)) {
            throw new ServiceException(SysErrorCodeConstants.USER_MOBILE_EXISTS);
        }
    }

    /**
     * 校验密码
     * @param id          用户 id
     * @param oldPassword 旧密码
     */
    void validatePassword(Long id, String oldPassword,String newPassword) {
        SysUser user = userMapper.selectUserById(id);
        if (user == null) {
            throw new ServiceException(SysErrorCodeConstants.USER_NOT_EXISTS);
        }
        if (!SecurityUtils.matchesPassword(oldPassword, user.getPassword())) {
            throw new ServiceException(SysErrorCodeConstants.USER_PASSWORD_FAILED);
        }
        if (SecurityUtils.matchesPassword(oldPassword, newPassword)) {
            throw new ServiceException(SysErrorCodeConstants.USER_PASSWORD_NO_CHANGE);
        }
    }

    private void validateUserForCreateOrUpdate(Long id, String username, String mobile, String email) {
        // 校验用户存在
        validateUserExists(id);
        // 校验用户名唯一
        validateUsernameUnique(id, username);
        // 校验手机号唯一
        validateMobileUnique(id, mobile);
        // 校验邮箱唯一
        validateEmailUnique(id, email);
    }
}
