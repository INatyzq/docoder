package cn.yangzq.docoder.user.service.impl;

import cn.yangzq.docoder.user.entity.SysPermission;
import cn.yangzq.docoder.user.param.RbacParam;
import cn.yangzq.docoder.user.service.SysPermissionService;
import cn.yangzq.docoder.user.mapper.SysPermissionMapper;
import cn.yangzq.docoder.user.maputil.PoToVoMapper;
import cn.yangzq.docoder.user.vo.PermissionTreeVo;
import cn.yangzq.docoder.user.vo.RbacVo;
import cn.yangzq.docoder.user.entity.PermissionDetail;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yangzq
 * @description 系统权限表 服务实现类
 **/
@Transactional
@Component
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements SysPermissionService {

    @Autowired
    private SysPermissionMapper permissionMapper;
    @Autowired
    private PoToVoMapper poToVoMapper;

    @Override
    public List<PermissionDetail> getUserPermissionList(Integer userId) {
        List<SysPermission> permissionList = permissionMapper.selectUserPermissionList(userId);
        return poToVoMapper.permissionList(permissionList);
    }

    @Override
    public RbacVo getRbac(RbacParam param) {
        RbacVo vo = new RbacVo();

        List<Map<String,String>> rbacList = permissionMapper.selectRbac(param);

        Set<Integer> userIds = vo.getUserIds();
        Set<Integer> roleIds = vo.getRoleIds();
        Set<Integer> permissionIds = vo.getPermissionIds();
        for(Map<String,String> idTypeMap : rbacList){
            Object id = idTypeMap.get("id");
            Object type = idTypeMap.get("type");

            if(Objects.isNull(id) || Objects.isNull(type)){
                continue;
            }

            Integer idInt = Integer.valueOf(id.toString());
            long typeLong = Long.parseLong(type.toString());
            if(1==typeLong){
                userIds.add(idInt);
            }
            if(2==typeLong){
                roleIds.add(idInt);
            }
            if(3==typeLong){
                permissionIds.add(idInt);
            }
        }
        return vo;
    }

    @Override
    public List<PermissionTreeVo> getTree() {
        List<PermissionTreeVo> permissionList = permissionMapper.selectTreeNode();
        Map<Integer, List<PermissionTreeVo>> pidChildrenMap = new TreeMap<>();
        Map<Integer, PermissionTreeVo> idNode = new HashMap<>();

        permissionList.forEach(node -> {
            Integer parentId = node.getParentId();
            List<PermissionTreeVo> children = pidChildrenMap.computeIfAbsent(parentId, list -> new ArrayList<>());
            children.add(node);
            idNode.put(node.getId(), node);
        });

        permissionList.forEach(node -> {
            node.setChildren(pidChildrenMap.get(node.getId()));
            recursionFindChildrenId(node,node,idNode,pidChildrenMap.get(node.getParentId()));
        });

        return permissionList.stream().filter(item->item.getParentId()==0).collect(Collectors.toList());
    }

    /**
     * 递归查找父节点id或子节点id列表
     *
     * @param node
     * @param idNode
     * @param childrenList
     */
    private void recursionFindChildrenId(PermissionTreeVo original,PermissionTreeVo node, Map<Integer, PermissionTreeVo> idNode, List<PermissionTreeVo> childrenList) {
        if(childrenList==null||childrenList.size()==0){
            return;
        }
        Integer parentId = node.getParentId();
        PermissionTreeVo parentNode = idNode.get(parentId);
        if (parentNode != null) {
            Set<Integer> childrenIds = parentNode.getChildrenIds();
            childrenIds.addAll(childrenList.stream().map(PermissionTreeVo::getId).collect(Collectors.toList()));
            recursionFindChildrenId(original,parentNode, idNode, childrenList);
        }
        original.getPids().add(parentId);
    }
}
