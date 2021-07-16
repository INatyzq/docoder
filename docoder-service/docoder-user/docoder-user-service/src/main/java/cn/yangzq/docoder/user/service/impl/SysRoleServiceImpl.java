package cn.yangzq.docoder.user.service.impl;

import cn.yangzq.docoder.common.mybatis.utils.Pageable;
import cn.yangzq.docoder.user.entity.SysRole;
import cn.yangzq.docoder.user.mapper.SysRoleMapper;
import cn.yangzq.docoder.user.param.RbacParam;
import cn.yangzq.docoder.user.service.SysRoleService;
import cn.yangzq.docoder.user.entity.PermissionDetail;
import cn.yangzq.docoder.user.vo.SysRoleVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
*@author yangzq
*@description 系统角色表 服务实现类
**/
@Transactional
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    private SysRoleMapper roleMapper;

    @Override
    public Pageable<SysRoleVo> getPage(SysRoleVo param, Pageable<SysRoleVo> page) {
        return roleMapper.selectDataPage(param,page);
    }

    @Override
    public Pageable<SysRoleVo> getRbacPage(RbacParam param, Pageable<PermissionDetail> page) {
        return roleMapper.selectRbacPage(param,page);
    }
}
