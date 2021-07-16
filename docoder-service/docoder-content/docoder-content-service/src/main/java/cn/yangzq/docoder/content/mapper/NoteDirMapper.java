package cn.yangzq.docoder.content.mapper;

import cn.yangzq.docoder.content.entity.NoteDir;
import cn.yangzq.docoder.content.vo.DirTreeVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
*@author yangzq
*@description 笔记目录 Mapper 接口
**/
public interface NoteDirMapper extends BaseMapper<NoteDir> {

    List<DirTreeVo> selectTreeNode();
}
