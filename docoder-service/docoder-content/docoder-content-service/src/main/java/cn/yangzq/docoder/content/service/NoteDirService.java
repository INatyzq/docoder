package cn.yangzq.docoder.content.service;

import cn.yangzq.docoder.content.entity.NoteDir;
import cn.yangzq.docoder.content.vo.DirTreeVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
*@author yangzq
*@description 笔记目录 服务类
**/
public interface NoteDirService extends IService<NoteDir> {

    /**
     * 获取树形数据
     * @return
     */
    List<DirTreeVo> getTree();

}
