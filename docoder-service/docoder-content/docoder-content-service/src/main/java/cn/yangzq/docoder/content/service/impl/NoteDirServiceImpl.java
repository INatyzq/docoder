package cn.yangzq.docoder.content.service.impl;

import cn.yangzq.docoder.content.entity.NoteDir;
import cn.yangzq.docoder.content.mapper.NoteDirMapper;
import cn.yangzq.docoder.content.service.NoteDirService;
import cn.yangzq.docoder.content.vo.DirTreeVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
*@author yangzq
*@description 笔记目录 服务实现类  
**/
@Transactional
@Service
public class NoteDirServiceImpl extends ServiceImpl<NoteDirMapper, NoteDir> implements NoteDirService {

    @Autowired
    private NoteDirMapper dirMapper;
    
    @Override
    public List<DirTreeVo> getTree() {
        List<DirTreeVo> permissionList = dirMapper.selectTreeNode();
        Map<Integer, List<DirTreeVo>> pidChildrenMap = new TreeMap<>();

        permissionList.forEach(node -> {
            Integer parentId = node.getParentId();
            List<DirTreeVo> children = pidChildrenMap.computeIfAbsent(parentId, list -> new ArrayList<>());
            children.add(node);
        });

        permissionList.forEach(node -> {
            node.setChildren(pidChildrenMap.get(node.getId()));
        });

        return permissionList.stream().filter(item->item.getParentId()==0).collect(Collectors.toList());
    }
}
