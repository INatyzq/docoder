package cn.yangzq.docoder.content.controller;


import cn.yangzq.docoder.common.core.utils.ResultVo;
import cn.yangzq.docoder.content.entity.NoteDir;
import cn.yangzq.docoder.content.form.NoteDirForm;
import cn.yangzq.docoder.content.maputil.FormToPoMapper;
import cn.yangzq.docoder.content.service.NoteDirService;
import cn.yangzq.docoder.content.vo.DirTreeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
*@author yangzq
*@description 笔记目录 前端控制器
**/
@Api(tags = "笔记目录接口")
@RestController
@RequestMapping("/content/noteDir")
public class NoteDirController {

    @Autowired
    private NoteDirService dirService;
    @Autowired
    private FormToPoMapper formToPoMapper;

    @ApiOperation("获取树形数据")
    @GetMapping("/tree")
    public ResultVo<List<DirTreeVo>> getTree(){
        return ResultVo.success(dirService.getTree());
    }

    @ApiOperation("保存或更新")
    @PostMapping("/saveOrUpdate")
    public ResultVo<Object> saveOrUpdate(@RequestBody NoteDirForm form){
        NoteDir dir = formToPoMapper.toNoteDir(form);
        dirService.saveOrUpdate(dir);
        return ResultVo.success();
    }

    @ApiOperation("移动节点")
    @PostMapping("/move")
    public ResultVo<Object> moveNode(@RequestBody NoteDirForm form){
        NoteDir dir = formToPoMapper.toNoteDir(form);
        dirService.updateById(dir);
        return ResultVo.success();
    }

    @ApiOperation("删除")
    @DeleteMapping("/{id}")
    public ResultVo<Object> deleteById(@PathVariable("id")Integer id){
        dirService.removeById(id);
        return ResultVo.success();
    }

}

